package io.skysail.server.commands;

import org.apache.felix.service.command.CommandProcessor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.skysail.server.app.ApplicationList;
import lombok.extern.slf4j.Slf4j;

@Component(property = {
        CommandProcessor.COMMAND_SCOPE + ":String=skysail",
        CommandProcessor.COMMAND_FUNCTION + ":String=listApplications",
}, service = Object.class)
@Slf4j
public class ListApplicationsCommand { 

    @Reference
    private ApplicationList applicationList;
    
    public void listApplications() {
        log.info("List of active Applications:");
        log.info("============================");
        if (applicationList == null) {
            log.info("cannot access application list, sorry");
        }
        applicationList.getApplications().stream()
            .sorted((a1, a2) -> a1.getName().compareTo(a2.getName().toString())).forEach(app -> { // NOSONAR
                log.info("  {}", app.getName());
            });
    }

}
