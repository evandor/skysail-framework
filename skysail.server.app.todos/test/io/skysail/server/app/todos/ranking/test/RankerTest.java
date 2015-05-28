package io.skysail.server.app.todos.ranking.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.todos.ranking.Ranker;
import io.skysail.server.app.todos.todos.Todo;
import io.skysail.server.app.todos.todos.status.Status;

import java.time.*;
import java.util.*;

import org.junit.*;
import org.junit.rules.ExpectedException;

public class RankerTest {
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private List<Todo> referenceList;
    private Ranker ranker;

    @Before
    public void setUp() throws Exception {
        Date nextWeek = nowPlusWeeks(1);
        Date twoWeeksLater = nowPlusWeeks(2);
        referenceList = new ArrayList<>();
        Todo importantTodo = createTodo("important", Status.NEW, nextWeek, twoWeeksLater, 100);
        referenceList.add(importantTodo);
        ranker = new Ranker();
    }

    @Test
    public void urgency_is_high_for_active_todo_with_due_date_today() {
        Todo todo = createTodo("activeDueToday", Status.NEW, nowPlusWeeks(-1), nowPlusWeeks(0), 50);
        Integer urgency = Ranker.calcUrgency(todo);
        assertThat(urgency,is(greaterThan(90)));
    }

    @Test
    public void urgency_is_zero_for_non_active_todo_with_due_date_today() {
        Todo todo = createTodo("nonActiveDueToday", Status.FINISHED, nowPlusWeeks(-1), nowPlusWeeks(0), 51);
        Integer urgency = Ranker.calcUrgency(todo);
        assertThat(urgency,is(0));
    }

    @Test
    @Ignore
    public void urgency_is_50_for_active_todo_with_start_in_the_middle_from_now_to_due_date() {
        Todo todo = createTodo("activeDueInAWeek", Status.WIP, nowPlusWeeksAndDays(-1,-1), nowPlusWeeks(1), 52);
        Integer urgency = Ranker.calcUrgency(todo);
        assertThat(urgency,is(50));
    }

    private Todo createTodo(String string, Status status, Date startDate, Date dueDate, int i) {
        Todo todo = new Todo(string);
        todo.setStartDate(startDate);
        todo.setDue(dueDate);
        todo.setImportance(i);
        todo.setStatus(status);
        
        referenceList.add(todo);
        
        return todo;
    }

    private Date nowPlusWeeks(int weeksOffset) {
        return Date.from(LocalDate.now().plusWeeks(weeksOffset).atStartOfDay().toInstant(ZoneOffset.MIN));
    }
    
    private Date nowPlusWeeksAndDays(int weeksOffset, int dayOffset) {
        return Date.from(LocalDate.now().plusWeeks(weeksOffset).plusDays(dayOffset).atStartOfDay().toInstant(ZoneOffset.MIN));
    }
}

