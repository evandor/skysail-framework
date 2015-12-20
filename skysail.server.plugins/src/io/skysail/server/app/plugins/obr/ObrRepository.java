package io.skysail.server.app.plugins.obr;

import io.skysail.api.forms.Field;
import io.skysail.domain.Identifiable;

import java.util.*;
import java.util.stream.Collectors;

import lombok.Getter;

import org.apache.felix.bundlerepository.*;

@Getter
public class ObrRepository implements Identifiable {

    @Field
    private String id;

    @Field
    private String uri;

    @Field
    private long lastModified;

    private List<ObrResource> resources;

    public ObrRepository() {
    }

    public ObrRepository(Repository repository) {
        this(repository, false);
    }

    public ObrRepository(Repository repository, boolean addResources) {
        id = repository.getName();
        uri = repository.getURI().toString();
        lastModified = repository.getLastModified();
        if (addResources) {
            resources = map(repository.getResources());
        }
    }

    private List<ObrResource> map(Resource[] resources) {
        return Arrays.stream(resources).map(r -> new ObrResource(r)).collect(Collectors.toList());
    }

    @Override
    public void setId(String id) {
    }

}
