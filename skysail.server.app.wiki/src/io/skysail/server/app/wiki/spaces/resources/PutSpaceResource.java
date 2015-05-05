package io.skysail.server.app.wiki.spaces.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.wiki.PutDynamicEntityServerResource;
import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.app.wiki.spaces.Space;
import io.skysail.server.utils.OrientDbUtils;

import org.apache.commons.beanutils.DynaBean;

public class PutSpaceResource extends PutDynamicEntityServerResource<Space> {

    private String id;

    @Override
    protected void doInit() {
        id = getAttribute("id");
    }
    
    @Override
    public Space getEntity() {
        return ((WikiApplication) getApplication()).getRepository().getSpaceById(id);
    }

    @Override
    public SkysailResponse<?> updateEntity(Space entity) {
        DynaBean dynaBean = entity.getInstance();
        OrientDbUtils.updateFromDynamicEntity(entity);
        ((WikiApplication) getApplication()).getRepository().update(id, entity);
        return new SkysailResponse<>();
    }


}
