package io.skysail.server.app.bookmarks;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;
import io.skysail.api.forms.InputType;
import io.skysail.api.forms.ListView;
import io.skysail.api.forms.Prefix;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;

import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@ToString(of = { "id", "name", "url" })
@NoArgsConstructor
public class Bookmark implements Identifiable {

    @Id
    private String id;

    @Field(type = InputType.URL, listView = { ListView.TRUNCATE, ListView.LINK })
    @Prefix(methodName = "urlPrefix")
    private URL url;

    @Field(listView = { ListView.TRUNCATE, ListView.LINK })
//    @NotNull
//    @Size(min = 2)
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

    @Field(type = InputType.READONLY)
    private String favicon;
    
    @Field(type = InputType.READONLY, listView = ListView.HIDE)
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
                return "<img src='"+favicon+"'>";
            } else {
                return "<img src='"+url + "/" + favicon+"'>";
            }
        } catch (URISyntaxException e) {
            return "";
        }
    }


}
