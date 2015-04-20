package io.skysail.server.app.twitter4j.resources;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;

import java.util.Arrays;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.URLEntity;

@Getter
@NoArgsConstructor
public class Tweet implements Identifiable {

    @Setter
    private String id;

    @Field
    private String username;

    @Field
    private String text;

    public Tweet(Status status) {
        this.id = Long.toString(status.getId());
        
        this.username = status.getUser().getName();
        String miniProfileImageURL = status.getUser().getMiniProfileImageURL();

        this.username = "<img src='"+miniProfileImageURL+"'>&nbsp;" + this.username;
        this.text = status.getText();
        URLEntity[] urlEntities = status.getURLEntities();
        Arrays.stream(urlEntities).forEach(url -> {
            text = text.replace(url.getURL(), "<a href='"+url.getURL()+"' target='_blank'>"+url.getURL()+"</a>");
        });
        MediaEntity[] mediaEntities = status.getMediaEntities();
        Arrays.stream(mediaEntities).forEach(me -> {
            text = text.replace(me.getURL(), "<a href='"+me.getURL()+"'><img src='"+me.getMediaURL()+"' height='100px;'></a>");
        });
        
        
    }
}
