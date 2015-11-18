package io.skysail.server.db.test;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;

import java.util.concurrent.atomic.AtomicInteger;

import javax.persistence.Id;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
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
