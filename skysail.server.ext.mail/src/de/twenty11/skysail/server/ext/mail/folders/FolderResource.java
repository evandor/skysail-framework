package de.twenty11.skysail.server.ext.mail.folders;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

import de.twenty11.skysail.server.ext.mail.mails.MailsResource;

public class FolderResource extends EntityServerResource<MailFolder>{

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public MailFolder getEntity() {
        return new MailFolder("INBOX");
    }
    
    @Override
    public List<Link> getLinks() {
        return super.getLinks(MailsResource.class);
    }

}
