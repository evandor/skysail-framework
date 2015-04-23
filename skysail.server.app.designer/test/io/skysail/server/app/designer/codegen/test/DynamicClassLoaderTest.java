package io.skysail.server.app.designer.codegen.test;

import io.skysail.server.app.designer.codegen.*;

import org.junit.*;

public class DynamicClassLoaderTest {

    private CompiledCode compiledCode;

    @Before
    public void setUp() throws Exception {
        compiledCode = new CompiledCode("className");
    }

    @Test
    @Ignore
    public void testName() throws ClassNotFoundException {
        DynamicClassLoader classLoader = new DynamicClassLoader(null);
        classLoader.setCode(compiledCode);
        //assertThat(classLoader.find("className"), is(equalTo(compiledCode)));
    }
}
