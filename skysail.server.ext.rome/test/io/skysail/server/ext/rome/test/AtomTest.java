package io.skysail.server.ext.rome.test;

import java.net.URL;

import org.junit.Test;
import org.restlet.*;
import org.restlet.data.*;
import org.restlet.util.Series;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.*;

public class AtomTest {

    @Test
    public void testName() throws Exception {

        Client client = new Client(new Context(), Protocol.HTTPS);
        
        System.setProperty("http.proxyHost","192.168.11.140"); 
        System.setProperty("http.proxyPort","8080");
        System.setProperty("https.proxyHost","192.168.11.140"); 
        System.setProperty("https.proxyPort","8080");

        Series<Parameter> parameters = client.getContext().getParameters();
        // proxy with credentials Works as excepted with http sites
        parameters.add("proxyHost", "192.168.11.140");
        parameters.add("proxyPort", "8080");

//        ClientResource mailclient = new ClientResource("http://heise.de.feedsportal.com/c/35207/f/653902/index.rss");
//        //http://www.spiegel.de/schlagzeilen/tops/index.rss");
//                //http://skysailserver.blogspot.com/feeds/5140746528972280985/comments/default");
//        mailclient.setNext(client);
        
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(new URL("http://heise.de.feedsportal.com/c/35207/f/653902/index.rss")));
        System.out.println(feed);
        
    }
}
