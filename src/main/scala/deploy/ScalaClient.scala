package deploy

import java.io.File
import java.util.Collections

import client.Client
import conf.APSConfiguration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.yarn.api.ApplicationConstants
import org.apache.hadoop.yarn.api.ApplicationConstants.Environment
import org.apache.hadoop.yarn.api.records._
import org.apache.hadoop.yarn.client.api.YarnClient
import org.apache.hadoop.yarn.conf.YarnConfiguration
import org.apache.hadoop.yarn.util.{Apps, ConverterUtils, Records}

import scala.collection.mutable
import scala.collection.JavaConversions._


object ScalaClient {
  def main(args: Array[String]): Unit = {
    val apsconf = new APSConfiguration()
    val jarPath = new Path(apsconf.getJarPath)
    val className = args(0)
    val workerNum = apsconf.getInt("aps.worker.num",1)
    val amMemory = apsconf.getInt("aps.am.memory",256)
    val amCore = apsconf.getInt("aps.am.core",1)
    val dataPath = apsconf.getDataPath
    val isPs = Class.forName(className).newInstance().asInstanceOf[Client].isPs
    val psServer = if(isPs) 0 else 1
    println("Initializing YARN configuration")
    val yarnClient = YarnClient.createYarnClient()
    yarnClient.init(apsconf)
    yarnClient.start()
    println("Requesting ResourceManager for a new Application")
    val app = yarnClient.createApplication()
    println("Initializing ContainerLaunchContext for ApplicationMaster container")
    val amContainer = Records.newRecord(classOf[ContainerLaunchContext])
    println("Adding LocalResource")
    val appMasterJar: LocalResource = Records.newRecord(classOf[LocalResource])
    val  jarStat = FileSystem.get(apsconf).getFileStatus(jarPath)
    appMasterJar.setResource(ConverterUtils.getYarnUrlFromPath(jarPath))
    appMasterJar.setSize(jarStat.getLen)
    appMasterJar.setTimestamp(jarStat.getModificationTime)
    appMasterJar.setType(LocalResourceType.FILE)
    appMasterJar.setVisibility(LocalResourceVisibility.PUBLIC)
    println("Setting environment")
    val appMasterEnv = new mutable.HashMap[String,String]()
    for ( c : String <- apsconf.getStrings(YarnConfiguration.YARN_APPLICATION_CLASSPATH,YarnConfiguration.DEFAULT_YARN_APPLICATION_CLASSPATH : _*)){
      Apps.addToEnvironment(appMasterEnv,Environment.CLASSPATH.name(),c.trim)
    }
    Apps.addToEnvironment(appMasterEnv,Environment.CLASSPATH.name(),Environment.PWD.$()+File.separator+"*")
    println("Setting resource capability")
    val capability = Records.newRecord(classOf[Resource])
    capability.setMemory(amMemory)
    capability.setVirtualCores(amCore)
    println("Setting command to start ApplicationMaster service")
    amContainer.setCommands(Collections.singletonList("/usr/local/jdk1.8.0_161/bin/java"
      + " -Xmx256M" +  deploy.ScalaApplicationMaster
      + " " + String.valueOf(workerNum) + " " + className + " "
      + String.valueOf(psServer) +" " +dataPath+" "+" 1>"
      + ApplicationConstants.LOG_DIR_EXPANSION_VAR +
      "/stdout"
      + " 2>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR
      + "/stderr"))
    amContainer.setLocalResources(Collections.singletonMap("first-yarn-app.jar",appMasterJar))
    amContainer.setEnvironment(appMasterEnv)
    println("Initializing ApplicatonSubmissionContext")
    val appContext : ApplicationSubmissionContext = app.getApplicationSubmissionContext
    appContext.setApplicationName(apsconf.get("aps.app.name"))
    appContext.setApplicationType("YARN")
    appContext.setAMContainerSpec(amContainer)
    appContext.setResource(capability)
    appContext.setQueue("default")
    val appId = appContext.getApplicationId
    println("Submitting application " + appId)
    yarnClient.submitApplication(appContext)
    var appReport = yarnClient.getApplicationReport(appId)
    var appState = appReport.getYarnApplicationState
    while (
      appState != YarnApplicationState.FINISHED
        && appState != YarnApplicationState.KILLED
        && appState != YarnApplicationState.FAILED
    ){
      Thread.sleep(100)
      appReport = yarnClient.getApplicationReport(appId)
      appState = appReport.getYarnApplicationState
    }
    if(appState == YarnApplicationState.FINISHED) true else false
  }
}

