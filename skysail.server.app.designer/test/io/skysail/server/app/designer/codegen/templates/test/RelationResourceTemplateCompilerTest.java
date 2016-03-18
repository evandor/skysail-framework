package io.skysail.server.app.designer.codegen.templates.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import io.skysail.server.app.designer.codegen.templates.AbstractTemplateCompiler;
import io.skysail.server.app.designer.codegen.templates.RelationResourceTemplateCompiler;
import io.skysail.server.app.designer.model.DesignerEntityModel;

@RunWith(MockitoJUnitRunner.class)
public class RelationResourceTemplateCompilerTest extends TemplateCompilerTestBase {

    @Test
    public void templateName_depends_on_whether_entityModel_is_aggregate() {
        assertThat(createSuT(nonRootEntityModel).getTemplateName(),is(nullValue()));
        assertThat(createSuT(rootEntityModel).getTemplateName(),is("relationResource"));
    }
    
    @Test
    public void entityModel_is_cleared_and_added_to_templateGroup() {
        when(relation.getTargetEntityModel()).thenReturn(nonRootEntityModel);
        
        createSuT(nonRootEntityModel).apply(template);
        
        verify(template, times(1)).remove("entity");
        verify(template, times(1)).add("entity", nonRootEntityModel);
    }
    
    @Test
    public void getRoutePath() {
        when(relation.getTargetEntityModel()).thenReturn(nonRootEntityModel);
        assertThat(createSuT(rootEntityModel).getRoutePath(),is("/entityNameWithRoots/{id}/entityNameNonRoots"));
    }

    protected AbstractTemplateCompiler createSuT(DesignerEntityModel entityModel) {
        return new RelationResourceTemplateCompiler(compiler, entityModel, relation, codes);
    }



}
