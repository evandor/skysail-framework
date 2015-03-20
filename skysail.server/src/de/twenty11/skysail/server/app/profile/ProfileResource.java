package de.twenty11.skysail.server.app.profile;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import de.twenty11.skysail.server.core.restlet.EntityServerResource;

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
	public List<Link> getLinkheader() {
	    return super.getLinkheader(PutPasswordResource.class);
	}

}
