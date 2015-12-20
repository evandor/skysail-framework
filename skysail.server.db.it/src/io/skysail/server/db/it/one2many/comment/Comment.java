package io.skysail.server.db.it.one2many.comment;

import io.skysail.api.forms.Field;
import io.skysail.domain.Identifiable;

import java.util.concurrent.atomic.AtomicInteger;

import javax.persistence.Id;

import lombok.*;

@Getter
@Setter
@ToString
public class Comment implements Identifiable {

    private static AtomicInteger commentCounter = new AtomicInteger();

    @Id
    private String id;

    @Field
    private String comment;

    public Comment() {
        this.comment = "Comment #"  + commentCounter.incrementAndGet();
    }
}
