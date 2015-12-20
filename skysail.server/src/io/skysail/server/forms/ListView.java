package io.skysail.server.forms;

import io.skysail.api.links.LinkRelation;
import io.skysail.domain.Identifiable;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ListView {

    public static final class DEFAULT extends SkysailServerResource<Identifiable> {

        @Override
        public Identifiable getEntity() {
            return null;
        }

        @Override
        public LinkRelation getLinkRelation() {
            return null;
        }

    }

    Class<? extends SkysailServerResource<?>> link() default DEFAULT.class;

    int truncate() default -1;

    boolean hide() default false;

    String colorize() default "";

    String prefix() default "";
}
