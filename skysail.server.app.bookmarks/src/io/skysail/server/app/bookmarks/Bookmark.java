package io.skysail.server.app.bookmarks;

import java.net.*;
import java.util.Date;

import javax.persistence.Id;

import org.apache.commons.lang3.StringUtils;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.*;
import io.skysail.server.app.bookmarks.resources.BookmarkResource;
import io.skysail.server.forms.ListView;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@ToString(of = { "id", "name", "url" })
@NoArgsConstructor
@Slf4j
public class Bookmark implements Identifiable {

    @Id
    private String id;

    @Field(inputType = InputType.URL)
    @ListView(truncate = 20, link = BookmarkResource.class, prefix = "urlPrefix")
    @NonNull
    private URL url;

    @Field
    @ListView(truncate = 20, link = BookmarkResource.class)
    private String name;

    @Field(inputType = InputType.TEXTAREA)
    @ListView(hide = true)
    private String desc;

    @Field(inputType = InputType.READONLY)
    @ListView(hide = true)
    private Date created;

    @Field(inputType = InputType.READONLY)
    @ListView(hide = true)
    private Date modified;

    @Field(inputType = InputType.READONLY)
    @ListView(hide = true)
    private String owner;

    @Field(inputType = InputType.READONLY)
    @ListView(hide = true)
    private String favicon;

    @Field(inputType = InputType.READONLY)
    @ListView(truncate = 50)
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
                return "<img src='" + favicon + "' width='16'>";
            } else {
                return "<img src='" + url + "/" + favicon + "' width='16'>";
            }
        } catch (URISyntaxException e) {
            log.error(e.getMessage(),e);
            return "";
        }
    }

}
