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
import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.app.designer.fields.EntityField;
import io.skysail.server.app.designer.model.*;
import io.skysail.server.app.designer.repo.DesignerRepository;

@RunWith(MockitoJUnitRunner.class)
@Ignore
public class CodegenApplicationModelTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Application application;

    @Mock
    private SkysailRouter router;

    @Mock
    private DesignerRepository repo;

    @Mock
    private Bundle bundle;
    private List<Entity> entities;


    @Before
    public void setUp() throws Exception {
        application = new Application("testapp", "pkgName", "../", "projectName");
        application.setId("#1");
        entities = new ArrayList<>();
       // application.setEntities(entities);
        Mockito.when(repo.getById(Application.class, "1")).thenReturn(application);
    }

    @Test
    public void testName() {
        CodegenApplicationModel applicationModel = new CodegenApplicationModel(application, repo);
        assertThat(applicationModel.getApplicationName(), is(equalTo("testapp")));
    }

    @Test
    public void adding_entity_succeeds() {
        CodegenApplicationModel applicationModel = new CodegenApplicationModel(application, repo);
        CodegenEntityModel addedEntity = applicationModel.addEntity(new Entity("entityName"));
        assertThat(addedEntity.getId(), is(equalTo("entityName")));
    }

    @Test
    @Ignore // FIX ME
    public void adding_entity_twice_throws_exception() {
        thrown.expect(IllegalStateException.class);
        CodegenApplicationModel applicationModel = new CodegenApplicationModel(application, repo);
        applicationModel.addEntity(new Entity("entityName"));
        applicationModel.addEntity(new Entity("entityName"));
    }

    @Test
    public void validation_succeeds_for_reference_with_known_entity() {
        CodegenApplicationModel applicationModel = new CodegenApplicationModel(application, repo);
        CodegenEntityModel entity = applicationModel.addEntity(new Entity("entityName"));
        Entity unknownEntity = new Entity("entityName");
        entity.addReference(unknownEntity);
        applicationModel.validate();
    }

    @Test
    public void validation_throws_expection_for_reference_with_unknown_entity() {
        thrown.expect(IllegalStateException.class);
        CodegenApplicationModel applicationModel = new CodegenApplicationModel(application, repo);
        CodegenEntityModel entity = applicationModel.addEntity(new Entity("entityName"));
        Entity unknownEntity = new Entity("unknown");
        entity.addReference(unknownEntity);
        applicationModel.validate();
    }

    @Test
    public void simplest_model_is_empty() throws Exception {
        CodegenApplicationModel applicationModel = new CodegenApplicationModel(application, repo);
        assertThat(applicationModel.getApplicationName(), is(equalTo("testapp")));
        assertThat(applicationModel.getEntityModels().size(), is(0));
    }

    @Test
    public void creates_model_for_simpleEntity() {
        entities.add(new Entity("Bank"));

        CodegenApplicationModel applicationModel = new CodegenApplicationModel(application, repo);

        assertThat(applicationModel.getApplicationName(), is(equalTo("testapp")));
        assertThat(applicationModel.getEntityModels().size(), is(1));

        CodegenEntityModel entityModel = applicationModel.getEntityModels().iterator().next();
        assertThat(entityModel.getId(), is(equalTo("Bank")));
        assertThat(entityModel.getFields().size(), is(0));
        assertThat(entityModel.getReferences().size(), is(0));
    }

    @Test
    public void creates_model_for_Entity_with_field() {
        Entity entity = new Entity("Bank");
        EntityField field = new EntityField();
        field.setName("fieldname");
        //entity.setFields(Arrays.asList(field));
        entities.add(entity);

        CodegenApplicationModel applicationModel = new CodegenApplicationModel(application, repo);

        assertThat(applicationModel.getApplicationName(), is(equalTo("testapp")));

        CodegenEntityModel entityModel = applicationModel.getEntityModels().iterator().next();
        assertThat(entityModel.getId(), is(equalTo("Bank")));
        assertThat(entityModel.getFields().size(), is(1));
        assertThat(entityModel.getReferences().size(), is(0));
    }

    @Test
    public void creates_model_for_Entity_with_reference_to_itself() {
        Entity entity = new Entity("Bank");
        entity.setSubEntities(Arrays.asList(entity));
        entities.add(entity);

        CodegenApplicationModel applicationModel = new CodegenApplicationModel(application, repo);

        assertThat(applicationModel.getApplicationName(), is(equalTo("testapp")));

        CodegenEntityModel entityModel = applicationModel.getEntityModels().iterator().next();
        assertThat(entityModel.getId(), is(equalTo("Bank")));
        assertThat(entityModel.getFields().size(), is(0));
        assertThat(entityModel.getReferences().size(), is(1));
    }


    @Test
    public void creates_model_for_two_entities_with_reference() {
        Entity bankEntity = new Entity("Bank");
        EntityField field = new EntityField();
        field.setName("iban");
       // bankEntity.setFields(Arrays.asList(field));

        Entity accountEntity = new Entity("Account");
        accountEntity.setSubEntities(Arrays.asList(bankEntity));
        EntityField accountNumberField = new EntityField();
        accountNumberField.setName("accountNr");
       // accountEntity.setFields(Arrays.asList(accountNumberField));

        entities.add(bankEntity);
        entities.add(accountEntity);

        CodegenApplicationModel applicationModel = new CodegenApplicationModel(application, repo);

        assertThat(applicationModel.getApplicationName(), is(equalTo("testapp")));

        List<CodegenEntityModel> models = new ArrayList<>(applicationModel.getEntityModels());
        List<String> entityModelNames = models.stream().map(CodegenEntityModel::getId).collect(Collectors.toList());

        assertThat(entityModelNames, hasItem("Bank"));
        assertThat(entityModelNames, hasItem("Account"));

        //List<String> fieldModelNames = models.stream().map(CodegenEntityModel::getFields).flatMap(f -> f.stream()).map(CodegenFieldModel::getName).collect(Collectors.toList());
        //assertThat(fieldModelNames, hasItem("accountNr"));
    }

//    @Test
//    public void rejects_creates_model_for_Entity_with_reference_to_itself() {
//        Entity entity = new Entity("Bank");
//        entity.setSubEntities(Arrays.asList(entity));
//        entities.add(entity);
//
//        CodegenApplicationModel applicationModel = new CodegenApplicationModel(application, repo);
//
//        assertThat(applicationModel.getApplicationName(), is(equalTo("testapp")));
//
//        CodegenEntityModel entityModel = applicationModel.getEntities().iterator().next();
//        assertThat(entityModel.getEntityName(), is(equalTo("Bank")));
//        assertThat(entityModel.getFields().size(), is(0));
//        assertThat(entityModel.getReferences().size(), is(1));
//    }

}
