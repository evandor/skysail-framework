package io.skysail.server.app.fileserver;

import io.skysail.api.forms.Field;

import java.nio.file.Path;

import lombok.*;

@Getter
@Setter
public class FileDescriptor {

    public FileDescriptor(Path f) {
        this.name = f.getFileName().toString();
    }

    @Field
    private String name;
}
