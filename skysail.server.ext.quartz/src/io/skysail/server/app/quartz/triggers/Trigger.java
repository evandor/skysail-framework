package io.skysail.server.app.quartz.triggers;

import java.util.Date;

import de.twenty11.skysail.api.forms.Field;

public class Trigger {

	@Field
	private String name;
    private String group;
    private Date nextFireTime;
	
	public Trigger(org.quartz.Trigger trigger) {
	    name = trigger.getKey().getName();
	    group = trigger.getKey().getGroup();
	    nextFireTime = trigger.getNextFireTime();
	}
	
	public Trigger() {
    }

	public String getName() {
	    return name;
    }
	
	public void setName(String name) {
	    this.name = name;
    }

	public String getGroup() {
        return group;
    }
	 
	public Date getNextFireTime() {
        return nextFireTime;
    }
}
