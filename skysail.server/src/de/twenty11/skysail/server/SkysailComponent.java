package de.twenty11.skysail.server;

import lombok.extern.slf4j.Slf4j;

import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.ext.slf4j.Slf4jLoggerFacade;

@Slf4j
public class SkysailComponent extends Component {

    public SkysailComponent() {
        log.info("Creating Restlet Component: {}", SkysailComponent.class.getName());

        getClients().add(Protocol.CLAP);
        getClients().add(Protocol.HTTP);
        getClients().add(Protocol.HTTPS);
        getClients().add(Protocol.FILE);

        // setStatusService(new SkysailStatusService());
        Engine.getInstance().setLoggerFacade(new Slf4jLoggerFacade());
        // Engine.setRestletLogLevel(Level.FINEST);
        // Engine.setLogLevel(Level.FINEST);
        // getServices().add(new EnrolerService());
        
        //log.info("available servers: {}", getServers());
    }
}
