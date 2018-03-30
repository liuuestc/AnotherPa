package client;

import communication.StartAkkaSystem;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.net.NetUtils;
import org.apache.hadoop.yarn.api.ApplicationConstants;
import org.apache.hadoop.yarn.api.protocolrecords.AllocateResponse;
import org.apache.hadoop.yarn.api.records.*;
import org.apache.hadoop.yarn.client.api.AMRMClient;
import org.apache.hadoop.yarn.client.api.NMClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.util.Records;

import java.util.Collections;

public class ApplicationMaster {
    public static void main(String[] args) throws Exception {
        // Point #1 Read YARNconfiguration and initialize YARNClient
        System.out.println("Running ApplicationMaster");
        final String shellCommand = args[0];
        final int numOfContainers = Integer.valueOf(args[1]);
        Configuration conf = new YarnConfiguration();


// Point #2 Connect to RM and request for new application id
        System.out.println("Initializing AMRMCLient");
        AMRMClient<AMRMClient.ContainerRequest> rmClient = AMRMClient.createAMRMClient();
        rmClient.init(conf);
        rmClient.start();
        System.out.println("Initializing NMCLient");
        NMClient nmClient = NMClient.createNMClient();
        nmClient.init(conf);
        nmClient.start();


// Point #3 Register the attempt with the ResourceManager The ApplicationMaster
//registers itself to the ResourceManager service.
        System.out.println("Register ApplicationMaster");
        rmClient.registerApplicationMaster(NetUtils.getHostname(), 0, "");

        new StartAkkaSystem().start();

// Point #4 Define ContainerRequest and add the container's request
        Priority priority = Records.newRecord(Priority.class);
        priority.setPriority(0);
        System.out.println("Setting Resource capability for Containers");
        Resource capability = Records.newRecord(Resource.class);
        capability.setMemory(128);
        capability.setVirtualCores(1);
        for (int i = 0; i < numOfContainers; ++i) {
            AMRMClient.ContainerRequest containerRequested = new AMRMClient.ContainerRequest(
                    capability, null, null, priority, true);
// Resource, nodes, racks, priority and relax locality flag
            rmClient.addContainerRequest(containerRequested);
        }


// Point #5 Request allocation, define ContainerLaunchContext and start the containers
        int allocatedContainers = 0;
        System.out.println("Requesting container allocation from ResourceManager");
        while (allocatedContainers < numOfContainers) {
            AllocateResponse response = rmClient.allocate(0);
            for (Container container : response.getAllocatedContainers()) {
                ++allocatedContainers;
// Launch container by creating ContainerLaunchContext
                ContainerLaunchContext ctx = Records.newRecord(ContainerLaunchContext.class);


                // 需要修改的部分
                ctx.setCommands(Collections.singletonList("/usr/local/jdk1.8.0_161/bin/java jar" + shellCommand + " 1>"
                        + ApplicationConstants.LOG_DIR_EXPANSION_VAR
                        + "/stdout" + " 2>"
                        + ApplicationConstants.LOG_DIR_EXPANSION_VAR
                        + "/stderr"));
                //修改完成


                System.out.println("Starting container on node : " + container.getNodeHttpAddress());
                nmClient.startContainer(container, ctx);
            }
            Thread.sleep(100);
        }

        //test

        //end test


// Point #6 On completion, unregister ApplicationMaster from ResourceManager
        int completedContainers = 0;
        while (completedContainers < numOfContainers) {
            AllocateResponse response = rmClient.allocate(completedContainers / numOfContainers);
            for (ContainerStatus status : response.getCompletedContainersStatuses()) {
                ++completedContainers;
                System.out.println("Container completed : " + status.getContainerId());
                System.out.println("Completed container " + completedContainers);
            }
            Thread.sleep(100);
        }
        rmClient.unregisterApplicationMaster(FinalApplicationStatus.SUCCEEDED,"", "");
    }
}