//package de.twenty11.skysail.server.app.profile;
//
//import java.util.*;
//
//import javax.security.auth.Subject;
//
//import io.skysail.api.links.Link;
//import io.skysail.api.responses.SkysailResponse;
//import io.skysail.server.restlet.resources.EntityServerResource;
//
//public class ProfileResource extends EntityServerResource<Profile> {
//
//	@Override
//    public Profile getEntity() {
//		Subject subject = SecurityUtils.getSubject();
//	    return new Profile(subject.getPrincipal().toString());
//    }
//
//	@Override
//    public String getId() {
//	    return "";
//    }
//
//	@Override
//    public SkysailResponse<?> eraseEntity() {
//	    throw new IllegalAccessError();
//    }
//
//	@Override
//	public List<Link> getLinks() {
//	    Subject subject = SecurityUtils.getSubject();
//	    if (!((String)getUsername(subject)).equals("demo")) {
//	        return null;//super.getLinks(PutPasswordResource.class);
//	    }
//	    return Collections.emptyList();
//	}
//
//	private Object getUsername(Subject subject) {
//        return subject.getPrincipals().asList().get(1);
//    }
//}
