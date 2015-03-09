package io.skysail.server.app.crm.contracts;

import io.skysail.server.app.crm.ContactsGen;

import java.util.List;
import java.util.function.Consumer;

import org.apache.shiro.SecurityUtils;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class ContractsResource extends ListServerResource<Contract> {

    private String id;

    private int page = 1;

    private ContactsGen app;

    public ContractsResource() {
        // super(ContactResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "List of Contracts");
    }

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (ContactsGen) getApplication();
        String pageAsString = getQueryValue("page");
        if (pageAsString != null && pageAsString.trim().length() > 0) {
            page = Integer.parseInt(pageAsString);
        }
    }

    @Override
    public List<Contract> getEntity() {
        int linesPerPage = 10;
        String username = SecurityUtils.getSubject().getPrincipal().toString();

        // Series<Header> headers = HeadersUtils.getHeaders(getResponse());
        // long clipCount =
        // ContactsRepository.getInstance().getContactsCount(username);
        // headers.add(new Header(HeadersUtils.PAGINATION_PAGES, Long.toString(1
        // + Math.floorDiv(clipCount, linesPerPage))));
        // headers.add(new Header(HeadersUtils.PAGINATION_PAGE,
        // Integer.toString(page)));
        // headers.add(new Header(HeadersUtils.PAGINATION_HITS,
        // Long.toString(clipCount)));
        // return null;// ContactsRepository.getInstance().getContacts(page,
        // username, linesPerPage);

        return app.getRepository().findAll(Contract.class);
    }

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
    // return CrmRepository.getInstance().getContracts(page, username,
    // linesPerPage, linesPerPage);
    // }

    @Override
    public List<Linkheader> getLinkheader() {
        return super.getLinkheader(PostContractResource.class);
    }

    @Override
    public Consumer<? super Linkheader> getPathSubstitutions() {
        return l -> {
            l.substitute("id", id);
        };
    }
}
