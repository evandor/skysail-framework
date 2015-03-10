package io.skysail.server.app.crm.contracts;

import io.skysail.server.app.crm.CrmRepository;

import java.util.Set;

import org.restlet.data.Form;

import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.beans.EntityDynaProperty;
import de.twenty11.skysail.server.core.restlet.PostEntityServerResource;

public class PostContractResource extends PostEntityServerResource<Contract> {

    @Override
    public Contract createEntityTemplate() {
        return new Contract();
    }

    @Override
    protected Contract populate(Contract bean, Form form) {
        Set<EntityDynaProperty> properties = Contract.getProperties();
        properties.stream().forEach(p -> {
            String value = form.getFirstValue(p.getName());
            if (value != null) {
                bean.getInstance().set(p.getName(), value);
            }
        });
        return bean;
    }

    @Override
    public SkysailResponse<?> addEntity(Contract entity) {

        CrmRepository.addAsDocument(null);
        return new SkysailResponse<String>();
    }
}
