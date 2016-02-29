package io.skysail.server.app.dbviewer.dbclasses;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.dbviewer.*;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;
import java.util.stream.StreamSupport;

import org.restlet.resource.ResourceException;

import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.impls.orient.*;

public class DbClassesResource extends ListServerResource<DbClass> {

    private DbviewerApplication app;

    public DbClassesResource() {
        super(DbClassResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "show Vertices");
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        app = (DbviewerApplication) getApplication();
    }

    @Override
    public List<DbClass> getEntity() {
        List<DbClass> verticesNames = new ArrayList<>();//app.queryDb(connection, "SELECT expand(classes) FROM metadata:schema");
        Connection connection = (Connection) app.getRepository(Connection.class).findOne(getAttribute("connectionId"));

        OrientGraph graph = new OrientGraph(connection.getUrl(), connection.getUsername(), connection.getPassword());
        try {
            OrientDynaElementIterable execute = graph.command(new OCommandSQL("SELECT expand(classes) FROM metadata:schema")).execute();
            StreamSupport.stream(execute.spliterator(), false).forEach(v -> {
                verticesNames.add(new DbClass((OrientVertex)v));
            });
            Collections.sort(verticesNames);
        } finally {
            graph.shutdown();
        }

        return verticesNames;
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(ConnectionsResource.class);
    }

}
