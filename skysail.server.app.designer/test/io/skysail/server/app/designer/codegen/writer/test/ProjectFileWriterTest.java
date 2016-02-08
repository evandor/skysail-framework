package io.skysail.server.app.designer.codegen.writer.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import io.skysail.server.app.designer.codegen.writer.ProjectFileWriter;

public class ProjectFileWriterTest {

    @Before
    public void setUp() throws Exception {
    }
    
    @Test
    public void creating_and_deleting_directory_works() {
        ProjectFileWriter.mkdirs("generated/testdir/../testdir2");
        assertThat(new File("generated/testdir2").exists(),is(true));
        ProjectFileWriter.deleteDir("generated/testdir/../testdir2");
        assertThat(new File("generated/testdir2").exists(),is(false));
    }

}
