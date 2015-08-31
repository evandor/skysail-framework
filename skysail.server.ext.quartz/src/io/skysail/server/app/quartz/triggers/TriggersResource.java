package io.skysail.server.app.quartz.triggers;

import io.skysail.api.links.Link;
import io.skysail.server.app.quartz.QuartzApplication;
import io.skysail.server.app.quartz.jobs.JobsResource;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class TriggersResource extends ListServerResource<Trigger> {

    private QuartzApplication app;

    public TriggersResource() {
        app = (QuartzApplication) getApplication();
        addToContext(ResourceContextId.LINK_TITLE, "List Triggers");
    }

    @Override
    public List<Trigger> getEntity() {
        Scheduler scheduler = app.getScheduler();
        try {
            Function<? super String, ? extends Set<TriggerKey>> toJobKey = groupName -> {
                try {
                    return scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(groupName));
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            };
            return scheduler.getTriggerGroupNames().stream().map(toJobKey).filter(triggerKey -> {
                return triggerKey != null;
            }).flatMap(triggerKey -> triggerKey.stream()).map(triggerKey -> {
                try {
                    org.quartz.Trigger trigger = scheduler.getTrigger(triggerKey);
                    return new Trigger(trigger);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }).collect(Collectors.toList());
        } catch (SchedulerException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(JobsResource.class, PostTriggerResource.class);
    }
}
