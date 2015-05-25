//package io.skysail.server.converter.impl.factories.test;
//
//import static org.hamcrest.CoreMatchers.equalTo;
//import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.CoreMatchers.nullValue;
//import static org.junit.Assert.assertThat;
//import io.skysail.api.forms.InputType;
//import io.skysail.server.converter.impl.factories.DynamicEntityFieldFactory;
//import io.skysail.server.restlet.resources.SkysailServerResource;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mockito;
//import org.mockito.runners.MockitoJUnitRunner;
//
//import de.twenty11.skysail.server.beans.DynamicEntity;
//import de.twenty11.skysail.server.beans.EntityDynaProperty;
//import de.twenty11.skysail.server.core.FormField;
//
//@RunWith(MockitoJUnitRunner.class)
//public class DynamicEntityFieldFactoryTest {
//    
//    private DynamicEntityFieldFactory factory;
//
//    private SkysailServerResource<?> resource;
//
//    @Before
//    public void setUp() throws Exception {
//        resource = Mockito.mock(SkysailServerResource.class);
//    }
//
//    @Test
//    public void entity_with_no_properties_yields_empty_fields() throws Exception {
//        factory = new DynamicEntityFieldFactory(new DynamicEntity());
//        List<FormField> fields = factory.determineFrom(resource);
//        assertThat(fields.size(), is(0));
//    }
//
//    @Test
//    public void field_is_retrieved_from_dynamicEntities_properties() throws Exception {
//        DynamicEntity entity = new DynamicEntity() {
//            @SuppressWarnings("serial")
//            @Override
//            public Set<EntityDynaProperty> getProperties() {
//                return new HashSet<EntityDynaProperty>() {{
//                    add(new EntityDynaProperty("name", InputType.BUTTON, String.class));
//                }};
//            }
//        };
//        
//        factory = new DynamicEntityFieldFactory(entity);
//        List<FormField> fields = factory.determineFrom(resource);
//        assertThat(fields.size(), is(1));
//        assertThat(fields.get(0).getName(), is(equalTo("name")));
//        assertThat(fields.get(0).getNameKey(), is(equalTo(DynamicEntityFieldFactoryTest.class.getName() + "$1.name")));
//        assertThat(fields.get(0).getEntity(), is(nullValue()));
//        assertThat(fields.get(0).getValue(), is(nullValue()));
//        assertThat(fields.get(0).getInputType(), is(equalTo("button")));
//    }
//    
//    @Test
//    public void field_is_not_retrieved_from_dynamicEntities_properties_if_of_type_List() throws Exception {
//        DynamicEntity entity = new DynamicEntity() {
//            @SuppressWarnings("serial")
//            @Override
//            public Set<EntityDynaProperty> getProperties() {
//                return new HashSet<EntityDynaProperty>() {{
//                    add(new EntityDynaProperty("name", InputType.BUTTON, List.class));
//                }};
//            }
//        };
//        
//        factory = new DynamicEntityFieldFactory(entity);
//        List<FormField> fields = factory.determineFrom(resource);
//        assertThat(fields.size(), is(0));
//    }
//    
//
//}
