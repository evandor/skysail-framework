package io.skysail.server.app.plugins;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.plugins.features.*;
import io.skysail.server.app.plugins.installations.PostInstallationResource;
import io.skysail.server.app.plugins.obr.*;
import io.skysail.server.app.plugins.query.PostQueryResource;
import io.skysail.server.app.plugins.resources.*;
import io.skysail.server.menus.*;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.apache.felix.bundlerepository.*;
import org.apache.felix.bundlerepository.Resource;
import org.osgi.framework.*;

import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.restlet.*;

@aQute.bnd.annotation.component.Component(immediate = true)
@Slf4j
public class PluginApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    private static final String PLUGINS = "plugins";
    private static final String APP_NAME = "plugins";

    private FeaturesRepository featuresRepository;
    private AtomicReference<RepositoryAdmin> repositoryAdmin = new AtomicReference<>();

    public PluginApplication() {
        super(PLUGINS);
        addToAppContext(ApplicationContextId.IMG, "/static/img/silk/plugin.png");
    }

    @Override
    protected void attach() {
        super.attach();

       // router.setAuthorizationDefaults(anyOf("admin"));

        router.attach(new RouteBuilder("", PluginRootResource.class));//.authorizeWith(anyOf("admin")));
        router.attach(new RouteBuilder("/", PluginRootResource.class));//.authorizeWith(anyOf("admin")));
        router.attach(new RouteBuilder("/features/", FeaturesResource.class));
        router.attach(new RouteBuilder("/features/{id}/installations/", PostInstallationResource.class));

        router.attach(new RouteBuilder("/repos/", RepositoriesResource.class));
        router.attach(new RouteBuilder("/repos/{id}", RepositoryResource.class));
        router.attach(new RouteBuilder("/repos/{id}/resources", ResourcesResource.class));
        router.attach(new RouteBuilder("/repos/{id}/resources/{resourceId}", ResourceResource.class));

        router.attach(new RouteBuilder("/resolver/", PostResolverResource.class));

        router.attach(new RouteBuilder("/query/", PostQueryResource.class));

        router.attach(new RouteBuilder("/resources", OldResourcesResource.class));
        router.attach(new RouteBuilder("/resources/{id}", OldResourceResource.class));

    }

    @Override
    public List<MenuItem> getMenuEntries() {
        MenuItem menuItem = new MenuItem(APP_NAME, "/" + APP_NAME + getApiVersion().getVersionPath(), this);
        menuItem.setCategory(MenuItem.Category.ADMIN_MENU);
        return Arrays.asList(menuItem);
    }

    @Reference(dynamic = true, multiple = false, optional = true)
    public void setRepositoryAdmin(RepositoryAdmin repositoryAdmin) {
        this.repositoryAdmin.set(repositoryAdmin);
    }

    public void unsetRepositoryAdmin(RepositoryAdmin repositoryAdmin) {
        this.repositoryAdmin.compareAndSet(repositoryAdmin, null);
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

    public List<ObrRepository> getReposList() {
        if (repositoryAdmin.get() == null) {
            return Collections.emptyList();
        }
        return Arrays.stream(repositoryAdmin.get().listRepositories()).map(r -> new ObrRepository(r))
                .collect(Collectors.toList());
    }

    public List<ObrResource> getResources(String repoName) {
        if (repositoryAdmin.get() == null) {
            return Collections.emptyList();
        }

        Optional<ObrRepository> repository = Arrays.stream(repositoryAdmin.get().listRepositories()).filter(r -> {
            return r.getName().equals(repoName);
        }).findFirst().map(r -> new ObrRepository(r, true));
        if (!repository.isPresent()) {
            return Collections.emptyList();
        }
        return repository.get().getResources().stream().sorted((r1, r2) -> {
            return sortResources(r1,r2);
        }).collect(Collectors.toList());
    }

    private int sortResources(ObrResource r1, ObrResource r2) {
        int bySymbolicName = r1.getSymbolicName().compareTo(r2.getSymbolicName());
        if (bySymbolicName != 0) {
            return bySymbolicName;
        }
        return r1.getVersion().compareTo(r2.getVersion());
    }

    public List<Resource> discoverResources(String searchFor) {
        try {
            return Arrays.asList(repositoryAdmin.get().discoverResources(searchFor));
        } catch (InvalidSyntaxException e) {
            log.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    public List<Class<? extends SkysailServerResource<?>>> getMainLinks() {
        List<Class<? extends SkysailServerResource<?>>> result = new ArrayList<>();
        result.add(FeaturesResource.class);
        result.add(RepositoriesResource.class);
        //result.add(PostResolverResource.class);
        result.add(ResourcesResource.class);
        return result;
    }

}
