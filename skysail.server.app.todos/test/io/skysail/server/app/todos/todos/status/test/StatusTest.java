package io.skysail.server.app.todos.todos.status.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.todos.todos.status.Status;

import java.util.List;

import org.junit.Test;

public class StatusTest {

    @Test
    public void has_five_values() {
        Status[] values = Status.values();
        assertThat(values.length, is(5));
    }

    @Test
    public void testNextsOfNewStatus() {
        Status newStatus = Status.NEW;
        assertThat(newStatus.getNexts().size(), is(4));
        checkFollowUpStatuses(newStatus.getNexts());
    }

    @Test
    public void testNextsOfWipStatus() {
        Status newStatus = Status.WIP;
        assertThat(newStatus.getNexts().size(), is(2));
        checkFollowUpStatuses(newStatus.getNexts());
    }

    @Test
    public void testNextsOfPlannedStatus() {
        Status newStatus = Status.PLANNED;
        assertThat(newStatus.getNexts().size(), is(3));
        checkFollowUpStatuses(newStatus.getNexts());
    }

    private void checkFollowUpStatuses(List<String> nextStatuses) {
        nextStatuses.stream().forEach(s -> Status.valueOf(s));
    }

}
