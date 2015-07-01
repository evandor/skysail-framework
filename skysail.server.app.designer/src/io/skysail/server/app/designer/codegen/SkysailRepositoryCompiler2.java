package io.skysail.server.app.designer.codegen;

import io.skysail.server.app.designer.STGroupBundleDir;
import io.skysail.server.app.designer.model.ApplicationModel;
import io.skysail.server.app.designer.model.EntityModel;

import java.util.List;
import java.util.stream.Collectors;

import org.stringtemplate.v4.ST;

public class SkysailRepositoryCompiler2 extends SkysailCompiler2 {

    public SkysailRepositoryCompiler2(ApplicationModel applicationModel, STGroupBundleDir stGroup) {
        super(applicationModel, stGroup);
    }

    public String createRepository() {
        ST template = getStringTemplateIndex("repository");
        return setupForCompilation(template, applicationModel);
    }

    private String setupForCompilation(ST template, ApplicationModel applicationModel) {
        template.add("activationcode", activationCode(applicationModel));
        String entityCode = template.render();
        String entityClassName = applicationModel.getPackageName() + "." + applicationModel.getApplicationName()
                + "Repository";
        collect(entityClassName, entityCode);
        return entityClassName;
    }

    private String activationCode(ApplicationModel applicationModel) {
        StringBuilder activationCode = new StringBuilder();

        List<String> entityNames = applicationModel.getEntityModels().stream().map(EntityModel::getClassName).collect(Collectors.toList());
        activationCode.append("        dbService.createWithSuperClass(\"V\", ").append(entityNames.stream().map(n -> {
            return "\"".concat(n).concat("\"");
        }).collect(Collectors.joining(","))).append(");\n"); //

        List<String> entityClassNames = applicationModel.getEntityModels().stream().map(EntityModel::getClassName).collect(Collectors.toList());
        activationCode.append("        dbService.register(").append(entityClassNames.stream().map(n -> {
            return n.concat(".class");
        }).collect(Collectors.joining(","))).append(");\n");
        return activationCode.toString();
    }
}
