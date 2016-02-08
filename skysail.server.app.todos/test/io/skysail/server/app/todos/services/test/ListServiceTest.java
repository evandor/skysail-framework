package io.skysail.server.app.todos.services.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.apache.shiro.subject.Subject;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Response;

import com.tinkerpop.blueprints.impls.orient.OrientVertex;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.TodoList;
import io.skysail.server.app.todos.lists.ListsResource;
import io.skysail.server.app.todos.lists.PostListResource;
import io.skysail.server.app.todos.repo.ListsRepository;
import io.skysail.server.app.todos.services.ListService;
import io.skysail.server.app.todos.todos.Todo;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.testsupport.AbstractShiroTest;

@RunWith(MockitoJUnitRunner.class)
public class ListServiceTest extends AbstractShiroTest {

    @Mock
    private ListsRepository listRepo;

    @Mock
    private ListsResource listsResource;

    @InjectMocks
    private ListService listService;
    private TodoApplication application;

    @Before
    public void setUp() {
        super.setUp();
        subjectUnderTest = Mockito.mock(Subject.class);
        setSubject(subjectUnderTest);
        application = mock(TodoApplication.class);

        listService = new ListService(listRepo);

        when(subjectUnderTest.getPrincipal()).thenReturn("principal");
        when(listsResource.getResponse()).thenReturn(response);
        when(listsResource.getRequest()).thenReturn(request);
        when(listsResource.getApplication()).thenReturn(application);
        when(application.getListRepo()).thenReturn(listRepo);
    }

    @After
    public void tearDownSubject() {
        clearSubject();
    }

    @Test
    public void getList_delegates_to_repository_find_one() {
        listService.getList(listsResource, "#12:0");
        verify(listRepo).findOne("#12:0");
    }

    @Test
    public void getLists_delegates_to_listRepo_with_owner_set() {
        List<TodoList> lists = listService.getLists(listsResource);
        assertThat(lists.size(), is(0));
        verify(listRepo).findAllLists(argThat(new ArgumentMatcher<Filter>() {
            @Override
            public boolean matches(Object argument) {
                Filter f = (Filter)argument;
                return f.getParams().get("owner").equals("principal") && f.getPreparedStatement().equals("owner=:owner");            }

        }), org.mockito.Mockito.isA(Pagination.class));
    }

    @Test
    @Ignore 
    // FIXME
    public void addList_delegates_to_repository_save() {
        TodoList entity = new TodoList("title");
        PostListResource resource = mock(PostListResource.class);
        when(resource.getApplication()).thenReturn(application);
        OrientVertex orientVertex = Mockito.mock(OrientVertex.class);
        when(orientVertex.getId()).thenReturn("#12:0");
//        when(listRepo.save(entity, app.)).thenReturn(orientVertex);
        SkysailResponse<TodoList> addedListResponse = listService.addList(resource, entity);
        assertThat(addedListResponse.getEntity().getId(),is("#12:0"));
    }

    @Test
    // FIXME
    public void updateList_delegates_to_repo_update() {
        TodoList entity = new TodoList("title");
        SkysailServerResource<?> resource = mock(SkysailServerResource.class);
        when(resource.getApplication()).thenReturn(application);
        when(listRepo.findOne("#12:0")).thenReturn(entity);
        when(resource.getAttribute(TodoApplication.LIST_ID)).thenReturn("#12:0");

        listService.updateList(resource , entity);

        //verify(listRepo).update("#12:0", entity, "todos");
    }

    @Test
    public void delete_delegates_to_repository_delete_if_list_doesnot_have_associated_todos() {
        TodoList entity = new TodoList("title");
        SkysailServerResource<?> resource = mock(SkysailServerResource.class);
        when(resource.getApplication()).thenReturn(application);
        when(listRepo.findOne("listId")).thenReturn(entity);

        listService.delete(resource, "listId");

        verify(listRepo).delete("listId");
    }

    @Test
    public void delete_does_not_delegate_to_repository_delete_if_list_does_have_associated_todos() {
        TodoList entity = new TodoList("title");
        entity.setTodos(Arrays.asList(new Todo("hi")));
        SkysailServerResource<?> resource = mock(SkysailServerResource.class);
        when(resource.getApplication()).thenReturn(application);
        when(listRepo.findOne("listId")).thenReturn(entity);
        Response response = mock(Response.class);
        when(resource.getResponse()).thenReturn(response);

        listService.delete(resource, "listId");

        verify(listRepo, never()).delete("listId");
    }
}
