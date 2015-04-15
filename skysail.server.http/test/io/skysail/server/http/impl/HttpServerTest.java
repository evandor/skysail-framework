package io.skysail.server.http.impl;

import io.skysail.server.http.HttpServer;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.osgi.service.component.ComponentContext;

public class HttpServerTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }
    
    @Test
    public void testName() throws Exception {
        HttpServer httpServer = new HttpServer();
        ComponentContext componentContext = Mockito.mock(ComponentContext.class);
        httpServer.activate(componentContext);
    }

}
