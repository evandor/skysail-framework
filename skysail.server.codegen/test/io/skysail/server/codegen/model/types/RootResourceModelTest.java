package io.skysail.server.codegen.model.types;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import io.skysail.server.codegen.model.types.RootResourceModel;
import io.skysail.server.ext.apt.model.types.test.ModelTestBase;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
@Ignore
public class RootResourceModelTest extends ModelTestBase {

	@Test
	public void testToString() {
        RootResourceModel rootResourceModel = new RootResourceModel(element);

		assertThat(rootResourceModel.toString(), containsString("package io.skysail.server.test.crm;"));
		assertThat(rootResourceModel.toString(),
		        containsString("import de.twenty11.skysail.server.core.restlet.ListServerResource;"));
		assertThat(rootResourceModel.toString(),
		        containsString("public class RootResource extends ListServerResource<String>"));
	}

}
