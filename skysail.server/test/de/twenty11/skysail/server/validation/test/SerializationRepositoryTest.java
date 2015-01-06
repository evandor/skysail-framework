package de.twenty11.skysail.server.validation.test;
//package de.twenty11.skysail.server.validation.test;
//
//import static org.hamcrest.Matchers.is;
//import static org.hamcrest.Matchers.not;
//import static org.hamcrest.Matchers.nullValue;
//import static org.junit.Assert.assertThat;
//
//import java.util.List;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import de.twenty11.skysail.server.validation.SerializationRepository;
//
//public class SerializationRepositoryTest {
//
//    private SerializationRepository<Entity> serializationRepository;
//
//    @Before
//    public void setUp() throws Exception {
//        serializationRepository = new SerializationRepository<Entity>("etc/serializedRepo", Entity.class);
//    }
//
//    @Test
//    public void can_serialize_entity() {
//        Entity entity = new Entity("name");
//        serializationRepository.add(entity);
//        assertThat(entity.getId(), is(not(nullValue())));
//    }
//
//    @Test
//    public void can_read_serialized_entities() {
//        Entity entity = new Entity("name2");
//        serializationRepository.add(entity);
//
//        List<Entity> entities = serializationRepository.getAll();
//        assertThat(entities, is(not(nullValue())));
//    }
//
//}
