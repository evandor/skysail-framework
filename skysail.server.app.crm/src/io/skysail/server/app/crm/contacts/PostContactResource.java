package io.skysail.server.app.crm.contacts;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.crm.CrmRepository;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import java.util.*;

import org.apache.shiro.SecurityUtils;
import org.restlet.data.*;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostContactResource extends PostEntityServerResource<Contact> {

    public PostContactResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new Contact");
    }

    @Override
    public Contact createEntityTemplate() {
        String creator = SecurityUtils.getSubject().getPrincipal().toString();
        return new Contact(creator);
    }

    @Override
    public Contact getData(Form form) {
        // TODO need 1.9.3 of beanutils:
        // https://issues.apache.org/jira/browse/BEANUTILS-465
        Parameter worksForFromForm = form.getFirst("worksFor");
        form.removeFirst("worksFor");
        Contact data = super.getData(form);
        List<String> worksFor = data.getWorksFor();
        if (worksFor == null) {
            worksFor = new ArrayList<String>();
        }
        if (worksForFromForm != null) {
            worksFor.add(worksForFromForm.getValue());
        }
        data.setWorksFor(worksFor);
        return data;
    }

    @Override
    public SkysailResponse<Contact> addEntity(Contact entity) {
        entity.setOwner(SecurityUtils.getSubject().getPrincipal().toString());
        CrmRepository.add(entity, "worksFor");
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ContactsResource.class);
    }
}