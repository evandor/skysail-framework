//package io.skysail.server.app.crm.contracts;
//
//import io.skysail.api.links.Link;
//import io.skysail.server.app.crm.CrmApplication;
//import io.skysail.server.restlet.resources.ListServerResource;
//
//import java.util.List;
//import java.util.function.Consumer;
//
//import org.apache.shiro.SecurityUtils;
//import org.restlet.resource.ResourceException;
//
//import de.twenty11.skysail.server.core.restlet.ResourceContextId;
//
//public class ContractsResource extends ListServerResource<Contract> {
//
//    private String id;
//
//    private int page = 1;
//
//    private CrmApplication app;
//
//    public ContractsResource() {
//        // super(ContactResource.class);
//        addToContext(ResourceContextId.LINK_TITLE, "List of Contracts");
//    }
//
//    @Override
//    protected void doInit() throws ResourceException {
//        id = getAttribute("id");
//        app = (CrmApplication) getApplication();
//        String pageAsString = getQueryValue("page");
//        if (pageAsString != null && pageAsString.trim().length() > 0) {
//            page = Integer.parseInt(pageAsString);
//        }
//    }
//
//    @Override
//    public List<Contract> getEntity() {
//        int linesPerPage = 10;
//        String username = SecurityUtils.getSubject().getPrincipal().toString();
//
//        // Series<Header> headers = HeadersUtils.getHeaders(getResponse());
//        // long clipCount =
//        // ContactsRepository.getInstance().getContactsCount(username);
//        // headers.add(new Header(HeadersUtils.PAGINATION_PAGES, Long.toString(1
//        // + Math.floorDiv(clipCount, linesPerPage))));
//        // headers.add(new Header(HeadersUtils.PAGINATION_PAGE,
//        // Integer.toString(page)));
//        // headers.add(new Header(HeadersUtils.PAGINATION_HITS,
//        // Long.toString(clipCount)));
//        // return null;// ContactsRepository.getInstance().getContacts(page,
//        // username, linesPerPage);
//
//        return app.getRepository().findAll(Contract.class);
//    }
//
//    @Override
//    public List<Link> getLinks() {
//        return super.getLinks(PostContractResource.class);
//    }
//
//    @Override
//    public Consumer<? super Link> getPathSubstitutions() {
//        return l -> {
//            l.substitute("id", id);
//        };
//    }
//}
