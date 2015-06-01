package io.skysail.server.model.test;

import io.skysail.api.forms.*;
import lombok.*;

@Getter
@Setter
public class TestEntity {

    /**
     * This field is ignored by skysails ResourceModel
     */
    private String fieldWithoutAnnotation;
    
    @Field
    private String stringField;
    
    @Field(type = InputType.TEXTAREA)
    private String stringField_Textarea;
    
}
