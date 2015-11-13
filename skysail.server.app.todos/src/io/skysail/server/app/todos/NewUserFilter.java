package io.skysail.server.app.todos;

import io.skysail.server.app.todos.lists.PostListResource;
import io.skysail.server.app.todos.services.ListService;
import io.skysail.server.restlet.resources.SkysailServerResource;
import lombok.extern.slf4j.Slf4j;

import org.restlet.*;
import org.restlet.routing.Filter;

@Slf4j
public class NewUserFilter extends Filter {

    private TodoApplication application;
    private Class<? extends SkysailServerResource<?>> targetClass;

    public NewUserFilter(Class<? extends SkysailServerResource<?>> targetClass, TodoApplication todoApplication) {
        this.targetClass = targetClass;
        application = todoApplication;
        this.setContext(todoApplication.getContext());
        this.setNext(targetClass);
    }

    @Override
    protected int beforeHandle(Request request, Response response) {
        return super.beforeHandle(request, response);
    }

    @Override
    protected int doHandle(Request request, Response response) {
        return super.doHandle(request, response);
    }

    @Override
    protected void afterHandle(Request request, Response response) {
        ListService listService = (ListService) application.getContext().getAttributes().get(ListService.class.getName());
        SkysailServerResource<?> newInstance;
        try {
            newInstance = targetClass.newInstance();
            newInstance.init(application.getContext(), request, response);
            if (listService.getLists(newInstance).isEmpty()) {
                handleNewUser(listService);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
        //return super.doHandle(request, response);
    }

    private void handleNewUser(ListService listService) {
        log.info("new User to TodosApplication, creating default list...");
        PostListResource resource = new PostListResource();
        listService.addList(resource, new TodoList("default list"));
    }

    @Override
    public String toString() {
        return new StringBuilder(this.getClass().getSimpleName()).append(" (Restlet): -> ").append(targetClass != null ? targetClass.getName() : "???").toString();
    }

}