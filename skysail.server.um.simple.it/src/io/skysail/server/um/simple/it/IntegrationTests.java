package io.skysail.server.um.simple.it;

import io.skysail.client.testsupport.ApplicationClient;

import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.restlet.data.Form;

public class IntegrationTests {

    private static final String HOST = "http://localhost";
    private static final String PORT = "2015";

    protected Bundle thisBundle = FrameworkUtil.getBundle(this.getClass());
    protected Form form;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    protected ApplicationClient client;

    protected String getBaseUrl() {
        return HOST + (PORT != null ? ":" + PORT : "");
    }

}
