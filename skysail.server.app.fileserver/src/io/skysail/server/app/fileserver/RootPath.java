package io.skysail.server.app.fileserver;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;
import io.skysail.server.forms.ListView;

import java.io.File;
import java.util.Map.Entry;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class RootPath implements Identifiable {

    @Field
    @ListView(link = ListFilesResource.class)
    private String name;

    @Field
    private String path;

    public RootPath(Entry<String, String> entry) {
        this.name = entry.getKey().substring(FileserverApplication.CONFIG_ROOT_PATH_IDENTIFIER.length());
        try {
            this.path = new File(entry.getValue()).toPath().toAbsolutePath().toString();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
    }

    public String getId() {
        return name;
    }

    @Override
    public void setId(String id) {
    }

}
