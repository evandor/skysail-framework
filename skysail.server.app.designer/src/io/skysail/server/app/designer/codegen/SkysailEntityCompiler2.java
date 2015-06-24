package io.skysail.server.app.designer.codegen;

import io.skysail.server.app.designer.STGroupBundleDir;
import io.skysail.server.app.designer.model.ApplicationModel;
import io.skysail.server.app.designer.model.EntityModel;
import io.skysail.server.app.designer.model.RouteModel;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import org.stringtemplate.v4.ST;

public class SkysailEntityCompiler2 extends SkysailCompiler2 {

    protected String entityResourceClassName;

    @Getter
    protected String entityClassName;

    private List<RouteModel> routes = new ArrayList<>();

    public SkysailEntityCompiler2(ApplicationModel applicationModel, STGroupBundleDir stGroup) {
        super(applicationModel, stGroup);
        this.stGroupDir = stGroup;
    }

    public void createEntity(EntityModel entityModel) {
        ST template = getStringTemplateIndex("javafile");
        entityClassName = setupEntityForCompilation(template, applicationModel, entityModel);
        entityModel.setClassName(entityClassName);
    }

    public void createResources(EntityModel entityModel) {
        ST template = getStringTemplateIndex("entityResource");
        entityResourceClassName = setupEntityResourceForCompilation(template, applicationModel, entityModel);

        ST postResourceTemplate = getStringTemplateIndex("postResource");
        String postResourceClassName = setupPostResourceForCompilation(postResourceTemplate,  applicationModel, entityModel);
        routes.add(new RouteModel("/" + entityModel.getEntityName() + "s/", postResourceClassName));
        
        ST putResourceTemplate = getStringTemplateIndex("putResource");
        String putResourceClassName = setupPutResourceForCompilation(putResourceTemplate,  applicationModel, entityModel);
        routes.add(new RouteModel("/" + entityModel.getEntityName() + "s/{id}", putResourceClassName));
        
        ST listResourceTemplate = getStringTemplateIndex("listResource");
        String listResourceClassName = setupListResourceForCompilation(listResourceTemplate,  applicationModel, entityModel);
        routes.add(new RouteModel("/" + entityModel.getEntityName() + "s", listResourceClassName));
        
        if (entityModel.isRootEntity()) {
            routes.add(new RouteModel("", listResourceClassName));
        }
    }

    private String setupEntityForCompilation(ST template, ApplicationModel applicationModel, EntityModel entityModel) {
        template.remove("entity");
        template.add("entity", entityModel);
        String entityCode = template.render();
        String entityClassName = applicationModel.getPackageName() + "." + entityModel.getEntityName();
        collect(entityClassName, entityCode);
        return entityClassName;
    }

    private String setupEntityResourceForCompilation(ST template, ApplicationModel applicationModel, EntityModel entityModel) {
        template.remove("entity");
        template.add("entity", entityModel);
        String entityCode = template.render();
        String entityClassName = applicationModel.getPackageName() + "." + entityModel.getEntityName() + "Resource";
        collect(entityClassName, entityCode);
        return entityClassName;
    }

    private String setupPostResourceForCompilation(ST template, ApplicationModel applicationModel,
            EntityModel entityModel) {
        final String simpleClassName = "Post" + entityModel.getEntityName() + "Resource";
        template.remove("entity");
        template.add("entity", entityModel);
        String entityCode = template.render();
        String entityClassName = applicationModel.getPackageName() + "." + simpleClassName;
        collect(entityClassName, entityCode);
        return entityClassName;
    }
    
    private String setupPutResourceForCompilation(ST template, ApplicationModel applicationModel,
            EntityModel entityModel) {
        final String simpleClassName = "Put" + entityModel.getEntityName() + "Resource";
        template.remove("entity");
        template.add("entity", entityModel);
        String entityCode = template.render();
        String entityClassName = applicationModel.getPackageName() + "." + simpleClassName;
        collect(entityClassName, entityCode);
        return entityClassName;
    }
    
    private String setupListResourceForCompilation(ST template, ApplicationModel applicationModel,
            EntityModel entityModel) {
        final String simpleClassName = entityModel.getEntityName() + "sResource";
        template.remove("entity");
        template.add("entity", entityModel);
        String entityCode = template.render();
        String entityClassName = applicationModel.getPackageName() + "." + simpleClassName;
        collect(entityClassName, entityCode);
        return entityClassName;
    }

    public List<RouteModel> getRouteModels() {
        return routes;
    }

}
