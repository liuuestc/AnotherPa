package deploy


import java.util.Collections

import conf.APSConfiguration
import org.apache.hadoop.net.NetUtils
import org.apache.hadoop.yarn.api.ApplicationConstants
import org.apache.hadoop.yarn.api.records._
import org.apache.hadoop.yarn.client.api.{AMRMClient, NMClient}
import org.apache.hadoop.yarn.client.api.AMRMClient.ContainerRequest
import org.apache.hadoop.yarn.conf.YarnConfiguration
import org.apache.hadoop.yarn.util.Records

import scala.collection.JavaConversions._


class ScalaApplicationMaster(conf : APSConfiguration) {
  def main(args: Array[String]): Unit = {
    println("Running ScalaApplicationMaster")
    val shellCommand = args(0)
    val numOfContainers = Integer.valueOf(args(1))
    val conf = new YarnConfiguration()
    println("Initializing AMRMCLient")
    val rmClient : AMRMClient[ContainerRequest] = AMRMClient.createAMRMClient()
    rmClient.init(conf)
    rmClient.start()
    println("Initializing NMCLient")
    val nmClient : NMClient= NMClient.createNMClient();
    nmClient.init(conf)
    nmClient.start()
    println("Register ApplicationMaster")
    rmClient.registerApplicationMaster(NetUtils.getHostname,0,"")
    val priority = Records.newRecord(classOf[Priority])
    priority.setPriority(0)
    println("Setting Resource capability for containers")
    val capability : Resource = Records.newRecord(classOf[Resource])
    capability.setMemory(128)
    capability.setVirtualCores(1)
    for ( i <- 0 to(numOfContainers)){
      val containerRequested = new ContainerRequest(capability,null,null,priority,true)
      rmClient.addContainerRequest(containerRequested)
    }
    var allocatedContainers = 0
    println("Requesting container allocation from ResourceManager")
    while (allocatedContainers < numOfContainers){
      val response = rmClient.allocate(0)
      for (container : Container <- response.getAllocatedContainers){
        allocatedContainers += 1
        val ctx = Records.newRecord(classOf[ContainerLaunchContext])
        ctx.setCommands(Collections.singletonList(shellCommand + "1> " + ApplicationConstants.LOG_DIR_EXPANSION_VAR
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
