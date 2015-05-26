package io.skysail.server.app.todos.ranking;

import io.skysail.server.app.todos.todos.Todo;

import java.time.*;
import java.util.Date;

public class Ranker {

    public static Integer calcUrgency(Todo entity) {
        return getUrgency(entity);
    }

    private static int getUrgency(Todo todo) {
        if (todo.getStatus() == null) {
            return 0;
        }
        if (!todo.getStatus().isActive()) {
            return 0;
        }
        Date referenceDueDate = handleDueDate(todo.getDue(), todo.getStartDate());
        Date referenceStartDate = handleStartDate(todo.getStartDate());
        Date now = new Date();

        if (now.before(referenceStartDate)) {
            return 0;
        } else if (referenceDueDate.before(now)) {
            return 100;
        }
        Long startUntilDue = referenceDueDate.getTime() - referenceStartDate.getTime();
        Long startUntilNow = now.getTime() - referenceStartDate.getTime();
        return Math.round(100.0F * startUntilNow / startUntilDue);
    }

    private static Date handleDueDate(Date dueDate, Date startDate) {
        if (dueDate != null) {
            return dueDate;
        }
        if (startDate != null) {
            Date now = new Date();
            if (now.before(startDate)) {
                Long doubleTimeToStart = 2 * (startDate.getTime() - now.getTime());
                return Date.from(LocalDate.from(Instant.ofEpochMilli(doubleTimeToStart)).atStartOfDay().toInstant(ZoneOffset.MIN));
            } else {
                return now;
            }
        }
        return Date.from(LocalDate.now().plusMonths(1).atStartOfDay().toInstant(ZoneOffset.MIN));
    }

    private static Date handleStartDate(Date startDate) {
        return startDate != null ? startDate : new Date();
    }

   
}
