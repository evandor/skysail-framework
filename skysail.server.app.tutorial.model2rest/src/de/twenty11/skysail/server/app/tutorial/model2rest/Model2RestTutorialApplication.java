package de.twenty11.skysail.server.app.tutorial.model2rest;

import io.skysail.api.links.Link;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.*;

import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.app.tutorial.model2rest.conclusion.ConclusionResource;
import de.twenty11.skysail.server.app.tutorial.model2rest.home.TutorialHomeResource;
import de.twenty11.skysail.server.app.tutorial.model2rest.step3.Step3DemoResource;
import de.twenty11.skysail.server.app.tutorial.model2rest.step4.Step4DemoResource;
import de.twenty11.skysail.server.app.tutorial.model2rest.step5.*;
import de.twenty11.skysail.server.app.tutorial.model2rest.step6.StepDemoResource;
import de.twenty11.skysail.server.core.restlet.*;
import de.twenty11.skysail.server.services.*;

@aQute.bnd.annotation.component.Component(immediate = true)
public class Model2RestTutorialApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    private static final String APP_NAME = "tutorialM2R";

    public Model2RestTutorialApplication() {
        super(APP_NAME);
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/help.png");
    }

    @Override
    protected void attach() {
        super.attach();

        router.attach(new RouteBuilder("", TutorialHomeResource.class).noAuthenticationNeeded());
        router.attach(new RouteBuilder("/", TutorialHomeResource.class).noAuthenticationNeeded());

        router.attach(new RouteBuilder("/step1", de.twenty11.skysail.server.app.tutorial.model2rest.step1.TodoResource.class).noAuthenticationNeeded());
        router.attach(new RouteBuilder("/step1/", de.twenty11.skysail.server.app.tutorial.model2rest.step1.PostTodoResource.class).noAuthenticationNeeded());

        router.attach(new RouteBuilder("/step2", de.twenty11.skysail.server.app.tutorial.model2rest.step2.TodoResource.class).noAuthenticationNeeded());
        router.attach(new RouteBuilder("/step2/", de.twenty11.skysail.server.app.tutorial.model2rest.step2.PostTodoResource.class).noAuthenticationNeeded());

        router.attach(new RouteBuilder("/step3", de.twenty11.skysail.server.app.tutorial.model2rest.step3.TodoResource.class).noAuthenticationNeeded());
        router.attach(new RouteBuilder("/step3/", de.twenty11.skysail.server.app.tutorial.model2rest.step3.PostTodoResource.class).noAuthenticationNeeded());
        router.attach(new RouteBuilder("/step3/{id}", de.twenty11.skysail.server.app.tutorial.model2rest.step3.TodoResource.class).noAuthenticationNeeded());
        router.attach(new RouteBuilder("/step3demo", Step3DemoResource.class).noAuthenticationNeeded());

        router.attach(new RouteBuilder("/step4", de.twenty11.skysail.server.app.tutorial.model2rest.step4.TodoResource.class).noAuthenticationNeeded());
        router.attach(new RouteBuilder("/step4/", de.twenty11.skysail.server.app.tutorial.model2rest.step4.PostTodoResource.class).noAuthenticationNeeded());
        router.attach(new RouteBuilder("/step4demo", Step4DemoResource.class).noAuthenticationNeeded());

        router.attach(new RouteBuilder("/step5", de.twenty11.skysail.server.app.tutorial.model2rest.step5.TodoResource.class).noAuthenticationNeeded());
        router.attach(new RouteBuilder("/step5/", de.twenty11.skysail.server.app.tutorial.model2rest.step5.PostTodoResource.class).noAuthenticationNeeded());
        router.attach(new RouteBuilder("/step5/{id}", de.twenty11.skysail.server.app.tutorial.model2rest.step5.TodoResource.class).noAuthenticationNeeded());
        router.attach(new RouteBuilder("/step5/{id}/", PutTodoResource.class).noAuthenticationNeeded());
        router.attach(new RouteBuilder("/step5demo", Step5DemoResource.class).noAuthenticationNeeded());

        router.attach(new RouteBuilder("/step6", de.twenty11.skysail.server.app.tutorial.model2rest.step6.TodoResource.class).noAuthenticationNeeded());
        router.attach(new RouteBuilder("/step6/", de.twenty11.skysail.server.app.tutorial.model2rest.step6.PostTodoResource.class).noAuthenticationNeeded());
        router.attach(new RouteBuilder("/step6demo", StepDemoResource.class).noAuthenticationNeeded());

        router.attach(new RouteBuilder("/conclusion", ConclusionResource.class).noAuthenticationNeeded());
    }

    @Override
    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem("Tutorial: Model2Rest", "/"+APP_NAME + "/v1", this);
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        return Arrays.asList(appMenu);
    }

    public List<Link> getAppNavigation(SkysailServerResource<?> resource) {
        List<Link> linkheader = new ArrayList<>();
//        linkheader.add(getLinkheader(resource, TutorialHomeResource.class).role(LinkRole.APPLICATION_NAVIGATION).setShowInHtml(false));
//        linkheader.add(getLinkheader(resource, de.twenty11.skysail.server.app.tutorial.model2rest.step1.TodoResource.class).setRole(LinkheaderRole.APPLICATION_NAVIGATION).setShowInHtml(false));
//        linkheader.add(getLinkheader(resource, de.twenty11.skysail.server.app.tutorial.model2rest.step2.TodoResource.class).setRole(LinkheaderRole.APPLICATION_NAVIGATION).setShowInHtml(false));
//        linkheader.add(getLinkheader(resource, de.twenty11.skysail.server.app.tutorial.model2rest.step3.TodoResource.class).setRole(LinkheaderRole.APPLICATION_NAVIGATION).setShowInHtml(false));
//        linkheader.add(getLinkheader(resource, de.twenty11.skysail.server.app.tutorial.model2rest.step4.TodoResource.class).setRole(LinkheaderRole.APPLICATION_NAVIGATION).setShowInHtml(false));
//        linkheader.add(getLinkheader(resource, de.twenty11.skysail.server.app.tutorial.model2rest.step5.TodoResource.class).setRole(LinkheaderRole.APPLICATION_NAVIGATION).setShowInHtml(false));
//        linkheader.add(getLinkheader(resource, de.twenty11.skysail.server.app.tutorial.model2rest.step6.TodoResource.class).setRole(LinkheaderRole.APPLICATION_NAVIGATION).setShowInHtml(false));
//        linkheader.add(getLinkheader(resource, ConclusionResource.class).setRole(LinkRole.APPLICATION_NAVIGATION).setShowInHtml(false));
        return linkheader;
    }

    private Link getLinkheader(SkysailServerResource<?> resource, Class<? extends SkysailServerResource<?>> resourceClass) {
        Link link = resource.getLinks(resourceClass).get(0);
        if (resource.getClass().equals(resourceClass)) {
            //link.set(LinkRelation.SELF);
        }
        return link;
    }
}
