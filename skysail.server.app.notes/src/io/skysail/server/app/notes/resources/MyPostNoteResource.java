package io.skysail.server.app.notes.resources;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.jsoup.Jsoup;

import io.skysail.api.links.Link;
import io.skysail.server.app.notes.*;

public class MyPostNoteResource extends PostNoteResource {
    
    public MyPostNoteResource() {
        super();
    }

    @Override
    public void addEntity(io.skysail.server.app.notes.Note entity) {
        Subject subject = SecurityUtils.getSubject();

        String content = entity.getContent();
        if (StringUtils.isEmpty(content)) {
            return;
        }
        String plainText = Jsoup.parse(content).text();
        entity.setTitle(plainText.length() > NotesApplication.TITLE_MAX_LENGTH ? plainText.substring(0, NotesApplication.TITLE_MAX_LENGTH) + "..." : plainText);
        entity.setUuid(java.util.UUID.randomUUID().toString());
        entity.setOwner(subject.getPrincipal().toString());
        String id = app.getRepository(io.skysail.server.app.notes.Note.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);
    }
    
    @Override
    public List<Link> getLinks() {
        return super.getLinks();
    }
    
    @Override
    public String redirectTo() {
        return super.redirectTo(MyNotesResource.class);
    }
}
