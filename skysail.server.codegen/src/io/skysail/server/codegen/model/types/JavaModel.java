package io.skysail.server.codegen.model.types;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NonNull;

@Getter
public abstract class JavaModel {

    
    /**
     * the package name for the current type
     */
    protected @NonNull String packageName;

    /**
     * the types name
     */
    protected @NonNull String typeName;
    
    /**
     * an optional entity's name this type is referring to 
     */
    protected String entityName;

    /**
     * an optional generic Parameter for this type, written as a String without the brackets.
     */
    protected String genericParameter;

    /**
     * an potentially empty set of imports.
     */
    protected Set<String> imports = new HashSet<>();

    /**
     * an potentially empty type this type extends.
     */
    protected JavaModel extendedModel;

    /**
     * an potentially empty set of types this type implements.
     */
    protected Set<JavaModel> implementedModels;

    public String getTypeDefinition() {
        StringBuilder sb = new StringBuilder("public class ").append(typeName);
        if (extendedModel != null) {
            sb.append(" extends ").append(extendedModel.typeName);
            if (extendedModel.genericParameter != null) {
                sb.append("<").append(extendedModel.genericParameter).append(">");
            }
        }
        if (implementedModels != null && implementedModels.size() > 0) {
            sb.append(" implements ").append(implementedModels.stream().map(i -> {
                return i.typeName;
            }).collect(Collectors.joining(", ")));
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("package ").append(packageName).append(";\n\n");

        sb.append(importStatement(new HashSet<JavaModel>(Arrays.asList(extendedModel))));
        sb.append(importStatement(implementedModels));
        sb.append("\n");
        sb.append(getTypeDefinition());

        return sb.toString();
    }

    private String importStatement(Set<JavaModel> models) {
        if (models == null) {
            return "";
        }
        return models.stream().filter(m -> {
            return m != null;
        }).map(m -> {
            return "import " + m.packageName + "." + m.typeName + ";\n";
        }).collect(Collectors.joining());
    }

    protected String firstLowercase(String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }
}
