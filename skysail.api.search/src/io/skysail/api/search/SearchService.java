package io.skysail.api.search;

import java.io.IOException;
import java.util.*;


public interface SearchService {

	void addDocument(Map<String, String> map) throws IOException;

    List<Document> search(List<String> fieldnames, String text) throws IOException;

}
