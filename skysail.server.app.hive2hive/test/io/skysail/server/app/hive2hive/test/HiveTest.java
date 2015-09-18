package io.skysail.server.app.hive2hive.test;

import java.io.*;
import java.net.InetAddress;
import java.nio.file.*;

import net.engio.mbassy.listener.*;

import org.apache.commons.io.FileUtils;
import org.hive2hive.core.api.H2HNode;
import org.hive2hive.core.api.configs.*;
import org.hive2hive.core.api.interfaces.*;
import org.hive2hive.core.events.framework.interfaces.IFileEventListener;
import org.hive2hive.core.events.framework.interfaces.file.*;
import org.hive2hive.core.model.PermissionType;
import org.hive2hive.core.security.UserCredentials;
import org.hive2hive.processframework.interfaces.IProcessComponent;
import org.junit.*;

public class HiveTest {

    private IH2HNode peerNode;

    @Test
    public void test_createInitial() throws Exception {
        setUpInitialNetwork();
    }

    @Test
    public void joining_existing_p2p_network() throws Exception {
        IFileConfiguration fileConfig = setUpInitialNetwork();

        INetworkConfiguration netConfig2 = NetworkConfiguration.create("second", InetAddress.getByName("127.0.0.1"));
        IH2HNode peerNode2 = H2HNode.createNode(fileConfig);
        peerNode2.connect(netConfig2);
    }

    @Test
    public void testName2() throws Exception {
        IFileConfiguration fileConfig = setUpInitialNetwork();
        IUserManager userManager = peerNode.getUserManager();

        UserCredentials credentials = new UserCredentials("userId", "password", "pin");
        Path rootDirectory = Paths.get("/Users/carsten/shared/aerofs/books/iText");

        if (!userManager.isRegistered(credentials.getUserId())) {
           userManager.createRegisterProcess(credentials);//.await();
        }
        //userManager.createLoginProcess(credentials, rootDirectory).await();
    }

    @Test
    public void testName3() throws Exception {
     // Create a node and open a new overlay network
        IH2HNode node = H2HNode.createNode(FileConfiguration.createDefault());
        node.connect(NetworkConfiguration.createInitial());

        // The register functionality is in the user manager API
        IUserManager userManager = node.getUserManager();

        // Create user credentials to register a new user 'Alice'
        UserCredentials alice = new UserCredentials("Alice", "very-secret-password", "secret-pin");

        // Create a new register process and start it (blocking)
        IProcessComponent<Void> registerAlice = userManager.createRegisterProcess(alice);
        registerAlice.execute();

        // Check if Alice is now registered
        boolean aliceRegistered = userManager.isRegistered("Alice");
        System.out.println("Alice is registered: " + aliceRegistered);

        // Let's login to Alice's user account (blocking)
        IProcessComponent<Void> loginAlice = userManager.createLoginProcess(alice, new ExampleFileAgent());
        loginAlice.execute();

        // Check if Alice is now logged in
        System.out.println("Alice is logged in: " + userManager.isLoggedIn());
    }

    @Test
    public void testName4() throws Exception {
        IFileConfiguration fileConfiguration = FileConfiguration.createDefault();

        // Initialize several nodes (not connected yet)
        IH2HNode node1 = H2HNode.createNode(fileConfiguration);
        IH2HNode node2 = H2HNode.createNode(fileConfiguration);
        IH2HNode node3 = H2HNode.createNode(fileConfiguration);
        IH2HNode node4 = H2HNode.createNode(fileConfiguration);

        // Create a new P2P network at the first (initial) peer
        node1.connect(NetworkConfiguration.createInitial());

        // Connect the 2nd peer to the network. This peer bootstraps to node1 running at the local host
        NetworkConfiguration node2Conf = NetworkConfiguration.create(InetAddress.getLocalHost());
        node2.connect(node2Conf);

        // The network configuration builder allows you to configure more details
        // here we set a custom (non-random) node id and a custom port that the node 3 binds to
        NetworkConfiguration node3Conf = NetworkConfiguration.create(InetAddress.getLocalHost()).setPort(4777)
                .setNodeId("node3");
        node3.connect(node3Conf);

        // Nodes can bootstrap to any of the connected peers. Therefore, we set that node4 should connect to
        // node3 to become a part of the P2P network
        NetworkConfiguration node4Conf = NetworkConfiguration.create(InetAddress.getLocalHost()).setBootstrapPort(4777);
        node4.connect(node4Conf);

        // We can test the connection status of these nodes
        System.out.println("Node 1 is connected: " + node1.isConnected());
        System.out.println("Node 2 is connected: " + node2.isConnected());
        System.out.println("Node 3 is connected: " + node3.isConnected());
        System.out.println("Node 4 is connected: " + node4.isConnected());
    }

 // A Strong reference is necessary if this object is not held in any variable, otherwise GC would clean it
    // and events are not triggered anymore. So keep either a reference to this listener object or add the
    // strong reference annotation.
    @Listener(references = References.Strong)
    private static class ExampleEventListener implements IFileEventListener {

