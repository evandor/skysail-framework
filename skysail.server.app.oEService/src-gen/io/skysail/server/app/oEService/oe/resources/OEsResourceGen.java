package io.skysail.server.app.oEService.oe.resources;

import io.skysail.server.db.DbClassName;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.*;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.oEService.*;

import io.skysail.server.app.oEService.oe.*;
import io.skysail.server.app.oEService.oe.resources.*;
import io.skysail.server.app.oEService.user.*;
import io.skysail.server.app.oEService.user.resources.*;


/**
 * generated from listResourceWithSelfReference.stg
 */
public class OEsResourceGen extends ListServerResource<io.skysail.server.app.oEService.oe.OE> {

    private OEServiceApplication app;
    private OERepository repository;

    public OEsResourceGen() {
        super(OEResourceGen.class, PostOEToNewOERelationResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list OEs");
    }

    public OEsResourceGen(Class<? extends OEResourceGen> cls) {
        super(cls);
    }

    @Override
    protected void doInit() {
        app = (OEServiceApplication) getApplication();
        repository = (OERepository) app.getRepository(io.skysail.server.app.oEService.oe.OE.class);
    }

    @Override
    public List<io.skysail.server.app.oEService.oe.OE> getEntity() {
        return (List<io.skysail.server.app.oEService.oe.OE>) repository.execute(io.skysail.server.app.oEService.oe.OE.class, "select * from " + DbClassName.of(io.skysail.server.app.oEService.oe.OE.class) + " where IN(OEs).size() = 0");
    }

    public List<Link> getLinks() {
              return super.getLinks(PostOEResourceGen.class,OEsResourceGen.class,UsersResourceGen.class);
    }
}