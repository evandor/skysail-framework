package io.skysail.server.app.wiki.pages.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.api.text.TranslationRenderService;
import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.app.wiki.pages.Page;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

import org.apache.shiro.SecurityUtils;

public class PageResource extends EntityServerResource<Page> {

    private WikiApplication app;
    private String pageId;

    protected void doInit() {
        pageId = getAttribute("pageId");
        app = (WikiApplication) getApplication();
    }

    public Page getEntity() {
        Page page = app.getRepository().getById(Page.class, pageId);
        String content = page.getVersions().get(page.getVersions().size() - 1).getContent();
        if (SecurityUtils.getSubject().isAuthenticated()) {
            page.setContent(content);
        } else {
            TranslationRenderService markdownRenderer = ((WikiApplication)getApplication()).getMarkdownRenderer();
            page.setContent(markdownRenderer.render(content));
        }
        return page;
    }

    public List<Link> getLinks() {
        return super.getLinks(PutPageResource.class, PostSubPageResource.class, SubpagesResource.class, PublicPageResource.class);
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

}
