package io.skysail.server.app.rss.feed.resources;

import java.util.Date;

import com.rometools.rome.feed.synd.SyndEntry;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedEntry implements Identifiable {

    private String id;

    @Field
    private String title;

    @Field
    private Date publishedDate;

    @Field
    private String value, link;

    public FeedEntry(SyndEntry e) {
        title = e.getTitle();
        publishedDate = e.getPublishedDate();
        value = e.getDescription().getValue();
        link = e.getLink();
    }

}
