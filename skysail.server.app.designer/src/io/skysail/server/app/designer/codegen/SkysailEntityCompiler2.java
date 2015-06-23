package io.skysail.server.app.designer.codegen;

import io.skysail.server.app.designer.model.ApplicationModel;
import io.skysail.server.app.designer.model.EntityModel;
import io.skysail.server.app.designer.repo.DesignerRepository;
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

    public SkysailEntityCompiler2(ApplicationModel applicationModel, ST template) {
        super(applicationModel, template);
        this.template = template;
    }

    public void createEntity(ApplicationModel applicationModel, EntityModel entityModel) {
        entityClassName = setupEntityForCompilation(template, applicationModel, entityModel);
        entityModel.setClassName(entityClassName);
    }

    private String setupEntityForCompilation(ST template, ApplicationModel applicationModel, EntityModel entityModel) {
        template.remove("entity");
        template.add("entity", entityModel);
        String entityCode = template.render();
        //System.out.println(entityCode);
        String entityClassName = applicationModel.getPackageName() + "." + entityModel.getEntityName();
        collect(entityClassName, entityCode);
        return entityClassName;
    }

}
