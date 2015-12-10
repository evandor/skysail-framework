package io.skysail.server.app.designer.model.test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.*;
import java.util.stream.Collectors;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.Bundle;

import de.twenty11.skysail.server.core.restlet.SkysailRouter;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.fields.DbEntityField;
import io.skysail.server.app.designer.model.*;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.domain.core.EntityModel;

@RunWith(MockitoJUnitRunner.class)
public class CodegenApplicationModelTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private DbApplication application;

    @Mock
    private SkysailRouter router;

    @Mock
    private DesignerRepository repo;

    @Mock
    private Bundle bundle;
    private List<DbEntity> entities;


    @Before
    public void setUp() throws Exception {
        application = new DbApplication("testapp", "pkgName", "../", "projectName");
        application.setId("#1");
        entities = new ArrayList<>();
        application.setEntities(entities);
        Mockito.when(repo.getById(DbApplication.class, "1")).thenReturn(application);
    }

    @Test
    public void testName() {
        CodegenApplicationModel applicationModel = new CodegenApplicationModel(application);
        assertThat(applicationModel.getName(), is(equalTo("testapp")));
    }

    @Test
    public void adding_entity_succeeds() {
        CodegenApplicationModel applicationModel = new CodegenApplicationModel(application);
        CodegenEntityModel addedEntity = applicationModel.addEntity(new DbEntity("entityName"));
        assertThat(addedEntity.getId(), is(equalTo("pkgName.entityName")));
    }

    @Test
    @Ignore //FIXME
    public void adding_entity_twice_throws_exception() {
        thrown.expect(IllegalStateException.class);
        CodegenApplicationModel applicationModel = new CodegenApplicationModel(application);
        applicationModel.addEntity(new DbEntity("entityName"));
        applicationModel.addEntity(new DbEntity("entityName"));
    }

    @Test
    public void validation_succeeds_for_reference_with_known_entity() {
        CodegenApplicationModel applicationModel = new CodegenApplicationModel(application);
        CodegenEntityModel entity = applicationModel.addEntity(new DbEntity("entityName"));
        DbEntity unknownEntity = new DbEntity("entityName");
        entity.addReference(unknownEntity);
    }

    @Test
    @Ignore
    public void validation_throws_expection_for_reference_with_unknown_entity() {
        thrown.expect(IllegalStateException.class);
        CodegenApplicationModel applicationModel = new CodegenApplicationModel(application);
        CodegenEntityModel entity = applicationModel.addEntity(new DbEntity("entityName"));
        DbEntity unknownEntity = new DbEntity("unknown");
        entity.addReference(unknownEntity);
    }

    @Test
    public void simplest_model_is_empty() throws Exception {
        CodegenApplicationModel applicationModel = new CodegenApplicationModel(application);
        assertThat(applicationModel.getName(), is(equalTo("testapp")));
        assertThat(applicationModel.getEntityValues().size(), is(0));
    }

    @Test
    public void creates_model_for_simpleEntity() {
        entities.add(new DbEntity("Bank"));

        CodegenApplicationModel applicationModel = new CodegenApplicationModel(application);

        assertThat(applicationModel.getName(), is(equalTo("testapp")));
        assertThat(applicationModel.getEntityValues().size(), is(1));

        CodegenEntityModel entityModel = (CodegenEntityModel) applicationModel.getEntityValues().iterator().next();
        assertThat(entityModel.getId(), is(equalTo("pkgName.Bank")));
        assertThat(entityModel.getFields().size(), is(0));
        assertThat(entityModel.getReferences().size(), is(0));
    }

    @Test
    @Ignore
    public void creates_model_for_Entity_with_field() {
        DbEntity entity = new DbEntity("Bank");
        DbEntityField field = new DbEntityField();
        field.setName("fieldname");
        //entity.setFields(Arrays.asList(field));
        entities.add(entity);

        CodegenApplicationModel applicationModel = new CodegenApplicationModel(application);

        assertThat(applicationModel.getName(), is(equalTo("testapp")));

        CodegenEntityModel entityModel = (CodegenEntityModel) applicationModel.getEntityValues().iterator().next();
        assertThat(entityModel.getId(), is(equalTo("pkgName.Bank")));
        assertThat(entityModel.getFields().size(), is(1));
        assertThat(entityModel.getReferences().size(), is(0));
    }

    @Test
    @Ignore
    public void creates_model_for_Entity_with_reference_to_itself() {
        DbEntity entity = new DbEntity("Bank");
        entity.setSubEntities(Arrays.asList(entity));
        entities.add(entity);

        CodegenApplicationModel applicationModel = new CodegenApplicationModel(application);

        assertThat(applicationModel.getName(), is(equalTo("testapp")));

        CodegenEntityModel entityModel = (CodegenEntityModel) applicationModel.getEntityValues().iterator().next();
        assertThat(entityModel.getId(), is(equalTo("pkgName.Bank")));
        assertThat(entityModel.getFields().size(), is(0));
        assertThat(entityModel.getReferences().size(), is(1));
    }


    @Test
    @Ignore
    public void creates_model_for_two_entities_with_reference() {
        DbEntity bankEntity = new DbEntity("Bank");
        DbEntityField field = new DbEntityField();
        field.setName("iban");
       // bankEntity.setFields(Arrays.asList(field));

        DbEntity accountEntity = new DbEntity("Account");
        accountEntity.setSubEntities(Arrays.asList(bankEntity));
        DbEntityField accountNumberField = new DbEntityField();
        accountNumberField.setName("accountNr");
       // accountEntity.setFields(Arrays.asList(accountNumberField));

        entities.add(bankEntity);
        entities.add(accountEntity);

        CodegenApplicationModel applicationModel = new CodegenApplicationModel(application);

        assertThat(applicationModel.getName(), is(equalTo("testapp")));

        List<EntityModel> models = new ArrayList<>(applicationModel.getEntityValues());
        List<String> entityModelNames = models.stream()
                .map(CodegenEntityModel.class::cast)
                .map(CodegenEntityModel::getId).collect(Collectors.toList());

        assertThat(entityModelNames, hasItem("Bank"));
        assertThat(entityModelNames, hasItem("Account"));

        //List<String> fieldModelNames = models.stream().map(CodegenEntityModel::getFields).flatMap(f -> f.stream()).map(CodegenFieldModel::getName).collect(Collectors.toList());
        //assertThat(fieldModelNames, hasItem("accountNr"));
    }

//    @Test
//    public void rejects_creates_model_for_Entity_with_reference_to_itself() {
//        DbEntity entity = new DbEntity("Bank");
//        entity.setSubEntities(Arrays.asList(entity));
//        entities.add(entity);
//
//        CodegenApplicationModel applicationModel = new CodegenApplicationModel(application, repo);
//
//        assertThat(applicationModel.getName(), is(equalTo("testapp")));
//
//        CodegenEntityModel entityModel = applicationModel.getEntities().iterator().next();
//        assertThat(entityModel.getEntityName(), is(equalTo("Bank")));
//        assertThat(entityModel.getFields().size(), is(0));
//        assertThat(entityModel.getReferences().size(), is(1));
//    }

}
