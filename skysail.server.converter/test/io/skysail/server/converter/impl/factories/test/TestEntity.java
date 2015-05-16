package io.skysail.server.converter.impl.factories.test;

import io.skysail.api.forms.Field;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestEntity {
    @Field
    private String title;
}