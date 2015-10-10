package io.skysail.server.ext.apt.test.withlist.folders;

import io.skysail.api.forms.Field;
import lombok.*;
import de.twenty11.skysail.server.ext.apt.annotations.*;

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
