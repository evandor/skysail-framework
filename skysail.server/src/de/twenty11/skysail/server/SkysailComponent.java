package de.twenty11.skysail.server;

import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.ext.slf4j.Slf4jLoggerFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkysailComponent extends Component {

    private static Logger logger = LoggerFactory.getLogger(SkysailComponent.class);

    public SkysailComponent() {
        logger.info("Creating Restlet Component: {}", SkysailComponent.class.getName());
        // System.setProperty("org.restlet.engine.loggerFacadeClass", "org.restlet.ext.slf4j.Slf4jLoggerFacade");
        getClients().add(Protocol.CLAP);
        getClients().add(Protocol.HTTP);
        getClients().add(Protocol.FILE);
        
        //setStatusService(new SkysailStatusService());
//        @NonNull Object ref = null;
        Engine.getInstance().setLoggerFacade(new Slf4jLoggerFacade());
//        Engine.setRestletLogLevel(Level.FINEST);
//        Engine.setLogLevel(Level.FINEST);
        //getServices().add(new EnrolerService());
    }
}
