package io.skysail.server.codegen.test.simple;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.codegen.test.simple.todos.TodoRepo;
import lombok.Getter;

public class TodoApplication extends SkysailApplication {

    @Getter
    private TodoRepo todoRepo;
}
