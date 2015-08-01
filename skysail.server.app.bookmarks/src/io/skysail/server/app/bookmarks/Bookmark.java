package io.skysail.server.app.bookmarks;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;
import io.skysail.server.app.bookmarks.resources.BookmarkResource;
import io.skysail.server.forms.ListView;

import java.net.*;
import java.util.Date;

import javax.persistence.Id;

import lombok.*;

import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@ToString(of = { "id", "name", "url" })
@NoArgsConstructor
public class Bookmark implements Identifiable {

    @Id
    private String id;

    @Field(inputType = InputType.URL)
    // , listView = { ListViewEnum.TRUNCATE, ListViewEnum.LINK })
    @ListView(truncate = 20, link = BookmarkResource.class, prefix = "urlPrefix")
    private URL url;

    @Field
    @ListView(truncate = 20, link = BookmarkResource.class)
    private String name;

    @Field(inputType = InputType.TEXTAREA)
    @ListView(truncate = 10)
    private String desc;

    @Field(inputType = InputType.TAGS)
    private String tags;

    @Field(inputType = InputType.READONLY)
    private Date created;

    @Field(inputType = InputType.READONLY)
    private Date modified;

    @Field(inputType = InputType.READONLY)
    @ListView(hide = true)
    private String owner;

    @Field(inputType = InputType.READONLY)
    @ListView(hide = true)
    private String favicon;

    @Field(inputType = InputType.READONLY)
    @ListView(hide = true)
    private String metaDescription;

    public Bookmark(String url, String name) throws MalformedURLException {
        this.url = new URL(url);
        this.name = name;
    }

    public String getUrlPrefix() {
        if (StringUtils.isEmpty(favicon)) {
            return "";
        }
        try {
            URI uri = new URI(favicon);
            if (uri.isAbsolute()) {
                return "<img src='" + favicon + "'>";
            } else {
                return "<img src='" + url + "/" + favicon + "'>";
            }
        } catch (URISyntaxException e) {
            return "";
        }
    }

}
