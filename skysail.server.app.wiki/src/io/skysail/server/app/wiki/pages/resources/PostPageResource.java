//package io.skysail.server.app.wiki.pages.resources;
//
//import io.skysail.api.links.Link;
//import io.skysail.api.responses.SkysailResponse;
//import io.skysail.server.app.wiki.WikiApplicationOld;
//import io.skysail.server.app.wiki.pages.Page;
//import io.skysail.server.app.wiki.spaces.resources.SpacesResource;
//import io.skysail.server.app.wiki.versions.Version;
//import io.skysail.server.restlet.resources.PostEntityServerResource;
//
//import java.util.List;
//
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.subject.Subject;
//import org.restlet.resource.ResourceException;
//
//import de.twenty11.skysail.server.core.restlet.ResourceContextId;
//
//public class PostPageResource extends PostEntityServerResource<Page> {
//
//    private String spaceId;
//    protected WikiApplicationOld app;
//
//    public PostPageResource() {
//        addToContext(ResourceContextId.LINK_TITLE, "create new Page");
//    }
//
//    @Override
//    protected void doInit() throws ResourceException {
//        app = (WikiApplicationOld) getApplication();
//        spaceId = getAttribute("id");
////        if (spaceId == null) {
////            return;
////        }
////        space = app.getRepository().getById(Space.class, spaceId);
////        if (space != null) {
////            Map<String, String> substitutions = new HashMap<>();
////            substitutions.put("/spaces/" + spaceId, space.getName());
////            getContext().getAttributes().put(ResourceContextId.PATH_SUBSTITUTION.name(), substitutions);
////        }
//    }
//
//    @Override
//    public Page createEntityTemplate() {
//        return new Page();
//    }
//
//    public SkysailResponse<Page> addEntity(Page entity) {
//        Subject subject = SecurityUtils.getSubject();
//
//        Version version = new Version();
//        version.setContent(entity.getContent());
//        version.setOwner(subject.getPrincipal().toString());
//
////        ORecordId addedVersion = (ORecordId) app.getPagesRepo().save(version);
////
////        entity.setContent(null);
////        entity.getVersions().add(addedVersion.getIdentity().toString());
////
////        entity.setOwner(subject.getPrincipal().toString());
////        Space space = app.getSpacesRepo().getById(spaceId);
////
////        ORecordId added = (ORecordId) app.getPagesRepo().save(entity);
////
////        space.getPages().add(added.getIdentity().toString());
////        //app.getRepository().update(spaceId, space);
////   //     app.getSpacesRepo().update(space, "pages", "versions");
//
//        return new SkysailResponse<>();
//    }
//
//    @Override
//    public List<Link> getLinks() {
//        return super.getLinks(PostPageResource.class);
//    }
//
//    @Override
//    public String redirectTo() {
//        return super.redirectTo(SpacesResource.class);
//    }
//
//}
