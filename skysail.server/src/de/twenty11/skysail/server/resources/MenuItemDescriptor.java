package de.twenty11.skysail.server.resources;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;
import io.skysail.server.forms.ListView;
import lombok.*;
import de.twenty11.skysail.server.services.MenuItem;

@NoArgsConstructor
@Getter
public class MenuItemDescriptor implements Identifiable {

    @Field
    @ListView(link = RemoteLoginResource.class) // TODO, thats just an awful workaround
    private String url;

    MenuItemDescriptor(MenuItem menuItem) {
        String name = menuItem.getName();
        String link = menuItem.getLink();
        url = "<a href='"+link+"'>"+name+"</a>";
    }

    @Override
    public String getId() {
        return url;
    }

    @Override
    public void setId(String id) {
    }

}
