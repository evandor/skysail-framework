package io.skysail.server.app.quartz.groups;

import io.skysail.server.app.quartz.QuartzApplication;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.*;

import org.restlet.resource.Resource;

public class GroupsProvider implements io.skysail.api.forms.SelectionProvider {

    public static GroupsProvider getInstance() {
        return new GroupsProvider();
    }

    private Resource resource;

    @Override
    public Map<String, String> getSelections() {
        QuartzApplication application = (QuartzApplication)((SkysailServerResource<?>)this.resource).getApplication();
        Map<String, String> result = new HashMap<>();
        List<Group> groups = application.getRepository().findAll(Group.class, new Filter(this.resource.getRequest()), null);
        groups.stream().forEach(group -> {result.put(group.getName(), group.getName());});
        if (result.isEmpty()) {
            result.put("default", "default");
        }
        return result;
    }

    @Override
    public void setConfiguration(Object osgiServicesProvider) {

    }



    @Override
    public void setResource(Resource resource) {
        this.resource = resource;
    }



}
