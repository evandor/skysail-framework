package io.skysail.api.links;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.restlet.Request;
import org.restlet.data.MediaType;
import org.restlet.data.Method;

import com.google.common.base.Predicate;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * Basic implementation of http://tools.ietf.org/html/rfc5988 to be used in
 * skysail.
 *
 * <p>
 * A Link instance is created using the {@link Link.Builder}; some of the
 * attributes like role and relation can be changed after creation using a
 * fluent API.
 * </p>
 *
 */
@Slf4j
@Getter
@ToString
public class Link {

    /** URI reference, absolute ("http://www.foo.org") or relative ("/todos"). */
    @Setter
    private String uri;

    private String title;

    private String alt;
    
    /** Indicates the relation between the current resource and the link target. */
    private LinkRelation rel;

    /** the HTTP methods applicable for the link. */
    private Set<Method> verbs = new HashSet<>();

    /** true if the target is not publicly available. */
    private boolean needsAuthentication;

    /** the links role: it could be used for navigation, documentation, ... */
    private LinkRole role = LinkRole.DEFAULT;

    /** to be done. */
    private Predicate<String[]> rolesPredicate;

    /** to be done. */
    private Map<MediaType, String> images = new HashMap<>();

    /**
     * a refId can be used to group links which belong together, for example, as
     * they reference the same entity.
     */
    private String refId;


    private Class<?> cls;

    @Getter
    public static class Builder {

        private String uri;
        private String title;
        private String alt;
        private LinkRelation rel = LinkRelation.ITEM;
        private Set<Method> verbs = new HashSet<>();
        private boolean authenticationNeeded = true;
        private Predicate<String[]> needsRoles;
        private LinkRole role = LinkRole.DEFAULT;
        private String refId;
        private Map<MediaType, String> images = new HashMap<>();
        private Class<?> cls;

        /**
         * Builder pattern for links.
         */
        public Builder(@NonNull Link linkTemplate) {
            this.uri = linkTemplate.getUri();
            this.title  = linkTemplate.getTitle();
            this.rel  = linkTemplate.getRel();
            this.verbs  = linkTemplate.getVerbs();
            this.authenticationNeeded  = linkTemplate.getNeedsAuthentication();
            this.needsRoles = linkTemplate.getRolesPredicate();
            this.role  = linkTemplate.getRole();
            this.refId = linkTemplate.getRefId();
            this.images  = linkTemplate.getImages();
            this.cls = linkTemplate.getCls();
        }

        public Builder(@NonNull String uri) {
            this.uri = uri.trim();
            verbs.add(Method.GET);
        }

        public Builder uri(@NonNull String uri) {
            this.uri = uri;
            return this;
        }

        public Link build() {
            return new Link(this);
        }

        public Builder title(@NonNull String title) {
            this.title = title;
            return this;
        }
        
        public Builder alt(String alt) {
            this.alt = alt;
            return this;
        }
        
        public Builder relation(@NonNull LinkRelation relation) {
            this.rel = relation;
            return this;
        }

        public Builder definingClass(@NonNull Class<?> cls) {
            this.cls = cls;
            return this;
        }

        public Builder role(@NonNull LinkRole role) {
            this.role = role;
            return this;
        }

        public Builder authenticationNeeded(boolean authNeeded) {
            this.authenticationNeeded = authNeeded;
            return this;
        }

        /**
         * sets the http methods applicable.
         *
         */
        public Builder verbs(Method... verbs) {
            this.verbs = new HashSet<>();
            this.verbs.addAll(Arrays.asList(verbs));
            return this;
        }

        public Builder needsRoles(Predicate<String[]> needsRoles) {
            this.needsRoles = needsRoles;
            return this;
        }

        public Builder refId(@NonNull String refId) {
            this.refId = refId;
            return this;
        }

        public Builder image(MediaType mediaType, String img) {
            images.put(mediaType, img);
            return this;
        }

    }

