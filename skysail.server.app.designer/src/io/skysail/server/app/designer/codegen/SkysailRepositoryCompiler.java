package io.skysail.server.app.designer.codegen;

import io.skysail.server.app.designer.application.Application;
import io.skysail.server.utils.BundleUtils;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.osgi.framework.Bundle;

public class SkysailRepositoryCompiler extends SkysailCompiler {

    private String repositoryClassName;

    public SkysailRepositoryCompiler(Bundle bundle, Application application) {
        super(application, bundle);
        repositoryClassName = "io.skysail.server.app.designer.gencode." + application.getName() + "Repository";
    }

    public void createRepository(List<String> entityNames, List<String> entityClassNames) {
        String template = BundleUtils.readResource(getBundle(), "code/Repository.codegen");
        setupForCompilation(template, entityNames, entityClassNames);
    }

    private void setupForCompilation(String template, List<String> entityNames, List<String> entityClassNames) {
        StringBuilder activationCode = new StringBuilder();
        activationCode.append("        dbService.createWithSuperClass(\"V\", ").append(entityNames.stream().map(n -> {
            return "\"".concat(n).concat("\"");
        }).collect(Collectors.joining(","))).append(");\n");
        activationCode.append("        dbService.register(").append(entityClassNames.stream().map(n -> {
            return n.concat(".class");
        }).collect(Collectors.joining(","))).append(");\n");
        @SuppressWarnings("serial")
        String entityCode = substitute(template, new HashMap<String, String>() {
            {
                put("$classname$", getApplication().getName() + "Repository");
                put("$activationcode$", activationCode.toString());
            }
        });

        collect(repositoryClassName, entityCode);
    }

    public Class<?> getRepositoryClass() {
        return getClass(repositoryClassName);
    }

}
