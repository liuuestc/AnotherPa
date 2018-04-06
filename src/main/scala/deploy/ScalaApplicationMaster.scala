package deploy


import java.util.Collections

import common.{ApsMaster, ScheduleMaster}
import conf.APSConfiguration
import org.apache.hadoop.net.NetUtils
import org.apache.hadoop.yarn.api.ApplicationConstants
import org.apache.hadoop.yarn.api.records._
import org.apache.hadoop.yarn.client.api.{AMRMClient, NMClient}
import org.apache.hadoop.yarn.client.api.AMRMClient.ContainerRequest
import org.apache.hadoop.yarn.util.Records
import util.Utilities

import scala.collection.JavaConversions._

 object ScalaApplicationMaster {
  //args(0) workerNumber args(1) client name
  def main(args: Array[String]): Unit = {
    val apsconf = new APSConfiguration()
    println("Running ScalaApplicationMaster")
    val workerCores = apsconf.getInt("aps.worker.core",1)
    val workerMemory = apsconf.getInt("aps.worker.memory",128)
    val numOfContainers = Integer.valueOf(args(0))
    val clientName = args(1)
    val isPs = if (Integer.valueOf(2) == 0) true else false
    val dataPath = args(3)
    val javahome = apsconf.getStrings("java_home","/usr/local/jdk1.8.0_161") + "/bin/java"
    println("Initializing AMRMCLient")
    val rmClient : AMRMClient[ContainerRequest] = AMRMClient.createAMRMClient()
    rmClient.init(apsconf)
    rmClient.start()
    println("Initializing NMCLient")
    val nmClient : NMClient= NMClient.createNMClient();
    nmClient.init(apsconf)
    nmClient.start()
    println("Register ApplicationMaster")
    rmClient.registerApplicationMaster(NetUtils.getHostname,0,"")
    val priority = Records.newRecord(classOf[Priority])
    priority.setPriority(0)
    println("Setting Resource capability for containers")
    val capability : Resource = Records.newRecord(classOf[Resource])
    capability.setMemory(workerMemory)
    capability.setVirtualCores(workerCores)
    for ( i <- 0 to numOfContainers){
      val containerRequested = new ContainerRequest(capability,null,null,priority,true)
      rmClient.addContainerRequest(containerRequested)
    }
    var allocatedContainers = 0
    val host = Utilities.getLocalHost
    //启动本机程序的master，根据调度器是否训练部分参数决定不同的调度器
    if (isPs) ApsMaster(clientName,dataPath,numOfContainers,host,apsconf).start() else ScheduleMaster(clientName,dataPath,numOfContainers,host,apsconf).start()
    //启动本机master后，启动其他container
    println("Requesting container allocation from ResourceManager")
    while (allocatedContainers < numOfContainers){
      val response = rmClient.allocate(0)
      for (container : Container <- response.getAllocatedContainers){
        allocatedContainers += 1
        val ctx = Records.newRecord(classOf[ContainerLaunchContext])
        ctx.setCommands(Collections.singletonList(javahome + " " + common.ApsSlave+ " " +
          clientName+ " "+host+" " +"1> " + ApplicationConstants.LOG_DIR_EXPANSION_VAR
        + "/stdout" + "2> " + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/stderr"))
        println("Starting container on node : " + container.getNodeHttpAddress)
        nmClient.startContainer(container,ctx)
      }
      Thread.sleep(100)
    }
    var completedContainers = 0
    while (completedContainers < numOfContainers){
      val response = rmClient.allocate(completedContainers / numOfContainers)
      for (status : ContainerStatus <- response.getCompletedContainersStatuses){
        completedContainers += 1
        println("Container completed ： " + status.getContainerId)
        println("Completed container " + completedContainers)
      }
       Thread.sleep(100)
    }
    rmClient.unregisterApplicationMaster(FinalApplicationStatus.SUCCEEDED, "", "")
  }
}