    private Link(Builder linkBuilder) {
        uri = linkBuilder.uri;
        title = linkBuilder.title;
        if (title == null) {
            title = linkBuilder.rel.getName();
        }
        alt = linkBuilder.alt;
        rel = linkBuilder.rel;
        verbs = linkBuilder.verbs;
        needsAuthentication = linkBuilder.authenticationNeeded;
        this.rolesPredicate = linkBuilder.needsRoles;
        if (this.rolesPredicate != null) {
            needsAuthentication = true;
        }
        this.role = linkBuilder.role;
        this.refId = linkBuilder.refId;
        this.images = linkBuilder.images;
        this.cls = linkBuilder.cls;
    }

    /**
     * creates a Link from its string representation.
     */
    public static Link valueOf(String linkheaderString) {
        if (StringUtils.isEmpty(linkheaderString)) {
            return null;
        }
        String[] parts = linkheaderString.split(";");
        String uriPart = parts[0].trim();
        String substring = uriPart.substring(1).substring(0, uriPart.length() - 2);

        Builder builder = new Link.Builder(substring);
        for (int i = 1; i < parts.length; i++) {
            parsePart(builder, parts[i]);
        }
        return builder.build();
    }

    public boolean isApplicable() {
        return true;
    }

    private static void parsePart(Builder builder, String part) {
        String[] keyValue = part.split("=");
        if (keyValue.length != 2) {
            throw new IllegalArgumentException();
        }
        switch (keyValue[0].trim()) {
        case "rel":
            String normalized = keyValue[1].toUpperCase().replace("-", "_").replace("\"", "");
            builder.relation(LinkRelation.valueOf(normalized));
            break;
        case "title":
            builder.title(keyValue[1].replace("\"", ""));
            break;
        case "refId":
            builder.refId(keyValue[1].replace("\"", ""));
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

    /**
     * toString conversion with path.
     */
    public String toString(String path) {
        StringBuilder sb = new StringBuilder().append("<").append(path + getUri()).append(">");
        sb.append("; rel=\"").append(getRel().getName()).append("\"");
        if (getTitle() != null) {
            sb.append("; title=\"").append(getTitle()).append("\"");
        }
        if (getRefId() != null) {
            sb.append("; refId=\"").append(getRefId()).append("\"");
        }
        sb.append("; verbs=\"")
                .append(getVerbs().stream().map(verb -> verb.getName()).collect(Collectors.joining(","))).append("\"");
        return sb.toString();
    }

    public String getImage() {
        return images.get(MediaType.TEXT_HTML);
    }

    public String getImage(MediaType mediaType) {
        return images.get(mediaType);
    }

    /**
     * show in html or not.
     */
    public boolean isShowAsButtonInHtml() {
        if (role.equals(LinkRole.LIST_VIEW)) {
            return false;
        }
        if (role.equals(LinkRole.MENU_ITEM)) {
            return false;
        }
        return getVerbs().contains(Method.GET);
    }

    public boolean isApplicationNavigation() {
        return LinkRole.APPLICATION_NAVIGATION.equals(role);
    }

    public boolean isSelfRelation() {
        return LinkRelation.SELF.equals(rel);
    }

    /**
     * substitute placeholders in URIs.
     */
    public Link substitute(String key, String value) {
        if (value == null) {
            return this;
        }

        String pattern = new StringBuilder("{").append(key).append("}").toString();
        if (uri.contains(pattern)) {
            String uriBefore = uri;
            uri = uri.replace(pattern, value);
            log.info("uri substitution: '{}' -> '{}'", uriBefore, uri);
        } else {
            log.warn("could not find pattern {} in link uri {}", pattern, uri);
        }
        return this;
    }

    public Link checkSelfRelation(Request request) {
        if (request.getResourceRef().getPath().equals(this.uri)) {
            this.rel = LinkRelation.SELF;
        }
        return this;
    }


}
