package io.skysail.server.designer.presentation;

import org.restlet.resource.ResourceException;

import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutItemResource extends PutEntityServerResource<io.skysail.server.designer.presentation.Item> {


    private String id;
    private PresentationApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (PresentationApplication)getApplication();
    }

    @Override
    public void updateEntity(Item  entity) {
        io.skysail.server.designer.presentation.Item original = getEntity();
        copyProperties(original,entity);

        app.getRepository(io.skysail.server.designer.presentation.Item.class).update(id, original);
    }

    @Override
    public io.skysail.server.designer.presentation.Item getEntity() {
        return (io.skysail.server.designer.presentation.Item)app.getRepository(io.skysail.server.designer.presentation.Item.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ItemsResource.class);
    }
}