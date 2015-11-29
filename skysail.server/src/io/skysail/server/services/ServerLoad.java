package io.skysail.server.services;

import java.lang.management.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;

import de.twenty11.skysail.server.core.osgi.EventHelper;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true)
@Slf4j
public class ServerLoad {

    private AtomicReference<EventAdmin> eventAdminRef = new AtomicReference<>();

    private volatile TimerTask timerTask;

    public class ServerLoadTimerTask extends TimerTask {

        private EventHelper eventHelper;
        private OperatingSystemMXBean operatingSystemMXBean;

        public ServerLoadTimerTask(AtomicReference<EventAdmin> eventAdminRef) {
            eventHelper = new EventHelper(eventAdminRef.get());
            operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        }

        @Override
        public void run() {
            double serverLoad = operatingSystemMXBean.getSystemLoadAverage();
            //eventHelper.channel(EventHelper.GUI_MSG).info(Double.toString(serverLoad)).fire();
            eventHelper.channel(EventHelper.GUI_PEITY_BAR).info(Double.toString(serverLoad)).fire();
        }
    }

    @Activate
    public void activate() {
        log.debug("activating ServerLoad task");
        timerTask = new ServerLoadTimerTask(eventAdminRef);
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 10000, 10 * 1000);
    }

    @Deactivate
    public void deactivate() {
        log.info("deactivating ServerLoad task");
        timerTask.cancel();
        timerTask = null;
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
    public void setEventAdmin(EventAdmin eventAdmin) {
        this.eventAdminRef = new AtomicReference<EventAdmin>(eventAdmin);
    }

    public void unsetEventAdmin(EventAdmin eventAdmin) {
        this.eventAdminRef.compareAndSet(eventAdmin, null);
    }

}
