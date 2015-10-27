package io.skysail.server.app.dbviewer;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;
import io.skysail.server.ext.apt.annotations.GenerateResources;
import io.skysail.server.forms.ListView;

import javax.persistence.Id;

import lombok.*;

@Getter
@Setter
@GenerateResources(application = "io.skysail.server.app.dbviewer.DbviewerApplication")
public class Connection implements Identifiable {

   @Id
   private String id;

   @Field
   private String name;

   @Field
   private String url;

   @Field
   private String username;

   @Field(inputType = InputType.PASSWORD)
   @ListView(hide = true)
   private String password;

}
