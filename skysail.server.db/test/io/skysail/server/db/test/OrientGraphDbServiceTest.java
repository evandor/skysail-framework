//package io.skysail.server.db.test;
// FIXME
//import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.Matchers.contains;
//import static org.hamcrest.Matchers.containsInAnyOrder;
//import static org.hamcrest.Matchers.notNullValue;
//import static org.junit.Assert.assertThat;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import com.tinkerpop.blueprints.impls.orient.OrientVertex;
//
//import io.skysail.server.db.DbClassName;
//import io.skysail.server.db.DbConfig;
//import io.skysail.server.db.DbConfigurationProvider;
//import io.skysail.server.db.OrientGraphDbService;
//import io.skysail.server.db.test.entities.folders.Folder;
//import io.skysail.server.db.test.entities.one2many.OneToManyEntity;
//import io.skysail.server.db.test.entities.one2many.ToMany;
//import io.skysail.server.db.test.entities.simple.SimpleEntity;
//
//public class OrientGraphDbServiceTest {
//
//    private OrientGraphDbService dbService;
//
//    @Before
//    public void setUp() throws Exception {
//        dbService = new OrientGraphDbService();
//        //dbService.setDbConfigurationProvider(defineConfigProvider());
//        dbService.activate();
//    }
//
//    // === GraphAPI Persisting Tests ==========================================
//
//    @Test
//    public void graphApi_can_persist_simple_entity() {
//        assertThat(persistSimpleEntity("name"), is(notNullValue()));
//    }
//
//    @Test
//    public void graphApi_can_persist_one2many_entity_with_no_elements_in_list() {
//        assertThat(persistOneToManyEntity("oneToMany"), is(notNullValue()));
//    }
//
//    @Test
//    public void graphApi_can_persist_one2many_entity_with_one_element_in_list() {
//        assertThat(persistOneToManyEntity("oneToMany", "toMany"), is(notNullValue()));
//    }
//
//    @Test
//    public void graphApi_can_persist_one2many_entity_with_two_elements_in_list() {
//        assertThat(persistOneToManyEntity("oneToMany", "toManyA", "toManyB"), is(notNullValue()));
//    }
//
////    @Test
////    public void graphApi_can_persist_recursive_Structure_with_no_subelement() {
////        Folder rootFolder = new Folder("root");
////        assertThat(dbService.persist(rootFolder, "subfolder"), is(notNullValue()));
////    }
////
////    @Test
////    public void graphApi_can_persist_recursive_Structure_with_a_subelement() {
////        Folder rootFolder = new Folder("root");
////        rootFolder.setSubfolder(Arrays.asList(new Folder("sub1")));
////        assertThat(dbService.persist(rootFolder, "subfolder"), is(notNullValue()));
////    }
////
////    @Test
////    public void graphApi_can_persist_recursive_Structure_with_two_subelements() {
////        Folder rootFolder = new Folder("root");
////        rootFolder.setSubfolder(Arrays.asList(new Folder("sub1"), new Folder("sub2")));
////        assertThat(dbService.persist(rootFolder, "subfolder"), is(notNullValue()));
////    }
////
////    @Test
////    public void graphApi_can_persist_recursive_Structure_with_a_subelement_with_a_subelement() {
////        Folder rootFolder = new Folder("root");
////        Folder subFolderLevel1 = new Folder("subFolderLevel1");
////        subFolderLevel1.setSubfolder(Arrays.asList(new Folder("subFolderLevel2")));
////        rootFolder.setSubfolder(Arrays.asList(subFolderLevel1));
////        assertThat(dbService.persist(rootFolder, "subfolder"), is(notNullValue()));
////    }
//
//    // === GraphAPI Retrieval Tests (Single Entity)====================================
//
//    @Test
//    public void graphApi_can_retrieve_simple_entity() {
//        OrientVertex vae = persistSimpleEntity("name");
//        Object id = vae.getId();
//        SimpleEntity entity = dbService.findById2(SimpleEntity.class, id.toString());
//        assertThat(entity.getId(), is(id.toString()));
//        assertThat(entity.getName(), is("name"));
//    }
//
//    @Test
//    public void graphApi_can_retrieve_one2many_entity_with_no_elements_in_list() {
//        OrientVertex persisted = persistOneToManyEntity("oneToMany");
//
//        OneToManyEntity entity = dbService.findById2(OneToManyEntity.class, persisted.getId().toString());
//
//        assertThat(entity.getId(), is(persisted.getId().toString()));
//        assertThat(entity.getName(), is("oneToMany"));
//        assertThat(entity.getToManies().size(), is(0));
//    }
//
//    @Test
//    public void graphApi_can_retrieve_one2many_entity_with_a_element_in_list() {
//        OrientVertex persisted = persistOneToManyEntity("oneToMany", "toMany");
//        dbService.register(OneToManyEntity.class, ToMany.class);
//
//        OneToManyEntity foundEntity = dbService.findById2(OneToManyEntity.class, persisted.getId().toString());
//
//        assertThat(foundEntity.getId(), is(persisted.getId().toString()));
//        assertThat(foundEntity.getName(), is("oneToMany"));
//        assertThat(foundEntity.getToManies().size(), is(1));
//        assertThat(foundEntity.getToManies().get(0).getName(), is("toMany"));
//    }
//
//    @Test
//    public void graphApi_can_retrieve_one2many_entity_with_two_elements_in_list() {
//        OrientVertex persisted = persistOneToManyEntity("oneToMany", "toManyA", "toManyB");
//        dbService.register(OneToManyEntity.class, ToMany.class);
//
//        OneToManyEntity entity = dbService.findById2(OneToManyEntity.class, persisted.getId().toString());
//
//        assertThat(entity.getId(), is(persisted.getId().toString()));
//        assertThat(entity.getName(), is("oneToMany"));
//        assertThat(entity.getToManies().size(), is(2));
//        assertThat(entity.getToManies().get(0).getName(), is("toManyA"));
//        assertThat(entity.getToManies().get(1).getName(), is("toManyB"));
//    }
//
//    @Test
//    public void graphApi_can_retrieve_recursive_Structure_with_no_subelement() {
//        OrientVertex persisted = persistFolder("root");
//
//        Folder foundEntity = dbService.findById2(Folder.class, persisted.getId().toString());
//
//        assertFolder(foundEntity, persisted.getId().toString(), "root");
//        assertThat(foundEntity.getSubfolder().size(), is(0));
//    }
//
//    @Test
//    public void graphApi_can_retrieve_recursive_Structure_with_a_subelement() {
//        OrientVertex persisted = persistFolder("root", "sub");
//        dbService.register(Folder.class);
//
//        Folder foundEntity = dbService.findById2(Folder.class, persisted.getId().toString());
//
//        assertFolder(foundEntity, persisted.getId().toString(), "root");
//        assertSubfolders(foundEntity, "sub");
//    }
//
//    @Test
//    public void graphApi_can_retrieve_recursive_Structure_with_two_subelements() {
//        OrientVertex persisted = persistFolder("root", "sub1", "sub2");
//
//        Folder foundEntity = dbService.findById2(Folder.class, persisted.getId().toString());
//
//        assertFolder(foundEntity, persisted.getId().toString(), "root");
//        assertSubfolders(foundEntity, "sub1", "sub2");
//    }
//
////    @Test
////    @Ignore
////    public void graphApi_can_retrieve_recursive_Structure_with_a_subelement_with_a_subelement() {
////        Folder rootFolder = new Folder("root");
////        Folder subFolderLevel1 = new Folder("subFolderLevel1");
////        subFolderLevel1.setSubfolder(Arrays.asList(new Folder("subFolderLevel2")));
////        rootFolder.setSubfolder(Arrays.asList(subFolderLevel1));
////        OrientVertex persisted = dbService.persist(rootFolder, "subfolder");
////
////        Folder foundEntity = dbService.findById2(Folder.class, persisted.getId().toString());
////        assertFolder(foundEntity, persisted.getId().toString(), "root");
////        assertSubfolders(foundEntity, "subFolderLevel1");
////
////    }
//
//    // === GraphAPI Retrieval Tests (Entity List) ====================================
//
//    @Test
//    public void graphApi_can_retrieve_simple_entities() {
//        persistSimpleEntity("nameA");
//        persistSimpleEntity("nameB");
//        dbService.register(SimpleEntity.class);
//        String sql = "SELECT FROM " + DbClassName.of(SimpleEntity.class);
//        List<SimpleEntity> entities = dbService.<SimpleEntity> findGraphs(SimpleEntity.class, sql,  new HashMap<String, Object>());
//        assertThat(entities.stream().map(SimpleEntity::getName).collect(Collectors.toList()), contains("nameA", "nameB"));
//    }
//
//
//    // === GraphAPI Updating Tests ==========================================
//    //
//    // Pattern: create, retrieve, change and update, re-retrieve and assert
//
////    @Test
////    public void graphApi_can_update_simple_entity() {
////        String id = persistSimpleEntity("name").getId().toString();
////        SimpleEntity entity = dbService.findById2(SimpleEntity.class, id);
////        entity.setName("changed");
////
////        dbService.update(id, entity);
////
////        entity = dbService.findById2(SimpleEntity.class, id);
////        assertThat(entity.getName(), is("changed"));
////    }
////
////    @Test
////    public void graphApi_can_update_one2many_entity_with_no_elements_in_list() {
////        String id = persistOneToManyEntity("oneToMany").getId().toString();
////        OneToManyEntity entity = dbService.findById2(OneToManyEntity.class, id);
////        entity.setName("changed");
////
////        dbService.update(id, entity, "toManies");
////
////        entity = dbService.findById2(OneToManyEntity.class, id);
////        assertThat(entity.getName(), is("changed"));
////    }
////
////    @Test
////    public void graphApi_can_update_one2many_entity_with_one_element_in_list() {
////        String id = persistOneToManyEntity("oneToMany", "toMany").getId().toString();
////        dbService.register(OneToManyEntity.class, ToMany.class);
////
////        OneToManyEntity entity = dbService.findById2(OneToManyEntity.class, id);
////        entity.getToManies().get(0).setName("tooooMany");
////
////        dbService.update(id, entity, "toManies");
////
////        entity = dbService.findById2(OneToManyEntity.class, id);
////        assertThat(entity.getToManies().get(0).getName(), is("tooooMany"));
////    }
//
////    @Test
////    public void graphApi_can_update_one2many_entity_with_two_elements_in_list() {
////        OrientVertex persisted = OrientVertexpersistOneToManyEntity("oneToMany", "toManyA", "toManyB");
////    }
//
//
//    // === Helpers ==========================================
//
////    private OrientVertex persistSimpleEntity(String name) {
////        return dbService.persist(new SimpleEntity(name));
////    }
////
////    private OrientVertex persistOneToManyEntity(String name, String... toManies) {
////        OneToManyEntity rootEntity = new OneToManyEntity(name);
////        rootEntity.setToManies(Arrays.stream(toManies).map(toMany -> new ToMany(toMany)).collect(Collectors.toList()));
////        return dbService.persist(rootEntity, "toManies");
////    }
////
////    private OrientVertex persistFolder(String name, String... subfolder) {
////        Folder rootFolder = new Folder(name);
////        rootFolder.setSubfolder(Arrays.stream(subfolder).map(sub -> new Folder(sub)).collect(Collectors.toList()));
////        return dbService.persist(rootFolder, "subfolder");
////    }
//
//    private void assertFolder(Folder folder, String id, String name) {
//        assertThat(folder.getId(), is(id));
//        assertThat(folder.getName(), is(name));
//    }
//
//    private void assertSubfolders(Folder foundEntity, String... expected) {
//        List<String> actual = foundEntity.getSubfolder().stream().map(Folder::getName).collect(Collectors.toList());
//        assertThat(actual, containsInAnyOrder(expected));
//    }
//
//
//    private DbConfigurationProvider defineConfigProvider() {
//        return new DbConfigurationProvider() {
//            @Override
//            public DbConfig getConfig() {
//                HashMap<String, String> configMap = new HashMap<String, String>() {
//                    {
//                        put("name", "skysailgraph");
//                        put("url", "remote:localhost/skysailcrm");
//                        put("username", "admin");
//                        put("password", "admin");
//                    }
//                };
//                DbConfig dbConfig = new DbConfig(configMap);
//                return dbConfig;
//            }
//        };
//    }
//
//
//}
