//package de.twenty11.skysail.server.osgi.osgimonitor.resources;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.osgi.service.cm.Configuration;
//import org.osgi.service.cm.ConfigurationAdmin;
//import org.restlet.resource.ResourceException;
//
//import de.twenty11.skysail.server.core.restlet.ListServerResource;
//import de.twenty11.skysail.server.osgi.OsgiMonitorViewerApplication;
//import de.twenty11.skysail.server.osgi.osgimonitor.domain.ConfigDescriptor;
//
////@Presentation(preferred = PresentationStyle.LIST2)
//public class ConfigAdminResource extends ListServerResource<ConfigDescriptor> {
//
//    public ConfigAdminResource() {
//        super(null);
//    }
//
//    private OsgiMonitorViewerApplication app;
//
//    @Override
//    protected void doInit() throws ResourceException {
//        app = (OsgiMonitorViewerApplication) getApplication();
//    }
//
//    @Override
//    public List<ConfigDescriptor> getData() {
//        List<ConfigDescriptor> result = new ArrayList<ConfigDescriptor>();
//        ConfigurationAdmin configadmin = app.getConfigadmin();
//        try {
//            Configuration[] listConfigurations = configadmin.listConfigurations(null);
//            for (Configuration configuration : listConfigurations) {
//                result.add(new ConfigDescriptor(configuration));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//}
