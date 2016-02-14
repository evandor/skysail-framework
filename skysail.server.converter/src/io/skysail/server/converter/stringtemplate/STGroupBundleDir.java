//package io.skysail.server.converter.stringtemplate;
//
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.LinkedHashSet;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//import org.osgi.framework.Bundle;
//import org.restlet.resource.Resource;
//import org.stringtemplate.v4.STGroupDir;
//import org.stringtemplate.v4.compiler.CompiledST;
//import org.stringtemplate.v4.misc.Misc;
//
//import lombok.extern.slf4j.Slf4j;
//import st4hidden.org.antlr.runtime.ANTLRInputStream;
//
//@Slf4j
//// TODO check duplication in server.app.designer!
//@Deprecated
//public class STGroupBundleDir extends STGroupDir {
//
//    private String resourceName;
//    private String bundleName;
//    private static Set<String> usedTemplates = new LinkedHashSet<>();
//
//    static {
//        //verbose = true;
//    }
//
//    public STGroupBundleDir(Bundle bundle, Resource resource, String resourcePath) {
//        super(bundle.getResource(resourcePath), "UTF-8", '$', '$');
//        this.resourceName = resource.getClass().getName();
//        this.bundleName =  bundle.getSymbolicName();
//        this.groupDirName = new StringBuilder(getClass().getSimpleName()).append(": ").append(bundle.getSymbolicName())
//                .append(" - ").append(resourcePath).toString();
//    }
//
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder(groupDirName);
//        //sb.append("Bundle Name: ").append(bundleName).append("\n");
//        //sb.append("usedTemplates: ").append(usedTemplates).append("\n");
//        //sb.append("importedGroups: ").append(getImportedGroups()).append("\n");
//        if (!getImportedGroups().isEmpty()) {
//            sb.append(":\nimportedGroups:\n");
//            sb.append(getImportedGroups().stream().map(g -> "  > ".concat(g.toString())).collect(Collectors.joining("\n")));
//        }
//        return sb.toString();
//    }
//
//    /**
//     * Load a template from dir or group file. Group file is given precedence
//     * over dir with same name.
//     */
//    @Override
//    protected CompiledST load(String name) {
//        if (resourceName != null) {
//            String sub = (resourceName + "Stg").replace(".", "/") + "/" + name;
//            CompiledST st = loadFromBundle(sub, "/");
//            if (st != null) {
//                return st;
//            }
//        }
//        String parent = Misc.getPrefix(name);
//        return loadFromBundle(name, parent);
//    }
//
//    private CompiledST loadFromBundle(String name, String parent) {
//        URL groupFileURL = determineGroupFileUrl(name, parent);
//        log.info("checking group file {}", groupFileURL);
//        if (groupFileURL == null) {
//            return null;
//        }
//        if (exists(groupFileURL)) {
//            log.info("found resource in {}: {}", bundleName, groupFileURL.toString());
//            usedTemplates.add(bundleName + ": " + groupFileURL.toString());
//            try {
//                loadGroupFile(parent, root + (parent + name + ".stg").replace("//", "/"));
//                return rawGetTemplate(name);
//                //CompiledST loadFromUrl = loadFromUrl("/", name, groupFileURL);
//                //System.out.println(loadFromUrl);
//            } catch (Exception e) {}
//        } else {
//            return loadTemplateFile(parent, name + ".st"); // load t.st file
//        }
//        if (root != null && parent.trim().length() != 0) {
//            loadGroupFile(parent, root + (parent + name + ".stg").replace("//", "/"));
//        }
//        return rawGetTemplate(name);
//    }
//
//    /** Load .st as relative file name relative to root by prefix */
//    public CompiledST loadTemplateFile(String prefix, String fileName) {
//        URL f = getUrl(fileName);
//        if (f == null) {
//            return null;
//        }
//        return loadFromUrl(prefix, fileName, f);
//    }
//
//    public Set<String> getUsedTemplates() {
//        return usedTemplates;
//    }
//
//    public void addUsedTemplates(Set<String> list) {
//        usedTemplates.addAll(list);
//    }
//
//    private CompiledST loadFromUrl(String prefix, String fileName, URL f) {
//        ANTLRInputStream fs;
//        try {
//            fs = new ANTLRInputStream(f.openStream(), encoding);
//            fs.name = fileName;
//            log.info("found resource in {}: {}", bundleName, f.toString());
//            usedTemplates.add(bundleName + ": " + f.toString());
//        } catch (IOException ioe) {
//            log.trace("resource does not exist in {}: {}", bundleName, f.toString());
//            return null;
//        }
//        return loadTemplateFile(prefix, fileName, fs);
//    }
//
//    private URL getUrl(String fileName) {
//        try {
//            return new URL(root + ("/" + fileName).replace("//", "/"));
//        } catch (MalformedURLException me) {
//            log.error(root + fileName, me);
//            return null;
//        }
//    }
//
//    private static boolean exists(URL url) {
//        try {
//            url.openConnection();
//        } catch (IOException e) {
//            return false;
//        }
//        return true;
//    }
//
//
//    public static void clearUsedTemplates() {
//        usedTemplates.clear();
//    }
//    
//    private URL determineGroupFileUrl(String name, String parent) {
//        if (root == null) {
//            return null;
//        }
//        try {
//            return new URL(root + ("/" + name + ".stg").replace("//", "/"));
//        } catch (MalformedURLException e) {
//            errMgr.internalError(null, "bad URL: " + root + parent + ".stg", e);
//        }
//        return null;
//    }
//
//
//
//}
