//package io.skysail.server.app.designer.matrix;
//
//import io.skysail.api.links.Link;
//import io.skysail.server.queryfilter.Filter;
//import io.skysail.server.restlet.resources.ListServerResource;
//
//import java.util.List;
//
//import de.twenty11.skysail.server.core.restlet.ResourceContextId;
//
//public class ContactsResource2 extends ListServerResource<Contact> {
//
//    private io.skysail.server.app.designer.matrix.MatrixApplication app;
//    private ContactRepo repository;
//
//    public ContactsResource2() {
//        super(ContactResource.class);
//        addToContext(ResourceContextId.LINK_TITLE, "List of Contacts");
//    }
//
//    protected void doInit() {
//        super.doInit();
//        app = (io.skysail.server.app.designer.matrix.MatrixApplication)getApplication();
//        repository = (ContactRepo) app.getRepository(Contact.class);
//    }
//
//    @Override
//    public List<Contact> getEntity() {
//        return repository.find(new Filter());
//    }
//
//    @Override
//    public List<Link> getLinks() {
//        return super.getLinks(PostContactResource.class, ContactsResource.class);
//    }
//}
