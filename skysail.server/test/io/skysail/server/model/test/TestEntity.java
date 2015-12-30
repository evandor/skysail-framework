package io.skysail.server.model.test;

import javax.validation.constraints.*;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.*;
import io.skysail.server.forms.*;
import lombok.*;

@Getter
@Setter
public class TestEntity implements Identifiable {

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

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {
    }

}
