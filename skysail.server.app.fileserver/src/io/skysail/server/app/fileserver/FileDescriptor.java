package io.skysail.server.app.fileserver;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;

import java.nio.file.Path;

import lombok.*;

@Getter
@Setter
public class FileDescriptor implements Identifiable {

    public FileDescriptor(Path f) {
        this.name = f.getFileName().toString();
    }

    @Field
    private String name;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {
    }
}
