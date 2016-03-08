package io.skysail.server.app.oEService.user;

import java.io.Serializable;
import java.util.*;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.*;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.*;

/**
 * generated from javafile.stg
 */
@SuppressWarnings("serial")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="id")
public class User implements Identifiable, Serializable {

    @Id
    private String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    // --- fields ---

    @Field(inputType = InputType.TEXT, htmlPolicy = HtmlPolicy.NO_HTML)
    //@ListView(link = OEsResourceGen.class)
    private String name;

    public void setName(String value) {
        this.name = value;
    }

    public String getName() {
        return this.name;
    }


    // --- relations ---

    @Relation
    @JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
    private List<io.skysail.server.app.oEService.oe.OE> oEs = new ArrayList<>();

    public void setOEs(List<io.skysail.server.app.oEService.oe.OE> value) {
        this.oEs = value;
    }

    public List<io.skysail.server.app.oEService.oe.OE> getOEs() {
        return oEs;
    }




}