package io.skysail.server.app.designer.codegen.templates.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.any;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import io.skysail.server.app.designer.codegen.templates.AbstractTemplateCompiler;
import io.skysail.server.app.designer.codegen.templates.ListResourceTemplateCompiler;
import io.skysail.server.app.designer.model.DesignerEntityModel;

@RunWith(MockitoJUnitRunner.class)
public class ListResourceTemplateCompilerTest extends TemplateCompilerTestBase {
    
    @Test
    public void source_template_depends_on_whether_entityModel_is_aggregate_and_or_has_selfreference() {
        DesignerEntityModel entityModel = mock(DesignerEntityModel.class);
       
        setupAggregateAndSelfreference(entityModel,false,false);
        assertThat(createSuT(entityModel).getTemplateName(),is("listResourceNonAggregate"));

        setupAggregateAndSelfreference(entityModel,true,false);
        assertThat(createSuT(entityModel).getTemplateName(),is("listResource"));

        setupAggregateAndSelfreference(entityModel,false,true);
        assertThat(createSuT(entityModel).getTemplateName(),is("listResourceNonAggregate"));

        setupAggregateAndSelfreference(entityModel,true,true);
        assertThat(createSuT(entityModel).getTemplateName(),is("listResourceWithSelfReference"));
}

    @Test
    public void entityWithOutRelations_yields_simple_links_attribute() {
        DesignerEntityModel entityModel = mock(DesignerEntityModel.class);
        
        setupAggregateAndSelfreference(entityModel,false,false);
        createSuT(entityModel).addAdditionalAttributes(template);
        verify(template, times(0)).add("listLinks", any(String.class));

        setupAggregateAndSelfreference(entityModel,true,false);
        createSuT(entityModel).addAdditionalAttributes(template);
        verify(template, times(1)).add("listLinks", "       return super.getLinks(PostnullResourceGen.classnull);");

        setupAggregateAndSelfreference(entityModel,false,true);
        createSuT(entityModel).addAdditionalAttributes(template);
        verify(template, times(0)).add("listLinks", any(String.class));
    }
    
    @Test
    public void compilers_collect_method_is_called() {
        createSuT(nonRootEntityModel).apply(template);
        verify(compiler, times(1)).collect("nonRootEntityModelPkg.entitynamenonroot.resources.entityNameNonRootsResourceGen", "", "src-gen");
    }

    @Test
    public void entityModel_is_cleared_and_added_to_templateGroup() {
        tc = createSuT(nonRootEntityModel);

        code = tc.apply(template);
        
        verify(template, times(1)).remove("entity");
        verify(template, times(1)).add("entity", nonRootEntityModel);
    }

    @Test
    public void getRoutePath() {
        ListResourceTemplateCompiler tc = new ListResourceTemplateCompiler(compiler, rootEntityModel, relation, codes);
        assertThat(tc.getRoutePath(),is("/entityNameWithRoots"));
    }
    
    protected AbstractTemplateCompiler createSuT(DesignerEntityModel entityModel) {
        return new ListResourceTemplateCompiler(compiler, entityModel, relation, codes);
    }



}
