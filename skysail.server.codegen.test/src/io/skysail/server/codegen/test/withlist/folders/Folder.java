package io.skysail.server.codegen.test.withlist.folders;

import io.skysail.api.forms.Field;
import lombok.*;

@Getter
@Setter
public class Folder {

    @Field
    private String foldername;
}
