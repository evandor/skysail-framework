package io.skysail.server.app.wiki.pages.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.app.wiki.pages.Page;
import io.skysail.server.app.wiki.versions.Version;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostSubPageResource extends PostPageResource {
    
    public PostSubPageResource() {
        addToContext(ResourceContextId.LINK_TITLE, "create new Subpage");
        app = (WikiApplication)getApplication();
    }
    
    public SkysailResponse<?> addEntity(Page page) {
        Subject subject = SecurityUtils.getSubject();

        Version version = new Version();
        version.setContent(page.getContent());
        version.setOwner(subject.getPrincipal().toString());
        
        page.setContent(null);
        page.addVersion(version);
        
        Page parentPage = app.getRepository().getById(Page.class, getAttribute("id"));
        page.setOwner(subject.getPrincipal().toString());
        parentPage.addPage(page);
        app.getRepository().update(getAttribute("id"), parentPage);
        return new SkysailResponse<String>();
    }

}
