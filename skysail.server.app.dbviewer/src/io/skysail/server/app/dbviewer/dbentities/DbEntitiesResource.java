package io.skysail.server.app.dbviewer.dbentities;

import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;

public class DbEntitiesResource extends ListServerResource<DbEntity> {

    @Override
    public List<DbEntity> getEntity() {
        return Collections.emptyList();
    }

}
