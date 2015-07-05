package io.skysail.server.restlet.resources;
 
import lombok.*;

@Getter
@ToString
@Builder
public class WebComponentCall {
    private final String type;
    private String url;
    private String linkTo;
    private String title;
    private String identifier;
    private String requestUrl;
 
    public String getHtml() {
        return "<"+type+" url=\""+url+"\" link-to=\""+linkTo+"\" "+identifiersAttribute()+" request-url=\""+requestUrl+"\"></"+type+">";
    }
 
    private String identifiersAttribute() {
        if (identifier == null) {
            return "";
        }
        return "identifier=\""+identifier+"\"";
    }
}