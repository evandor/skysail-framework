package io.skysail.server.app.designer.codegen.templates.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.stringtemplate.v4.ST;

import io.skysail.domain.core.EntityRelation;
import io.skysail.server.app.designer.codegen.CompiledCode;
import io.skysail.server.app.designer.codegen.SkysailCompiler;
import io.skysail.server.app.designer.codegen.templates.AbstractTemplateCompiler;
import io.skysail.server.app.designer.model.DesignerEntityModel;

@RunWith(MockitoJUnitRunner.class)
public class AbstractTemplateCompilerTest {

    @Mock
    private SkysailCompiler compiler;
    @Mock
    private DesignerEntityModel entityModel;
    @Mock
    private EntityRelation relation;
    @Mock
    private Map<String, CompiledCode> code;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testName() {
        AbstractTemplateCompiler atc = new AbstractTemplateCompiler(compiler, entityModel, relation, code) {
            @Override
            public String getTemplateName() {
                return null;
            }
            @Override
            public String getRoutePath() {
                return null;
            }
            @Override
            public CompiledCode apply(ST template) {
                return null;
            }
        };
        assertThat(atc.getSkysailCompiler(),is(compiler));
        assertThat(atc.getEntityModel(),is(entityModel));
        assertThat(atc.getRelation(),is(relation));
        assertThat(atc.getCodes(),is(code));
    }   
}
