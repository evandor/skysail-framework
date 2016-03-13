package io.skysail.server.app.loop;

import java.util.*;

import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;

import io.skysail.server.app.*;
import de.twenty11.skysail.server.core.restlet.*;
import io.skysail.domain.Identifiable;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.*;
import io.skysail.server.menus.MenuItemProvider;

public class LoopApplicationGen extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "Loop";

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private volatile EventAdmin eventAdmin;

    public LoopApplicationGen(String name, ApiVersion apiVersion, List<Class<? extends Identifiable>>  entityClasses) {
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
        router.attach(new RouteBuilder("/Timetables/{id}", io.skysail.server.app.loop.timetable.resources.TimetableResourceGen.class));
        router.attach(new RouteBuilder("/Timetables/", io.skysail.server.app.loop.timetable.resources.PostTimetableResourceGen.class));
        router.attach(new RouteBuilder("/Timetables/{id}/", io.skysail.server.app.loop.timetable.resources.PutTimetableResourceGen.class));
        router.attach(new RouteBuilder("/Timetables", io.skysail.server.app.loop.timetable.resources.TimetablesResourceGen.class));
        router.attach(new RouteBuilder("", io.skysail.server.app.loop.timetable.resources.TimetablesResourceGen.class));
        router.attach(new RouteBuilder("/Timetables/{id}/Entrys", io.skysail.server.app.loop.timetable.TimetablesEntrysResource.class));
        router.attach(new RouteBuilder("/Timetables/{id}/Entrys/", io.skysail.server.app.loop.timetable.PostTimetableToNewEntryRelationResource.class));
        router.attach(new RouteBuilder("/Timetables/{id}/Entrys/{targetId}", io.skysail.server.app.loop.timetable.TimetablesEntryResource.class));
        router.attach(new RouteBuilder("/Entrys/{id}", io.skysail.server.app.loop.entry.resources.EntryResourceGen.class));
        router.attach(new RouteBuilder("/Entrys/", io.skysail.server.app.loop.entry.resources.PostEntryResourceGen.class));
        router.attach(new RouteBuilder("/Entrys/{id}/", io.skysail.server.app.loop.entry.resources.PutEntryResourceGen.class));
        router.attach(new RouteBuilder("/Entrys", io.skysail.server.app.loop.entry.resources.EntrysResourceGen.class));
        router.attach(new RouteBuilder("", io.skysail.server.app.loop.entry.resources.EntrysResourceGen.class));

    }

    public EventAdmin getEventAdmin() {
        return eventAdmin;
    }

}