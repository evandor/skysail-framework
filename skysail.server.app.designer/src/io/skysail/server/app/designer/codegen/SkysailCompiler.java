package io.skysail.server.app.designer.codegen;

import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.app.designer.fields.EntityField;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.utils.BundleUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.osgi.framework.Bundle;

@Slf4j
public class SkysailCompiler {

    private String appId;
    private String entityName;
    private String appEntityName;
    @Getter
    private String entityClassName;
    private DesignerRepository repo;
    private Bundle bundle;

    public SkysailCompiler(DesignerRepository repo, Bundle bundle, String appId, String entityName, String appEntityName) {
        this.repo = repo;
        this.bundle = bundle;
        this.appId = appId;
        this.entityName = entityName;
        this.appEntityName = appEntityName;
    }

    public void createEntity() {
        String entityTemplate = BundleUtils.readResource(bundle, "code/Entity.codegen");
        entityClassName = setupEntityForCompilation(entityTemplate, appId, entityName, appEntityName);

    }

    private String setupEntityForCompilation(String entityTemplate, String appId, String entityName,
            String appEntityName) {

        List<EntityField> fields = getFields(repo, appEntityName, appId);
        String codeForFields = fields.stream().map(f -> {
            StringBuilder sb = new StringBuilder("\n    @Field\n");
            sb.append("    private String " + f.getName() + ";");
            return sb.toString();
        }).collect(Collectors.joining(";\n"));

        String codeForGettersAndSetters = fields.stream().map(f -> {
            StringBuilder sb = new StringBuilder();
            String methodName = getMethodName(f);
            sb.append("    public void set" + methodName + "(String value) {\n");
            sb.append("        this." + f.getName() + " = value;\n");
            sb.append("    }\n");
            sb.append("\n");
            sb.append("    public String get" + methodName + "() {\n");
            sb.append("        return " + f.getName() + ";\n");
            sb.append("    }\n");
            return sb.toString();
        }).collect(Collectors.joining(";\n"));

        @SuppressWarnings("serial")
        String entityCode = substitute(entityTemplate, new HashMap<String, String>() {
            {
                put("$classname$", entityName);
                put("$applicationId$", appId);
                put("$appEntityName$", appEntityName);
                put("$fields$", codeForFields);
                put("$gettersAndSetters$", codeForGettersAndSetters);
            }
        });
        String entityClassName = "io.skysail.server.app.designer.codegen." + entityName;
        collect(entityClassName, entityCode);
        return entityClassName;
    }

    private String getMethodName(EntityField f) {
        return f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
    }

    private String substitute(String template, Map<String, String> substitutionMap) {
        for (String key : substitutionMap.keySet()) {
            template = template.replace(key, substitutionMap.get(key));
        }
        return template;
    }

    private void collect(String className, String entityCode) {
        try {
            InMemoryJavaCompiler.collect(className, entityCode);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        String filename = "../skysail.server.app.designer/src-gen/" + classNameToPath(className);
        // String filename = "../skysail.server.app.designer/src/" +
        // classNameToPath(className);
        try {
            Path path = Paths.get(filename);
            new File(path.getParent().toString()).mkdirs();
            Files.write(path, entityCode.getBytes());
        } catch (IOException e) {
            log.debug("could not write source code for compilation unit '{}' to '{}'", className, filename);
        }

    }

    private String classNameToPath(String className) {
        return Arrays.stream(className.split("\\.")).collect(Collectors.joining("/")).concat(".java");
    }
    
    
    private List<EntityField> getFields(DesignerRepository repo2, String beanName, String appIdentifier) {
        Application designerApplication = repo.getById(Application.class, appIdentifier.replace("#", ""));
        List<Entity> entities = designerApplication.getEntities();

        // streams dont't seem to work here ?!?! (with orientdb objects)
        for (Entity entity : entities) {
            if (beanName.equals(entity.getName())) {
               return entity.getFields();
//                for (EntityField entityField : fields) {
//                    //properties.add(new EntityDynaProperty(entityField.getName(), entityField.getType(), String.class));
//                }
//                
////                List<Entity> subEntities = entity.getSubEntities();
////                for (Entity sub : subEntities) {
////                    properties.add(new EntityDynaProperty(sub.getName(), null, List.class));
////                }
//                break;
            }
        }
        return Collections.emptyList();
    }

    public void createResources() {
        // TODO Auto-generated method stub
        
    }

}
