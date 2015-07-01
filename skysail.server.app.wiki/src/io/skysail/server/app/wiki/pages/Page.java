package io.skysail.server.app.wiki.pages;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;
import io.skysail.server.app.wiki.pages.resources.PutPageResource;
import io.skysail.server.app.wiki.versions.Version;
import io.skysail.server.forms.*;

import java.io.Serializable;
import java.util.*;

import javax.persistence.Id;
import javax.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@ToString
@UniquePerParentOrSpace
public class Page implements Serializable, Identifiable {

    private static final long serialVersionUID = 5061219768727410582L;

    @Id
    //@Field(type = InputType.READONLY)
    private String id;

    @Field
    @NotNull
    @Size(min = 2)
    //@Prefix(methodName="boldStart")
    //@Postfix(methodName="boldEnd")
    @ListView(link = PutPageResource.class)
    private String name;
    
    @Field(type = InputType.READONLY)
    private Date created;

    @Field(type = InputType.READONLY)
    private Date modified;

    @Reference(cls = Version.class)
    @PostView(visibility=Visibility.HIDE)
    @PutView(visibility=Visibility.HIDE)
    @ListView(hide=true)
    private List<Version> versions = new ArrayList<>();

    @Reference(cls = Page.class)
    @PostView(visibility=Visibility.HIDE)
    @PutView(visibility=Visibility.HIDE)
    //@ListView(hide=true)
    private List<Page> subpages = new ArrayList<>();

    public void addPage(Page entity) {
        subpages.add(entity);
       // entity.setParent(this);
    }

    //@Field(type = InputType.READONLY)
    //@JsonIgnore
   // private Page parent;

    @Field(type = InputType.TEXTAREA)
    @ListView(hide=true)
    private String content;

    public void addVersion(Version entity) {
        versions.add(entity);
    }

    @Field(type = InputType.READONLY)
    @ListView(hide = true)
    private String owner;

    public Page() {
        this.created = new Date();
    }
    
    public Page(String name) {
        this.name = name;
        this.created = new Date();
    }
    

}
