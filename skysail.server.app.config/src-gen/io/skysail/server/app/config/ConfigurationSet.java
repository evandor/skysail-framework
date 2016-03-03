package io.skysail.server.app.config;

import java.io.Serializable;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.*;

import java.util.*;
import io.skysail.server.db.DbClassName;
import io.skysail.domain.Identifiable;
import io.skysail.domain.html.*;
import io.skysail.server.forms.*;

import org.apache.commons.lang3.StringUtils;

/**
 * generated from javafile.stg
 */
@SuppressWarnings("serial")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="id")
public class ConfigurationSet implements Identifiable, Serializable {

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
    @ListView(link = ConfigurationEntrysResource.class)
    private String name;

    public void setName(String value) {
        this.name = value;
    }

    public String getName() {
        return this.name;
    }

    @Field(inputType = InputType.TEXTAREA, htmlPolicy = HtmlPolicy.NO_HTML)
    @ListView(link = ConfigurationEntrysResource.class)
    private String description;

    public void setDescription(String value) {
        this.description = value;
    }

    public String getDescription() {
        return this.description;
    }

    @Field(inputType = InputType.TEXT, htmlPolicy = HtmlPolicy.NO_HTML)
    @ListView(link = ConfigurationEntrysResource.class)
    @PostView(visibility = Visibility.HIDE)
    @PutView(visibility = Visibility.HIDE)
    private String owner;

    public void setOwner(String value) {
        this.owner = value;
    }

    public String getOwner() {
        return this.owner;
    }


    // --- relations ---

    @Relation
    @JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
    private List<io.skysail.server.app.config.ConfigurationEntry> configurationEntrys = new ArrayList<>();

    public void setConfigurationEntrys(List<io.skysail.server.app.config.ConfigurationEntry> value) {
        this.configurationEntrys = value;
    }

    public List<io.skysail.server.app.config.ConfigurationEntry> getConfigurationEntrys() {
        return configurationEntrys;
    }




}