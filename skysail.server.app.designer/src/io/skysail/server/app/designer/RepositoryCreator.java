package io.skysail.server.app.designer;

import java.util.*;
import java.util.stream.Collectors;

import io.skysail.server.app.designer.codegen.*;
import io.skysail.server.app.designer.model.CodegenApplicationModel;
import io.skysail.domain.core.EntityModel;
import lombok.val;

public class RepositoryCreator {

    private CodegenApplicationModel applicationModel;
    private JavaCompiler compiler;

    public RepositoryCreator(CodegenApplicationModel applicationModel, JavaCompiler compiler) {
        this.applicationModel = applicationModel;
        this.compiler = compiler;
    }

    public List<String> create(STGroupBundleDir stGroup) {
        val result = new ArrayList<String>();
        List<EntityModel> aggregateEntities = applicationModel.getEntityValues().stream().filter(e -> e.isAggregate()).collect(Collectors.toList());
        aggregateEntities.stream().forEach(e -> {
            SkysailRepositoryCompiler entityCompiler = new SkysailRepositoryCompiler(applicationModel, e, stGroup, compiler);
            result.add(entityCompiler.createRepository());
        });
        return result;
    }


}
