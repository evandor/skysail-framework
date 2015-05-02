package io.skysail.server.app.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.api.text.*;
import io.skysail.server.app.*;

import java.util.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.BundleContext;
import org.restlet.Request;

import de.twenty11.skysail.server.app.ApplicationListProvider;

@RunWith(MockitoJUnitRunner.class)
public class ServiceListTest {

    @Mock
    private ApplicationListProvider applicationListProvider;
    
    @InjectMocks
    private ServiceList serviceList;

    private SkysailApplication application;

    private TranslationStore translationStore;
    
    private TranslationRenderService renderService;

    @Before
    public void setUp() throws Exception {
        List<SkysailApplication> applications = new ArrayList<>();
        application = new SkysailApplication() {};
        applications.add(application);
        Mockito.when(applicationListProvider.getApplications()).thenReturn(applications);
        translationStore = Mockito.mock(TranslationStore.class);
        renderService = Mockito.mock(TranslationRenderService.class);
    }

    /** === Translation Stores ================================== */
    
    @Test
    public void adding_new_store_creates_holder_in_serviceList_and_application() {
        serviceList.addTranslationStore(translationStore, new HashMap<>());
        assertThat(serviceList.getTranslationStores().size(), is(1));
        assertThat(application.getTranslationStores().size(), is(1));
    }
    
    @Test
    public void adding_the_same_store_twice_yields_only_one_holder() {
        serviceList.addTranslationStore(translationStore, new HashMap<>());
        serviceList.addTranslationStore(translationStore, new HashMap<>());
        assertThat(serviceList.getTranslationStores().size(), is(1));
        assertThat(application.getTranslationStores().size(), is(1));
    }
    
    @Test
    public void adding_another_store_yields_two_holders() {
        serviceList.addTranslationStore(translationStore, new HashMap<>());
        serviceList.addTranslationStore(new TranslationStore() { // need a store with a different "name".
            public boolean persist(String key, String message, Locale locale, BundleContext bundleContext) {return false;}
            public Optional<String> get(String key, ClassLoader cl, Request request, Locale locale) {return null;}
            public Optional<String> get(String key, ClassLoader cl, Request request) {return null;}
            public Optional<String> get(String key, ClassLoader cl) {return null;}
            public Optional<String> get(String key) {return null;}
        }, new HashMap<>());
        assertThat(serviceList.getTranslationStores().size(), is(2));
        assertThat(application.getTranslationStores().size(), is(2));
    }
    
    @Test
    public void removing_a_store_again_yields_empty_list_of_stores() {
        serviceList.addTranslationStore(translationStore, new HashMap<>());
        serviceList.removeTranslationStore(translationStore);
        assertThat(serviceList.getTranslationStores().size(), is(0));
        assertThat(application.getTranslationStores().size(), is(0));
    }
    
    /** === Translation Render Services ================================== */
    
    @Test
    public void adding_new_renderService_creates_holder_in_serviceList_and_application() {
        serviceList.addTranslationRenderService(renderService, new HashMap<>());
        assertThat(serviceList.getTranslationRenderServices().size(), is(1));
        assertThat(application.getTranslationRenderServices().size(), is(1));
    }
    
    @Test
    public void adding_the_same_renderService_twice_yields_only_one_holder() {
        serviceList.addTranslationRenderService(renderService, new HashMap<>());
        serviceList.addTranslationRenderService(renderService, new HashMap<>());
        assertThat(serviceList.getTranslationRenderServices().size(), is(1));
        assertThat(application.getTranslationRenderServices().size(), is(1));
    }
    
    @Test
    public void adding_another_renderService_yields_two_holders() {
        serviceList.addTranslationRenderService(renderService, new HashMap<>());
        serviceList.addTranslationRenderService(new TranslationRenderService() {
            public String render(Translation translation, Object... substitutions) {return null;}
            public boolean applicable(String unformattedTranslation) {return false;}
            public String adjustText(String unformatted) {return null;}
            public String addRendererInfo() {return "";}
        }, new HashMap<>());
        assertThat(serviceList.getTranslationRenderServices().size(), is(2));
        assertThat(application.getTranslationRenderServices().size(), is(2));
    }
    
    @Test
    public void removing_a_renderService_again_yields_empty_list_of_renderServices() {
        serviceList.addTranslationRenderService(renderService, new HashMap<>());
        serviceList.removeTranslationRenderService(renderService);
        assertThat(serviceList.getTranslationRenderServices().size(), is(0));
        assertThat(application.getTranslationRenderServices().size(), is(0));

    }

}
