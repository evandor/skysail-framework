package io.skysail.server.ext.sse;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.ext.sse.resources.GuiMessageResource;

import java.util.*;

import org.osgi.service.event.*;

import aQute.bnd.annotation.component.Component;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.osgi.EventHelper;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import de.twenty11.skysail.server.services.*;

@Component(immediate = true, properties = { "event.topics=" + EventHelper.GUI_MSG + "/*" })
public class SseApplication extends SkysailApplication implements MenuItemProvider, ApplicationProvider, EventHandler {

    private static final String APP_NAME = "SSE";
    
    private EventsQueue events = new EventsQueue();
    
    public SseApplication() {
        super(APP_NAME);
        //addToAppContext(ApplicationContextId.IMG, "/static/img/silk/page_link.png");
    }

//    private SseThread sseThread;
//    
//    @Activate
//    protected void activate(ComponentContext componentContext) throws ConfigurationException {
//        super.activate(componentContext);
//        sseThread = new SseThread();
//        sseThread.start();
//    }

    @Override
    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem(APP_NAME, "/" + APP_NAME, this);
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        return Arrays.asList(appMenu);
    }

    @Override
    public void handleEvent(Event event) {
       events.add(event);
       // sseThread.addEvent(event);
    }
    
    @Override
    protected void attach() {
        super.attach();
        router.attach(new RouteBuilder("", GuiMessageResource.class));
    }

    public List<Message> getEvents(String username) {
        return events.getFor(username);
    }
    
   

}
