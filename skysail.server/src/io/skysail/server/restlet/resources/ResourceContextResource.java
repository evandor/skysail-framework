package io.skysail.server.restlet.resources;

import io.skysail.server.model.*;

import java.util.List;

import lombok.Getter;

@Getter
public class ResourceContextResource {

    List<Breadcrumb> breadcrumbs;

    public ResourceContextResource(SkysailServerResource<?> skysailServerResource) {
        breadcrumbs = new Breadcrumbs().create(skysailServerResource);
    }

}
