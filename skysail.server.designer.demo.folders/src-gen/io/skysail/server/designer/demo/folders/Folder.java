package io.skysail.server.designer.demo.folders;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.HtmlPolicy;
import io.skysail.domain.html.InputType;
import io.skysail.domain.html.Relation;
import io.skysail.server.forms.ListView;

@SuppressWarnings("serial")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="id")
public class Folder implements Identifiable, Serializable {

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
    @ListView(link = FoldersResource.class)
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
    private List<io.skysail.server.designer.demo.folders.Folder> folders = new ArrayList<>();

    public void setFolders(List<io.skysail.server.designer.demo.folders.Folder> value) {
        this.folders = value;
    }

    public List<io.skysail.server.designer.demo.folders.Folder> getFolders() {
        return folders;
    }




}