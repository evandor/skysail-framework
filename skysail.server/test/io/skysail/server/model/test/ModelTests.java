package io.skysail.server.model.test;

import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Request;

@RunWith(MockitoJUnitRunner.class)
public abstract class ModelTests {

    protected TestEntity testEntity;
    
    @Spy protected TestListResource testListResource;
    @Spy protected TestPutResource testPutResource;
    @Spy protected TestPostResource testPostResource;
    @Spy protected TestEntityResource testEntityResource;

    protected Request request;
    
    public void setUp() throws Exception {
        request = Mockito.mock(Request.class);
        Mockito.doReturn(request).when(testPutResource).getRequest();
    }


}
