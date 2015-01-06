package de.twenty11.skysail.server.services;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface SearchService {

	void addDocument(Map<String, String> map) throws IOException;

    List<Document> search(List<String> fieldnames, String text) throws IOException;

}
