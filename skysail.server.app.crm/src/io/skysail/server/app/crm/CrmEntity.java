package io.skysail.server.app.crm;

import io.skysail.api.forms.Field;
import io.skysail.api.forms.InputType;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Field(type = InputType.READONLY)
    protected String creator;

    @NotNull
    // need this as method annotation, otherwise problem with validation
    // framework
    public String getCreator() {
        return creator;
    }

    @Field(type = InputType.READONLY)
    protected Date created;
    //
    // @Field(type = InputType.READONLY)
    // protected Date changed;

    @Field(type = InputType.READONLY)
    protected String changedBy;

    @Field(type = InputType.READONLY)
    protected String owner;

}
