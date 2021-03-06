//package io.skysail.server.app.wiki.pages.resources;
//
//import io.skysail.api.links.Link;
//import io.skysail.api.responses.SkysailResponse;
//import io.skysail.api.text.TranslationRenderService;
//import io.skysail.server.app.wiki.WikiApplicationOld;
//import io.skysail.server.app.wiki.pages.Page;
//import io.skysail.server.restlet.resources.EntityServerResource;
//
//import java.util.List;
//
//import org.apache.shiro.SecurityUtils;
//
//public class PageResource extends EntityServerResource<Page> {
//
//    private WikiApplicationOld app;
//    private String pageId;
//
//    protected void doInit() {
//        pageId = getAttribute("pageId");
//        app = (WikiApplicationOld) getApplication();
//    }
//
//    public Page getEntity() {
//        Page page = app.getPagesRepo().findOne(pageId);
//        String content = "xxx";//page.getVersions().get(page.getVersions().size() - 1).getContent();
//        if (SecurityUtils.getSubject().isAuthenticated()) {
//            page.setContent(content);
//        } else {
//            TranslationRenderService markdownRenderer = ((WikiApplicationOld)getApplication()).getMarkdownRenderer();
//            page.setContent(markdownRenderer.render(content));
//        }
//        return page;
//    }
//
//    public List<Link> getLinks() {
//        return super.getLinks(PutPageResource.class, PostSubPageResource.class, SubpagesResource.class, PublicPageResource.class);
//    }
//
//    @Override
//    public SkysailResponse<?> eraseEntity() {
//        return null;
//    }
//
//}
