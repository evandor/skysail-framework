package io.skysail.server.app.designer.codegen;

import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.app.designer.fields.EntityField;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import io.skysail.server.restlet.resources.PutEntityServerResource;
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
import org.osgi.framework.BundleContext;

import de.twenty11.skysail.server.beans.DynamicEntity;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import de.twenty11.skysail.server.core.restlet.SkysailRouter;

@Slf4j
public class SkysailCompiler {

    private String appId;
    private String entityName;
    private String appEntityName;
    @Getter
    private String entityClassName;
    private DesignerRepository repo;
    private Bundle bundle;
    private String entityResourceClassName;
    
    @Getter
    private String postResourceClassName;
    
    @Getter
    private String putResourceClassName;
    
    @Getter
    private String listResourceClassName;

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
    
    public void createResources() {
        String entityResourceTemplate = BundleUtils.readResource(bundle, "code/EntityResource.codegen");
        entityResourceClassName = setupEntityResourceForCompilation(entityResourceTemplate, appId, entityName, appEntityName);
        
        String postResourceTemplate = BundleUtils.readResource(bundle, "code/PostResource.codegen");
        postResourceClassName = setupPostResourceForCompilation(postResourceTemplate, entityName, appEntityName); 
        
        String putResourceTemplate = BundleUtils.readResource(bundle, "code/PutResource.codegen");
        putResourceClassName = setupPutResourceForCompilation(putResourceTemplate, entityName, appId, appEntityName); 
        
        String listServerResourceTemplate = BundleUtils.readResource(bundle, "code/ListServerResource.codegen");
        listResourceClassName = setupListResourceForCompilation(listServerResourceTemplate, entityName, entityClassName);
    }
    
    public void compile(BundleContext bundleContext) {
        try {
            InMemoryJavaCompiler.compile(bundleContext);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
    
    public String getEntityResourceClassName() {
        return entityResourceClassName;
    }
    
    public void updateRepository(DesignerRepository designerRepository) {
        designerRepository.createWithSuperClass(DynamicEntity.class.getSimpleName(), entityName);
        designerRepository.register(getClass(entityClassName));
    }

    public void attachToRouter(SkysailRouter router, String applicationName, Entity e) {
        String path = "/preview/" + applicationName;

        Class<? extends PostEntityServerResource<?>> postResourceClass = (Class<? extends PostEntityServerResource<?>>) getClass(postResourceClassName);
        router.attach(new RouteBuilder(path + "/" + appEntityName + "s/",
                postResourceClass));

        Class<? extends PutEntityServerResource<?>> putResourceClass = (Class<? extends PutEntityServerResource<?>>) getClass(putResourceClassName);
        router.attach(new RouteBuilder(path + "/" + appEntityName + "s/{id}",
                putResourceClass));

        Class<? extends ListServerResource<?>> listResourceClass = (Class<? extends ListServerResource<?>>) getClass(listResourceClassName);
        router.attach(new RouteBuilder(path + "/" + appEntityName + "s",
                listResourceClass));
        if (e.isRootEntity()) {
            router.attach(new RouteBuilder(path, listResourceClass));
        }        
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
        String entityClassName = "io.skysail.server.app.designer.gencode." + entityName;
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

    private String setupEntityResourceForCompilation(String entityTemplate, String appId, String entityName, String appEntityName) {
        @SuppressWarnings("serial")
        String entityCode = substitute(entityTemplate, new HashMap<String, String>() {
            {
                put("$classname$", entityName + "Resource");
                put("$entityname$", entityName);
            }
        });
        String entityClassName = "io.skysail.server.app.designer.gencode." + entityName + "Resource";
        collect(entityClassName, entityCode);
        return entityClassName;
    }

    private String setupPostResourceForCompilation(String postResourceTemplate, String entityName, String entityShortName) {
        final String className2 = "Post" + entityName + "Resource";
        @SuppressWarnings("serial")
        String postResourceCode = substitute(postResourceTemplate, new HashMap<String, String>() {
            {
                put("$classname$", className2);
                put("$entityname$", entityName);
                put("$entityShortName$", entityShortName);
            }
        });
        String fullClassName = "io.skysail.server.app.designer.gencode." + className2;
        collect(fullClassName, postResourceCode);
        return fullClassName;
    }

    private String setupPutResourceForCompilation(String putResourceTemplate, String entityName, String appId, String entityShortName) {
        final String className2 = "Put" + entityName + "Resource";
        
        StringBuilder updateEntity = new StringBuilder(entityName + " original = getEntity();\n");
                
        List<EntityField> fields = getFields(repo, entityShortName, appId);
        String codeForFields = fields.stream().map(f -> {
            StringBuilder sb = new StringBuilder("\n");
            sb.append("        original.set"+getMethodName(f)+"(entity.get"+getMethodName(f)+"());");
            return sb.toString();
        }).collect(Collectors.joining(";\n"));
        
        updateEntity.append(codeForFields).append("\n");
        updateEntity.append("app.getRepository().update(id, original);\n");
        
        
        @SuppressWarnings("serial")
        String putResourceCode = substitute(putResourceTemplate, new HashMap<String, String>() {
            {
                put("$classname$", className2);
                put("$entityname$", entityName);
                put("$entityShortName$", entityShortName);
                
                put("$updateEntity$", updateEntity.toString());
            }
        });
        String fullClassName = "io.skysail.server.app.designer.gencode." + className2;
        collect(fullClassName, putResourceCode);
        return fullClassName;
    }
    
    private String setupListResourceForCompilation(String listServerResourceTemplate, String entityName, String entityClassName) {
        final String theClassName = entityName + "sResource";
        @SuppressWarnings("serial")
        String listServerResourceCode = substitute(listServerResourceTemplate, new HashMap<String, String>() {
            {
                put("$classname$", theClassName);
                put("$entityname$", entityName);
            }
        });

        String className = "io.skysail.server.app.designer.gencode." + theClassName;
        collect(className, listServerResourceCode);
        return className;
    }

    private Class<?> getClass(String className) {
        try {
            return InMemoryJavaCompiler.getClass(className);
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }



}