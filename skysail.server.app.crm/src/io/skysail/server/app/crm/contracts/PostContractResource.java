package io.skysail.server.app.crm.contracts;

import io.skysail.server.app.crm.CrmRepository;

import java.util.Set;

import org.restlet.data.Form;

import com.orientechnologies.orient.core.record.impl.ODocument;

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

        ODocument doc = new ODocument("Person");
        doc.field("name", "Luke");
        doc.field("surname", "Skywalker");
        doc.field("city", new ODocument("City").field("name", "Rome").field("country", "Italy"));

        CrmRepository.add(doc);
        return new SkysailResponse<String>();
    }
}
