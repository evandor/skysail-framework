package io.skysail.server.app.quartz;

import io.skysail.server.app.SkysailApplication;

import java.util.*;

import javax.naming.ConfigurationException;

import lombok.extern.slf4j.Slf4j;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.simpl.CascadingClassLoadHelper;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.ApplicationContextId;
import de.twenty11.skysail.server.services.*;

@aQute.bnd.annotation.component.Component(immediate = true, properties = { "service.pid=quartz" })
@Slf4j
public class QuartzApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider { //,
      //  ManagedService {

    private static final String APP_NAME = "quartz";

    private Scheduler scheduler;

    private Dictionary<String, ?> properties;

    public QuartzApplication() {
        super(APP_NAME);
    }

    @Override
    @Activate
    public void activate(ComponentContext componentContext) throws ConfigurationException {
        super.activate(componentContext);
        // startScheduler();
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/clock.png");
    }

    @Override
    @Deactivate
    public void deactivate(ComponentContext componentContext) {
        super.deactivate(componentContext);
        stopScheduler();
    }

    private void stopScheduler() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void attach() {
//        router.attach(new RouteBuilder("", DashboardResource.class));
//        router.attach(new RouteBuilder("/", DashboardResource.class));
//        router.attach(new RouteBuilder("/groups", GroupsResource.class));
//        router.attach(new RouteBuilder("/groups/", PostGroupsResource.class));
//        router.attach(new RouteBuilder("/groups/{id}", GroupResource.class));
//        router.attach(new RouteBuilder("/groups/{id}/", PutGroupResource.class));
//        router.attach(new RouteBuilder("/jobs", JobsResource.class));
//        router.attach(new RouteBuilder("/jobs/", PostJobResource.class));
//        router.attach(new RouteBuilder("/jobs/{id}", JobResource.class));
//        router.attach(new RouteBuilder("/jobs/{id}/schedules/", PostScheduleResource.class));
//        router.attach(new RouteBuilder("/jobDetails", JobDetailsResource.class));
//        router.attach(new RouteBuilder("/triggers", TriggersResource.class));
//        router.attach(new RouteBuilder("/triggers/", PostTriggerResource.class));
//        router.attach(new RouteBuilder("/schedules", SchedulesResource.class));
    }

    @Override
    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem("Quartz Scheduler", "/quartz", this);
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        return Arrays.asList(appMenu);
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

//    @Override
//    public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
//        this.properties = properties;
//        if (properties == null) {
//            stopScheduler();
//            return;
//        }
//        startScheduler();
//    }

    private synchronized void startScheduler() {
        log.info("Activating Skysail Scheduler...");
        try {
            ClassLoader original = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(CascadingClassLoadHelper.class.getClassLoader());

            StdSchedulerFactory sf = new StdSchedulerFactory();
            Properties props = new Properties();

            Enumeration<String> keys = this.properties.keys();
            Collections.list(keys).stream().filter(key -> {
                return key.startsWith("org.quartz");
            }).forEach(key -> {
                props.put(key, properties.get(key));
            });

            sf.initialize(props);
            scheduler = sf.getScheduler();

            // Set back to the original thread context classloader
            Thread.currentThread().setContextClassLoader(original);

            scheduler.start();
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

}
