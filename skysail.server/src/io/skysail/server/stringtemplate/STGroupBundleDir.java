package io.skysail.server.stringtemplate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;
import org.osgi.framework.Bundle;
import org.restlet.resource.Resource;
import org.stringtemplate.v4.STGroupDir;
import org.stringtemplate.v4.compiler.CompiledST;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import st4hidden.org.antlr.runtime.ANTLRInputStream;

@Slf4j
public class STGroupBundleDir extends STGroupDir {

    private String resourceName;
    private String bundleName;
    @Getter
    private static Set<String> usedTemplates = new LinkedHashSet<>();

    static {
        // verbose = true; // NOSONAR
    }

    public STGroupBundleDir(Bundle bundle, String resourcePath) {
        this(bundle, null, resourcePath);
    }

    public STGroupBundleDir(Bundle bundle, Resource resource, String resourcePath) {
        super(bundle.getResource(resourcePath), "UTF-8", '$', '$');
        this.resourceName = resource != null ? resource.getClass().getName() : "";
        this.bundleName = bundle.getSymbolicName();
        this.groupDirName = new StringBuilder(getClass().getSimpleName()).append(": ").append(bundle.getSymbolicName())
                .append(" - ").append(resourcePath).toString();
        log.debug("created '{}'", groupDirName);
    }

    public void addUsedTemplates(Set<String> list) {
        usedTemplates.addAll(list);
    }

    /**
     * From parent: Load a template from directory or group file. Group file is
     * given precedence over directory with same name. {@code name} is always
     * fully-qualified. 
     */
    @Override
    public CompiledST load(@NonNull String name) {
        Validate.isTrue(name.startsWith("/"), "name is supposed to start with '/'");
        Validate.isTrue(!name.contains("."), "name is not supposed to contain a dot");
        Validate.isTrue(!name.substring(1).contains("/"), "name must not contain another '/' char.");
        
        String resourceLevelTemplate = (resourceName + "Stg").replace(".", "/") + "/" + name;
        CompiledST st = loadFromBundle(name, resourceLevelTemplate);
        if (st != null) {
            return st;
        }
        return loadFromBundle(name, name);
    }

    /** Load .st as relative file name relative to root by prefix */
    @Override
    public CompiledST loadTemplateFile(String prefix, String fileName) {
        return loadFromUrl(prefix, fileName, getUrl(fileName));
    }

    public static void clearUsedTemplates() {
        usedTemplates.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(groupDirName);
        if (!getImportedGroups().isEmpty()) {
            sb.append(":\nimportedGroups:\n");
            sb.append(getImportedGroups().stream().map(g -> "  > ".concat(g.toString()))
                    .collect(Collectors.joining("\n")));
        }
        return sb.toString();
    }

    private CompiledST loadFromBundle(String originalName, String name) {
        Validate.isTrue(!name.contains("."), "name is not supposed to contain a dot");
        
        URL groupFileURL = determineGroupFileUrl(name);
        log.debug("checking group file {}", groupFileURL);
        if (groupFileURL == null) {
            return null;
        }
        String fileName = root + ("/" + name + ".stg").replace("//", "/");
        if (exists(groupFileURL)) {
            usedTemplates.add(bundleName + ": " + groupFileURL.toString());
            try {
                loadGroupFile("/", fileName);
                log.debug("found resource in {}: {}", bundleName, groupFileURL.toString());
                log.debug("");
                return rawGetTemplate(originalName);
            } catch (Exception e) { // NOSONAR
            }
        } else {
            return loadTemplateFile("/", name + ".st"); // load t.st file // NOSONAR
        }
        if (root != null && "/".trim().length() != 0) {
            loadGroupFile("/", fileName);
        }
        return rawGetTemplate(name);
    }

    private CompiledST loadFromUrl(String prefix, String fileName, URL f) {
        ANTLRInputStream fs;
        try {
            fs = new ANTLRInputStream(f.openStream(), encoding);
            fs.name = fileName;
            log.info("found resource in {}: {}", bundleName, f.toString());
            usedTemplates.add(bundleName + ": " + f.toString());
        } catch (IOException ioe) {
            log.trace("resource does not exist in {}: {}", bundleName, f.toString());
            return null;
        }
        return loadTemplateFile(prefix, fileName, fs);
    }

    private URL getUrl(String fileName) {
        try {
            return new URL(root + ("/" + fileName).replace("//", "/"));
        } catch (MalformedURLException me) {
            log.error(root + fileName, me);
            return null;
        }
    }

    private static boolean exists(URL url) {
        try {
            url.openConnection().connect();
        } catch (IOException e) { // NOSONAR
            return false;
        }
        return true;
    }

    private URL determineGroupFileUrl(String name) {
        if (root == null) {
            return null;
        }
        String url = root + ("/" + name + ".stg").replace("//", "/");
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            errMgr.internalError(null, "bad URL: " + url, e);
        }
        return null;
    }

}
