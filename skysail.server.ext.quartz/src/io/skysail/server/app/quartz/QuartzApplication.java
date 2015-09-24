package io.skysail.server.app.quartz;

import io.skysail.server.app.*;
import io.skysail.server.app.quartz.groups.resources.*;
import io.skysail.server.app.quartz.jobdetails.JobDetailsResource;
import io.skysail.server.app.quartz.jobs.*;
import io.skysail.server.app.quartz.jobs.Job;
import io.skysail.server.app.quartz.schedules.*;
import io.skysail.server.app.quartz.triggers.resources.*;
import io.skysail.server.menus.*;
import io.skysail.server.repo.DbRepository;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.simpl.CascadingClassLoadHelper;

import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.*;
import de.twenty11.skysail.server.services.*;

@Slf4j
@Component(immediate = true)
public class QuartzApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    private static final String APP_NAME = "Quartz";

    private Scheduler scheduler;
    private QuartzRepository repository;
    private Map<String, String> config;

    public QuartzApplication() {
        super(APP_NAME, new ApiVersion(1));
        //documentEntities(new Todo(), new TodoList());
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/tag_yellow.png");
    }

    @Activate
    public void activate(Map<String, String> config, ComponentContext componentContext) throws ConfigurationException {
        super.activate(componentContext);
        this.config = config;
        startScheduler();
    }

    @Deactivate
    public void deactivate() {
        //super.deactivate(componentContext);
        stopScheduler();
        this.config = null;
    }

    @Reference(dynamic = true, multiple = false, optional = false, target = "(name=QuartzRepository)")
    public void setRepository(DbRepository repo) {
        this.repository = (QuartzRepository) repo;
    }

    public void unsetRepository(DbRepository repo) {
        this.repository = null;
    }

    public QuartzRepository getRepository() {
        return repository;
    }

    @Override
    protected void attach() {
        super.attach();

        router.attach(new RouteBuilder("", DashboardResource.class));
        router.attach(new RouteBuilder("/", DashboardResource.class));
        router.attach(new RouteBuilder("/groups", GroupsResource.class));
        router.attach(new RouteBuilder("/groups/", PostGroupResource.class));
        router.attach(new RouteBuilder("/groups/{id}", GroupResource.class));
        router.attach(new RouteBuilder("/groups/{id}/", PutGroupResource.class));
        router.attach(new RouteBuilder("/jobs", JobsResource.class));
        router.attach(new RouteBuilder("/jobs/", PostJobResource.class));
        router.attach(new RouteBuilder("/jobs/{id}", JobResource.class));
        router.attach(new RouteBuilder("/jobs/{id}/schedules/", PostScheduleResource.class));
        router.attach(new RouteBuilder("/jobDetails", JobDetailsResource.class));
        router.attach(new RouteBuilder("/triggers", TriggersResource.class));
        router.attach(new RouteBuilder("/triggers/", PostTriggerResource.class));
        router.attach(new RouteBuilder("/schedules", SchedulesResource.class));
    }

    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem(APP_NAME, "/" + APP_NAME + getApiVersion().getVersionPath(), this);
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        return Arrays.asList(appMenu);
    }

    private synchronized void startScheduler() {
        log.info("Activating Skysail Scheduler...");
        try {
            ClassLoader original = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(CascadingClassLoadHelper.class.getClassLoader());

            StdSchedulerFactory sf = new StdSchedulerFactory();
            Properties props = new Properties();

            Set<String> keys = this.config.keySet();
            keys.stream().filter(key -> {
                return key.startsWith("org.quartz");
            }).forEach(key -> {
                props.put(key, config.get(key));
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

    private void stopScheduler() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public Scheduler getScheduler() {
        return this.scheduler;
    }

    public Class<SkysailServerResource<?>>[] defaultResourcesPlus(Class<? extends SkysailServerResource<?>>... classes) {
        List<Class<? extends SkysailServerResource<?>>> result = new ArrayList<>();
        result.addAll(Arrays.asList(GroupsResource.class, JobDetailsResource.class,TriggersResource.class));
        Arrays.stream(classes).forEach(cls -> result.add(cls));
        return result.toArray(new Class[result.size()]);
    }

    public List<Job> getJobs() {
        try {
            Function<? super String, ? extends Set<JobKey>> toJobKey = groupName -> {
                try {
                    return scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName));
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            };
            return scheduler.getJobGroupNames().stream().map(toJobKey).filter(jobKey -> {
                return jobKey != null;
            }).flatMap(jobKey -> jobKey.stream()).map(jobKey -> {
                return new Job(jobKey);
            }).collect(Collectors.toList());
        } catch (SchedulerException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

    }

}
