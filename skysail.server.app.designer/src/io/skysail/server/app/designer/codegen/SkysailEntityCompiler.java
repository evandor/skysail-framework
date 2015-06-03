package io.skysail.server.app.designer.codegen;

import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.app.designer.fields.EntityField;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.utils.BundleUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.osgi.framework.Bundle;

import de.twenty11.skysail.server.core.restlet.SkysailRouter;

@Slf4j
public class SkysailEntityCompiler extends SkysailCompiler {

    private String entityName;
    private String appEntityName;
    
    private DesignerRepository repo;
    private Bundle bundle;
    private String entityResourceClassName;
    
    @Getter
    private String entityClassName;

    @Getter
    private String postResourceClassName;
    
    @Getter
    private String putResourceClassName;
    
    @Getter
    private String listResourceClassName;
    
    private Application application;

    public SkysailEntityCompiler(DesignerRepository repo, Bundle bundle, Application a, String entityName, String appEntityName) {
        this.repo = repo;
        this.bundle = bundle;
        this.application = a;
        this.entityName = entityName;
        this.appEntityName = appEntityName;
    }

    public void createEntity(List<String> entityNames, List<String> entityClassNames) {
        String entityTemplate = BundleUtils.readResource(bundle, "code/Entity.codegen");
        entityClassName = setupEntityForCompilation(entityTemplate, application.getId(), entityName, appEntityName);
        entityNames.add(entityName);
        entityClassNames.add(entityClassName);
    }
    
    public void createResources() {
        String entityResourceTemplate = BundleUtils.readResource(bundle, "code/EntityResource.codegen");
        entityResourceClassName = setupEntityResourceForCompilation(entityResourceTemplate, application.getId(), entityName, appEntityName);
        
        String postResourceTemplate = BundleUtils.readResource(bundle, "code/PostResource.codegen");
        postResourceClassName = setupPostResourceForCompilation(postResourceTemplate, entityName, appEntityName); 
        
        String putResourceTemplate = BundleUtils.readResource(bundle, "code/PutResource.codegen");
        putResourceClassName = setupPutResourceForCompilation(putResourceTemplate, entityName, application.getId(), appEntityName); 
        
        String listServerResourceTemplate = BundleUtils.readResource(bundle, "code/ListServerResource.codegen");
        listResourceClassName = setupListResourceForCompilation(listServerResourceTemplate, entityName, entityClassName);
    }
    
    public String getEntityResourceClassName() {
        return entityResourceClassName;
    }
    
    public void updateRepository(DesignerRepository designerRepository) {
        //designerRepository.createWithSuperClass(DynamicEntity.class.getSimpleName(), entityName);
        designerRepository.register(getClass(entityClassName));
    }

    public void attachToRouter(SkysailRouter router, String applicationName, Entity e, Map<String, String> routerPaths) {
        String path = "";

//        Class<? extends PostEntityServerResource<?>> postResourceClass = (Class<? extends PostEntityServerResource<?>>) getClass(postResourceClassName);
//        router.attach(new RouteBuilder(path + "/" + appEntityName + "s/",
//                postResourceClass));
        routerPaths.put(path + "/" + appEntityName + "s/", postResourceClassName);

//        Class<? extends PutEntityServerResource<?>> putResourceClass = (Class<? extends PutEntityServerResource<?>>) getClass(putResourceClassName);
//        router.attach(new RouteBuilder(path + "/" + appEntityName + "s/{id}",
//                putResourceClass));
        routerPaths.put(path + "/" + appEntityName + "s/{id}", putResourceClassName);

//        Class<? extends ListServerResource<?>> listResourceClass = (Class<? extends ListServerResource<?>>) getClass(listResourceClassName);
//        router.attach(new RouteBuilder(path + "/" + appEntityName + "s",
//                listResourceClass));
        routerPaths.put(path + "/" + appEntityName + "s", listResourceClassName);
        
        if (e.isRootEntity()) {
//            router.attach(new RouteBuilder(path, listResourceClass));
            routerPaths.put(path, listResourceClassName);
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
                put("$applicationName$", application.getName() + "Application");
            }
        });
        String entityClassName = "io.skysail.server.app.designer.gencode." + entityName;
        collect(entityClassName, entityCode);
        return entityClassName;
    }

    private String getMethodName(EntityField f) {
        return f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
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
                put("$applicationName$", application.getName() + "Application");
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
                put("$applicationName$", application.getName() + "Application");
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
                put("$applicationName$", application.getName() + "Application");
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
                put("$applicationName$", application.getName() + "Application");
            }
        });

        String className = "io.skysail.server.app.designer.gencode." + theClassName;
        collect(className, listServerResourceCode);
        return className;
    }

   



}