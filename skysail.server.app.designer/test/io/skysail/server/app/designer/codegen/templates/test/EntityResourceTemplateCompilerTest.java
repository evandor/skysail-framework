package io.skysail.server.app.designer.codegen.templates.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import io.skysail.domain.core.EntityModel;
import io.skysail.server.app.designer.codegen.templates.AbstractTemplateCompiler;
import io.skysail.server.app.designer.codegen.templates.EntityResourceTemplateCompiler;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.model.DesignerEntityModel;

@RunWith(MockitoJUnitRunner.class)
public class EntityResourceTemplateCompilerTest extends TemplateCompilerTestBase {

    @Test
    public void templateName_depends_on_whether_entityModel_is_aggregate() {
        assertThat(createSuT(nonRootEntityModel).getTemplateName(),is("entityResourceNonAggregate"));
        assertThat(createSuT(rootEntityModel).getTemplateName(),is("entityResource"));
    }

    @Test
    public void entityWithOutRelations_yields_simple_links_attribute() {
        createSuT(nonRootEntityModel).apply(template);
        assertLinksAttribute(template, "PutentityNameNonRootResourceGen.class");
    }
    
    @Test
    public void entityModel_is_cleared_and_added_to_templateGroup() {
        tc = createSuT(nonRootEntityModel);

        code = tc.apply(template);
        
        verify(template, times(1)).remove("entity");
        verify(template, times(1)).add("entity", nonRootEntityModel);
    }

    @Test
    public void entityWithRelation_yields_links_attribute_with_that_relation() {
        EntityModel targetEntityModel = new DesignerEntityModel(new DbEntity("targetEntityName", false), "nonRootEntityModelPkg");
        when(relation.getTargetEntityModel()).thenReturn(targetEntityModel);
        createSuT(entityModelWithRelation).apply(template);
        assertLinksAttribute(template, "PutentityNameWithRelationResourceGen.class", "PostentityNameWithRelationToNewtargetEntityNameRelationResource.class", "entityNameWithRelationstargetEntityNamesResource.class");
    }

    @Test
    public void entityWithReference_yields_links_attribute_with_that_reference() {
        createSuT(entityModelWithReference).apply(template);
        assertLinksAttribute(template, "PutentityNameWithReferenceResourceGen.class", "PostentityNameWithRelationResourceGen.class");
    }

    @Test
    public void compilers_collect_method_is_called() {
        createSuT(nonRootEntityModel).apply(template);
        verify(compiler, times(1)).collect("nonRootEntityModelPkg.entitynamenonroot.resources.entityNameNonRootResourceGen", "", "src-gen");
    }

    @Test
    public void getRoutePath() {
        tc = createSuT(rootEntityModel);
        assertThat(tc.getRoutePath(),is("/entityNameWithRoots/{id}"));
    }

    protected AbstractTemplateCompiler createSuT(DesignerEntityModel entityModel) {
        return new EntityResourceTemplateCompiler(compiler, entityModel, relation, codes);
    }

}
