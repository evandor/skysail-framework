package io.skysail.server.app.notes.resources;

import java.util.List;

import org.jsoup.Jsoup;

import io.skysail.api.links.Link;
import io.skysail.server.app.notes.PostNoteResource;

public class MyPostNoteResource extends PostNoteResource {
    
    public MyPostNoteResource() {
        super();
    }

    @Override
    public void addEntity(io.skysail.server.app.notes.Note entity) {
        String content = entity.getContent();
        entity.setTitle(Jsoup.parse(content).text().substring(20) + "...");
        String id = app.getRepository(io.skysail.server.app.notes.Note.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);
    }
    
    @Override
    public List<Link> getLinks() {
        return super.getLinks();
    }
}
