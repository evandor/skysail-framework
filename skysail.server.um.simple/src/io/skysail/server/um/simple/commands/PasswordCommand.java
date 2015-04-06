package io.skysail.server.um.simple.commands;

import io.skysail.server.utils.PasswordUtils;

import org.apache.felix.service.command.CommandProcessor;

import aQute.bnd.annotation.component.Component;

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
