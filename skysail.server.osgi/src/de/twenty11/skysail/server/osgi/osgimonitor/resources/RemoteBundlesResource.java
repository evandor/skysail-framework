//package de.twenty11.skysail.server.ext.osgimonitor.resources;
//
//import java.io.BufferedReader;
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.ObjectInputStream;
//import java.io.PrintWriter;
//import java.net.Socket;
//import java.net.UnknownHostException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//import de.twenty11.skysail.common.ext.osgimonitor.domain.BundleDescriptor;
//import de.twenty11.skysail.common.ext.osgimonitor.extern.Base64Coder;
//
//public class RemoteBundlesResource extends BundlesResource {
//
//    private Socket socket;
//
//    public RemoteBundlesResource() {
//        try {
//            socket = new Socket("localhost", 9898);
//        } catch (UnknownHostException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//    }
//
//    @Override
//    protected List<BundleDescriptor> getData() {
//        if (socket == null) {
//            return Collections.emptyList();
//        }
//
//        List<BundleDescriptor> result = new ArrayList<BundleDescriptor>();
//
//        BufferedReader in;
//        try {
//            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//            // Consume the initial welcoming messages from the server
//            for (int i = 0; i < 3; i++) {
//                System.out.println(in.readLine() + "\n");
//            }
//            out.println("hi");
//            String response = in.readLine();
//
//            List<BundleDescriptor> deserializedList = (List<BundleDescriptor>)fromString(response);
//            result = deserializedList;
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        return result;
//
//    }
//
//    /** Read the object from Base64 string. */
//    private static Object fromString( String s ) throws IOException ,
//                                                        ClassNotFoundException {
//        byte [] data = Base64Coder.decode( s );
//        ObjectInputStream ois = new ObjectInputStream( 
//                                        new ByteArrayInputStream(  data ) );
//        Object o  = ois.readObject();
//        ois.close();
//        return o;
//    }
// }
