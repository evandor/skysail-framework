package io.skysail.server.um.simple.commands;

import org.apache.felix.service.command.CommandProcessor;
import org.osgi.service.component.annotations.Component;

import io.skysail.server.utils.PasswordUtils;

@Component(property = {
        /* Felix GoGo Shell Commands */
        CommandProcessor.COMMAND_SCOPE + ":String=skysail", 
        CommandProcessor.COMMAND_FUNCTION + ":String=getPassword",
}, service = Object.class)
public class PasswordCommand {
    public void getPassword(String newPassword) {
        System.out.println(PasswordUtils.createBCryptHash(newPassword));
    }
}
