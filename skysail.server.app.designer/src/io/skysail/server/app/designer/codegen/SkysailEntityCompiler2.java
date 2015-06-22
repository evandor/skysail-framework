package io.skysail.server.app.designer.codegen;

import io.skysail.server.app.designer.model.ApplicationModel;
import io.skysail.server.app.designer.model.EntityModel;
import io.skysail.server.app.designer.model.FieldModel;
import io.skysail.server.app.designer.repo.DesignerRepository;

import java.util.stream.Collectors;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.stringtemplate.v4.ST;

@Slf4j
public class SkysailEntityCompiler2 extends SkysailCompiler2 {

    protected String entityName;
    protected String appEntityName;

    private DesignerRepository repo;
    protected String entityResourceClassName;

    @Getter
    protected String entityClassName;

    @Getter
    protected String postResourceClassName;

    @Getter
    protected String putResourceClassName;

    @Getter
    protected String listResourceClassName;
    
    private ST template;
    
    public SkysailEntityCompiler2(
            //DesignerRepository repo, Bundle bundle, Application application, String entityName,
            //String appEntityName
            ) {
//        super(application, bundle);
//        this.repo = repo;
//        this.entityName = entityName;
//        this.appEntityName = appEntityName;
    }

    public SkysailEntityCompiler2(ST template) {
        this.template = template;
    }

