package io.skysail.server.commands;

import org.apache.felix.service.command.CommandProcessor;
import org.osgi.service.component.annotations.Component;

import lombok.extern.slf4j.Slf4j;

@Component(property = {
        CommandProcessor.COMMAND_SCOPE + ":String=skysail",
        CommandProcessor.COMMAND_FUNCTION + ":String=systemProperties",
}, service = Object.class)
@Slf4j
public class SystemPropertiesCommand { // NO_UCD (unused code)

    public void systemProperties() {
        log.info("System Properties:");
        log.info("==================");
        System.getProperties().entrySet().stream()
                .sorted((e1, e2) -> e1.getKey().toString().compareTo(e2.getKey().toString())).forEach(entry -> {
                    Object value = entry.getValue();
                    log.info("  {} => '{}'", entry.getKey(), value == null ? "<null>" : value.toString());
                });
    }

}
