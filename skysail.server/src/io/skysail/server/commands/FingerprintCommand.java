package io.skysail.server.commands;

import java.util.Arrays;

import org.apache.felix.service.command.CommandProcessor;
import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.*;

@Component(properties = {
        CommandProcessor.COMMAND_SCOPE + ":String=skysail", 
        CommandProcessor.COMMAND_FUNCTION + ":String=fingerprint",
}, provide = Object.class)
public class FingerprintCommand {

    private ComponentContext ctx;

    @Activate
    public void activate(ComponentContext ctx) {
        this.ctx = ctx;
    }

    @Deactivate
    public void deactivate(ComponentContext ctx) {
        this.ctx = null;
    }

    public void fingerprint() {
        Arrays.stream(ctx.getBundleContext().getBundles()).forEach(b -> {
            System.out.println(b.getSymbolicName() + "("+b.getVersion()+")");
        });
    }

}
