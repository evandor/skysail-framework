package io.skysail.server.app.wiki.pages.resources;

import io.skysail.server.app.wiki.pages.Page;

import org.restlet.data.Reference;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PublicPageResource extends PageResource {

    public PublicPageResource() {
        addToContext(ResourceContextId.LINK_TITLE, "preview page");
        addToContext(ResourceContextId.LINK_GLYPH, "eye-open");
    }

    @Override
    public Page getEntity() {
        Reference originalRef = getOriginalRef();
        getResponse().redirectSeeOther(originalRef.toString().replace("/preview", "").concat("?_asUser=anonymous"));
        return new Page();
    }

}
