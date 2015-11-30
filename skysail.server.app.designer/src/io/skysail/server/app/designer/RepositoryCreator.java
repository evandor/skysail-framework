package io.skysail.server.app.designer;

import io.skysail.server.app.designer.codegen.SkysailRepositoryCompiler2;
import io.skysail.server.app.designer.model.CodegenApplicationModel;

public class RepositoryCreator {

    private CodegenApplicationModel applicationModel;

    public RepositoryCreator(CodegenApplicationModel applicationModel) {
        this.applicationModel = applicationModel;
    }

    public String create(STGroupBundleDir stGroup) {
        SkysailRepositoryCompiler2 entityCompiler = new SkysailRepositoryCompiler2(applicationModel, stGroup);
        return entityCompiler.createRepository();
    }


}
