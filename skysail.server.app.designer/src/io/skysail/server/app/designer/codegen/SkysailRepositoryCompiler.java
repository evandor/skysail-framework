package io.skysail.server.app.designer.codegen;

import java.util.List;
import java.util.stream.Collectors;

import org.osgi.framework.Bundle;
import org.stringtemplate.v4.ST;

import io.skysail.domain.core.EntityModel;
import io.skysail.server.app.designer.codegen.templates.TemplateProvider;
import io.skysail.server.app.designer.codegen.writer.ProjectFileWriter;
import io.skysail.server.app.designer.model.DesignerApplicationModel;
import io.skysail.server.app.designer.model.DesignerEntityModel;
import io.skysail.server.stringtemplate.STGroupBundleDir;

public class SkysailRepositoryCompiler extends SkysailCompiler {

    private EntityModel entityModel;
    private Bundle bundle;
    private TemplateProvider templateProvider;

    public SkysailRepositoryCompiler(DesignerApplicationModel applicationModel, EntityModel entityModel,
            STGroupBundleDir stGroup, JavaCompiler compiler, Bundle bundle, TemplateProvider templateProvider) {
        super(applicationModel, stGroup, compiler);
        this.entityModel = entityModel;
        this.bundle = bundle;
        this.templateProvider = templateProvider;
    }

    public CompiledCode createRepository() {
        ST template = templateProvider.templateFor("repository");
        CompiledCode compiledCode = setupForCompilation(template, applicationModel);

        ST dsTemplate = templateProvider.templateFor("OSGI-INF/repositoryXml");
        dsTemplate.add("entityModel", entityModel);
        String xml = dsTemplate.render();
        ProjectFileWriter.save(applicationModel, "bundle/OSGI-INF",
                applicationModel.getPackageName() + "." + entityModel.getSimpleName() + "Repository.xml",
                xml.getBytes());

        return compiledCode;
    }

    private CompiledCode setupForCompilation(ST template, DesignerApplicationModel applicationModel) {
        template.add("activationcode", activationCode(applicationModel));
        template.add("entity", entityModel);
        String entityCode = template.render();
        String entityClassName = applicationModel.getPackageName() + "." + entityModel.getSimpleName() + "Repository";
        return collect(entityClassName, entityCode, "src-gen");
    }

    private String activationCode(DesignerApplicationModel applicationModel) {
        StringBuilder activationCode = new StringBuilder();

        List<String> entityNames = applicationModel.getEntityValues().stream().map(DesignerEntityModel.class::cast)
                .map(DesignerEntityModel::getClassName).collect(Collectors.toList());
        activationCode.append("        dbService.createWithSuperClass(\"V\", ").append(entityNames.stream().map(n -> {
            return "\"".concat(n).concat("\"");
        }).collect(Collectors.joining(","))).append(");\n"); //

        List<String> entityClassNames = applicationModel.getEntityValues().stream().map(DesignerEntityModel.class::cast)
                .map(DesignerEntityModel::getClassName).collect(Collectors.toList());
        activationCode.append("        dbService.register(").append(entityClassNames.stream().map(n -> {
            return n.concat(".class");
        }).collect(Collectors.joining(","))).append(");\n");
        return activationCode.toString();
    }
}
