package io.skysail.server.app.twitter4j.resources;

import io.skysail.api.forms.Field;
import io.skysail.domain.Identifiable;

import java.util.Arrays;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import twitter4j.ExtendedMediaEntity;
import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.SymbolEntity;
import twitter4j.URLEntity;
import twitter4j.UserMentionEntity;

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
            text = text.replace(me.getURL(), "<br><a href='"+me.getURL()+"'><img src='"+me.getMediaURL()+"' height='100px;'></a>");
        });
        
        SymbolEntity[] symbolEntities = status.getSymbolEntities();
        Arrays.stream(symbolEntities).forEach(se -> {
            text = text.replace(se.getText(), "***" + se.getText() + "***");//"<a href='"+se.getURL()+"'><img src='"+se.getMediaURL()+"' height='100px;'></a>");
        });
        
        ExtendedMediaEntity[] extendedMediaEntities = status.getExtendedMediaEntities();
        Arrays.stream(extendedMediaEntities).forEach(eme -> {
            text = text.replace(eme.getText(), "---" + eme.getText() + "---");//"<a href='"+se.getURL()+"'><img src='"+se.getMediaURL()+"' height='100px;'></a>");
        });
        
        HashtagEntity[] hashtagEntities = status.getHashtagEntities();
        Arrays.stream(hashtagEntities).forEach(he -> {
            text = text.replace(he.getText(), "<a href='https://twitter.com/hashtag/"+he.getText()+"' target='_blank'>"+he.getText()+"</a>");
        });

        UserMentionEntity[] userMentionEntities = status.getUserMentionEntities();
        Arrays.stream(userMentionEntities).forEach(ume -> {
            text = text.replace(ume.getText(), "<a href='https://twitter.com/" +ume.getText()+"' target='_blank'>"+ume.getText()+"</a>");
        });

    }
}
