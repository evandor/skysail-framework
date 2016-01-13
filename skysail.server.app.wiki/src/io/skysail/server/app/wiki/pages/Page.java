//package io.skysail.server.app.wiki.pages;
//
//import io.skysail.api.domain.Identifiable;
//import io.skysail.api.forms.*;
//import io.skysail.server.app.wiki.pages.resources.PutPageResource;
//import io.skysail.server.forms.*;
//
//import java.io.Serializable;
//import java.util.*;
//
//import javax.persistence.Id;
//import javax.validation.constraints.*;
//
//import lombok.*;
//
//@Getter
//@Setter
//@ToString
//@UniquePerParentOrSpace
//public class Page implements Serializable, Identifiable {
//
//    private static final long serialVersionUID = 5061219768727410582L;
//
//    @Id
//    private String id;
//
//    @Field
//    @NotNull
//    @Size(min = 2)
//    @ListView(link = PutPageResource.class)
//    private String name;
//
//    @Field(inputType = InputType.READONLY)
//    private Date created;
//
//    @Field(inputType = InputType.READONLY)
//    private Date modified;
//
//    @Reference
//    @PostView(visibility=Visibility.HIDE)
//    @PutView(visibility=Visibility.HIDE)
//    @ListView(hide=true)
//    private List<String> versions = new ArrayList<>();
//
//    @Reference
//    @PostView(visibility=Visibility.HIDE)
//    @PutView(visibility=Visibility.HIDE)
//    private List<String> subpages = new ArrayList<>();
//
//    public List<String> getSubpages() {
//        if (subpages == null) {
//            subpages = new ArrayList<>();
//        }
//        return subpages;
//    }
//
//    @Field(inputType = InputType.TEXTAREA)
//    @ListView(hide=true)
//    private String content;
//
//    public List<String> getVersions() {
//        if (versions == null) {
//            versions = new ArrayList<>();
//        }
//        return versions;
//    }
//
//    @Field(inputType = InputType.READONLY)
//    @ListView(hide = true)
//    private String owner;
//
//    public Page() {
//        this.created = new Date();
//    }
//
//    public Page(String name) {
//        this.name = name;
//        this.created = new Date();
//    }
//
//
//}
