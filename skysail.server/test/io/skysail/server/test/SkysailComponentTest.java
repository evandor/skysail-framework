package io.skysail.server.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Ignore;
import org.junit.Test;
import org.restlet.Client;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.engine.log.LoggerFacade;
import org.restlet.ext.slf4j.Slf4jLoggerFacade;

import io.skysail.server.SkysailComponent;

public class SkysailComponentTest {

    @Test
    public void hasProperProtocols() {
        SkysailComponent skysailComponent = new SkysailComponent();
        assertThat(hasProtocol(skysailComponent, org.restlet.data.Protocol.CLAP),is(true));
        assertThat(hasProtocol(skysailComponent, org.restlet.data.Protocol.HTTP),is(true));
        assertThat(hasProtocol(skysailComponent, org.restlet.data.Protocol.HTTPS),is(true));
        assertThat(hasProtocol(skysailComponent, org.restlet.data.Protocol.FILE),is(true));
    }
    
    @Test
    @Ignore
    public void hasProperLoggerFacade() {
        LoggerFacade loggerFacade = Engine.getInstance().getLoggerFacade();
        assertThat(loggerFacade instanceof Slf4jLoggerFacade, is(true));
    }

    private boolean hasProtocol(SkysailComponent skysailComponent, Protocol protocol) {
        return skysailComponent.getClients().stream()
                .filter(c -> test(c, protocol))
                .findFirst()
                .isPresent();
    }

    private boolean test(Client c, Protocol protocol) {
        return c.getProtocols().stream()
                .filter(p -> p.equals(protocol))
                .findFirst().isPresent();
    }

}
