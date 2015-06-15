package io.skysail.server.services;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import lombok.extern.slf4j.Slf4j;

import org.osgi.service.event.EventAdmin;

import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.core.osgi.EventHelper;

@Component(immediate = true)
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
                //eventHelper.channel(EventHelper.GUI_MSG).markObsolete(msgId).fire();
                msgId = eventHelper.channel(EventHelper.GUI_MSG).info(currentTime).fire();
            }
        }
    }

    @Activate
    public void activate() {
        log.info("activating ServerTime task");
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
    
    @Reference(dynamic = true, multiple = false, optional = false)
    public void setEventAdmin(EventAdmin eventAdmin) {
        this.eventAdminRef = new AtomicReference<EventAdmin>(eventAdmin);
    }

    public void unsetEventAdmin(EventAdmin eventAdmin) {
        this.eventAdminRef.compareAndSet(eventAdmin, null);
    }
    

}
