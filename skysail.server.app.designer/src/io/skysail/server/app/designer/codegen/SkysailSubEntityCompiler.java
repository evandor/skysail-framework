package io.skysail.server.app.designer.codegen;

import io.skysail.server.app.designer.application.Application;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.utils.BundleUtils;

import java.util.HashMap;

import org.osgi.framework.Bundle;

public class SkysailSubEntityCompiler extends SkysailEntityCompiler {

    private String parentEntityName;

    public SkysailSubEntityCompiler(DesignerRepository repo, Bundle bundle, Application application, String entityName,
            String parentName) {
        super(repo, bundle, application, entityName, parentName);
    }

    public void createResources() {
        String entityResourceTemplate = BundleUtils.readResource(getBundle(), "code/EntityResource.codegen");
        entityResourceClassName = setupEntityResourceForCompilation(entityResourceTemplate, getApplication().getId(),
                entityName, appEntityName);

        String postResourceTemplate = BundleUtils.readResource(getBundle(), "code/PostSubResource.codegen");
        postResourceClassName = setupPostResourceForCompilation(postResourceTemplate, entityName, appEntityName);

        String putResourceTemplate = BundleUtils.readResource(getBundle(), "code/PutResource.codegen");
        putResourceClassName = setupPutResourceForCompilation(putResourceTemplate, entityName,
                getApplication().getId(), appEntityName);

        String listServerResourceTemplate = BundleUtils.readResource(getBundle(), "code/ListServerResource.codegen");
        listResourceClassName = setupListResourceForCompilation(listServerResourceTemplate, entityName, entityClassName);
    }
    
    protected String setupPostResourceForCompilation(String postResourceTemplate, String entityName,
            String entityShortName) {
        final String className2 = "Post" + entityName + "Resource";
        @SuppressWarnings("serial")
        String postResourceCode = substitute(postResourceTemplate, new HashMap<String, String>() {
            {
                put("$classname$", className2);
                put("$entityname$", entityName);
                put("$entityShortName$", entityShortName);
                put("$applicationName$", getApplication().getName() + "Application");
                put("$packagename$", getApplication().getPackageName());
                put("$parententity$", parentEntityName);
            }
        });
        String fullClassName = application.getPackageName() + "." + className2;
        collect(fullClassName, postResourceCode);
        return fullClassName;
    }

    public void setParent(String parentEntityName) {
        this.parentEntityName = parentEntityName;
    }
}
