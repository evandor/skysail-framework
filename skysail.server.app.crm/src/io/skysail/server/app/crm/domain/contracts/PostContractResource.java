package io.skysail.server.app.crm.domain.contracts;

import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.PostEntityServerResource;

public class PostContractResource extends PostEntityServerResource<Contract> {

    @Override
    public Contract createEntityTemplate() {
        return new Contract();
    }

    @Override
    public SkysailResponse<?> addEntity(Contract entity) {
        ContractsRepository.getInstance().add(entity);
        return new SkysailResponse<String>();
    }
}
