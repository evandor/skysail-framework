package io.skysail.domain.core.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import io.skysail.domain.Identifiable;
import io.skysail.domain.core.ApplicationModel;
import io.skysail.domain.core.EntityModel;
import io.skysail.domain.core.FieldModel;
import io.skysail.domain.core.Repositories;
import io.skysail.domain.core.repos.DbRepository;

public class ApplicationModelTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void simple_application_structure_can_be_created() {
        ApplicationModel app = new ApplicationModel("app17")
            .addOnce(new EntityModel("e23")
                    .add(new FieldModel("f23", String.class)))
            .addOnce(new EntityModel("e24"));

        assertThat(app.getName(),is("app17"));
        assertThat(app.getEntityIds().size(),is(2));
        assertThat(app.getEntity("e23").getId(),is("e23"));
        assertThat(app.getEntity("e24").getId(),is("e24"));
    }
    
    @Test
    public void same_entity_can_be_added_only_once() {
        ApplicationModel app = new ApplicationModel("app17")
            .addOnce(new EntityModel("e23"))
            .addOnce(new EntityModel("e23"));

        assertThat(app.getName(),is("app17"));
        assertThat(app.getEntityIds().size(),is(1));
        assertThat(app.getEntity("e23").getId(),is("e23"));
    }

    @Test
    public void repositories_can_be_set_and_retrieved() {
        ApplicationModel app = new ApplicationModel("app56");
        Repositories repos = new Repositories();
        DbRepository aRepository = new DbRepository() {

            @Override
            public Object update(Identifiable entity, ApplicationModel model) {
                return null;
            }

            @Override
            public Object save(Identifiable identifiable, ApplicationModel appModel) {
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

            @Override
            public void delete(Identifiable identifiable) {
            }
        };
        repos.setRepository(aRepository);
        app.setRepositories(repos);

        assertThat(app.getRepositoryIds(),contains("io.skysail.domain.core.test.AThing"));
        assertThat(app.getRepository("io.skysail.domain.core.test.AThing"), is(aRepository));
    }

    @Test
    public void toString_contains_main_details() {
        ApplicationModel app = new ApplicationModel("app56");
        assertThat(app.toString(), containsString("app56"));
    }
    
    @Test
    public void toString_is_formatted_nicely() {
        ApplicationModel app = new ApplicationModel("app37")
                .addOnce(new EntityModel("e23")
                        .add(new FieldModel("f23", String.class)))
                .addOnce(new EntityModel("e24"));
        
        String[] toString = app.toString().split("\n");
        
        int i=0;
        assertThat(toString[i++],is("ApplicationModel: app37"));
        assertThat(toString[i++],is("Entities: "));
        assertThat(toString[i++],is(" * EntityModel: id='e23', isAggregate=true"));
        assertThat(toString[i++],is("   Fields:"));
        assertThat(toString[i++],is("    - FieldModel(id=f23, type=String, inputType=null)"));
        assertThat(toString[i++],is(""));
        assertThat(toString[i++],is(" * EntityModel: id='e24', isAggregate=true"));
        assertThat(toString[i++],is(""));
        assertThat(toString[i++],is("Repositories: "));
        assertThat(toString[i++],is("Repositories(repositories={})"));
    }
}
