package io.skysail.server.app.todos.services.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import io.skysail.server.app.todos.TodoList;
import io.skysail.server.app.todos.lists.ListsResource;
import io.skysail.server.app.todos.repo.ListsRepository;
import io.skysail.server.app.todos.services.ListService;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.testsupport.AbstractShiroTest;

import java.util.List;

import org.apache.shiro.subject.Subject;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ListServiceTest extends AbstractShiroTest {

    @Mock
    private ListsRepository listRepo;
    @Mock
    private ListsResource listsResource;

    @InjectMocks
    private ListService listService;

    @Before
    public void setUp() {
        super.setUp();
        subjectUnderTest = Mockito.mock(Subject.class);
        setSubject(subjectUnderTest);
        listService = new ListService(listRepo);
        when(subjectUnderTest.getPrincipal()).thenReturn("principal");
        Mockito.when(listsResource.getResponse()).thenReturn(response);
        when(listsResource.getRequest()).thenReturn(request);
    }

    @After
    public void tearDownSubject() {
        clearSubject();
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

        }), isA(Pagination.class));
    }
}
