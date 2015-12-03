package io.skysail.server.app.designer.codegen;

import java.util.List;
import java.util.stream.Collectors;

import org.stringtemplate.v4.ST;

import io.skysail.server.app.designer.STGroupBundleDir;
import io.skysail.server.app.designer.model.*;
import io.skysail.server.domain.core.EntityModel;

public class SkysailRepositoryCompiler extends SkysailCompiler {

    private EntityModel entityModel;

    public SkysailRepositoryCompiler(CodegenApplicationModel applicationModel, EntityModel entityModel, STGroupBundleDir stGroup) {
        super(applicationModel, stGroup);
        this.entityModel = entityModel;
    }

    public String createRepository() {
        ST template = getStringTemplateIndex("repository");
        return setupForCompilation(template, applicationModel);
    }

    private String setupForCompilation(ST template, CodegenApplicationModel applicationModel) {
        template.add("activationcode", activationCode(applicationModel));
        template.add("entity", entityModel);
        String entityCode = template.render();
        String entityClassName = applicationModel.getPackageName() + "." + entityModel.getSimpleName()
                + "Repository";
        collect(entityClassName, entityCode);
        return entityClassName;
    }

    private String activationCode(CodegenApplicationModel applicationModel) {
        StringBuilder activationCode = new StringBuilder();

        List<String> entityNames = applicationModel.getEntityValues().stream()
                .map(CodegenEntityModel.class::cast)
                .map(CodegenEntityModel::getClassName).collect(Collectors.toList());
        activationCode.append("        dbService.createWithSuperClass(\"V\", ").append(entityNames.stream().map(n -> {
            return "\"".concat(n).concat("\"");
        }).collect(Collectors.joining(","))).append(");\n"); //

        List<String> entityClassNames = applicationModel.getEntityValues().stream()
                .map(CodegenEntityModel.class::cast)
                .map(CodegenEntityModel::getClassName).collect(Collectors.toList());
        activationCode.append("        dbService.register(").append(entityClassNames.stream().map(n -> {
            return n.concat(".class");
        }).collect(Collectors.joining(","))).append(");\n");
        return activationCode.toString();
    }
}
