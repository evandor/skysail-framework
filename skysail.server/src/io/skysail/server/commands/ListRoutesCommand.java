package io.skysail.server.commands;

import java.util.*;

import org.apache.felix.service.command.CommandProcessor;
import org.osgi.framework.*;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.*;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import lombok.extern.slf4j.Slf4j;

@Component(property = { CommandProcessor.COMMAND_SCOPE + ":String=skysail",
        CommandProcessor.COMMAND_FUNCTION + ":String=listRoutes", }, service = Object.class)
@Slf4j
public class ListRoutesCommand {

    private ComponentContext ctx;

    @Activate
    public void activate(ComponentContext ctx) {
        this.ctx = ctx;
    }

    @Deactivate
    public void deactivate(ComponentContext ctx) {
        this.ctx = null;
    }

    public void listRoutes(String bsnPart) {
        Optional<Bundle> matchingBundle = Arrays.stream(ctx.getBundleContext().getBundles())
                .filter(b -> b.getSymbolicName().contains(bsnPart)).findFirst();
        if (matchingBundle.isPresent()) {
            Bundle theBundle = matchingBundle.get();
            Optional<ServiceReference<?>> optionalApplicationProvider = Arrays.stream(theBundle.getRegisteredServices()).filter(s -> {
                String[] objectClassProperties = (String[]) s.getProperty("objectClass");
                return Arrays.stream(objectClassProperties).filter(objectClass -> {
                    return objectClass.equals(ApplicationProvider.class.getName());
                }).findFirst().isPresent();
            }).findFirst();
            if (optionalApplicationProvider.isPresent()) {
                ServiceReference<?> serviceReference = optionalApplicationProvider.get();
                ApplicationProvider service = (ApplicationProvider) ctx.getBundleContext().getService(serviceReference);
                Map<String, RouteBuilder> routesMap = service.getApplication().getSkysailRoutes();
                routesMap.keySet().stream().forEach(routeKey -> {
                    log.info("  " + routeKey + " -> " + routesMap.get(routeKey).getTargetClass().getName());
                });
                return;
            }
            log.info("bundle does not provide ApplicationProvider service.");
            return;
        }
        log.info("no matching bundle found.");
    }

}
