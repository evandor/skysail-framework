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

import io.skysail.server.designer.demo.folders.*;

import io.skysail.server.designer.demo.folders.folder.*;
import io.skysail.server.designer.demo.folders.folder.resources.*;


/**
 * generated from application.stg
 */
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
        router.attach(new RouteBuilder("/Folders/{id}", FolderResourceGen.class));
        router.attach(new RouteBuilder("/Folders/", PostFolderResourceGen.class));
        router.attach(new RouteBuilder("/Folders/{id}/", PutFolderResourceGen.class));
        router.attach(new RouteBuilder("/Folders", FoldersResourceGen.class));
        router.attach(new RouteBuilder("", io.skysail.server.designer.demo.folders.folder.resources.FoldersResourceGen.class));
        router.attach(new RouteBuilder("/Folders/{id}/Folders", FoldersFoldersResource.class));
        router.attach(new RouteBuilder("/Folders/{id}/Folders/", PostFolderToNewFolderRelationResource.class));
        router.attach(new RouteBuilder("/Folders/{id}/Folders/{targetId}", FoldersFolderResource.class));

    }

    public EventAdmin getEventAdmin() {
        return eventAdmin;
    }

}