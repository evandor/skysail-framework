package io.skysail.server.um.simple.commands;

import org.apache.felix.service.command.CommandProcessor;

import aQute.bnd.annotation.component.Component;
import de.twenty11.skysail.server.utils.PasswordUtils;

@Component(properties = {
        /* Felix GoGo Shell Commands */
        CommandProcessor.COMMAND_SCOPE + ":String=skysail", 
        CommandProcessor.COMMAND_FUNCTION + ":String=getPassword",
}, provide = Object.class)
public class PasswordCommand {
    public void getPassword(String newPassword) {
        System.out.println(PasswordUtils.createBCryptHash(newPassword));
    }
}
