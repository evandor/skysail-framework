package de.twenty11.skysail.server.ext.mail;

import io.skysail.api.links.Link;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;

import de.twenty11.skysail.server.ext.mail.accounts.impl.AccountsResource;

public class MailRootResource extends ListServerResource<String> {

    @Override
    public List<String> getEntity() {
        return Collections.emptyList();
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(AccountsResource.class);
    }

}
