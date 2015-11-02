package io.skysail.server.app.dbviewer.dbclasses;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.dbviewer.DbviewerApplication;
import io.skysail.server.app.dbviewer.dbentities.DbEntitiesResource;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

import org.restlet.resource.ResourceException;

public class DbClassResource extends EntityServerResource<DbClass> {

    private DbviewerApplication app;

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        app = (DbviewerApplication) getApplication();
        getAttribute("id");
        getAttribute("name");
    }

    @Override
    public DbClass getEntity() {
//        List<DbClass> verticesNames = new ArrayList<>();//app.queryDb(connection, "SELECT expand(classes) FROM metadata:schema");
//        Connection connection = app.getConnectionRepository().findOne(getAttribute("id"));
//
//        OrientGraph graph = new OrientGraph(connection.getUrl(), connection.getUsername(), connection.getPassword());
//        try {
//            OrientDynaElementIterable execute = graph.command(new OCommandSQL("SELECT expand(classes) FROM metadata:schema")).execute();
//            StreamSupport.stream(execute.spliterator(), false).forEach(v -> {
//                verticesNames.add(new DbClass((OrientVertex)v));
//            });
//            Collections.sort(verticesNames);
//        } finally {
//            graph.shutdown();
//        }

        return new DbClass(getAttribute("name"));// verticesNames;
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        throw new RuntimeException("operation not supported");
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(DbEntitiesResource.class);
    }
}
