package io.skysail.server.app.designer.model.test;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.Bundle;

import de.twenty11.skysail.server.core.restlet.SkysailRouter;
import io.skysail.domain.core.EntityModel;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.fields.DbEntityField;
import io.skysail.server.app.designer.fields.DbEntityTextField;
import io.skysail.server.app.designer.model.DesignerApplicationModel;
import io.skysail.server.app.designer.model.DesignerEntityModel;
import io.skysail.server.app.designer.repo.DesignerRepository;

@RunWith(MockitoJUnitRunner.class)
public class DesignerApplicationModelTest {

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
        DesignerApplicationModel applicationModel = new DesignerApplicationModel(application);
        assertThat(applicationModel.getName(), is(equalTo("testapp")));
    }

//    @Test
//    public void adding_entity_succeeds() {
//        DesignerApplicationModel applicationModel = new DesignerApplicationModel(application);
//        DesignerEntityModel addedEntity = applicationModel.addEntity(new DbEntity("entityName"));
//        assertThat(addedEntity.getId(), is(equalTo("pkgName.entityName")));
//    }
//
//    @Test
//    public void validation_succeeds_for_reference_with_known_entity() {
//        DesignerApplicationModel applicationModel = new DesignerApplicationModel(application);
//        DesignerEntityModel entity = applicationModel.addEntity(new DbEntity("entityName"));
//        DbEntity unknownEntity = new DbEntity("entityName");
//        entity.addReference(unknownEntity);
//    }
//
//    @Test
//    @Ignore // TO CHECK
//    public void validation_throws_expection_for_reference_with_unknown_entity() {
//        thrown.expect(IllegalStateException.class);
//        DesignerApplicationModel applicationModel = new DesignerApplicationModel(application);
//        DesignerEntityModel entity = applicationModel.addEntity(new DbEntity("entityName"));
//        DbEntity unknownEntity = new DbEntity("unknown");
//        entity.addReference(unknownEntity);
//    }

    @Test
    public void simplest_model_is_empty() throws Exception {
        DesignerApplicationModel applicationModel = new DesignerApplicationModel(application);
        assertThat(applicationModel.getName(), is(equalTo("testapp")));
        assertThat(applicationModel.getEntityValues().size(), is(0));
    }

    @Test
    public void creates_model_for_simpleEntity() {
        entities.add(new DbEntity("Bank"));

        DesignerApplicationModel applicationModel = new DesignerApplicationModel(application);

        assertThat(applicationModel.getName(), is(equalTo("testapp")));
        assertThat(applicationModel.getEntityValues().size(), is(1));

        DesignerEntityModel entityModel = (DesignerEntityModel) applicationModel.getEntityValues().iterator().next();
        assertThat(entityModel.getId(), is(equalTo("pkgName.Bank")));
        assertThat(entityModel.getFields().size(), is(0));
        assertThat(entityModel.getReferences().size(), is(0));
    }

    @Test
    public void creates_model_for_Entity_with_field() {
        DbEntity entity = new DbEntity("Bank");
        DbEntityField field = new DbEntityTextField("fieldname",true);
        entity.setFields(Arrays.asList(field));
        entities.add(entity);

        DesignerApplicationModel applicationModel = new DesignerApplicationModel(application);

        assertThat(applicationModel.getName(), is(equalTo("testapp")));

        DesignerEntityModel entityModel = (DesignerEntityModel) applicationModel.getEntityValues().iterator().next();
        assertThat(entityModel.getId(), is(equalTo("pkgName.Bank")));
        assertThat(entityModel.getFields().size(), is(1));
        assertThat(entityModel.getReferences().size(), is(0));
    }

    @Test
    @Ignore
    public void creates_model_for_Entity_with_reference_to_itself() {
        DbEntity entity = new DbEntity("Bank");
       // entity.setSubEntities(Arrays.asList(entity));
        entities.add(entity);

        DesignerApplicationModel applicationModel = new DesignerApplicationModel(application);

        assertThat(applicationModel.getName(), is(equalTo("testapp")));

        DesignerEntityModel entityModel = (DesignerEntityModel) applicationModel.getEntityValues().iterator().next();
        assertThat(entityModel.getId(), is(equalTo("pkgName.Bank")));
        assertThat(entityModel.getFields().size(), is(0));
        assertThat(entityModel.getReferences().size(), is(1));
    }


    @Test
    public void creates_model_for_two_entities_with_relation() {
        DbEntity bankEntity = new DbEntity("Bank");
        DbEntityField field = new DbEntityTextField("iban", true);
        bankEntity.setFields(Arrays.asList(field));

        DbEntity accountEntity = new DbEntity("Account");
        DbEntityField accountNumberField = new DbEntityTextField("accountNumber", true);
        accountEntity.setFields(Arrays.asList(accountNumberField));

//        DbRelation relation = new DbRelation();
//        relation.setId("17");
//        relation.setName("accounts");
//        relation.setRelationType(EntityRelationType.ONE_TO_MANY.name());
//        relation.setTarget("Account");
        bankEntity.setOneToManyRelations(Arrays.asList(accountEntity));

        entities.add(bankEntity);
        entities.add(accountEntity);

        
        DesignerApplicationModel applicationModel = new DesignerApplicationModel(application);

        assertThat(applicationModel.getName(), is(equalTo("testapp")));

        List<EntityModel> models = new ArrayList<>(applicationModel.getEntityValues());
        List<String> entityModelNames = models.stream()
                .map(DesignerEntityModel.class::cast)
                .map(DesignerEntityModel::getId).collect(Collectors.toList());

        assertThat(entityModelNames, hasItem("pkgName.Bank"));
        assertThat(entityModelNames, hasItem("pkgName.Account"));
        
        assertThat(applicationModel.getEntity("pkgName.Bank").getRelations().size(), is(1));

        //List<String> fieldModelNames = models.stream().map(DesignerEntityModel::getFields).flatMap(f -> f.stream()).map(DesignerFieldModel::getName).collect(Collectors.toList());
        //assertThat(fieldModelNames, hasItem("accountNr"));
    }

//    @Test
//    public void rejects_creates_model_for_Entity_with_reference_to_itself() {
//        DbEntity entity = new DbEntity("Bank");
//        entity.setSubEntities(Arrays.asList(entity));
//        entities.add(entity);
//
//        DesignerApplicationModel applicationModel = new DesignerApplicationModel(application, repo);
//
//        assertThat(applicationModel.getName(), is(equalTo("testapp")));
//
//        DesignerEntityModel entityModel = applicationModel.getEntities().iterator().next();
//        assertThat(entityModel.getEntityName(), is(equalTo("Bank")));
//        assertThat(entityModel.getFields().size(), is(0));
//        assertThat(entityModel.getReferences().size(), is(1));
//    }
    
    @Test
    public void toString_is_inherited() {
        entities.add(new DbEntity("entityA"));
        DesignerApplicationModel applicationModel = new DesignerApplicationModel(application);

        String[] toString = applicationModel.toString().split("\n");
        
        int i = 0;
        assertThat(toString[i++], is("DesignerApplicationModel: testapp, projectName=projectName, path=../"));
        assertThat(toString[i++], is("Entities: "));
        assertThat(toString[i++], is(" * DesignerEntityModel: pkgName.entityA"));
        assertThat(toString[i++], is(""));
        assertThat(toString[i++], is("Repositories: "));
    }

}
