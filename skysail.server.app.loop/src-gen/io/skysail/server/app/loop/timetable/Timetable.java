package io.skysail.server.app.loop.timetable;

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
public class Timetable implements Identifiable, Serializable {

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
    @ListView(link = EntrysResourceGen.class)
    private String name;

    public void setName(String value) {
        this.name = value;
    }

    public String getName() {
        return this.name;
    }

    @Field(inputType = InputType.TEXTAREA, htmlPolicy = HtmlPolicy.NO_HTML)
    @ListView(link = EntrysResourceGen.class)
    private String description;

    public void setDescription(String value) {
        this.description = value;
    }

    public String getDescription() {
        return this.description;
    }

    @Field(inputType = InputType.DATE, htmlPolicy = HtmlPolicy.NO_HTML)
    @ListView(link = EntrysResourceGen.class)
    private Date start;

    public void setStart(Date value) {
        this.start = value;
    }

    public Date getStart() {
        return this.start;
    }

    @Field(inputType = InputType.DATE, htmlPolicy = HtmlPolicy.NO_HTML)
    @ListView(link = EntrysResourceGen.class)
    private Date end;

    public void setEnd(Date value) {
        this.end = value;
    }

    public Date getEnd() {
        return this.end;
    }


    // --- relations ---

    @Relation
    @JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
    private List<io.skysail.server.app.loop.entry.Entry> entrys = new ArrayList<>();

    public void setEntrys(List<io.skysail.server.app.loop.entry.Entry> value) {
        this.entrys = value;
    }

    public List<io.skysail.server.app.loop.entry.Entry> getEntrys() {
        return entrys;
    }




}