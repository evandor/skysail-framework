package io.skysail.client.testsupport;

import io.skysail.server.http.PortProvider;

import java.io.*;
import java.nio.file.*;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.junit.Rule;
import org.junit.rules.*;
import org.junit.runner.Description;
import org.osgi.framework.*;

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

    protected void stopAndStartBundle(Class<?> cls) throws BundleException {
        Bundle bundle = FrameworkUtil.getBundle(cls);
        bundle.stop();
        bundle.start();
    }

    protected void capture(String html) throws IOException {
        StringBuilder path = new StringBuilder("generated").append(File.separatorChar).append("test-reports-largeTests").append(File.separatorChar);
        path.append(getFileName());
        new File(path.toString()).mkdirs();
        path.append(File.separatorChar);
        path.append(getMethodName()).append(".html");
        Files.write(Paths.get(path.toString()), Arrays.stream(html.split("\n")).collect(Collectors.toList()));
    }

    private static String getFileName() {
        return Thread.currentThread().getStackTrace()[3].getFileName().replace(".java", "");
    }

    private static String getMethodName() {
        return Thread.currentThread().getStackTrace()[4].getMethodName();
    }


}
