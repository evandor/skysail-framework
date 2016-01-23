package io.skysail.server.app.notes.resources;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;

import io.skysail.server.app.notes.Note;
import io.skysail.server.app.notes.PutNoteResource;

public class MyPutNoteResource extends PutNoteResource {

    @Override
    public void updateEntity(Note  entity) {
        io.skysail.server.app.notes.Note original = getEntity();
        copyProperties(original,entity);
        
        String content = entity.getContent();
        if (StringUtils.isEmpty(content)) {
            return;
        }
        String plainText = Jsoup.parse(content).text();
        original.setTitle(plainText.length() > 20 ? plainText.substring(0, 20) + "..." : plainText);
        app.getRepository(io.skysail.server.app.notes.Note.class).update(original,app.getApplicationModel());
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(MyNotesResource.class);
    }
}
