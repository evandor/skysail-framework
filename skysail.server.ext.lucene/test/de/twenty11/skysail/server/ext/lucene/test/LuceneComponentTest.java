package de.twenty11.skysail.server.ext.lucene.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.*;

import org.junit.*;

import de.twenty11.skysail.server.ext.lucene.LuceneComponent;

public class LuceneComponentTest {

	private static final String HAYSTACK = "this is the text to search";
	private static LuceneComponent lucene;

	@BeforeClass
	public static void init() throws Exception {
		lucene = new LuceneComponent();
	}

	@Test
	public void lets_user_add_document() throws Exception {
		Map<String, String> docMap = new HashMap<>();
		docMap.put("fieldname", HAYSTACK);
		lucene.addDocument(docMap);
		// no exception
	}

	@Test
	public void testName() throws Exception {
		Map<String, String> docMap = new HashMap<>();
		docMap.put("field", HAYSTACK);
		lucene.addDocument(docMap);
		List<io.skysail.api.search.Document> hits = lucene.search(Arrays.asList("field"), "text");
		assertThat(hits.size(), is(1));
	}

}
