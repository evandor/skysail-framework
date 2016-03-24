package io.skysail.server.designer.demo.apps.demovalueobject;

import java.io.Serializable;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.*;

import java.util.*;
import io.skysail.server.db.DbClassName;
import io.skysail.domain.Identifiable;
import io.skysail.domain.html.*;
import io.skysail.server.forms.*;

import io.skysail.server.designer.demo.apps.demoapplication.*;
import io.skysail.server.designer.demo.apps.demoapplication.resources.*;
import io.skysail.server.designer.demo.apps.demovalueobject.*;
import io.skysail.server.designer.demo.apps.demovalueobject.resources.*;


import org.apache.commons.lang3.StringUtils;

/**
 * generated from javafile.stg
 */
@SuppressWarnings("serial")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="id")
public class DemoValueObject implements Identifiable, Serializable {

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
    private String name;

    public void setName(String value) {
        this.name = value;
    }

    public String getName() {
        return this.name;
    }


    // --- relations ---



}