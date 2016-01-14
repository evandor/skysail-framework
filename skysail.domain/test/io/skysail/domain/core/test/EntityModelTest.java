package io.skysail.domain.core.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.*;

import io.skysail.domain.core.*;

public class EntityModelTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void construct_from_full_path_initializes_correct() {
        EntityModel entity = new EntityModel("io.skysail.domain.core.test.AThing");
        assertThat(entity.getFields().size(), is(0));
        assertThat(entity.getPackageName(), is("io.skysail.domain.core.test"));
        assertThat(entity.getSimpleName(), is("AThing"));
    }

    @Test
    public void construct_from_default_path_initializes_correct() {
        EntityModel entity = new EntityModel("AThing");
        assertThat(entity.getFields().size(), is(0));
        assertThat(entity.getPackageName(), is(""));
        assertThat(entity.getSimpleName(), is("AThing"));
    }

    @Test
    public void added_field_can_be_retrieved_again() {
        EntityModel entity = new EntityModel("AThing");
        FieldModel field = new FieldModel("fieldname");
        entity.add(field);
        assertThat(entity.getFields().size(), is(1));
        assertThat(entity.getField("fieldname").getId(), is("fieldname"));
    }

    @Test
    public void toString_is_formatted_nicely() {
        EntityModel entity = new EntityModel("AThing");
        entity.add(new FieldModel("fieldname1"));
        entity.add(new FieldModel("fieldname2"));
        entity.setRelations(Arrays.asList(new EntityRelation("relName", new EntityModel("ASubThing"), EntityRelationType.ONE_TO_MANY)));

        String[] toString = entity.toString().split("\n");
        
        int i = 0;
        assertThat(toString[i++], is("EntityModel: id='AThing', isAggregate=true"));
        assertThat(toString[i++], is("   Fields:"));
        assertThat(toString[i++], is("    - FieldModel(id=fieldname1, type=null, inputType=null)"));
        assertThat(toString[i++], is("    - FieldModel(id=fieldname2, type=null, inputType=null)"));
        assertThat(toString[i++], is("   Relations:"));
        assertThat(toString[i++], is("    - EntityRelation(name=relName, targetEntityModel=ASubThing, type=ONE_TO_MANY)"));
    }

    @Test
    public void finds_parent_AggregateRoot() {
        ApplicationModel applicationModel = new ApplicationModel("appName");
        
        EntityModel entityModel = new EntityModel("rootEntity");
        entityModel.setAggregate(true);
        EntityModel subEntityModel = new EntityModel("subEntity");
        subEntityModel.setAggregate(false);
        entityModel.setRelations(Arrays.asList(new EntityRelation("relation", subEntityModel, EntityRelationType.ONE_TO_MANY)));
        applicationModel.addOnce(entityModel);
        applicationModel.addOnce(subEntityModel);
       
        assertThat(subEntityModel.getAggregateRoot(),is(entityModel));
        
    }

}
