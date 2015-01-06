//package de.twenty11.skysail.server.sync.git.impl.test;
//
//import static org.hamcrest.Matchers.containsString;
//import static org.hamcrest.Matchers.is;
//import static org.junit.Assert.assertThat;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mockito;
//import org.restlet.Request;
//import org.restlet.data.Reference;
//import org.restlet.representation.Representation;
//import org.restlet.representation.Variant;
//import org.restlet.resource.Resource;
//import org.restlet.service.ConverterService;
//
//import de.twenty11.skysail.api.domain.Identifiable;
//import de.twenty11.skysail.server.services.RestletServicesProvider;
//
//public class GitSyncerTest {
//
//    private GitSyncer syncer;
//
//    private class Entity implements Identifiable {
//
//        private String id;
//
//        public Entity(String id) {
//            this.id = id;
//        }
//
//        @Override
//        public String getId() {
//            return id;
//        }
//
//        @Override
//        public void setId(String id) {
//            this.id = id;
//        }
//    };
//
//    @Before
//    public void setUp() throws Exception {
//        syncer = new GitSyncer();
//        RestletServicesProvider rsp = Mockito.mock(RestletServicesProvider.class);
//        ConverterService converterService = Mockito.mock(ConverterService.class);
//        Representation representation = Mockito.mock(Representation.class);
//        Mockito.when(representation.getText()).thenReturn("text");
//        Mockito.when(
//                converterService.toRepresentation(Mockito.anyObject(), Mockito.<Variant> any(),
//                        Mockito.<Resource> any())).thenReturn(representation);
//        Mockito.when(rsp.getConverterSerivce()).thenReturn(converterService);
//        syncer.setRestletServicesProvider(rsp);
//    }
//
//    @Test
//    public void path_contains_user_home_and_dot_skysail() {
//        String path = syncer.getGitPath("test");
//        assertThat(path, containsString(System.getProperty("user.home")));
//        assertThat(path, containsString(".skysail"));
//        assertThat(path.indexOf("//"), is(-1));
//    }
//
//    @Test
//    public void repository_exists_or_is_created() {
//        Request request = Mockito.mock(Request.class);
//        Reference originalRef = Mockito.mock(Reference.class);
//        Mockito.when(request.getOriginalRef()).thenReturn(originalRef);
//        syncer.pushEntityWasAdded(request, new Entity("17"), "test");
//    }
//
//    @Test
//    public void new_entity_is_writen_to_git() {
//        Request request = Mockito.mock(Request.class);
//        Reference originalRef = Mockito.mock(Reference.class);
//        Mockito.when(request.getOriginalRef()).thenReturn(originalRef);
//        Mockito.when(originalRef.getPath()).thenReturn("path");
//        syncer.pushEntityWasAdded(request, new Entity("17"), "test");
//
//    }
//
//    @Test
//    public void updated_entity_is_writen_to_git() {
//        Request request = Mockito.mock(Request.class);
//        Reference originalRef = Mockito.mock(Reference.class);
//        Mockito.when(request.getOriginalRef()).thenReturn(originalRef);
//        Mockito.when(originalRef.getPath()).thenReturn("path");
//        Entity entity = new Entity("19");
//        syncer.pushEntityWasAdded(request, entity, "test");
//
//        Mockito.when(originalRef.getPath()).thenReturn("path/19");
//        syncer.pushEntityWasChanged(request, entity, "test");
//
//    }
//
//}