        private final IFileManager fileManager;

        public ExampleEventListener(IFileManager fileManager) {
            this.fileManager = fileManager;
        }

        @Override
        @Handler
        public void onFileAdd(IFileAddEvent fileEvent) {
            System.out.println("File was added: " + fileEvent.getFile().getName());
            try {
                // download the new file
                fileManager.createDownloadProcess(fileEvent.getFile()).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        @Handler
        public void onFileUpdate(IFileUpdateEvent fileEvent) {
            System.out.println("File was updated: " + fileEvent.getFile().getName());
            try {
                // download the newest version
                fileManager.createDownloadProcess(fileEvent.getFile()).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        @Handler
        public void onFileDelete(IFileDeleteEvent fileEvent) {
            System.out.println("File was deleted: " + fileEvent.getFile().getName());
            // delete it at the event receiver as well
            fileEvent.getFile().delete();
        }

        @Override
        @Handler
        public void onFileMove(IFileMoveEvent fileEvent) {
            try {
                // Move the file to the new destination if it exists
                if (fileEvent.isFile() && fileEvent.getSrcFile().exists()) {
                    FileUtils.moveFile(fileEvent.getSrcFile(), fileEvent.getDstFile());
                    System.out.println("File was moved from " + fileEvent.getSrcFile() + " to " + fileEvent.getDstFile());
                } else if (fileEvent.isFolder() && fileEvent.getSrcFile().exists()) {
                    FileUtils.moveDirectory(fileEvent.getSrcFile(), fileEvent.getDstFile());
                    System.out.println("Folder was moved from " + fileEvent.getSrcFile() + " to " + fileEvent.getDstFile());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        @Handler
        public void onFileShare(IFileShareEvent fileEvent) {
            System.out.println("File was shared by " + fileEvent.getInvitedBy());
            // Currently, no further actions necessary. The invitation is accepted
            // automatically and 'onFileAdd' is called in an instant.
        }

    }

    @Test
    @Ignore
    public void testName5() throws Exception {
        IFileConfiguration fileConfiguration = FileConfiguration.createDefault();

        // Create two nodes and open a new overlay network
        IH2HNode node1 = H2HNode.createNode(fileConfiguration);
        IH2HNode node2 = H2HNode.createNode(fileConfiguration);
        node1.connect(NetworkConfiguration.createInitial());
        node2.connect(NetworkConfiguration.create(InetAddress.getLocalHost()));

        // These two file agents are used to configure the root directory of the logged in user
        ExampleFileAgent node1FileAgent = new ExampleFileAgent();
        ExampleFileAgent node2FileAgent = new ExampleFileAgent();

        // Register user 'Alice' and login her at node 1 and 2
        UserCredentials alice = new UserCredentials("Alice", "password", "pin");
        node1.getUserManager().createRegisterProcess(alice).execute();
        node1.getUserManager().createLoginProcess(alice, node1FileAgent).execute();
        node2.getUserManager().createLoginProcess(alice, node2FileAgent).execute();

        // In this example, a file event listener is registered at node 2. Therefore, we will listen to events
        // that happen by actions taken by node 1.
        node2.getFileManager().subscribeFileEvents(new ExampleEventListener(node2.getFileManager()));

        // To demonstrate the 'add' event, we will add a new file with node 1
        // Let's create a file and upload it at node 1
        File fileAtNode1 = new File(node1FileAgent.getRoot(), "test-file-event.txt");
        FileUtils.write(fileAtNode1, "Hello"); // add some content
        node1.getFileManager().createAddProcess(fileAtNode1).execute();

        // Let's trigger a deletion event
        fileAtNode1.delete();
        node1.getFileManager().createDeleteProcess(fileAtNode1).execute();
    }

    @Test
    @Ignore
    public void testName6() throws Exception {
        IFileConfiguration fileConfiguration = FileConfiguration.createDefault();

        // Create two nodes and open a new overlay network
        IH2HNode node1 = H2HNode.createNode(fileConfiguration);
        IH2HNode node2 = H2HNode.createNode(fileConfiguration);
        node1.connect(NetworkConfiguration.createInitial());
        node2.connect(NetworkConfiguration.create(InetAddress.getLocalHost()));

        // These two file agents are used to configure the root directory of the logged in users
        ExampleFileAgent node1FileAgent = new ExampleFileAgent();
        ExampleFileAgent node2FileAgent = new ExampleFileAgent();

        // Register and login user 'Alice' at node 1
        UserCredentials alice = new UserCredentials("Alice", "password", "pin");
        node1.getUserManager().createRegisterProcess(alice).execute();
        node1.getUserManager().createLoginProcess(alice, node1FileAgent).execute();

        // Also login user 'Alice' at node 2
        node2.getUserManager().createLoginProcess(alice, node2FileAgent).execute();

        // All file management operations are located at the file manager. Here we get the file managers of
        // each peer alice is connected to.
        IFileManager fileManager1 = node1.getFileManager(); // for node 1
        IFileManager fileManager2 = node2.getFileManager(); // for node 2

        // Let's create a file and upload it at node 1
        File fileAtNode1 = new File(node1FileAgent.getRoot(), "test-file.txt");
        FileUtils.write(fileAtNode1, "Hello"); // add some content
        fileManager1.createAddProcess(fileAtNode1).execute();

        // Normally, the node 2 would be notified about the new file through the event bus (shown in another
        // example). However, we just know that the file exists in the network and can download it at node 2.
        // This is only possible because Alice is logged in into node 2 as well.
        File fileAtNode2 = new File(node2FileAgent.getRoot(), "test-file.txt");
        // this file does not exist yet (as we did not start the download process yet)
        System.out.println("Existence of the file prior to download: " + fileAtNode2.exists());
        fileManager2.createDownloadProcess(fileAtNode2).execute();

        // We can now re-check whether the file exists or not
        System.out.println("Existence of the file after download: " + fileAtNode2.exists());
        System.out.println("Content of the first version at node 2: " + FileUtils.readFileToString(fileAtNode2));

        // Now, let's modify the file at node 2 and re-upload a new version of it
        FileUtils.write(fileAtNode2, " World!", true); // append the text
        fileManager2.createUpdateProcess(fileAtNode2).execute();

        // The file has now updated, therefore we should download the new version at node 1
        fileManager1.createDownloadProcess(fileAtNode1).execute();
        System.out.println("Content of the second version at node 1: " + FileUtils.readFileToString(fileAtNode1));

    }

    @Test
    @Ignore
    public void sharetest() throws Exception {
        IFileConfiguration fileConfiguration = FileConfiguration.createDefault();

        // Create two nodes and open a new overlay network
        IH2HNode node1 = H2HNode.createNode(fileConfiguration);
        IH2HNode node2 = H2HNode.createNode(fileConfiguration);
        node1.connect(NetworkConfiguration.createInitial());
        node2.connect(NetworkConfiguration.create(InetAddress.getLocalHost()));

        // These two file agents are used to configure the root directory of the logged in users
        ExampleFileAgent node1FileAgent = new ExampleFileAgent();
        ExampleFileAgent node2FileAgent = new ExampleFileAgent();

        // Register and login user 'Alice' at node 1
        UserCredentials alice = new UserCredentials("Alice", "password", "pin");
        node1.getUserManager().createRegisterProcess(alice).execute();
        node1.getUserManager().createLoginProcess(alice, node1FileAgent).execute();

        // Register and login user 'Bob' at node 2
        UserCredentials bob = new UserCredentials("Bob", "password", "pin");
        node2.getUserManager().createRegisterProcess(bob).execute();
        node2.getUserManager().createLoginProcess(bob, node2FileAgent).execute();

        // Let's create a folder at Alice and upload it
        IFileManager fileManager1 = node1.getFileManager(); // The file management of Alice's peer
        File folderAtAlice = new File(node1FileAgent.getRoot(), "shared-folder");
        folderAtAlice.mkdirs();
        fileManager1.createAddProcess(folderAtAlice).execute();

        // Let's share the folder with Bob giving him write permission
        fileManager1.createShareProcess(folderAtAlice, "Bob", PermissionType.WRITE).execute();

        // Wait some time in order to get the file share event propagated to Bob
        System.out.println("Alice shared a folder with Bob. We'll wait some time for its propagation...");
        Thread.sleep(20000);

        // Bob can now 'download' the folder (yes, sounds a little bit silly...)
        IFileManager fileManager2 = node2.getFileManager(); // The file management of Bob's peer
        File folderAtBob = new File(node2FileAgent.getRoot(), "shared-folder");
        fileManager2.createDownloadProcess(folderAtBob).execute();

        // Bob could for example upload a new file to the shared folder
        File fileAtBob = new File(folderAtBob, "shared-file.txt");
        FileUtils.write(fileAtBob, "This is a shared file of Alice and Bob");
        fileManager2.createAddProcess(fileAtBob).execute();

        // Wait some time in order to get the file share event propagated to Bob
        System.out.println("Waiting that Alice sees the file from Bob...");
        Thread.sleep(20000);

        // Alice now can obtain the shared file
        File fileAtAlice = new File(folderAtAlice, "shared-file.txt");
        fileManager1.createDownloadProcess(fileAtAlice).execute();
        System.out.println("Content of the file in the shared folder at Alice: " + FileUtils.readFileToString(fileAtAlice));

    }

    private IFileConfiguration setUpInitialNetwork() {
        INetworkConfiguration netConfig = NetworkConfiguration.createInitial("first");
        IFileConfiguration fileConfig = FileConfiguration.createDefault();

        peerNode = H2HNode.createNode(fileConfig);
        peerNode.connect(netConfig);
        return fileConfig;
    }

}
