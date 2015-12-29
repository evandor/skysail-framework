package io.skysail.server.commands;

import java.util.*;

import org.apache.felix.service.command.CommandProcessor;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.*;

import de.twenty11.skysail.server.app.ApplicationListProvider;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import io.skysail.server.app.SkysailApplication;
import lombok.extern.slf4j.Slf4j;

@Component(property = { CommandProcessor.COMMAND_SCOPE + ":String=skysail",
        CommandProcessor.COMMAND_FUNCTION + ":String=listRoutes", }, service = Object.class)
@Slf4j
public class ListRoutesCommand {

    private ComponentContext ctx;

    @Reference
    private volatile ApplicationListProvider alp;
    
    @Activate
    public void activate(ComponentContext ctx) {
        this.ctx = ctx;
    }

    @Deactivate
    public void deactivate(ComponentContext ctx) {
        this.ctx = null;
    }

    public void listRoutes(String applicationNamePart) {
        Optional<SkysailApplication> optionalApplication = alp.getApplications().stream()
                .filter(application -> application.getName().contains(applicationNamePart)).findFirst();
        if (optionalApplication.isPresent()) {
            Map<String, RouteBuilder> routesMap = optionalApplication.get().getSkysailRoutes();
            routesMap.keySet().stream().sorted((key1, key2) -> key1.compareTo(key2)).forEach(routeKey -> log
                    .info("  " + routeKey + " -> " + routesMap.get(routeKey).getTargetClass().getName()));
        }
    }
    
//    public void listRoutesOld(String bsnPart) {
//        Optional<Bundle> matchingBundle = Arrays.stream(ctx.getBundleContext().getBundles())
//                .filter(b -> b.getSymbolicName().contains(bsnPart)).findFirst();
//        if (matchingBundle.isPresent()) {
//            Bundle theBundle = matchingBundle.get();
//            Optional<ServiceReference<?>> optionalApplicationProvider = Arrays.stream(theBundle.getRegisteredServices()).filter(s -> {
//                String[] objectClassProperties = (String[]) s.getProperty("objectClass");
//                return Arrays.stream(objectClassProperties).filter(objectClass -> {
//                    return objectClass.equals(ApplicationProvider.class.getName());
//                }).findFirst().isPresent();
//            }).findFirst();
//            if (optionalApplicationProvider.isPresent()) {
//                ServiceReference<?> serviceReference = optionalApplicationProvider.get();
//                ApplicationProvider service = (ApplicationProvider) ctx.getBundleContext().getService(serviceReference);
//                Map<String, RouteBuilder> routesMap = service.getApplication().getSkysailRoutes();
//                routesMap.keySet().stream()
//                    .sorted((key1,key2) -> key1.compareTo(key2))
//                    .forEach(routeKey -> {
//                        log.info("  " + routeKey + " -> " + routesMap.get(routeKey).getTargetClass().getName());
//                    });
//                return;
//            }
//            log.info("bundle does not provide ApplicationProvider service.");
//            return;
//        }
//        log.info("no matching bundle found.");
//    }

}
