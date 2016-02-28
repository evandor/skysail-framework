package io.skysail.server.app.um.db;

import java.util.*;

import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;

import de.twenty11.skysail.server.core.restlet.*;
import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.um.db.permissions.resources.*;
import io.skysail.server.app.um.db.repo.*;
import io.skysail.server.app.um.db.roles.resources.*;
import io.skysail.server.app.um.db.users.resources.*;
import io.skysail.server.menus.*;
import io.skysail.server.restlet.resources.SkysailServerResource;
import lombok.Getter;

@Component(immediate = true)
public class UmApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    private static final String APP_NAME = "um";

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    @Getter
    private volatile EventAdmin eventAdmin;

    @Getter
    private UserRepository userRepo;

    @Getter
    private RoleRepository roleRepo;

    @Getter
    private PermissionRepository permissionRepo;

    public UmApplication() {
        super(APP_NAME);
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/tag_yellow.png");
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY, target = "(name=UserRepository)")
    public void setUserRepository(DbRepository repo) {
        this.userRepo = (UserRepository) repo;
    }

    public void unsetUserRepository(DbRepository repo) {
        this.userRepo = null;
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY, target = "(name=RoleRepository)")
    public void setRoleRepository(DbRepository repo) {
        this.roleRepo = (RoleRepository) repo;
    }

    public void unsetRoleRepository(DbRepository repo) {
        this.roleRepo = null;
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY, target = "(name=PermissionRepository)")
    public void setPermissionRepository(DbRepository repo) {
        this.permissionRepo = (PermissionRepository) repo;
    }

    public void unsetPermissionRepository(DbRepository repo) {
        this.permissionRepo = null;
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

        router.attach(new RouteBuilder("/permissions", PermissionsResource.class));
        router.attach(new RouteBuilder("/permissions/", PostPermissionResource.class));
        router.attach(new RouteBuilder("/permissions/{id}", PermissionResource.class));
        router.attach(new RouteBuilder("/permissions/{id}/", PutPermissionResource.class));

    }

    public UserRepository getUserRepository() {
        return userRepo;
    }

    public List<Class<? extends SkysailServerResource<?>>> getMainLinks() {
        List<Class<? extends SkysailServerResource<?>>> result = new ArrayList<>();
        result.add(UsersResource.class);
        result.add(RolesResource.class);
        result.add(PermissionsResource.class);
        return result;
    }

}
