package io.skysail.server.db.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.server.db.*;
import io.skysail.server.db.test.entities.folders.Folder;
import io.skysail.server.db.test.entities.one2many.*;
import io.skysail.server.db.test.entities.simple.SimpleEntity;

import java.util.*;
import java.util.stream.Collectors;

import org.junit.*;

import com.orientechnologies.orient.core.id.ORecordId;

public class OrientGraphDbServiceTest {

    private OrientGraphDbService dbService;

    @Before
    public void setUp() throws Exception {
        dbService = new OrientGraphDbService();
        // dbService.setDbConfigurationProvider(defineConfigProvider());
        dbService.activate();
    }

    // === GraphAPI Persisting Tests ==========================================

    @Test
    public void graphApi_can_persist_simple_entity() {
        assertThat(persistSimpleEntity("name"), is(notNullValue()));
    }

    @Test
    public void graphApi_can_persist_one2many_entity_with_no_elements_in_list() {
        assertThat(persistOneToManyEntity("oneToMany"), is(notNullValue()));
    }

    @Test
    public void graphApi_can_persist_one2many_entity_with_one_element_in_list() {
        assertThat(persistOneToManyEntity("oneToMany", "toMany"), is(notNullValue()));
    }

    @Test
    public void graphApi_can_persist_one2many_entity_with_two_elements_in_list() {
        assertThat(persistOneToManyEntity("oneToMany", "toManyA", "toManyB"), is(notNullValue()));
    }

    @Test
    public void graphApi_can_persist_recursive_Structure_with_no_subelement() {
        Folder rootFolder = new Folder("root");
        assertThat(dbService.persist(rootFolder, "subfolder"), is(notNullValue()));
    }

    @Test
    public void graphApi_can_persist_recursive_Structure_with_a_subelement() {
        Folder rootFolder = new Folder("root");
        rootFolder.setSubfolder(Arrays.asList(new Folder("sub1")));
        assertThat(dbService.persist(rootFolder, "subfolder"), is(notNullValue()));
    }

    @Test
    public void graphApi_can_persist_recursive_Structure_with_two_subelements() {
        Folder rootFolder = new Folder("root");
        rootFolder.setSubfolder(Arrays.asList(new Folder("sub1"), new Folder("sub2")));
        assertThat(dbService.persist(rootFolder, "subfolder"), is(notNullValue()));
    }

    @Test
    public void graphApi_can_persist_recursive_Structure_with_a_subelement_with_a_subelement() {
        Folder rootFolder = new Folder("root");
        Folder subFolderLevel1 = new Folder("subFolderLevel1");
        subFolderLevel1.setSubfolder(Arrays.asList(new Folder("subFolderLevel2")));
        rootFolder.setSubfolder(Arrays.asList(subFolderLevel1));
        assertThat(dbService.persist(rootFolder, "subfolder"), is(notNullValue()));
    }

    // === GraphAPI Retrieval Tests ==========================================

    @Test
    public void graphApi_can_retrieve_simple_entity() {
        ORecordId id = persistSimpleEntity("name");
        SimpleEntity entity = dbService.findById2(SimpleEntity.class, id.toString());
        assertThat(entity.getId(), is(id.toString()));
        assertThat(entity.getName(), is("name"));
    }

    @Test
    public void graphApi_can_retrieve_one2many_entity_with_no_elements_in_list() {
        ORecordId persisted = (ORecordId) persistOneToManyEntity("oneToMany");

        OneToManyEntity entity = dbService.findById2(OneToManyEntity.class, persisted.toString());

        assertThat(entity.getId(), is(persisted.toString()));
        assertThat(entity.getName(), is("oneToMany"));
        assertThat(entity.getToManies().size(), is(0));
    }

    @Test
    public void graphApi_can_retrieve_one2many_entity_with_a_element_in_list() {
        ORecordId persisted = (ORecordId) persistOneToManyEntity("oneToMany", "toMany");
        dbService.register(OneToManyEntity.class, ToMany.class);

        OneToManyEntity foundEntity = dbService.findById2(OneToManyEntity.class, persisted.toString());

        assertThat(foundEntity.getId(), is(persisted.toString()));
        assertThat(foundEntity.getName(), is("oneToMany"));
        assertThat(foundEntity.getToManies().size(), is(1));
        assertThat(foundEntity.getToManies().get(0).getName(), is("toMany"));
    }

    @Test
    public void graphApi_can_retrieve_one2many_entity_with_two_elements_in_list() {
        ORecordId persisted = (ORecordId) persistOneToManyEntity("oneToMany", "toManyA", "toManyB");
        dbService.register(OneToManyEntity.class, ToMany.class);

        OneToManyEntity entity = dbService.findById2(OneToManyEntity.class, persisted.toString());

        assertThat(entity.getId(), is(persisted.toString()));
        assertThat(entity.getName(), is("oneToMany"));
        assertThat(entity.getToManies().size(), is(2));
        assertThat(entity.getToManies().get(0).getName(), is("toManyA"));
        assertThat(entity.getToManies().get(1).getName(), is("toManyB"));
    }

