package de.twenty11.skysail.server.ext.mail;

import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

public class MailRootResource extends ListServerResource<String> {

    public MailRootResource() {
        super(null);
    }

    @Override
    public List<String> getEntity() {
        return null;
    }

//    @Override
//    public List<Link> getLinks() {
//        List<Link> links = super.getLinks();
//        links.add(new RelativeLink(getContext(), "mail/accounts", "accounts"));
//        return links;
//    }

}
