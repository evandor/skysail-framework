package io.skysail.server.app.dbviewer;

import javax.persistence.Id;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.*;
import io.skysail.server.codegen.ResourceType;
import io.skysail.server.codegen.annotations.GenerateResources;
import io.skysail.server.forms.ListView;
import lombok.*;

@Getter
@Setter
@GenerateResources(application = "io.skysail.server.app.dbviewer.DbviewerApplication", exclude = {ResourceType.GET})
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
