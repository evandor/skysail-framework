package io.skysail.server.designer.demo.folders;

import java.util.*;

import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;

import io.skysail.server.app.*;
import de.twenty11.skysail.server.core.restlet.*;
import io.skysail.domain.Identifiable;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.*;
import io.skysail.server.menus.MenuItemProvider;

public class FoldersApplicationGen extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "Folders";

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private volatile EventAdmin eventAdmin;

    public FoldersApplicationGen(String name, ApiVersion apiVersion, List<Class<? extends Identifiable>>  entityClasses) {
        super(name, apiVersion, entityClasses);
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
    public void setRepositories(Repositories repos) {
        super.setRepositories(repos);
    }

    public void unsetRepositories(Repositories repo) {
        super.setRepositories(null);
    }



    @Override
    protected void attach() {
        super.attach();
        router.attach(new RouteBuilder("/Folders/{id}", io.skysail.server.designer.demo.folders.FolderResource.class));
        router.attach(new RouteBuilder("/Folders/", io.skysail.server.designer.demo.folders.PostFolderResource.class));
        router.attach(new RouteBuilder("/Folders/{id}/", io.skysail.server.designer.demo.folders.PutFolderResource.class));
        router.attach(new RouteBuilder("/Folders", io.skysail.server.designer.demo.folders.FoldersResource.class));
        router.attach(new RouteBuilder("", io.skysail.server.designer.demo.folders.FoldersResource.class));
        router.attach(new RouteBuilder("/Folders/{id}/Folders", io.skysail.server.designer.demo.folders.FoldersFoldersResource.class));
        router.attach(new RouteBuilder("/Folders/{id}/Folders/", io.skysail.server.designer.demo.folders.PostFolderToNewFolderRelationResource.class));
        router.attach(new RouteBuilder("/Folders/{id}/Folders/{targetId}", io.skysail.server.designer.demo.folders.FoldersFolderResource.class));

    }

    public EventAdmin getEventAdmin() {
        return eventAdmin;
    }

}