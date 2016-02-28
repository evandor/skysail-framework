package io.skysail.server.commands;

import java.util.Map;
import java.util.Optional;

import org.apache.felix.service.command.CommandProcessor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import io.skysail.server.app.ApplicationListProvider;
import io.skysail.server.app.SkysailApplication;
import lombok.extern.slf4j.Slf4j;

@Component(property = { CommandProcessor.COMMAND_SCOPE + ":String=skysail",
        CommandProcessor.COMMAND_FUNCTION + ":String=listRoutes", }, service = Object.class)
@Slf4j
public class ListRoutesCommand {

    @Reference
    private volatile ApplicationListProvider alp;
    
    public void listRoutes(String applicationNamePart) {
        Optional<SkysailApplication> optionalApplication = alp.getApplications().stream()
                .filter(application -> application.getName().contains(applicationNamePart)).findFirst();
        if (optionalApplication.isPresent()) {
            Map<String, RouteBuilder> routesMap = optionalApplication.get().getSkysailRoutes();
            routesMap.keySet().stream()
                .sorted((key1, key2) -> key1.compareTo(key2))
                .forEach(routeKey -> log
                    .info("  " + routeKey + " -> " + routesMap.get(routeKey).getTargetClass().getName()));
        } else {
            log.info("nothing found, existing applications are:");
            log.info("-----------------------------------------");
            log.info("");
            alp.getApplications().stream()
                .map(SkysailApplication::getName)
                .sorted((a1, a2) -> a1.toLowerCase().compareTo(a2.toLowerCase()))
                .forEach(a -> log.info("{}", a));
        }
    }

}
