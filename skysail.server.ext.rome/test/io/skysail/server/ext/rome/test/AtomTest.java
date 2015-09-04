package io.skysail.server.ext.rome.test;

import org.junit.*;
import org.restlet.*;
import org.restlet.data.*;
import org.restlet.ext.atom.*;
import org.restlet.resource.ClientResource;
import org.restlet.util.Series;

public class AtomTest {

    @Test
    @Ignore
    public void testName() throws Exception {

        Client client = new Client(new Context(), Protocol.HTTP);

        Series<Parameter> parameters = client.getContext().getParameters();
        // proxy with credentials Works as excepted with http sites
        parameters.add("proxyHost", "192.168.11.140");
        parameters.add("proxyPort", "8080");

        ClientResource mailclient = new ClientResource("http://skysailserver.blogspot.com/feeds/5140746528972280985/comments/default");
        mailclient.setNext(client);


        Feed atomFeed = mailclient.get(Feed.class);
        System.out.println("\nAtom feed: " + atomFeed.getTitle() + "\n");
        for (Entry entry : atomFeed.getEntries()) {
            System.out.println("Title : " + entry.getTitle());
            System.out.println("Summary: " + entry.getSummary());
        }
//        SyndFeed rssFeed = mailClient.get(SyndFeed.class);
//        System.out.println("\nRSS feed: " + rssFeed.getTitle() + "\n");
//        @SuppressWarnings("unchecked")
//        List<SyndEntry> entries = (List<SyndEntry>) rssFeed.getEntries();
//        for (SyndEntry entry : entries) {
//            System.out.println("Title : " + entry.getTitle());
//            System.out.println("Summary: " + entry.getDescription().getValue());
//        }

    }
}
