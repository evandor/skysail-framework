//package io.skysail.server.app.wiki.versions;
//
//import io.skysail.api.domain.Identifiable;
//import io.skysail.api.forms.*;
//import io.skysail.server.forms.ListView;
//
//import java.io.Serializable;
//import java.util.Date;
//
//import javax.persistence.Id;
//
//import lombok.*;
//
//@Getter
//@Setter
//public class Version implements Serializable, Identifiable {
//
//    private static final long serialVersionUID = -4476680964779969964L;
//
//    @Id
//    private String id;
//
//    @Field(inputType = InputType.READONLY)
//    private Date created;
//
//    @Field(inputType = InputType.READONLY)
//    private Integer versionNumber;
//
//    @Field(inputType = InputType.TEXTAREA)
//    @ListView(hide=true)
//    private String content;
//
//    @Field(inputType = InputType.READONLY)
//    @ListView(hide = true)
//    private String owner;
//
//    public Version() {
//        versionNumber = 1;
//        created = new Date();
//    }
//}