    @Test
    public void graphApi_can_retrieve_recursive_Structure_with_no_subelement() {
        Object persisted = persistFolder("root");

        Folder foundEntity = dbService.findById2(Folder.class, persisted.toString());

        assertFolder(foundEntity, persisted.toString(), "root");
        assertThat(foundEntity.getSubfolder().size(), is(0));
    }

    @Test
    public void graphApi_can_retrieve_recursive_Structure_with_a_subelement() {
        Object persisted = persistFolder("root", "sub");
        dbService.register(Folder.class);

        Folder foundEntity = dbService.findById2(Folder.class, persisted.toString());

        assertFolder(foundEntity, persisted.toString(), "root");
        assertSubfolders(foundEntity, "sub");
    }

    @Test
    public void graphApi_can_retrieve_recursive_Structure_with_two_subelements() {
        Object persisted = persistFolder("root", "sub1", "sub2");

        Folder foundEntity = dbService.findById2(Folder.class, persisted.toString());

        assertFolder(foundEntity, persisted.toString(), "root");
        assertSubfolders(foundEntity, "sub1", "sub2");
    }

    @Test
    @Ignore
    public void graphApi_can_retrieve_recursive_Structure_with_a_subelement_with_a_subelement() {
        Folder rootFolder = new Folder("root");
        Folder subFolderLevel1 = new Folder("subFolderLevel1");
        subFolderLevel1.setSubfolder(Arrays.asList(new Folder("subFolderLevel2")));
        rootFolder.setSubfolder(Arrays.asList(subFolderLevel1));
        Object persisted = dbService.persist(rootFolder, "subfolder");

        Folder foundEntity = dbService.findById2(Folder.class, persisted.toString());
        assertFolder(foundEntity, persisted.toString(), "root");
        assertSubfolders(foundEntity, "subFolderLevel1");

    }

    // === GraphAPI Updating Tests ==========================================
    //
    // Pattern: create, retrieve, change and update, re-retrieve and assert

    @Test
    public void graphApi_can_update_simple_entity() {
        String id = persistSimpleEntity("name").toString();
        SimpleEntity entity = dbService.findById2(SimpleEntity.class, id);
        entity.setName("changed");

        dbService.update(id, entity);

        entity = dbService.findById2(SimpleEntity.class, id);
        assertThat(entity.getName(), is("changed"));
    }

    @Test
    public void graphApi_can_update_one2many_entity_with_no_elements_in_list() {
        String id = persistOneToManyEntity("oneToMany").toString();
        OneToManyEntity entity = dbService.findById2(OneToManyEntity.class, id);
        entity.setName("changed");

        dbService.update(id, entity, "toManies");

        entity = dbService.findById2(OneToManyEntity.class, id);
        assertThat(entity.getName(), is("changed"));
    }

    @Test
    public void graphApi_can_update_one2many_entity_with_one_element_in_list() {
        String id = persistOneToManyEntity("oneToMany", "toMany").toString();
        dbService.register(OneToManyEntity.class, ToMany.class);

        OneToManyEntity entity = dbService.findById2(OneToManyEntity.class, id);
        entity.getToManies().get(0).setName("tooooMany");

        dbService.update(id, entity, "toManies");

        entity = dbService.findById2(OneToManyEntity.class, id);
        assertThat(entity.getToManies().get(0).getName(), is("tooooMany"));
    }

    @Test
    public void graphApi_can_update_one2many_entity_with_two_elements_in_list() {
        ORecordId persisted = (ORecordId) persistOneToManyEntity("oneToMany", "toManyA", "toManyB");
    }


    // === Helpers ==========================================

    private ORecordId persistSimpleEntity(String name) {
        return (ORecordId) dbService.persist(new SimpleEntity(name));
    }

    private Object persistOneToManyEntity(String name, String... toManies) {
        OneToManyEntity rootEntity = new OneToManyEntity(name);
        rootEntity.setToManies(Arrays.stream(toManies).map(toMany -> new ToMany(toMany)).collect(Collectors.toList()));
        return dbService.persist(rootEntity, "toManies");
    }

    private Object persistFolder(String name, String... subfolder) {
        Folder rootFolder = new Folder(name);
        rootFolder.setSubfolder(Arrays.stream(subfolder).map(sub -> new Folder(sub)).collect(Collectors.toList()));
        return dbService.persist(rootFolder, "subfolder");
    }

    private void assertFolder(Folder folder, String id, String name) {
        assertThat(folder.getId(), is(id));
        assertThat(folder.getName(), is(name));
    }

    private void assertSubfolders(Folder foundEntity, String... expected) {
        List<String> actual = foundEntity.getSubfolder().stream().map(Folder::getName).collect(Collectors.toList());
        assertThat(actual, containsInAnyOrder(expected));
    }


    private DbConfigurationProvider defineConfigProvider() {
        return new DbConfigurationProvider() {
            @Override
            public DbConfig getConfig() {
                HashMap<String, String> configMap = new HashMap<String, String>() {
                    {
                        put("name", "skysailgraph");
                        put("url", "remote:localhost/skysailcrm");
                        put("username", "admin");
                        put("password", "admin");
                    }
                };
                DbConfig dbConfig = new DbConfig(configMap);
                return dbConfig;
            }
        };
    }


}
