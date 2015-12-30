package io.skysail.server.codegen.test.withlist.folders;

import io.skysail.domain.html.Field;
import lombok.*;

@Getter
@Setter
public class Folder {

    @Field
    private String foldername;
}
