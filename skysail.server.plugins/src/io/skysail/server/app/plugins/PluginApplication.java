package io.skysail.server.app.plugins;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.plugins.features.Feature;
import io.skysail.server.app.plugins.features.FeaturesRepository;
import io.skysail.server.app.plugins.features.FeaturesResource;
import io.skysail.server.app.plugins.installations.PostInstallationResource;
import io.skysail.server.app.plugins.obr.ObrRepository;
import io.skysail.server.app.plugins.obr.PostResolverResource;
import io.skysail.server.app.plugins.obr.RepositoriesResource;
import io.skysail.server.app.plugins.obr.RepositoryResource;
import io.skysail.server.app.plugins.query.PostQueryResource;
import io.skysail.server.app.plugins.resources.ResourceResource;
import io.skysail.server.app.plugins.resources.ResourcesResource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.apache.felix.bundlerepository.RepositoryAdmin;
import org.apache.felix.bundlerepository.Resource;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.InvalidSyntaxException;

import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.ApplicationContextId;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import de.twenty11.skysail.server.services.MenuItem;
import de.twenty11.skysail.server.services.MenuItemProvider;

@aQute.bnd.annotation.component.Component(immediate = true)
@Slf4j
public class PluginApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    private static final String PLUGINS = "plugins";

    private FeaturesRepository featuresRepository;

    private RepositoryAdmin repositoryAdmin;

    public PluginApplication() {
        super(PLUGINS);
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/plugin.png");
    }

    @Override
    protected void attach() {
        super.attach();

        router.setAuthorizationDefaults(anyOf("admin"));

        router.attach(new RouteBuilder("", PluginRootResource.class).authorizeWith(anyOf("admin")));
        router.attach(new RouteBuilder("/", PluginRootResource.class).authorizeWith(anyOf("admin")));
        router.attach(new RouteBuilder("/features/", FeaturesResource.class));
        router.attach(new RouteBuilder("/features/{id}/installations/", PostInstallationResource.class));
        router.attach(new RouteBuilder("/obr/repos/", RepositoriesResource.class));
        router.attach(new RouteBuilder("/obr/repos/{id}", RepositoryResource.class));
        router.attach(new RouteBuilder("/obr/resolver/", PostResolverResource.class));

        router.attach(new RouteBuilder("/query/", PostQueryResource.class));

        router.attach(new RouteBuilder("/resources", ResourcesResource.class));
        router.attach(new RouteBuilder("/resources/{id}", ResourceResource.class));

    }

    @Override
    public List<MenuItem> getMenuEntries() {
        MenuItem menuItem = new MenuItem(getApplication(), PluginRootResource.class);
        menuItem.setCategory(MenuItem.Category.ADMIN_MENU);
        return Arrays.asList(menuItem);
    }

    @Reference(dynamic = true, multiple = false, optional = true)
    public void setRepositoryAdmin(RepositoryAdmin repositoryAdmin) {
        this.repositoryAdmin = repositoryAdmin;
    }

    public void unsetRepositoryAdmin(RepositoryAdmin repositoryAdmin) {
        this.repositoryAdmin = null;
    }

    public synchronized FeaturesRepository getFeaturesRepository() {
        if (featuresRepository == null) {
            featuresRepository = new FeaturesRepository();
        }
        return featuresRepository;
    }

    public synchronized void install(Feature feature) {
        List<Bundle> installedBundles = new ArrayList<>();
        feature.getLocations().stream().forEach(location -> {
            try {
                installedBundles.add(install(location));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        installedBundles.stream().forEach(b -> {
            try {
                b.start();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        });
        if (feature.getLandingPage() == null) {
            return;
        }
        MenuItem menuItem = new MenuItem(feature.getName(), feature.getLandingPage());
        menuItem.setCategory(MenuItem.Category.APPLICATION_MAIN_MENU);
        menuItem.setOpenInNewWindow(feature.isOpenInNewWindow());
        // TODO menuItems.add
    }

    private synchronized Bundle install(String location) throws BundleException {
        if (getBundleContext() == null) {
            return null;
        }
        log.info("about to install '{}'", location.trim());
        Bundle installedBundle = getBundleContext().installBundle(location.trim());
        if (installedBundle != null) {
            log.info("bundle '{}' install with id '{}'", location.trim(), installedBundle.getBundleId());
        }
        return installedBundle;
    }

    public synchronized List<ObrRepository> getReposList() {
        if (repositoryAdmin == null) {
            return Collections.emptyList();
        }
        return Arrays.stream(repositoryAdmin.listRepositories()).map(r -> new ObrRepository(r))
                .collect(Collectors.toList());
    }

    public List<Resource> discoverResources(String searchFor) {
        try {
            return Arrays.asList(repositoryAdmin.discoverResources(searchFor));
        } catch (InvalidSyntaxException e) {
            log.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

}
