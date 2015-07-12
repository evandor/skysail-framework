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
    private boolean disabled;
    private String glyphicon;
    
    public String getHtml() {
        StringBuilder sb = new StringBuilder("<").append(type).append(" ");
        append(sb, "url", url);
        append(sb, "link-to", linkTo);
        appendOptional(sb, "identifier", identifier);
        appendOptional(sb, "glyphicon", glyphicon);
        appendBoolean(sb, "disabled", disabled);
        append(sb, "request-url", requestUrl);
        sb.append("></").append(type).append(">");
        return sb.toString();
    }
 
    private void append(StringBuilder sb, String key, String value) {
        sb.append(key).append("=\"").append(value).append("\" ");
    }

    private void appendOptional(StringBuilder sb, String key, String value) {
        if (value == null) {
            return;
        }
        sb.append(key).append("=\"").append(value).append("\" ");
    }

    private void appendBoolean(StringBuilder sb, String key, boolean bool) {
        if (bool) {
            sb.append(key).append(" ");
        }
    }


}