package io.skysail.server.app.todos;

import io.skysail.server.app.todos.domain.TodosResource;

import java.util.List;

import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.core.restlet.ListServerResource;

@javax.annotation.Generated(value = "de.twenty11.skysail.server.ext.apt.GenerateRootResourceProcessor")
public class RootResource extends ListServerResource<String> {

    @Override
    public List<Linkheader> getLinkheader() {
        return super.getLinkheader(TodosResource.class);
    }

}