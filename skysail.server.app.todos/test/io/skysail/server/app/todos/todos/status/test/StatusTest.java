package io.skysail.server.app.todos.todos.status.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.todos.todos.status.Status;

import java.util.List;

import org.junit.Test;

public class StatusTest {

    @Test
    public void has_six_values() {
        Status[] values = Status.values();
        assertThat(values.length, is(6));
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
        assertThat(newStatus.getNexts().size(), is(1));
        checkFollowUpStatuses(newStatus.getNexts());
    }

    @Test
    public void testNextsOfPlannedStatus() {
        Status newStatus = Status.PLANNED;
        assertThat(newStatus.getNexts().size(), is(1));
        checkFollowUpStatuses(newStatus.getNexts());
    }
    
    @Test
    public void testNextsOfPostponedStatus() {
        Status newStatus = Status.POSTPONED;
        assertThat(newStatus.getNexts().size(), is(1));
        checkFollowUpStatuses(newStatus.getNexts());
    }

    @Test
    public void testNextsOfClosedStatus() {
        Status newStatus = Status.CLOSED;
        assertThat(newStatus.getNexts().size(), is(0));
        checkFollowUpStatuses(newStatus.getNexts());
    }

    @Test
    public void testNextsOfFinishedStatus() {
        Status newStatus = Status.FINISHED;
        assertThat(newStatus.getNexts().size(), is(0));
        checkFollowUpStatuses(newStatus.getNexts());
    }

    private void checkFollowUpStatuses(List<String> nextStatuses) {
        nextStatuses.stream().forEach(s -> Status.valueOf(s));
    }

}