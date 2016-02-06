package io.skysail.server.commands;

import org.apache.felix.service.command.CommandProcessor;
import org.osgi.service.component.annotations.Component;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import lombok.extern.slf4j.Slf4j;

@Component(property = {
        CommandProcessor.COMMAND_SCOPE + ":String=skysail",
        CommandProcessor.COMMAND_FUNCTION + ":String=loggingProperties",
}, service = Object.class)
@Slf4j
public class LoggingPropertiesCommand { // NO_UCD (unused code)

    public void loggingProperties() {
        log.info("Logging Properties:");
        log.info("===================");
        
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusPrinter.print(lc);

    }

}
