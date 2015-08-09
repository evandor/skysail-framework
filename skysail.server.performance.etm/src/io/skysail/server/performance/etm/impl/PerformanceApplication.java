package io.skysail.server.performance.etm.impl;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.performance.etm.EtmPerformanceMonitor;

import java.util.*;

import aQute.bnd.annotation.component.Component;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.*;
import de.twenty11.skysail.server.services.*;
import etm.core.monitor.EtmMonitor;

@Component(immediate = true)
public class PerformanceApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String APP_NAME = "Performance";

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