package io.skysail.server.model.test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.server.forms.FormField;
import io.skysail.server.model.DefaultEntityFieldFactory;

import java.util.Map;

import org.junit.*;
import org.mockito.Mockito;

public class DefaultEntityFieldFactoryTest extends ModelTests {

    private DefaultEntityFieldFactory defaultEntityFieldFactory;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        defaultEntityFieldFactory = new DefaultEntityFieldFactory(TestEntity.class);
    }

    /** --- importance field ------------------------------------------------ */

    @Test
    public void returns_importance_field_for_entityResurce() throws Exception {
        Map<String, FormField> fieldsMap = defaultEntityFieldFactory.determineFrom(testEntityResource);
        assertThat(fieldsMap.keySet(), hasItem("importance"));
    }

    @Test
    public void returns_importance_field_for_postResurce() throws Exception {
        Map<String, FormField> fieldsMap = defaultEntityFieldFactory.determineFrom(testPostResource);
        assertThat(fieldsMap.keySet(), hasItem("importance"));
    }

    @Test
    public void returns_importance_field_for_putResurce() throws Exception {
        Mockito.when(request.toString()).thenReturn("/parent:null/");
        Map<String, FormField> fieldsMap = defaultEntityFieldFactory.determineFrom(testPutResource);
        assertThat(fieldsMap.keySet(), hasItem("importance"));
    }

    @Test
    public void does_not_return_importance_field_for_listResurce() throws Exception {
        Map<String, FormField> fieldsMap = defaultEntityFieldFactory.determineFrom(testListResource);
        assertThat(fieldsMap.keySet(), not(hasItem("importance")));
    }

    /** --- parent field ------------------------------------------------- */

    @Test
    public void returns_parent_field_for_entityResurce() throws Exception {
        Map<String, FormField> fieldsMap = defaultEntityFieldFactory.determineFrom(testEntityResource);
        assertThat(fieldsMap.keySet(), hasItem("parent"));
    }

    @Test
    public void returns_parent_field_for_postResurce() throws Exception {
        Map<String, FormField> fieldsMap = defaultEntityFieldFactory.determineFrom(testPostResource);
        assertThat(fieldsMap.keySet(), hasItem("parent"));
    }

    @Test
    public void returns_parent_field_for_putResource_if_null() throws Exception {
        Mockito.when(request.toString()).thenReturn("/parent:null/");
        Map<String, FormField> fieldsMap = defaultEntityFieldFactory.determineFrom(testPutResource);
        assertThat(fieldsMap.keySet(), hasItem("parent"));
    }

    @Test
    public void does_not_return_parent_field_for_putResource_if_not_null() throws Exception {
        Mockito.when(request.toString()).thenReturn("/parent:55/");
        Map<String, FormField> fieldsMap = defaultEntityFieldFactory.determineFrom(testPutResource);
        assertThat(fieldsMap.keySet(), not(hasItem("parent")));
    }

    @Test
    public void does_not_return_parent_field_for_listResurce() throws Exception {
        Map<String, FormField> fieldsMap = defaultEntityFieldFactory.determineFrom(testListResource);
        assertThat(fieldsMap.keySet(), not(hasItem("parent")));
    }










}
