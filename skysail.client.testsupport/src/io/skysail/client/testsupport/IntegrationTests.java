package io.skysail.client.testsupport;

import io.skysail.server.http.PortProvider;

import java.security.SecureRandom;

import lombok.extern.slf4j.Slf4j;

import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

@Slf4j
public class IntegrationTests<T extends ApplicationBrowser<?,?>, U> {

    protected T browser;

    protected Bundle thisBundle = FrameworkUtil.getBundle(this.getClass());
    
    protected SecureRandom random = new SecureRandom();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            log.info("");
            log.info("--------------------------------------------");
            log.info("{}running test '{}'", ApplicationClient.TESTTAG, description.getMethodName());
            log.info("--------------------------------------------");
            log.info("");
        }
    };
    
    protected String determinePort() {
        log.info("setting up test case...");
        ServiceReference<PortProvider> serviceReference = this.thisBundle.getBundleContext().getServiceReference(PortProvider.class);
        PortProvider service = thisBundle.getBundleContext().getService(serviceReference);
        log.info("setting test port to {}", service.getPort());
        return service.getPort();
    }

   

}
