package io.skysail.server.app.crm.contacts;

import io.skysail.api.links.Link;
import io.skysail.server.app.crm.CrmApplication;

import java.util.List;
import java.util.function.Consumer;

import org.apache.shiro.SecurityUtils;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class ContactsResource extends ListServerResource<Contact> {

    private String id;

    private int page = 1;

    private CrmApplication app;

    public ContactsResource() {
        super(ContactResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "List of Contacts");
    }

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (CrmApplication) getApplication();
        String pageAsString = getQueryValue("page");
        if (pageAsString != null && pageAsString.trim().length() > 0) {
            page = Integer.parseInt(pageAsString);
        }
    }

    @Override
    public List<Contact> getEntity() {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return app.getRepository().findAll(Contact.class);
    }

    // @Override
    // public List<Contact> getData() {
    // int linesPerPage = 10;
    // String username = SecurityUtils.getSubject().getPrincipal().toString();
    //
    // Series<Header> headers = HeadersUtils.getHeaders(getResponse());
    // long clipCount =
    // ContactsRepository.getInstance().getContactsCount(username);
    // headers.add(new Header(HeadersUtils.PAGINATION_PAGES, Long.toString(1 +
    // Math.floorDiv(clipCount, linesPerPage))));
    // headers.add(new Header(HeadersUtils.PAGINATION_PAGE,
    // Integer.toString(page)));
    // headers.add(new Header(HeadersUtils.PAGINATION_HITS,
    // Long.toString(clipCount)));
    // return null;// ContactsRepository.getInstance().getContacts(page,
    // // username, linesPerPage);
    //
    // // return ContactsRepository.getInstance().getContacts();
    // }

    // @Override
    // protected List<String> getDataAsJson() {
    // Object principal = SecurityUtils.getSubject().getPrincipal();
    // // return
    // //
    // ContactsRepository.getInstance().getContactsAsJson(principal.toString());
    //
    // int linesPerPage = 5;
    // String username = SecurityUtils.getSubject().getPrincipal().toString();
    //
    // // Series<Header> headers = HeadersUtils.getHeaders(getResponse());
    // // long clipCount =
    // // ContactsRepository.getInstance().getContactsCount(username);
    // // headers.add(new Header(HeadersUtils.PAGINATION_PAGES, Long.toString(1
    // // + Math.floorDiv(clipCount, linesPerPage))));
    // // headers.add(new Header(HeadersUtils.PAGINATION_PAGE,
    // // Integer.toString(page)));
    // // headers.add(new Header(HeadersUtils.PAGINATION_HITS,
    // // Long.toString(clipCount)));
    // return CrmRepository.getInstance().getContacts(page, username,
    // linesPerPage, linesPerPage);
    //
    // }

    @Override
    public List<Link> getLinkheader() {
        return super.getLinkheader(PostContactResource.class);
    }

    @Override
    public Consumer<? super Link> getPathSubstitutions() {
        return l -> {
            l.substitute("id", id);
        };
    }
}
