package io.skysail.server.codegen.model.types;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import io.skysail.server.codegen.model.entities.*;
import io.skysail.server.ext.apt.model.types.test.ModelTestBase;

import org.junit.Test;
import org.mockito.Mockito;

public class RepositoryModelTest extends ModelTestBase {

    @Test
    public void testToString() {
        Mockito.when(name.toString()).thenReturn("Contact");
        AptEntity entity = new AptEntity(element);

        EntityGraph graph = Mockito.mock(EntityGraph.class);
        JavaModel rootResourceModel = new RepositoryModel(entity, graph);

        assertThat(rootResourceModel.toString(), containsString("package io.skysail.server.test.crm;"));
        assertThat(rootResourceModel.toString(), containsString("public class ContactsRepository"));
    }

}
