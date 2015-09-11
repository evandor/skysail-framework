package io.skysail.server.app.crm;

import io.skysail.api.forms.*;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class CrmEntity implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -8977053023931029701L;
    @Id
    private String id;

    public CrmEntity(String creator) {
        this.creator = creator;
        this.owner = creator;
        this.created = new Date();
    }

    @Field(inputType = InputType.READONLY)
    protected String creator;

    @NotNull
    // need this as method annotation, otherwise problem with validation
    // framework
    public String getCreator() {
        return creator;
    }

    @Field(inputType = InputType.READONLY)
    protected Date created;
    //
    // @Field(type = InputType.READONLY)
    // protected Date changed;

    @Field(inputType = InputType.READONLY)
    protected String changedBy;

    @Field(inputType = InputType.READONLY)
    protected String owner;

}
