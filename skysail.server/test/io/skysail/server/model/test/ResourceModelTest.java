package io.skysail.server.model.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.server.model.ResourceModel;

import java.util.List;

import org.junit.*;

public class ResourceModelTest {

    private TestEntity testEntity;
    private TestListResource testListResource;

    @Before
    public void setUp() throws Exception {
        testEntity = new TestEntity();
        testListResource = new TestListResource();
    }

    @Test
    public void finds_formField_from_entityMember() {
        ResourceModel<TestListResource, List<TestEntity>> resourceModel = new ResourceModel<>(testListResource, testEntity);
        assertThat(resourceModel.getTitle(), is(equalTo("Skysail")));
        assertThat(resourceModel.getParameterType(), is(equalTo(TestEntity.class)));
        assertThat(resourceModel.getFormfields().size(), is(2));
        //assertThat(resourceModel.getFormfields().get(0).getInputType(), is(equalTo(InputType.TEXT.name())));
        assertThat(resourceModel.getFormfields().get(0).getName(), is(equalTo("stringField")));
    }
}
