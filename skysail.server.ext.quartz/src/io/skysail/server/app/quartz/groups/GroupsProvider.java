package io.skysail.server.app.quartz.groups;

import java.util.*;

import org.restlet.resource.Resource;

public class GroupsProvider implements io.skysail.api.forms.SelectionProvider {

    public static GroupsProvider getInstance() {
        return new GroupsProvider();
    }



    @Override
    public Map<String, String> getSelections() {
        Map<String, String> result = new HashMap<>();
        List<Group> groups = GroupsRepository.getInstance().findAll();
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
    }



}
