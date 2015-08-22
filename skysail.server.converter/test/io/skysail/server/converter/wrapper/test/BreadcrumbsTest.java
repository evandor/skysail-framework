//package io.skysail.server.converter.wrapper.test;
//
//import static org.hamcrest.CoreMatchers.equalTo;
//import static org.hamcrest.CoreMatchers.is;
//import static org.junit.Assert.assertThat;
//import io.skysail.api.favorites.FavoritesService;
//import io.skysail.server.app.SkysailApplication;
//import io.skysail.server.converter.Breadcrumb;
//import io.skysail.server.converter.wrapper.Breadcrumbs;
//import io.skysail.server.restlet.resources.SkysailServerResource;
//import io.skysail.server.testsupport.AbstractShiroTest;
//
//import java.util.Arrays;
//import java.util.List;
//
//import org.apache.shiro.subject.Subject;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mockito;
//import org.restlet.data.Reference;
//
//public class BreadcrumbsTest extends AbstractShiroTest {
//
//    private FavoritesService favService;
//    private SkysailServerResource<?> resource;
//    private Reference reference;
//
//    @Before
//    public void setUp() throws Exception {
//        favService = Mockito.mock(FavoritesService.class);
//        resource = Mockito.mock(SkysailServerResource.class);
//        SkysailApplication application = Mockito.mock(SkysailApplication.class);
//        Mockito.when(resource.getApplication()).thenReturn(application);
//        reference = Mockito.mock(Reference.class);
//        Mockito.when(resource.getReference()).thenReturn(reference);
//        subjectUnderTest = Mockito.mock(Subject.class);
//        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn("admin");
//        setSubject(subjectUnderTest);
//    }
//
//    @After
//    public void tearDownSubject() {
//        clearSubject();
//    }
//
//    @Test
//    public void has_home_entry() {
//        List<Breadcrumb> crumbs = new Breadcrumbs(favService).create(resource);
//        assertThat(crumbs.size(), is(1));
//        assertThat(crumbs.get(0).getValue(), is(equalTo("<span class='glyphicon glyphicon-home'></span>")));
//        assertThat(crumbs.get(0).getHref(), is(equalTo("/")));
//    }
//
//    @Test
//    public void has_application_entry() {
//        Mockito.when(reference.getSegments()).thenReturn(Arrays.asList("app"));
//
//        List<Breadcrumb> crumbs = new Breadcrumbs(favService).create(resource);
//
//        assertThat(crumbs.size(), is(2));
//        assertThat(crumbs.get(1).getValue(), is(equalTo(" null")));
//        assertThat(crumbs.get(1).getHref(), is(equalTo("/null")));
//    }
//
//}
