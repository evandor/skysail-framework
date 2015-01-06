package de.twenty11.skysail.server;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import org.junit.Test;
import org.markdown4j.Markdown4jProcessor;

public class MarkdownTest {

	@Test
    public void testName() throws Exception {
		String html = new Markdown4jProcessor().process("This is a **bold** text");
		assertThat(html, is(equalTo("<p>This is a <strong>bold</strong> text</p>\n")));
    }
}
