package io.skysail.server.app.designer.restclient;

import javax.persistence.Id;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.*;
import io.skysail.server.codegen.ResourceType;
import io.skysail.server.codegen.annotations.GenerateResources;
import lombok.*;

@Getter
@Setter
@GenerateResources(application = "io.skysail.server.app.designer.restclient.RestclientApplication", exclude = {ResourceType.GET})
public class ClientApplication implements Identifiable {

    @Id
    private String id;

    @Field
    private String name;

    @Field(inputType = InputType.URL)
    private String url;

    @Field(selectionProvider = AuthenticationSelectionProvider.class)
    private String authentication;

}
