package io.skysail.server.services;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;

import io.skysail.server.EventHelper;
import lombok.extern.slf4j.Slf4j;

//@Component(immediate = true)
@Slf4j
public class ServerTime {

    private AtomicReference<EventAdmin> eventAdminRef = new AtomicReference<>();

    private volatile TimerTask timerTask;

    public class MyTimerTask extends TimerTask {

        private EventHelper eventHelper;
        private String currentTime;
        private int msgId;

        public MyTimerTask(AtomicReference<EventAdmin> eventAdminRef) {
            eventHelper = new EventHelper(eventAdminRef.get());
        }

        @Override
        public void run() {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            if (!sdf.format(new Date()).equals(currentTime)) {
                currentTime = sdf.format(new Date());
                msgId = eventHelper.channel(EventHelper.GUI_MSG).info(currentTime).lifetime(1000 * 60L).fire();
            }
        }
    }

    @Activate
    public void activate() {
        log.debug("activating ServerTime task");
        timerTask = new MyTimerTask(eventAdminRef);
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 10000, 10*1000);
    }

    @Deactivate
    public void deactivate() {
        log.info("deactivating ServerTime task");
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
