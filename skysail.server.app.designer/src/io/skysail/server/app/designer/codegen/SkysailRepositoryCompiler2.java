package io.skysail.server.app.designer.codegen;

import io.skysail.server.app.designer.STGroupBundleDir;
import io.skysail.server.app.designer.model.CodegenApplicationModel;
import io.skysail.server.app.designer.model.CodegenEntityModel;

import java.util.List;
import java.util.stream.Collectors;

import org.stringtemplate.v4.ST;

public class SkysailRepositoryCompiler2 extends SkysailCompiler2 {

    public SkysailRepositoryCompiler2(CodegenApplicationModel applicationModel, STGroupBundleDir stGroup) {
        super(applicationModel, stGroup);
    }

    public String createRepository() {
        ST template = getStringTemplateIndex("repository");
        return setupForCompilation(template, applicationModel);
    }

    private String setupForCompilation(ST template, CodegenApplicationModel applicationModel) {
        template.add("activationcode", activationCode(applicationModel));
        String entityCode = template.render();
        String entityClassName = applicationModel.getPackageName() + "." + applicationModel.getApplicationName()
                + "Repository";
        collect(entityClassName, entityCode);
        return entityClassName;
    }

    private String activationCode(CodegenApplicationModel applicationModel) {
        StringBuilder activationCode = new StringBuilder();

        List<String> entityNames = applicationModel.getEntityModels().stream().map(CodegenEntityModel::getClassName).collect(Collectors.toList());
        activationCode.append("        dbService.createWithSuperClass(\"V\", ").append(entityNames.stream().map(n -> {
            return "\"".concat(n).concat("\"");
        }).collect(Collectors.joining(","))).append(");\n"); //

        List<String> entityClassNames = applicationModel.getEntityModels().stream().map(CodegenEntityModel::getClassName).collect(Collectors.toList());
        activationCode.append("        dbService.register(").append(entityClassNames.stream().map(n -> {
            return n.concat(".class");
        }).collect(Collectors.joining(","))).append(");\n");
        return activationCode.toString();
    }
}
