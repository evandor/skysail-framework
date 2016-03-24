package io.skysail.server.app.designer.codegen.templates.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.stringtemplate.v4.ST;

import io.skysail.domain.core.EntityRelation;
import io.skysail.server.app.designer.codegen.CompiledCode;
import io.skysail.server.app.designer.codegen.SkysailEntityCompiler;
import io.skysail.server.app.designer.codegen.templates.AbstractTemplateCompiler;
import io.skysail.server.app.designer.codegen.templates.EntityResourceTemplateCompiler;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.model.DesignerEntityModel;

public abstract class TemplateCompilerTestBase {
    
    @Mock
    protected SkysailEntityCompiler compiler;

    @Mock
    protected EntityRelation relation;
    
    @Spy
    protected ST template = new ST("");

    protected Map<String, CompiledCode> codes = new HashMap<>();
    
    private DbEntity rootEntity = new DbEntity("entityNameWithRoot", true);
    protected DesignerEntityModel rootEntityModel = new DesignerEntityModel(rootEntity, "rootEntityModelPkg");
    
    private DbEntity nonRootEntity = new DbEntity("entityNameNonRoot", false);
    protected DesignerEntityModel nonRootEntityModel = new DesignerEntityModel(nonRootEntity, "nonRootEntityModelPkg");

    protected DesignerEntityModel entityModelWithRelation;
    protected DesignerEntityModel entityModelWithReference;

    protected CompiledCode code;

    protected AbstractTemplateCompiler tc;
    
    protected Pattern inParenthesesPattern = Pattern.compile("\\(([^)]+)\\)");
    
    protected abstract AbstractTemplateCompiler createSuT(DesignerEntityModel entityModel);

    @Before
    public void setup() {
        codes = new HashMap<>();
        
        //template = new ST("");
        
        DbEntity anDbEntity = new DbEntity("entityNameWithRelation", true);
        entityModelWithRelation = new DesignerEntityModel(anDbEntity, "entityModelWithRelationPkg");
        entityModelWithRelation.getRelations().add(relation);

        DbEntity dbEntityWithReference = new DbEntity("entityNameWithReference", true);
        entityModelWithReference = new DesignerEntityModel(dbEntityWithReference, "entityModelWithReferencePkg");
        entityModelWithReference.addReference(anDbEntity);
    }
        
    @Test
    public void templateGroups_render_method_is_called() {
        tc = new EntityResourceTemplateCompiler(compiler, nonRootEntityModel, relation, codes);
        code = tc.apply(template);
        verify(template, times(1)).render();
    }

    protected void setupAggregateAndSelfreference(DesignerEntityModel entityModel, boolean aggreate, boolean selfref) {
        when(entityModel.isAggregate()).thenReturn(aggreate);
        when(entityModel.hasSelfReference()).thenReturn(selfref);
    }

    protected void assertLinksAttribute(ST template, String... expectedClassNames) {
        String attribute = (String)template.getAttribute("links");
        assertThat(attribute, containsString("return super.getLinks"));
        Matcher matcher = inParenthesesPattern.matcher(attribute);
        matcher.find();
        String group = matcher.group(0).replace("(","").replace(")","");
        String[] classesSplit = group.split(",");
        assertThat(classesSplit, is(expectedClassNames));
    }




}
