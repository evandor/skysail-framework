package io.skysail.server.domain.core.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.server.domain.core.*;

import org.junit.*;

public class ApplicationTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void simple_application_structure_can_be_created() {
        Application app = new Application("app17")
            .add(new Entity("e23")
                    .add(new Field("f23")))
            .add(new Entity("e24"));

        assertThat(app.getId(),is("app17"));
        assertThat(app.getEntities().size(),is(2));
        assertThat(app.getEntities().get("e23").getId(),is("e23"));
        assertThat(app.getEntities().get("e24").getId(),is("e24"));

        assertThat(app.getEntity("e23").getId(), is("e23"));
    }


}
