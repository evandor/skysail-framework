package io.skysail.server.testsupport.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.server.testsupport.InMemoryDbService;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class InMemoryDbServiceTest {

    private InMemoryDbService dbService;

    @Before
    public void setUp() throws Exception {
        dbService = new InMemoryDbService();
    }

    @Test
    public void can_handle_single_entity() {
        dbService.persist(new String("a string"));
        List<Object> findObjects = dbService.findObjects(String.class, "username");
        assertThat(findObjects.size(), is(1));
        assertThat(((String) findObjects.get(0)), is(equalTo("a string")));
    }

    @Test
    public void can_handle_update_of_single_entity() {
        Object id = dbService.persist(new Integer(8));

        dbService.update(id, new Integer(9));

        List<Object> findObjects = dbService.findObjects(Integer.class, "username");
        assertThat(findObjects.size(), is(1));
        assertThat(((Integer) findObjects.get(0)), is(equalTo(9)));
    }

    @Test
    public void can_handle_two_entites_of_same_type() {
        dbService.persist(new String("7"));
        dbService.persist(new String("8"));
        List<Object> findObjects = dbService.findObjects(String.class, "username");
        assertThat(findObjects.size(), is(2));
        assertThat(((String) findObjects.get(0)), is(equalTo("7")));
        assertThat(((String) findObjects.get(1)), is(equalTo("8")));
    }

    @Test
    public void can_handle_two_entites_of_different_type() {
        dbService.persist(new String("7"));
        dbService.persist(new Integer(8));
        List<Object> findObjects = dbService.findObjects(String.class, "username");
        assertThat(findObjects.size(), is(1));
        assertThat(((String) findObjects.get(0)), is(equalTo("7")));
    }

}
