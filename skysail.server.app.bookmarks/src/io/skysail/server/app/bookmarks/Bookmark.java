package io.skysail.server.app.bookmarks;

import io.skysail.api.forms.Field;
import io.skysail.api.forms.InputType;
import io.skysail.api.forms.ListView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import de.twenty11.skysail.api.domain.Identifiable;

@Getter
@Setter
@ToString(of = { "id", "name", "url" })
@NoArgsConstructor
public class Bookmark implements Identifiable {

    @Id
    private String id;

    @Field(type = InputType.URL)
    private URL url;

    @Field(listView = { ListView.TRUNCATE, ListView.LINK })
    @NotNull
    @Size(min = 2)
   // @UniquePerOwner
    private String name;

    @Field(type = InputType.TEXTAREA, listView = { ListView.TRUNCATE })
    private String desc;

    @Field(type = InputType.READONLY)
    private Date created;

    @Field(type = InputType.READONLY)
    private Date modified;

    @Field(type = InputType.READONLY, listView = { ListView.HIDE })
    private String owner;

    public Bookmark(String url, String name) throws MalformedURLException {
        this.url = new URL(url);
        this.name = name;
    }


}
