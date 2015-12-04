package io.skysail.server.http.impl;

import java.lang.annotation.Annotation;

import org.junit.*;
import org.mockito.Mockito;
import org.osgi.service.component.ComponentContext;

import io.skysail.server.http.*;

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
        httpServer.activate(new ServerConfig() {
            
            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }
            
            @Override
            public int port() {
                return 0;
            }
        }, componentContext);
    }

}