    public void createEntity(ApplicationModel applicationModel, EntityModel entityModel) {
        entityClassName = setupEntityForCompilation(template, applicationModel, entityModel);//, getApplication().getId(), entityName, appEntityName, entityModel);
//        entityNames.add(entityName);
//        entityClassNames.add(entityClassName);
    }

//    public void createResources() {
//        String entityResourceTemplate = BundleUtils.readResource(getBundle(), "code/EntityResource.codegen");
//        entityResourceClassName = setupEntityResourceForCompilation(entityResourceTemplate, getApplication().getId(),
//                entityName, appEntityName);
//
//        String postResourceTemplate = BundleUtils.readResource(getBundle(), "code/PostResource.codegen");
//        postResourceClassName = setupPostResourceForCompilation(postResourceTemplate, entityName, appEntityName);
//
//        String putResourceTemplate = BundleUtils.readResource(getBundle(), "code/PutResource.codegen");
//        putResourceClassName = setupPutResourceForCompilation(putResourceTemplate, entityName,
//                getApplication().getId(), appEntityName);
//
//        String listServerResourceTemplate = BundleUtils.readResource(getBundle(), "code/ListServerResource.codegen");
//        listResourceClassName = setupListResourceForCompilation(listServerResourceTemplate, entityName, entityClassName);
//    }
//
//    public String getEntityResourceClassName() {
//        return entityResourceClassName;
//    }
//
//    public void updateRepository(DesignerRepository designerRepository) {
//        // designerRepository.createWithSuperClass(DynamicEntity.class.getSimpleName(),
//        // entityName);
//        designerRepository.register(getClass(entityClassName));
//    }
//
//    public void attachToRouter(SkysailRouter router, String applicationName, Entity e, Map<String, String> routerPaths) {
//        String path = "";
//
//        routerPaths.put(path + "/" + appEntityName + "s/", postResourceClassName);
//        routerPaths.put(path + "/" + appEntityName + "s/{id}", putResourceClassName);
//        routerPaths.put(path + "/" + appEntityName + "s", listResourceClassName);
//
//        if (e.isRootEntity()) {
//            routerPaths.put(path, listResourceClassName);
//        }
//    }
//
    private String setupEntityForCompilation(ST template, ApplicationModel applicationModel, EntityModel entityModel) {//, String appId, String entityName,
//            String appEntityName, EntityModel entityModel) {

        String codeForFields = entityModel.getFields().stream().map(f -> {
            
            StringBuilder sb = new StringBuilder("\n    @Field\n");
            sb.append("    private String " + f.getName() + ";");
            return sb.toString();
        }).collect(Collectors.joining(";\n"));
        
        String codeForReferences = entityModel.getReferences().stream().map(f -> {
            
              StringBuilder sb = new StringBuilder("\n    @Reference(cls = "+f.getName()+".class)\n");
            sb.append("    @PostView(visibility=Visibility.HIDE)\n");
            sb.append("    @PutView(visibility=Visibility.HIDE)\n");
            sb.append("    private List<"+f.getName()+"> " + f.getName().toLowerCase() + "s = new ArrayList<>();\n");
            sb.append("\n");
            sb.append("    public void add"+f.getName()+"("+f.getName()+" entity) {\n");
            sb.append("        "+f.getName().toLowerCase()+"s.add(entity);\n");
            sb.append("    }\n");
            return sb.toString();
        }).collect(Collectors.joining(";\n"));

        String codeForGettersAndSetters = entityModel.getFields().stream().map(f -> {
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
        String entityCode = "";//
//        substitute(entityTemplate, new HashMap<String, String>() {
//            {
//                put("application", applicationModel);
//                put("entity", entityModel);
//                put("$classname$", entityName);
//               // put("$applicationId$", appId);
//                put("$appEntityName$", appEntityName);
//                put("$fields$", codeForFields);
//                put("$references$", codeForReferences);
//                put("$gettersAndSetters$", codeForGettersAndSetters + "\n");// + codeForGettersAndSetters2);
//               // put("$applicationName$", getApplication().getName() + "Application");
//                put("$packagename$", applicationModel.getPackageName());
//            }
//        });
        template.add("entity", entityModel);
        entityCode = template.render();
        System.out.println(entityCode);
        String entityClassName = applicationModel.getPackageName() + "." + entityModel.getEntityName();
        collect(entityClassName, entityCode);
        return entityClassName;
    }
//
//    private void fireEvent(String name) {
//        
//    }
//
    private String getMethodName(FieldModel f) {
        return f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
    }
//
//    private List<EntityField> getFields(DesignerRepository repo2, String beanName, String appIdentifier) {
//        Application designerApplication = repo.getById(Application.class, appIdentifier.replace("#", ""));
//        List<Entity> entities = designerApplication.getEntities();
//        return findFields(repo2, beanName, appIdentifier, entities);
//    }
//
//    private List<EntityField> findFields(DesignerRepository repo2, String beanName, String appIdentifier,
//            List<Entity> entities) {
//        // streams dont't seem to work here ?!?! (with orientdb objects)
//        for (Entity entity : entities) {
//            if (beanName.equals(entity.getName())) {
//                return entity.getFields();
//            }
//            List<EntityField> fieldsFromSubEntity = findFields(repo2, beanName, appIdentifier, entity.getSubEntities());
//            if (fieldsFromSubEntity.size() > 0) {
//                return fieldsFromSubEntity;
//            }
//        }
//        return Collections.emptyList();
//    }
//
//    private List<Entity> getReferences(DesignerRepository repo2, String beanName, String appIdentifier) {
//        Application designerApplication = repo.getById(Application.class, appIdentifier.replace("#", ""));
//        List<Entity> entities = designerApplication.getEntities();
//        return findReferences(repo2, beanName, appIdentifier, entities);
//    }
//    
//    private List<Entity> findReferences(DesignerRepository repo2, String beanName, String appIdentifier,
//            List<Entity> entities) {
//        // streams dont't seem to work here ?!?! (with orientdb objects)
//        for (Entity entity : entities) {
//            if (beanName.equals(entity.getName())) {
//                return entity.getSubEntities();
//            }
//            List<Entity> fieldsFromSubEntity = findReferences(repo2, beanName, appIdentifier, entity.getSubEntities());
//            if (fieldsFromSubEntity.size() > 0) {
//                return fieldsFromSubEntity;
//            }
//        }
//        return Collections.emptyList();
//    }
//
//    protected String setupEntityResourceForCompilation(String entityTemplate, String appId, String entityName,
//            String appEntityName) {
//        @SuppressWarnings("serial")
//        String entityCode = substitute(entityTemplate, new HashMap<String, String>() {
//            {
//                put("$classname$", entityName + "Resource");
//                put("$entityname$", entityName);
//                put("$applicationName$", getApplication().getName() + "Application");
//                put("$packagename$", getApplication().getPackageName());
//            }
//        });
//        String entityClassName = application.getPackageName() + "." + entityName + "Resource";
//        collect(entityClassName, entityCode);
//        return entityClassName;
//    }
//
//    protected String setupPostResourceForCompilation(String postResourceTemplate, String entityName,
//            String entityShortName) {
//        final String className2 = "Post" + entityName + "Resource";
//        @SuppressWarnings("serial")
//        String postResourceCode = substitute(postResourceTemplate, new HashMap<String, String>() {
//            {
//                put("$classname$", className2);
//                put("$entityname$", entityName);
//                put("$entityShortName$", entityShortName);
//                put("$applicationName$", getApplication().getName() + "Application");
//                put("$packagename$", getApplication().getPackageName());
//            }
//        });
//        String fullClassName = application.getPackageName() + "." + className2;
//        collect(fullClassName, postResourceCode);
//        return fullClassName;
//    }
//
//    protected String setupPutResourceForCompilation(String putResourceTemplate, String entityName, String appId,
//            String entityShortName) {
//        final String className2 = "Put" + entityName + "Resource";
//
//        StringBuilder updateEntity = new StringBuilder(entityName + " original = getEntity();\n");
//
//        List<EntityField> fields = getFields(repo, entityShortName, appId);
//        String codeForFields = fields.stream().map(f -> {
//            StringBuilder sb = new StringBuilder("\n");
//            sb.append("        original.set" + getMethodName(f) + "(entity.get" + getMethodName(f) + "());");
//            return sb.toString();
//        }).collect(Collectors.joining(";\n"));
//
//        updateEntity.append(codeForFields).append("\n");
//        updateEntity.append("app.getRepository().update(id, original);\n");
//
//        @SuppressWarnings("serial")
//        String putResourceCode = substitute(putResourceTemplate, new HashMap<String, String>() {
//            {
//                put("$classname$", className2);
//                put("$entityname$", entityName);
//                put("$entityShortName$", entityShortName);
//
//                put("$updateEntity$", updateEntity.toString());
//                put("$applicationName$", getApplication().getName() + "Application");
//                put("$packagename$", getApplication().getPackageName());
//            }
//        });
//        String fullClassName = application.getPackageName() + "." + className2;
//        collect(fullClassName, putResourceCode);
//        return fullClassName;
//    }
//
//    protected String setupListResourceForCompilation(String listServerResourceTemplate, String entityName,
//            String entityClassName) {
//        final String theClassName = entityName + "sResource";
//        @SuppressWarnings("serial")
//        String listServerResourceCode = substitute(listServerResourceTemplate, new HashMap<String, String>() {
//            {
//                put("$classname$", theClassName);
//                put("$entityname$", entityName);
//                put("$applicationName$", getApplication().getName() + "Application");
//                put("$packagename$", getApplication().getPackageName());
//            }
//        });
//
//        String className = application.getPackageName() + "." + theClassName;
//        collect(className, listServerResourceCode);
//        return className;
//    }

//    protected String substitute(String template, Map<String, Object> substitutionMap) {
//        for (String key : substitutionMap.keySet()) {
//            if (substitutionMap.get(key) != null) {
//                template = template.replace(key, substitutionMap.get(key));
//            }
//        }
//        return template;
//    }

}
