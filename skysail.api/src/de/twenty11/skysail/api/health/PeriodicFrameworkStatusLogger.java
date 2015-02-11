package de.twenty11.skysail.api.health;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import lombok.extern.slf4j.Slf4j;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Deactivate;

//@Component
// Currently not active
// matching logback config

//<appender name="BUNDLE_STATUS_FILE" class="ch.qos.logback.core.FileAppender">
//<param name="Append" value="false" />
//<filter class="ch.qos.logback.classic.filter.ThresholdFilter">
//    <level>INFO</level>
//</filter>
//<maxFileSize>1MB</maxFileSize>
//<file>logs/status.log</file>
//<append>false</append>
//<encoder>
//    <pattern>%date{ISO8601} PeriodicFrameworkStatusLogger - %msg%n
//    </pattern>
//</encoder>
//</appender>
//
//<logger name="de.twenty11.skysail.api.health" level="INFO" additivity="false">
//<appender-ref ref="BUNDLE_STATUS_FILE"/>
//</logger>
@Slf4j
public class PeriodicFrameworkStatusLogger {

    private static final String SERVICE_ID = "service.id";

    private static final String OBJECT_CLASS = "objectClass";

    private static final int SLEEP_INTERVAL_IN_SECONDS = 60;

    private Thread loggerThread;

    private int sleepInSeconds;

    private static AtomicLong counter = new AtomicLong();

    public PeriodicFrameworkStatusLogger() {
        this.sleepInSeconds = SLEEP_INTERVAL_IN_SECONDS;
    }

    public PeriodicFrameworkStatusLogger(int sleep) {
        this.sleepInSeconds = sleep;
    }

    @Activate
    public void activate(BundleContext bundleContext) {
        Runnable runnable = () -> {
            startBundleLogger(bundleContext);
        };
        loggerThread = new Thread(runnable);
        loggerThread.start();
    }

    @Deactivate
    public void deactivate() {
        if (loggerThread != null) {
            loggerThread.interrupt();
        }
    }

    private void startBundleLogger(BundleContext bundleContext) {
        try {
            while (true) {
                logBundleStatus(bundleContext);
                Thread.sleep(this.sleepInSeconds * 1000);
            }
        } catch (InterruptedException e) {
            log.error("Exception running PeriodicFrameworkStatusLogger", e);
        }
    }

    private void logBundleStatus(BundleContext bundleContext) {
        String now = new SimpleDateFormat("dd.mm.YYYY HH:mm:ss").format(new Date());
        logBundleStatus(bundleContext, now);
        try {
            logServicesStatus(bundleContext, now);
        } catch (InvalidSyntaxException e) {
            log.error("Problem getting all service references", e);
        }
    }

    private void logBundleStatus(BundleContext bundleContext, String now) {
        log.info("current bundle status (invocation #{}, {})", counter.incrementAndGet(), now);
        log.info("============================================================");
        StringBuilder sb = new StringBuilder();
        Arrays.stream(bundleContext.getBundles()).forEach(b -> {
            sb.append("\n").append(Long.toString(b.getBundleId())).append(": ").append(b.getSymbolicName()).//
                    append(" (").append(b.getVersion()).append(") [").//
                    append(translate(b.getState())).append("]");
        });
        log.info(sb.toString());
        log.info("");
    }

    private String translate(int state) {
        switch (state) {
        case Bundle.ACTIVE:
            return "ACTIVE";
        case Bundle.INSTALLED:
            return "INSTALLED";
        case Bundle.RESOLVED:
            return "RESOLVED";
        case Bundle.STARTING:
            return "STARTING";
        case Bundle.UNINSTALLED:
            return "UNINSTALLED";
        default:
            return "UNKNOWN";
        }
    }

    private void logServicesStatus(BundleContext bundleContext, String now) throws InvalidSyntaxException {
        ServiceReference<?>[] allServiceReferences = bundleContext.getAllServiceReferences(null, null);
        log.info("current services status (invocation #{}, {})", counter, now);
        log.info("============================================================");
        StringBuilder sb = new StringBuilder();
        Comparator<? super ServiceReference<?>> comparator = new Comparator<ServiceReference<?>>() {
            @Override
            public int compare(ServiceReference<?> o1, ServiceReference<?> o2) {
                return o1.getProperty(OBJECT_CLASS).toString().compareTo(o2.getProperty(OBJECT_CLASS).toString());
            }
        };
        Arrays.stream(allServiceReferences).sorted(comparator).forEach(sr -> {
            sb.append("\n").append(sr.getProperty(SERVICE_ID)).append(" ").append(sr.getProperty(OBJECT_CLASS));
        });
        log.info(sb.toString());
        log.info("");
    }

}
