package de.twenty11.skysail.api.responses;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import org.restlet.Request;
import org.restlet.data.MediaType;
import org.restlet.data.Method;

import com.google.common.base.Predicate;

/**
 * Basic implementation of http://tools.ietf.org/html/rfc5988.
 * 
 * A Linkheader instance is created using the {@link Linkheader.Builder}; some of the attributes like role and relation 
 * can be changed after creation using a fluent API.
 *
 */
@Slf4j
@Getter
@ToString
public class Linkheader {

    private LinkHeaderRelation rel;
    private String uri;
    private String title;
    
    private Set<Method> verbs = new HashSet<Method>();
    private boolean needsAuthentication;
    private Predicate<String[]> rolesPredicate;
    private Map<MediaType, String> images = new HashMap<>();
    private boolean showInHtml = true; // TODO combine with Role
    private LinkheaderRole role = LinkheaderRole.DEFAULT;

    public static class Builder {

        private String uri;
        private String title;
        private LinkHeaderRelation rel = LinkHeaderRelation.ITEM;
        private Set<Method> verbs = new HashSet<>();
        private boolean authenticationNeeded = true;
        private Predicate<String[]> needsRoles;

        public Builder(@NonNull String uri) {
            this.uri = uri.trim();
            verbs.add(Method.GET);
        }

        public Linkheader build() {
            return new Linkheader(this);
        }

        public Builder title(@NonNull String title) {
            this.title = title;
            return this;
        }

        public Builder relation(@NonNull LinkHeaderRelation relation) {
            this.rel = relation;
            return this;
        }
        
        public Builder authenticationNeeded(boolean authNeeded) {
            this.authenticationNeeded = authNeeded;
            return this;
        }
        
        public Builder verbs(Method... verbs) {
            this.verbs = new HashSet<Method>();
            this.verbs.addAll(Arrays.asList(verbs));
            return this;
        }
        
        public Builder needsRoles( Predicate<String[]> needsRoles) {
            this.needsRoles = needsRoles;
            return this;
        }
    }

    protected Linkheader(Builder linkBuilder) {
        uri = linkBuilder.uri;
        title = linkBuilder.title;
        if (title == null) {
            title = linkBuilder.rel.getName();
        }
        rel = linkBuilder.rel;
        verbs = linkBuilder.verbs;
        needsAuthentication = linkBuilder.authenticationNeeded;
        this.rolesPredicate = linkBuilder.needsRoles;
        if (this.rolesPredicate != null) {
            needsAuthentication = true;
        }
    }

    public static Linkheader valueOf(String linkheaderString) {
        if (linkheaderString == null) {
            return null;
        }
        String[] parts = linkheaderString.split(";");
        if (parts.length == 0) {
            return null;
        }
        String uriPart = parts[0].trim();
        String substring = uriPart.substring(1).substring(0, uriPart.length() - 2);
        
        Builder builder = new Linkheader.Builder(substring);
        for (int i = 1; i < parts.length; i++) {
            parsePart(builder, parts[i]);
        }
        return builder.build();
    }

    private static void parsePart(Builder builder, String part) {
        String[] keyValue = part.split("=");
        if (keyValue == null || keyValue.length != 2) {
            return;
        }
        switch (keyValue[0].trim()) {
        case "rel":
            String normalized = keyValue[1].toUpperCase().replace("-", "_").replace("\"", "");
            builder.relation(LinkHeaderRelation.valueOf(normalized));
            break;
        case "title":
            builder.title(keyValue[1].replace("\"", ""));
            break;
        case "verbs":
            builder.verbs(parseVerbs(keyValue[1].replace("\"", "")));
            break;
        default:
            break;
        }
    }

    private static Method[] parseVerbs(String str) {
        Set<Method> verbs = Arrays.asList(str.split(",")).stream().map(verb -> Method.valueOf(verb.trim()))
                .collect(Collectors.toSet());
        return verbs.toArray(new Method[verbs.size()]);
    }

    public boolean getNeedsAuthentication() {
        return needsAuthentication;
    }

    public String toString(Request request, String path) {
        StringBuilder sb = new StringBuilder().append("<").append(path + getUri()).append(">");
        if (getRel() != null) {
            sb.append("; rel=\"").append(getRel().getName()).append("\"");
        }
        if (getTitle() != null) {
            sb.append("; title=\"").append(getTitle()).append("\"");
        }
        sb.append("; verbs=\"")
                .append(getVerbs().stream().map(verb -> verb.getName()).collect(Collectors.joining(","))).append("\"");
        return sb.toString();
    }

    public void setImage(MediaType mediaType, String img) {
        images.put(mediaType, img);
    }

    public String getImage(MediaType mediaType) {
        return images.get(mediaType);
    }

    public Linkheader setShowInHtml(boolean showInHtml) {
        this.showInHtml = showInHtml;
        return this;
    }

    public boolean isShowAsButtonInHtml() {
        if (!showInHtml) {
            return false;
        }
        return getVerbs().contains(Method.GET);
    }

    public boolean isApplicationNavigation() {
        return LinkheaderRole.APPLICATION_NAVIGATION.equals(role);
    }

    public boolean isSelfRelation() {
        return LinkHeaderRelation.SELF.equals(rel);
    }

    public void appendToUri(String string) {
        this.uri = this.uri + string;
    }

    public Linkheader substitute(String key, String value) {
        String pattern = new StringBuilder("{").append(key).append("}").toString();
        if (uri.contains(pattern)) {
            String uriBefore = uri;
            uri = uri.replace(pattern, value == null ? "" : value);
            log.info("uri substitution: '{}' -> '{}'", uriBefore, uri);
        } else {
            log.warn("could not find pattern {} und linkheader uri {}", pattern, uri);
        }
        return this;
    }

    public Linkheader setRole(LinkheaderRole role) {
        this.role = role;
        return this;
    }

    public Linkheader setRelation(LinkHeaderRelation relation) {
        this.rel = relation;
        return this;
    }

}
