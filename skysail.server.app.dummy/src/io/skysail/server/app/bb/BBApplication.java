package io.skysail.server.app.bb;

import io.skysail.api.repos.DbRepository;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.bb.achievements.PostAchievementResource;
import io.skysail.server.app.bb.goals.f.*;
import io.skysail.server.app.bb.goals.hf.*;
import io.skysail.server.app.bb.goals.pg.*;
import io.skysail.server.app.bb.goals.rf.*;
import io.skysail.server.app.bb.goals.wc.*;
import io.skysail.server.menus.*;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.*;

import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;

@aQute.bnd.annotation.component.Component(immediate = true)
public class BBApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    private static final String APP_NAME = "bodybooster";

    private BBRepository bbRepo;

    @Reference(dynamic = true, multiple = false, optional = false, target = "(name=BodyboosterRepository)")
    public void setBodyboosterRepository(DbRepository repo) {
        this.bbRepo = (BBRepository) repo;
    }

    public void unsetBodyboosterRepository(DbRepository repo) {
        this.bbRepo = null;
    }

    @Override
    public BBRepository getRepository() {
        return this.bbRepo;
    }

    public BBApplication() {
        super(APP_NAME);
    }

    @Override
    protected void attach() {
        super.attach();

        router.attach(new RouteBuilder("/wc", WorkAndCareerGoalsResource.class));
        router.attach(new RouteBuilder("/wc/", PostWorkAndCareerGoalResource.class));
        router.attach(new RouteBuilder("/wc/{id}", WorkAndCareerGoalResource.class));
        router.attach(new RouteBuilder("/wc/{id}/", PutWorkAndCareerGoalResource.class));

        router.attach(new RouteBuilder("/rf", RecreationAndFreetimeGoalsResource.class));
        router.attach(new RouteBuilder("/rf/", PostRecreationAndFreetimeGoalResource.class));
        router.attach(new RouteBuilder("/rf/{id}", RecreationAndFreetimeGoalResource.class));
        router.attach(new RouteBuilder("/rf/{id}/", PutRecreationAndFreetimeGoalResource.class));

        router.attach(new RouteBuilder("/f",  FinanceGoalsResource.class));
        router.attach(new RouteBuilder("/f/",  PostFinanceGoalsResource.class));

        router.attach(new RouteBuilder("/hf", HealthAndFitnessGoalsResource.class));
        router.attach(new RouteBuilder("/hf/", PostHealthAndFitnessGoalsResource.class));

        router.attach(new RouteBuilder("/pg", PersonalGoalsResource.class));
        router.attach(new RouteBuilder("/pg/", PostPersonalGoalsResource.class));

        router.attach(new RouteBuilder("/wc/{id}/achievement/", PostAchievementResource.class));


    }

    @Override
    public List<MenuItem> getMenuEntries() {
        MenuItem menuItem = new MenuItem(APP_NAME, "/" + APP_NAME + getApiVersion().getVersionPath());
        menuItem.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        return Arrays.asList(menuItem);
    }

    public List<Class<? extends SkysailServerResource<?>>> getMainLinks() {
        List<Class<? extends SkysailServerResource<?>>> result = new ArrayList<>();
        result.add(WorkAndCareerGoalsResource.class);
        result.add(RecreationAndFreetimeGoalsResource.class);
        result.add(FinanceGoalsResource.class);
        result.add(HealthAndFitnessGoalsResource.class);
        result.add(PersonalGoalsResource.class);
        return result;
    }

}
