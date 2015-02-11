package io.skysail.server.ext.apt.model.types.test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import io.skysail.server.ext.apt.model.entities.Entity;
import io.skysail.server.ext.apt.model.entities.EntityGraph;
import io.skysail.server.ext.apt.model.types.JavaModel;
import io.skysail.server.ext.apt.model.types.RepositoryModel;

import org.junit.Test;
import org.mockito.Mockito;

public class RepositoryModelTest extends ModelTestBase {

    @Test
    public void testToString() {
        Mockito.when(name.toString()).thenReturn("Contact");
        Entity entity = new Entity(element);

        EntityGraph graph = Mockito.mock(EntityGraph.class);
        JavaModel rootResourceModel = new RepositoryModel(entity, graph);

        System.out.println(rootResourceModel.toString());
        assertThat(rootResourceModel.toString(), containsString("package io.skysail.server.test.crm;"));
        assertThat(rootResourceModel.toString(), containsString("public class ContactsRepository"));
        // assertThat(rootResourceModel.toString(),
        // containsString("public class RootResource extends ListServerResource<String>"));
    }

}
