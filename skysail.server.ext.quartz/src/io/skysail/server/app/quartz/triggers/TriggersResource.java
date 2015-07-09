package io.skysail.server.app.quartz.triggers;

import io.skysail.server.app.quartz.QuartzApplication;
import io.skysail.server.app.quartz.jobs.JobsResource;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;

import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class TriggersResource extends ListServerResource<Trigger> {

    private QuartzApplication app;

    public TriggersResource() {
        app = (QuartzApplication) getApplication();
        addToContext(ResourceContextId.LINK_TITLE, "List Triggers");
    }

    @Override
    public List<Trigger> getData() {
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
    public List<Linkheader> getLinkheader() {
        return super.getLinkheader(JobsResource.class, PostTriggerResource.class);
    }
}
