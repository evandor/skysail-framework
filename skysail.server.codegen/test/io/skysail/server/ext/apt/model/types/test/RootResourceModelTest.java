package io.skysail.server.ext.apt.model.types.test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import io.skysail.server.ext.apt.model.types.RootResourceModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
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
