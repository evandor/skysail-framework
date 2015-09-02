package io.skysail.server.app.quartz.triggers;

import io.skysail.api.forms.Field;

import java.util.Date;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Trigger {

	@Field
	private String name;

    @Field(selectionProvider = JobsSelectionProvider.class)
	private String job;

    private Date nextFireTime;

	public Trigger(org.quartz.Trigger trigger) {
	    name = trigger.getKey().getName();
	    //group = trigger.getKey().getGroup();
	    nextFireTime = trigger.getNextFireTime();
	}

	public Date getNextFireTime() {
        return nextFireTime;
    }
}
