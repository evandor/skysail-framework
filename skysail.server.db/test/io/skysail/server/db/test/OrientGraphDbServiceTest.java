package io.skysail.server.db.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
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
        DbConfigurationProvider provider = new DbConfigurationProvider() {
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
       // dbService.setDbConfigurationProvider(provider);
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
        SimpleEntity entity = dbService.findById(SimpleEntity.class, id.toString());
        assertThat(entity.getId(), is(id.toString()));
        assertThat(entity.getName(), is("name"));
    }

    @Test
    public void graphApi_can_retrieve_one2many_entity_with_no_elements_in_list() {
        ORecordId persisted = (ORecordId) persistOneToManyEntity("oneToMany");
        OneToManyEntity entity = dbService.findById(OneToManyEntity.class, persisted.toString());
        assertThat(entity.getId(), is(persisted.toString()));
        assertThat(entity.getName(), is("oneToMany"));
        assertThat(entity.getToManies().size(), is(0));
    }

    @Test
    public void graphApi_can_retrieve_one2many_entity_with_a_element_in_list() {
        ORecordId persisted = (ORecordId) persistOneToManyEntity("oneToMany", "toMany");
        dbService.register(OneToManyEntity.class);

//        List<OneToManyEntity> query = dbService.getObjectDb().query(new OSQLSynchQuery<OneToManyEntity>("select from " + persisted.toString()));
//
//        query.stream().forEach(e -> {
//            System.out.println(e.getName());
//            System.out.println(e.getToManies());
//        });

        OneToManyEntity entity = dbService.findById2(OneToManyEntity.class, persisted.toString());
        assertThat(entity.getId(), is(persisted.toString()));
        assertThat(entity.getName(), is("oneToMany"));
        assertThat(entity.getToManies().get(0).getName(), is("toMany"));
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

}
