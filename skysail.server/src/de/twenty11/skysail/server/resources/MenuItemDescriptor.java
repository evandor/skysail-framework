package de.twenty11.skysail.server.resources;

import io.skysail.api.forms.Field;
import io.skysail.domain.Identifiable;
import io.skysail.server.forms.ListView;
import io.skysail.server.menus.MenuItem;
import lombok.*;

@NoArgsConstructor
@Getter
public class MenuItemDescriptor implements Identifiable {

    @Field
    @ListView(link = RemoteLoginResource.class) // TODO, thats just an awful workaround
    private String url;

    private String name;

    MenuItemDescriptor(MenuItem menuItem) {
        name = menuItem.getName();
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
