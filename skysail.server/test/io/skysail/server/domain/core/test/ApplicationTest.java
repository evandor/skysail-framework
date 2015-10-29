package io.skysail.server.domain.core.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.api.domain.Identifiable;
import io.skysail.api.repos.Repository;
import io.skysail.server.domain.core.*;

import org.junit.*;

public class ApplicationTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void simple_application_structure_can_be_created() {
        Application app = new Application("app27")
            .add(new Entity("e23")
                    .add(new Field("f23")))
            .add(new Entity("e24"));

        assertThat(app.getId(),is("app27"));
        assertThat(app.getEntities().size(),is(2));
        assertThat(app.getEntities().get(0).getId(),is("e23"));
        assertThat(app.getEntities().get(0).getId(),is("e24"));
    }

    @Test
    public void testName() {
        Application app = new Application("app27")
            .add(new Entity("e23")
                .add(new Field("f23")))
            .add(new Repository() {

                @Override
                public Object update(String id, Identifiable entity, String... edges) {
                    return null;
                }

                @Override
                public Object save(Identifiable identifiable) {
                    return null;
                }

                @Override
                public Class<Identifiable> getRootEntity() {
                    return null;
                }

                @Override
                public Identifiable findOne(String id) {
                    return null;
                }
            });

    }
}
