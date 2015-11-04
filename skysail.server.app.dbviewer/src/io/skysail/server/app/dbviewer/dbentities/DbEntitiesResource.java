package io.skysail.server.app.dbviewer.dbentities;

import io.skysail.server.app.dbviewer.*;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;
import java.util.stream.StreamSupport;

import org.restlet.resource.ResourceException;

import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.impls.orient.*;

public class DbEntitiesResource extends ListServerResource<DbEntity> {

    private DbviewerApplication app;

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        app = (DbviewerApplication) getApplication();
    }

    @Override
    public List<DbEntity> getEntity() {
        List<DbEntity> verticesNames = new ArrayList<>();
        Connection connection = (Connection) app.getRepository(Connection.class).findOne(getAttribute("connectionId"));

        OrientGraph graph = new OrientGraph(connection.getUrl(), connection.getUsername(), connection.getPassword());
        try {
            OrientDynaElementIterable execute = graph.command(new OCommandSQL("SELECT * FROM " + getAttribute("id"))).execute();
            StreamSupport.stream(execute.spliterator(), false).forEach(v -> {
                OrientVertex vertex = (OrientVertex)v;
                verticesNames.add(new DbEntity(getAttribute("id"), vertex.getRecord().toMap()));
            });
            //Collections.sort(verticesNames);
        } finally {
            graph.shutdown();
        }

        return verticesNames;
    }

}
