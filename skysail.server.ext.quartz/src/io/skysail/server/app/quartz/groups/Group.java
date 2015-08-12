package io.skysail.server.app.quartz.groups;

import io.skysail.api.forms.Field;

import javax.persistence.Id;
import javax.validation.constraints.Size;

public class Group {

	@Id
	private Object rid;

	public Object getRid() {
	    return rid;
    }

	public void setRid(Object rid) {
	    this.rid = rid;
    }

	@Field
	@Size(min = 1)
	private String name;

	public String getName() {
	    return name;
    }

	public void setName(String name) {
	    this.name = name;
    }
}