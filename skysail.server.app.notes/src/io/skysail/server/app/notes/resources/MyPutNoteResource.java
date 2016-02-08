package io.skysail.server.app.notes.resources;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;

import io.skysail.server.app.notes.*;

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
        original.setTitle(plainText.length() > NotesApplication.TITLE_MAX_LENGTH ? plainText.substring(0, NotesApplication.TITLE_MAX_LENGTH) + "..." : plainText);
        original.setModifiedAt(new Date());
        app.getRepository(io.skysail.server.app.notes.Note.class).update(original,app.getApplicationModel());
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(MyNotesResource.class);
    }
}
