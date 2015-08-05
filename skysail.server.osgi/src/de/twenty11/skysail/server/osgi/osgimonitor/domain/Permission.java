//package de.twenty11.skysail.server.osgi.osgimonitor.domain;
//
//import org.osgi.service.permissionadmin.PermissionInfo;
//
//public class Permission {
//
//    private String actions;
//    private String encoded;
//    private String name;
//    private String type;
//    private final String sub;
//
//    public Permission(String sub, PermissionInfo permissionInfo) {
//        this.sub = sub;
//        actions = permissionInfo.getActions();
//        encoded = permissionInfo.getEncoded();
//        name = permissionInfo.getName();
//        type = permissionInfo.getType();
//    }
//    
//    public String getActions() {
//        return actions;
//    }
//    
//    public String getEncoded() {
//        return encoded;
//    }
//    
//    public String getName() {
//        return name;
//    }
//    
//    public String getType() {
//        return type;
//    }
//    public String getSub() {
//        return sub;
//    }
//
// }
