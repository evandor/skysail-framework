package io.skysail.server.app.designer;

import java.util.*;
import java.util.stream.Collectors;

import io.skysail.server.app.designer.codegen.SkysailRepositoryCompiler;
import io.skysail.server.app.designer.model.CodegenApplicationModel;
import io.skysail.server.domain.core.EntityModel;
import lombok.val;

public class RepositoryCreator {

    private CodegenApplicationModel applicationModel;

    public RepositoryCreator(CodegenApplicationModel applicationModel) {
        this.applicationModel = applicationModel;
    }

    public List<String> create(STGroupBundleDir stGroup) {
        val result = new ArrayList<String>();
        List<EntityModel> aggregateEntities = applicationModel.getEntityValues().stream().filter(e -> e.isAggregate()).collect(Collectors.toList());
        aggregateEntities.stream().forEach(e -> {
            SkysailRepositoryCompiler entityCompiler = new SkysailRepositoryCompiler(applicationModel, e, stGroup);
            result.add(entityCompiler.createRepository());
        });
        return result;
    }


}
