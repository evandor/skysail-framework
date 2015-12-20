package io.skysail.server.app.plugins.obr;

import io.skysail.api.forms.Field;
import io.skysail.domain.Identifiable;
import lombok.Getter;

import org.apache.felix.bundlerepository.Resource;

@Getter
public class ObrResource implements Identifiable {

    @Field
    private String searchFor;

    //@Field
    private String presentationName;

    @Field
    private String symbolicName;

    @Field
    private String version;

    @Field
    private Long size;

    @Field
    private String id;

    public ObrResource() {
    }

    public ObrResource(String searchFor) {
        this.searchFor = searchFor;
    }

    public ObrResource(Resource resource) {
        presentationName = resource.getPresentationName();
        symbolicName = resource.getSymbolicName();
        version = resource.getVersion().toString();
        size = resource.getSize();
        id = resource.getId();
    }

    public String getSearchFor() {
        return searchFor;
    }

    public void setSearchFor(String searchFor) {
        this.searchFor = searchFor;
    }

    @Override
    public void setId(String id) {
    }

}
