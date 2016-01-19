package io.skysail.server.app.designer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.osgi.framework.Bundle;

import io.skysail.domain.core.EntityModel;
import io.skysail.server.app.designer.codegen.CompiledCode;
import io.skysail.server.app.designer.codegen.JavaCompiler;
import io.skysail.server.app.designer.codegen.SkysailRepositoryCompiler;
import io.skysail.server.app.designer.model.DesignerApplicationModel;
import lombok.val;

public class RepositoryCreator {

    private DesignerApplicationModel applicationModel;
    private JavaCompiler compiler;
    private Bundle bundle;

    public RepositoryCreator(DesignerApplicationModel applicationModel, JavaCompiler compiler, Bundle bundle) {
        this.applicationModel = applicationModel;
        this.compiler = compiler;
        this.bundle = bundle;
    }

    public List<CompiledCode> create(STGroupBundleDir stGroup) {
        val result = new ArrayList<CompiledCode>();
        List<EntityModel> aggregateEntities = applicationModel.getEntityValues().stream().filter(e -> e.isAggregate()).collect(Collectors.toList());
        aggregateEntities.stream().forEach(e -> {
            SkysailRepositoryCompiler entityCompiler = new SkysailRepositoryCompiler(applicationModel, e, stGroup, compiler, bundle);
            CompiledCode compiledCode = entityCompiler.createRepository();
            result.add(compiledCode);
        });
        return result;
    }


}
