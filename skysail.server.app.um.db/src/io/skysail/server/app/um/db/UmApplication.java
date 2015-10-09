package io.skysail.server.app.um.db;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.um.db.repo.*;
import io.skysail.server.app.um.db.roles.resources.*;
import io.skysail.server.app.um.db.users.resources.*;
import io.skysail.server.menus.*;
import io.skysail.server.repo.DbRepository;

import java.util.*;

import lombok.Getter;
import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.*;

@Component(immediate = true)
public class UmApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    private static final String APP_NAME = "um";

    @Getter
    private UserRepository userRepo;

    @Getter
    private RoleRepository roleRepo;

    public UmApplication() {
        super(APP_NAME);
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/tag_yellow.png");
    }

    @Reference(dynamic = true, multiple = false, optional = false, target = "(name=UserRepository)")
    public void setUserRepository(DbRepository repo) {
        this.userRepo = (UserRepository) repo;
    }

    public void unsetUserRepository(DbRepository repo) {
        this.userRepo = null;
    }

    @Reference(dynamic = true, multiple = false, optional = false, target = "(name=RoleRepository)")
    public void setRoleRepository(DbRepository repo) {
        this.roleRepo = (RoleRepository) repo;
    }

    public void unsetRoleRepository(DbRepository repo) {
        this.roleRepo = null;
    }

    @Override
    public List<MenuItem> getMenuEntries() {
        MenuItem appMenu = new MenuItem(APP_NAME, "/" + APP_NAME + getApiVersion().getVersionPath(), this);
        appMenu.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        return Arrays.asList(appMenu);
    }

    @Override
    protected void attach() {
        super.attach();

        router.attach(new RouteBuilder("", UsersResource.class));

        router.attach(new RouteBuilder("/users", UsersResource.class));
        router.attach(new RouteBuilder("/users/", PostUserResource.class));
        router.attach(new RouteBuilder("/users/{id}", UserResource.class));
        router.attach(new RouteBuilder("/users/{id}/", PutUserResource.class));

        router.attach(new RouteBuilder("/roles", RolesResource.class));
        router.attach(new RouteBuilder("/roles/", PostRoleResource.class));
        router.attach(new RouteBuilder("/roles/{id}", RoleResource.class));
        router.attach(new RouteBuilder("/roles/{id}/", PutRoleResource.class));
    }

    @Override
    public UserRepository getRepository() {
        return userRepo;
    }

}
