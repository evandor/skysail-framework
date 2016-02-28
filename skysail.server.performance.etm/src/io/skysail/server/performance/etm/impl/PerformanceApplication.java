package io.skysail.server.performance.etm.impl;

import java.util.*;

import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;

import de.twenty11.skysail.server.core.restlet.*;
import etm.core.monitor.EtmMonitor;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.menus.*;
import io.skysail.server.performance.etm.EtmPerformanceMonitor;
import lombok.Getter;

@Component(immediate = true)
public class PerformanceApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String APP_NAME = "Performance";
    
    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    @Getter
    private volatile EventAdmin eventAdmin;

    public PerformanceApplication() {
        super(APP_NAME);
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/page_link.png");
    }

    @Override
    protected void attach() {
        super.attach();
        router.attach(new RouteBuilder("", PerformanceResource.class));
    }

    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem(APP_NAME, "/" + APP_NAME + getApiVersion().getVersionPath(), this);
        appMenu.setCategory(MenuItem.Category.ADMIN_MENU);
        return Arrays.asList(appMenu);
    }


    public EtmMonitor getMonitor() {
        return EtmPerformanceMonitor.getEtmMonitor();
    }

}