package io.skysail.server.app.loop.entry;

import java.io.Serializable;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.*;

import java.util.*;
import io.skysail.server.db.DbClassName;
import io.skysail.domain.Identifiable;
import io.skysail.domain.html.*;
import io.skysail.server.forms.*;

import io.skysail.server.app.loop.timetable.*;
import io.skysail.server.app.loop.timetable.resources.*;
import io.skysail.server.app.loop.entry.*;
import io.skysail.server.app.loop.entry.resources.*;


import org.apache.commons.lang3.StringUtils;

/**
 * generated from javafile.stg
 */
@SuppressWarnings("serial")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="id")
public class Entry implements Identifiable, Serializable {

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

    @Field(inputType = InputType.TIME, htmlPolicy = HtmlPolicy.NO_HTML)
    private Date start;

    public void setStart(Date value) {
        this.start = value;
    }

    public Date getStart() {
        return this.start;
    }

    @Field(inputType = InputType.TIME, htmlPolicy = HtmlPolicy.NO_HTML)
    private Date end;

    public void setEnd(Date value) {
        this.end = value;
    }

    public Date getEnd() {
        return this.end;
    }

    @Field(inputType = InputType.TEXT, htmlPolicy = HtmlPolicy.NO_HTML)
    private String room;

    public void setRoom(String value) {
        this.room = value;
    }

    public String getRoom() {
        return this.room;
    }

    @Field(inputType = InputType.TEXT, htmlPolicy = HtmlPolicy.NO_HTML)
    private String weekday;

    public void setWeekday(String value) {
        this.weekday = value;
    }

    public String getWeekday() {
        return this.weekday;
    }

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