package de.twenty11.skysail.server.ext.lucene.document;

import java.util.*;

import org.apache.lucene.document.Document;

public class LuceneDocument implements io.skysail.api.search.Document {

    private Map<String, Object> docMap;

    public LuceneDocument(Document doc) {
        Map<String, Object> docMap = new HashMap<>();
        doc.getFields().stream().forEach(field -> {docMap.put(field.name(),field.stringValue());});
        this.docMap = docMap;
    }

    @Override
    public Map<String, Object> getDocMap() {
        return docMap;
    }
}
