package deploy

import java.io.File
import java.util.Collections

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
    val conf = new APSConfiguration()
    val clientObj = new ScalaClient(conf)
    if(clientObj.run(args)){
      println("Application completed successfully")
    }else{
      println("Application Failed / Killed")
    }
  }
}



class ScalaClient(conf : APSConfiguration) {
  def run(args : Array[String]) : Boolean = {
    val command = args(0)
    val n = Integer.valueOf(args(1))
    val jarPath = new Path(args(2))
    println("Initializing YARN configuration")
    val conf = new YarnConfiguration()
    val yarnClient = YarnClient.createYarnClient()
    yarnClient.init(conf)
    yarnClient.start()
    println("Requesting ResourceManager for a new Application")
    val app = yarnClient.createApplication()
    println("Initializing ContainerLaunchContext for ApplicationMaster container")
    val amContainer = Records.newRecord(classOf[ContainerLaunchContext])
    println("Adding LocalResource")
    val appMasterJar: LocalResource = Records.newRecord(classOf[LocalResource])
    val  jarStat = FileSystem.get(conf).getFileStatus(jarPath)
    appMasterJar.setResource(ConverterUtils.getYarnUrlFromPath(jarPath))
    appMasterJar.setSize(jarStat.getLen)
    appMasterJar.setTimestamp(jarStat.getModificationTime)
    appMasterJar.setType(LocalResourceType.FILE)
    appMasterJar.setVisibility(LocalResourceVisibility.PUBLIC)
    println("Setting environment")
    val appMasterEnv = new mutable.HashMap[String,String]()
    for ( c : String <- conf.getStrings(YarnConfiguration.YARN_APPLICATION_CLASSPATH,YarnConfiguration.DEFAULT_YARN_APPLICATION_CLASSPATH : _*)){
      Apps.addToEnvironment(appMasterEnv,Environment.CLASSPATH.name(),c.trim)
    }
    Apps.addToEnvironment(appMasterEnv,Environment.CLASSPATH.name(),Environment.PWD.$()+File.separator+"*")
    println("Setting resource capability")
    val capability = Records.newRecord(classOf[Resource])
    capability.setMemory(256)
    capability.setVirtualCores(1)
    println("Setting command to start ApplicationMaster service")
    amContainer.setCommands(Collections.singletonList("/usr/local/jdk1.8.0_161/bin/java"
      + " -Xmx256M" + " scala.ScalaApplicationMaster"
      + " " + command + " " + String.valueOf(n) + " 1>"
      + ApplicationConstants.LOG_DIR_EXPANSION_VAR +
      "/stdout"
      + " 2>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR
      + "/stderr"))
    amContainer.setLocalResources(Collections.singletonMap("first-yarn-app.jar",appMasterJar))
    amContainer.setEnvironment(appMasterEnv)
    println("Initializing ApplicatonSubmissionContext")
    val appContext : ApplicationSubmissionContext = app.getApplicationSubmissionContext
    appContext.setApplicationName("first-yarn-app")
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
    if(appState == YarnApplicationState.FINISHED){
      true
    }else{
      false
    }
  }
}

