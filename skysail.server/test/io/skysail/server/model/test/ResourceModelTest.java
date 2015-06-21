package io.skysail.server.model.test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.*;
import io.skysail.server.model.ResourceModel;

import java.util.*;

import org.junit.*;
import org.mockito.Mockito;

public class ResourceModelTest extends ModelTests {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        testEntity = new TestEntity();
        testEntity.setFieldWithoutAnnotation("fwa");
        testEntity.setStringField("sf");
        testEntity.setStringField_Textarea("sf_t");
    }

    @Test
    public void finds_formField_from_listResource() {
        ListServerResponse<TestEntity> listResponse = new ListServerResponse<>(Arrays.asList(testEntity));
        ResourceModel<TestListResource, List<TestEntity>> resourceModel = new ResourceModel<>(testListResource, listResponse);
        assertThat(resourceModel.getTitle(), is(equalTo("Skysail")));
        assertThat(resourceModel.getParameterizedType(), is(equalTo(TestEntity.class)));
        assertThat(resourceModel.getFormfields().size(), is(2));
        assertThat(resourceModel.getFormfields().get(0).getName(), is(equalTo("stringField")));
    }
    
    @Test
    public void constructor_works_for_putResource() {
        Mockito.when(request.toString()).thenReturn("/parent:22/");

        FormResponse<TestEntity> formResponse = new FormResponse<>(testEntity, ".");
        ResourceModel<TestPutResource, TestEntity> resourceModel = new ResourceModel<>(testPutResource, formResponse);
        assertThat(resourceModel.getParameterizedType(),is(equalTo(TestEntity.class)));
        //assertThat(resourceModel.getFields().keySet(), hasItem("stringField"));
    }
    
    @Test
    public void constructor_works_for_postResource() {
        FormResponse<TestEntity> formResponse = new FormResponse<>(testEntity, ".");
        ResourceModel<TestPostResource, TestEntity> resourceModel = new ResourceModel<>(testPostResource, formResponse);
        assertThat(resourceModel.getParameterizedType(),is(equalTo(TestEntity.class)));
        assertThat(resourceModel.getFields().keySet(), hasItem("stringField"));
    }

    @Test
    public void constructor_works_for_entityResource() {
        EntityServerResponse<TestEntity> entityResponse = new EntityServerResponse<>(testEntity);
        ResourceModel<TestEntityResource, TestEntity> resourceModel = new ResourceModel<>(testEntityResource, entityResponse);
        assertThat(resourceModel.getParameterizedType(),is(equalTo(TestEntity.class)));
        assertThat(resourceModel.getFields().keySet(), hasItem("stringField"));
    }

//    @Test
//    public void constructor_works_for_listResource() {
//        ResourceModel<TestListResource, List<TestEntity>> resourceModel = new ResourceModel<>(testListResource, Arrays.asList(testEntity));
//        assertThat(resourceModel.getParameterizedType(),is(equalTo(TestEntity.class)));
//        assertThat(resourceModel.getFormfields().size(), is(2));
//        assertThat(resourceModel.getModelType(),is(equalTo(ModelType.LIST)));
//    }
//    
//    @SuppressWarnings("unchecked")
//    @Test
//    public void converter_works_for_listResource() {
//        ResourceModel<TestListResource, List<TestEntity>> resourceModel = new ResourceModel<>(testListResource, Arrays.asList(testEntity));
//        //resourceModel.convert();
//        assertThat(resourceModel.getConvertedSource(), is(instanceOf(List.class)));
//        List<?> list = (List<?>)resourceModel.getConvertedSource();
//        assertThat(list.size(), is(1));
//        assertThat(list.get(0), is(instanceOf(Map.class)));
//        Map<String,Object> map = (Map<String, Object>) list.get(0);
//        assertThat(map.get("stringField"), is(equalTo("sf")));
//    }
}
