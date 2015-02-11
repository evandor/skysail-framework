package io.skysail.server.ext.apt.test.withlist.folders;

import lombok.Getter;
import lombok.Setter;
import de.twenty11.skysail.api.forms.Field;
import de.twenty11.skysail.server.ext.apt.annotations.GenerateEntityResource;
import de.twenty11.skysail.server.ext.apt.annotations.GenerateListResource;
import de.twenty11.skysail.server.ext.apt.annotations.GeneratePostResource;
import de.twenty11.skysail.server.ext.apt.annotations.GeneratePutResource;

@GenerateEntityResource
@GenerateListResource
@GeneratePostResource
@GeneratePutResource
@Getter
@Setter
public class Folder {

    @Field
    private String foldername;
}
