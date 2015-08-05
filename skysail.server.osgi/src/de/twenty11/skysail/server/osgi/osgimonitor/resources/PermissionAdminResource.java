//package de.twenty11.skysail.server.osgi.osgimonitor.resources;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.osgi.service.permissionadmin.PermissionAdmin;
//import org.osgi.service.permissionadmin.PermissionInfo;
//import org.restlet.data.Form;
//import org.restlet.resource.ResourceException;
//
//import de.twenty11.skysail.api.responses.SkysailResponse;
//import de.twenty11.skysail.server.core.restlet.ListServerResource2;
//import de.twenty11.skysail.server.osgi.OsgiMonitorViewerApplication;
//import de.twenty11.skysail.server.osgi.osgimonitor.domain.Permission;
//
//public class PermissionAdminResource extends ListServerResource2<Permission> {
//
//    private OsgiMonitorViewerApplication app;
//
//    @Override
//    protected void doInit() throws ResourceException {
//        app = (OsgiMonitorViewerApplication) getApplication();
//    }
//
//    @Override
//    protected List<Permission> getData() {
//        PermissionAdmin permissionAdmin = app.getPermissionAdminService();
//        List<Permission> result = new ArrayList<Permission>();
//        if (permissionAdmin != null) {
//            addPermissions(result, "Default", permissionAdmin.getDefaultPermissions());
//            String[] locations = permissionAdmin.getLocations();
//            if (locations != null) {
//                for (String loc : locations) {
//                    addPermissions(result, loc, permissionAdmin.getPermissions(loc));
//                }
//            }
//        }
//        return result;
//    }
//
//    private void addPermissions(List<Permission> result, String location, PermissionInfo[] permissions) {
//        if (permissions == null) {
//            return;
//        }
//        for (PermissionInfo permissionInfo : permissions) {
//            result.add(new Permission(location, permissionInfo));
//        }
//    }
//
//    @Override
//    public Permission getData(Form form) {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    @Override
//    public SkysailResponse<?> addEntity(Permission entity) {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
// }
