package io.skysail.server;

import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.ext.slf4j.Slf4jLoggerFacade;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SkysailComponent extends Component {

    public SkysailComponent() {
        log.debug("Creating Restlet Component: {}", SkysailComponent.class.getName());

        getClients().add(Protocol.CLAP);
        getClients().add(Protocol.HTTP);
        getClients().add(Protocol.HTTPS);
        getClients().add(Protocol.FILE);

        Engine.getInstance().setLoggerFacade(new Slf4jLoggerFacade());
    }
}
