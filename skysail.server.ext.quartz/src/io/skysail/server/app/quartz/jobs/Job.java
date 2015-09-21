package io.skysail.server.app.quartz.jobs;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;
import io.skysail.server.app.quartz.groups.GroupsProvider;

import javax.validation.constraints.Size;

import org.quartz.JobKey;

public class Job implements Identifiable {

	@Field
	@Size(min = 1)
	private String name;

	@Field(selectionProvider = JobsProviderImpl.class)
	private String jobImpl;

    @Field(selectionProvider = GroupsProvider.class)
    private String group;

    private String id;

	public Job(JobKey jobKey) {
        this.name = jobKey.getName();
        this.group = jobKey.getGroup();
        id = name + "-" + group;
    }

    public Job() {
        // TODO Auto-generated constructor stub
    }

    public String getId() {
		return id.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJobImpl() {
        return jobImpl;
    }

	public void setJobImpl(String jobImpl) {
        this.jobImpl = jobImpl;
    }

	public String getGroup() {
        return group;
    }

    @Override
    public void setId(String id) {
    }
}
