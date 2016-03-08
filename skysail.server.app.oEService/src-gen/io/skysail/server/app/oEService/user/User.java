package io.skysail.server.app.oEService.user;

import java.io.Serializable;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.*;

import java.util.*;
import io.skysail.server.db.DbClassName;
import io.skysail.domain.Identifiable;
import io.skysail.domain.html.*;
import io.skysail.server.forms.*;

import io.skysail.server.app.oEService.oe.*;
import io.skysail.server.app.oEService.oe.resources.*;
import io.skysail.server.app.oEService.user.*;
import io.skysail.server.app.oEService.user.resources.*;


import org.apache.commons.lang3.StringUtils;

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
    @ListView(link = OEsResourceGen.class)
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