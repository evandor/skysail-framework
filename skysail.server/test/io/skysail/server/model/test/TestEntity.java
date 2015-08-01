package io.skysail.server.model.test;

import io.skysail.api.forms.*;
import io.skysail.server.forms.*;

import javax.validation.constraints.*;

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

    @Field(inputType = InputType.TEXTAREA)
    private String stringField_Textarea;

    @Field(inputType = InputType.RANGE)
    @Min(0)
    @Max(100)
    @ListView(hide=true)
    private Integer importance;

    @Reference() // , selectionProvider = ListSelectionProvider.class)
    @PostView(visibility = Visibility.SHOW_IF_NULL)
    @ListView(hide = true)
    //@ValidListId
    private String parent;

}
