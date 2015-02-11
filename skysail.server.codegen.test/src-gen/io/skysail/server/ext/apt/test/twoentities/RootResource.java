package io.skysail.server.ext.apt.test.twoentities;

import java.util.Collections;
import java.util.List;

import io.skysail.server.ext.apt.test.crm.contacts.ContactsResource;
import io.skysail.server.ext.apt.test.simple.todos.TodosResource;
import io.skysail.server.ext.apt.test.twoentities.jobs.JobsResource;
import io.skysail.server.ext.apt.test.twoentities.schedules.SchedulesResource;
import io.skysail.server.ext.apt.test.withlist.companies.CompanysResource;
import io.skysail.server.ext.apt.test.withlist.folders.FoldersResource;


import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.core.restlet.ListServerResource;

@javax.annotation.Generated(value = "de.twenty11.skysail.server.ext.apt.GenerateRootResourceProcessor")
public class RootResource extends ListServerResource<String> {

    @Override
    public List<String> getData() {
        return Collections.emptyList();
    }

    @Override
    public List<Linkheader> getLinkheader() {
        return super.getLinkheader(ContactsResource.class,TodosResource.class,JobsResource.class,SchedulesResource.class,CompanysResource.class,FoldersResource.class);
    }

}