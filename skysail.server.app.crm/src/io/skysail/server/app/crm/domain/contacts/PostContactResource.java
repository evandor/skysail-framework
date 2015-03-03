package io.skysail.server.app.crm.domain.contacts;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.restlet.data.Form;
import org.restlet.data.Parameter;

import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostContactResource extends PostEntityServerResource<Contact> {

    public PostContactResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new Contact");
    }

    @Override
    public Contact createEntityTemplate() {
        return new Contact();
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
        worksFor.add(worksForFromForm.getValue());
        data.setWorksFor(worksFor);
        return data;
    }

    @Override
    public SkysailResponse<?> addEntity(Contact entity) {
        entity.setOwner(SecurityUtils.getSubject().getPrincipal().toString());
        entity = ContactsRepository.getInstance().add(entity);
        return new SkysailResponse<String>();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ContactsResource.class);
    }
}