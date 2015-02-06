package de.twenty11.skysail.server.ext.converter.st;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.Bundle;
import org.restlet.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupDir;
import org.stringtemplate.v4.compiler.CompiledST;
import org.stringtemplate.v4.misc.Misc;

import st4hidden.org.antlr.runtime.ANTLRInputStream;

public class STGroupBundleDir extends STGroupDir {

    private static final Logger logger = LoggerFactory.getLogger(STGroupBundleDir.class);
    private String resourceName;
    private String bundleName;
    private List<String> usedTemplates = new ArrayList<>();

    static {
        // verbose = true;
    }

    public STGroupBundleDir(Bundle bundle, Resource resource, String resourcePath) {
        super(bundle.getResource(resourcePath), "UTF-8", '$', '$');
        this.resourceName = resource.getClass().getName();
        this.bundleName = bundle.getSymbolicName() + " [" + bundle.getVersion() + "]";
    }

    @Override
    public String toString() {
        return new StringBuilder(bundleName).append(": ").append(root != null ? root.toString() : "")
                .append(", resourceName: ").append(resourceName).toString();
    }

    @Override
    public ST getInstanceOf(String name) {
        return super.getInstanceOf(name);
    }

    /**
     * Load a template from dir or group file. Group file is given precedence
     * over dir with same name.
     */
    @Override
    protected CompiledST load(String name) {
        if (resourceName != null) {
            String sub = (resourceName + "Stg").replace(".", "/") + "/" + name;
            CompiledST st = loadFromBundle(sub, "/");
            if (st != null) {
                return st;
            }
        }
        String parent = Misc.getPrefix(name);
        return loadFromBundle(name, parent);
    }

    private CompiledST loadFromBundle(String name, String parent) {
        URL groupFileURL = null;
        try {
            if (root != null) {
                groupFileURL = new URL(root + ("/" + name + ".stg").replace("//", "/"));
            }
        } catch (MalformedURLException e) {
            errMgr.internalError(null, "bad URL: " + root + parent + ".stg", e);
            return null;
        }
        InputStream is = null;
        try {
            is = load(groupFileURL);
            logger.debug("found resource in {}: {}", bundleName, groupFileURL.toString());
            usedTemplates.add(bundleName + ": " + groupFileURL.toString());
        } catch (FileNotFoundException fnfe) {
            return loadTemplateFile(parent, name + ".st"); // load t.st file
        } catch (Exception e) {
            logger.info("can't load template file " + name);
        }
        try { // clean up
            if (is != null)
                is.close();
        } catch (IOException ioe) {
            errMgr.internalError(null, "can't close template file stream " + name, ioe);
        }
        if (root != null && parent.trim().length() != 0) {
            loadGroupFile(parent, root + (parent + name + ".stg").replace("//", "/"));
        }
        return rawGetTemplate(name);
    }

    /** Load .st as relative file name relative to root by prefix */
    public CompiledST loadTemplateFile(String prefix, String fileName) {
        URL f = getUrl(fileName);
        if (f == null) {
            return null;
        }
        return loadFromUrl(prefix, fileName, f);
    }
    
    public List<String> getUsedTemplates() {
        return usedTemplates;
    }

    public void addUsedTemplates(List<String> list) {
        usedTemplates.addAll(list);
    }

    private CompiledST loadFromUrl(String prefix, String fileName, URL f) {
        ANTLRInputStream fs;
        try {
            fs = new ANTLRInputStream(f.openStream(), encoding);
            fs.name = fileName;
            logger.debug("found resource in {}: {}", bundleName, f.toString());
            usedTemplates.add(bundleName + ": " + f.toString());
        } catch (IOException ioe) {
            logger.debug("resource does not exist in {}: {}", bundleName, f.toString());
            return null;
        }
        return loadTemplateFile(prefix, fileName, fs);
    }

    private URL getUrl(String fileName) {
        try {
            return new URL(root + ("/" + fileName).replace("//", "/"));
        } catch (MalformedURLException me) {
            logger.error(root + fileName, me);
            return null;
        }
    }

    private InputStream load(URL url) throws FileNotFoundException {

        InputStream is;
        try {
            is = new BufferedInputStream(url.openStream());
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
        return is;

    }


}
