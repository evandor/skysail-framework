//package io.skysail.server.model.test;
//
//import static org.hamcrest.Matchers.*;
//import static org.junit.Assert.assertThat;
//import io.skysail.api.responses.FormResponse;
//import io.skysail.server.model.*;
//
//import java.util.*;
//
//import org.junit.*;
//import org.restlet.data.MediaType;
//import org.restlet.engine.resource.VariantInfo;
//
//public class ResourceModelTest {
//
//    private TestEntity testEntity;
//    private TestListResource testListResource;
//    private TestPutResource testPutResource;
//    private TestPostResource testPostResource;
//
//    @Before
//    public void setUp() throws Exception {
//        testEntity = new TestEntity();
//        testEntity.setFieldWithoutAnnotation("fwa");
//        testEntity.setStringField("sf");
//        testEntity.setStringField_Textarea("sf_t");
//
//        testListResource = new TestListResource();
//        testPutResource = new TestPutResource();
//        testPostResource = new TestPostResource();
//    }
//
//    @Test
//    @Ignore
//    public void finds_formField_from_entityMember() {
//        ResourceModel<TestListResource, List<TestEntity>> resourceModel = new ResourceModel<>(testListResource, testEntity, new VariantInfo(MediaType.TEXT_HTML));
//        assertThat(resourceModel.getTitle(), is(equalTo("Skysail")));
//        assertThat(resourceModel.getParameterizedType(), is(equalTo(TestEntity.class)));
//        assertThat(resourceModel.getFormfields().size(), is(2));
//        //assertThat(resourceModel.getFormfields().get(0).getInputType(), is(equalTo(InputType.TEXT.name())));
//        assertThat(resourceModel.getFormfields().get(0).getName(), is(equalTo("stringField")));
//    }
//    
//    @Test
//    public void constructor_works_for_putResource_with_formResponse() {
//        FormResponse<TestEntity> formResponse = new FormResponse<>(testEntity, ".");
//        ResourceModel<TestPutResource, TestEntity> resourceModel = new ResourceModel<>(testPutResource, formResponse, new VariantInfo(MediaType.TEXT_HTML));
//        assertThat(resourceModel.getParameterizedType(),is(equalTo(TestEntity.class)));
//        assertThat(resourceModel.getFormfields().size(), is(2));
//        assertThat(resourceModel.getModelType(),is(equalTo(ModelType.FORM)));
//    }
//    
//    @Test
//    public void constructor_works_for_postResource_with_formResponse() {
//        FormResponse<TestEntity> formResponse = new FormResponse<>(testEntity, ".");
//        ResourceModel<TestPostResource, TestEntity> resourceModel = new ResourceModel<>(testPostResource, formResponse, new VariantInfo(MediaType.TEXT_HTML));
//        assertThat(resourceModel.getParameterizedType(),is(equalTo(TestEntity.class)));
//        assertThat(resourceModel.getFormfields().size(), is(2));
//        assertThat(resourceModel.getModelType(),is(equalTo(ModelType.FORM)));
//    }
//    
//    @Test
//    public void constructor_works_for_listResource() {
//        ResourceModel<TestListResource, List<TestEntity>> resourceModel = new ResourceModel<>(testListResource, Arrays.asList(testEntity), new VariantInfo(MediaType.TEXT_HTML));
//        assertThat(resourceModel.getParameterizedType(),is(equalTo(TestEntity.class)));
//        assertThat(resourceModel.getFormfields().size(), is(2));
//        assertThat(resourceModel.getModelType(),is(equalTo(ModelType.LIST)));
//    }
//    
//    @SuppressWarnings("unchecked")
//    @Test
//    public void converter_works_for_listResource() {
//        ResourceModel<TestListResource, List<TestEntity>> resourceModel = new ResourceModel<>(testListResource, Arrays.asList(testEntity), new VariantInfo(MediaType.TEXT_HTML));
//        //resourceModel.convert();
//        assertThat(resourceModel.getConvertedSource(), is(instanceOf(List.class)));
//        List<?> list = (List<?>)resourceModel.getConvertedSource();
//        assertThat(list.size(), is(1));
//        assertThat(list.get(0), is(instanceOf(Map.class)));
//        Map<String,Object> map = (Map<String, Object>) list.get(0);
//        assertThat(map.get("stringField"), is(equalTo("sf")));
//    }
//}
