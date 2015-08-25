package de.twenty11.skysail.server.app.profile;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.*;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class ProfileResource extends EntityServerResource<Profile> {

	@Override
    public Profile getEntity() {
		Subject subject = SecurityUtils.getSubject();
	    return new Profile(subject.getPrincipal().toString());
    }

	@Override
    public String getId() {
	    return "";
    }

	@Override
    public SkysailResponse<?> eraseEntity() {
	    throw new IllegalAccessError();
    }

	@Override
	public List<Link> getLinks() {
	    Subject subject = SecurityUtils.getSubject();
	    if (!((String)getUsername(subject)).equals("demo")) {
	        return super.getLinks(PutPasswordResource.class);
	    }
	    return Collections.emptyList();
	}

	private Object getUsername(Subject subject) {
        return subject.getPrincipals().asList().get(1);
    }
}
