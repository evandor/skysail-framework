package io.skysail.server.ext.rome;

import java.net.URL;

import org.osgi.service.component.annotations.Component;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

@Component(immediate = true, service = RssClient.class)
public class RssClient {

    public SyndFeed getFeed(URL url) throws Exception {
   //     Client client = new Client(new Context(), Protocol.HTTPS);
//        System.setProperty("http.proxyHost","192.168.11.140"); 
//        System.setProperty("http.proxyPort","8080");
//        System.setProperty("https.proxyHost","192.168.11.140"); 
//        System.setProperty("https.proxyPort","8080");
//        Series<Parameter> parameters = client.getContext().getParameters();
//        parameters.add("proxyHost", "192.168.11.140");
//        parameters.add("proxyPort", "8080");
        SyndFeedInput input = new SyndFeedInput();
        return input.build(new XmlReader(url));
    }
    
}
