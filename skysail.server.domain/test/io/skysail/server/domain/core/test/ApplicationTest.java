package io.skysail.server.domain.core.test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import io.skysail.api.domain.Identifiable;
import io.skysail.api.repos.DbRepository;
import io.skysail.server.domain.core.*;

import org.junit.*;

public class ApplicationTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void simple_application_structure_can_be_created() {
        Application app = new Application("app17")
            .add(new Entity("e23")
                    .add(new Field("f23")))
            .add(new Entity("e24"));

        assertThat(app.getId(),is("app17"));
        assertThat(app.getEntityNames().size(),is(2));
        assertThat(app.getEntity("e23").getId(),is("e23"));
        assertThat(app.getEntity("e24").getId(),is("e24"));
    }

    @Test
    public void repositories_can_be_set_and_retrieved() {
        Application app = new Application("app56");
        Repositories repos = new Repositories();
        DbRepository aRepository = new DbRepository() {

            @Override
            public Object update(String id, Identifiable entity, String... edges) {
                return null;
            }

            @Override
            public Object save(Identifiable identifiable) {
                return null;
            }

            @Override
            public Class<? extends Identifiable> getRootEntity() {
                return AThing.class;
            }

            @Override
            public Identifiable findOne(String id) {
                return null;
            }
        };
        repos.setRepository(aRepository);
        app.setRepositories(repos);

        assertThat(app.getRepositoryIdentifiers(),contains("io.skysail.server.domain.core.test.AThing"));
        assertThat(app.getRepository("io.skysail.server.domain.core.test.AThing"), is(aRepository));
    }

    @Test
    public void toString_contains_main_details() {
        Application app = new Application("app56");
        assertThat(app.toString(), containsString("app56"));
    }
}