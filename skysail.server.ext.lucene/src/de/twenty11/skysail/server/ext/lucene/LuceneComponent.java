package de.twenty11.skysail.server.ext.lucene;

import io.skysail.api.search.SearchService;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.*;

import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.ext.lucene.document.LuceneDocument;

@Component(immediate = true)
public class LuceneComponent implements SearchService {

	private Analyzer analyzer = new StandardAnalyzer();
	private Directory directory = new RAMDirectory();

	@Activate
	public void activate() {
		try {
	        directory = new NIOFSDirectory(Paths.get("./etc"));
        } catch (IOException e) {
	        e.printStackTrace();
        }
	}

	@Deactivate
	public void deactivate() {
		directory = null;
		handleDirectory();
	}

	@Override
	public void addDocument(Map<String, String> docMap) throws IOException {
		Document doc = new Document();
		docMap.keySet().stream().forEach(docMapKey -> {
			String value = docMap.get(docMapKey);
			if (value != null) {
				doc.add(new Field(docMapKey, value, TextField.TYPE_STORED));
			}
		});
		@SuppressWarnings("deprecation")
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		try (IndexWriter iwriter = new IndexWriter(directory, config)) {
			iwriter.addDocument(doc);
		}
	}

	@Override
	public List<io.skysail.api.search.Document> search(List<String> fieldnames, String text) throws IOException {
	    if (fieldnames == null | fieldnames.size() == 0) {
	        return Collections.emptyList();
	    }

        @SuppressWarnings("deprecation")
	    MultiFieldQueryParser parser = new MultiFieldQueryParser(
                fieldnames.toArray(new String[fieldnames.size()]),
                new StandardAnalyzer());



		//QueryParser parser = new QueryParser(Version.LUCENE_CURRENT, fieldname, analyzer);
		Query query;
        try {
            query = parser.parse(text);
        } catch (ParseException e1) {
            e1.printStackTrace();
            return Collections.emptyList();
        }
		try (DirectoryReader ireader = DirectoryReader.open(directory)) {
			IndexSearcher isearcher = new IndexSearcher(ireader);
			ScoreDoc[] scoreDocs = isearcher.search(query, null, 1000).scoreDocs;
			return Arrays.stream(scoreDocs).map(scoreDoc -> {
				try {
					return new LuceneDocument(isearcher.doc(scoreDoc.doc));
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}).collect(Collectors.toList());//map(Document.class::cast)
		}
	}

	private void handleDirectory() {
		try {
			directory.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
