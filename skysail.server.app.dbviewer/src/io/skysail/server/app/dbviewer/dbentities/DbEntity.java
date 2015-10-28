package io.skysail.server.app.dbviewer.dbentities;

import io.skysail.api.domain.Identifiable;
import lombok.*;

@Getter
@Setter
public class DbEntity implements Identifiable {

    private String id;

}
