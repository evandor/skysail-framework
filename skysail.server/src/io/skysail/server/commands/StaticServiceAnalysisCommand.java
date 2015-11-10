package io.skysail.server.commands;

import io.skysail.server.utils.BundleUtils;

import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

import lombok.Getter;

import org.apache.felix.service.command.CommandProcessor;
import org.osgi.framework.Bundle;
import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.*;

@Component(properties = { CommandProcessor.COMMAND_SCOPE + ":String=skysail",
        CommandProcessor.COMMAND_FUNCTION + ":String=analysis", }, provide = Object.class)
public class StaticServiceAnalysisCommand { // NO_UCD (unused code)

    @Getter
    public class Ref {

        private String source;
        private String target;
        private String group;

        public Ref(String source, String target, String group) {
            this.source = source.substring(source.lastIndexOf(".")+1);
            this.target = target.substring(target.lastIndexOf(".")+1);
            this.group = group.substring(group.lastIndexOf(".")+1);
        }

    }

    private ComponentContext ctx;

    private Pattern implementationPattern = Pattern.compile("implementation class=\"(.*)\"");
    private Pattern interfacesPattern = Pattern.compile("provide interface=\"(.*)\"");
    private Pattern referencesPattern = Pattern.compile("reference (.)*interface=\"([^\"]*)\"");

    private List<Ref> refs = new ArrayList<>();

    @Activate
    public void activate(ComponentContext ctx) {
        this.ctx = ctx;
    }

    @Deactivate
    public void deactivate(ComponentContext ctx) {
        this.ctx = null;
    }

    public void analysis() {
        System.out.println("Running static services analysis ");
        System.out.println("===================================");
        Arrays.stream(ctx.getBundleContext().getBundles()).forEach(b -> {
            List<String> files = getDSFiles(b);
            files.stream().forEach(file -> parse(file));
        });
        System.out.println("digraph {");
        refs.forEach(ref -> {
            System.out.println(ref.getSource().replace(".", "") + " -> " + ref.getTarget().replace(".", "")
                    + "[label=\"" + ref.getGroup() + "\"];");
        });
        System.out.println("}");

    }

    private Object parse(String file) {
        Matcher matcher = implementationPattern.matcher(file);
        while (matcher.find()) {
            String group = matcher.group(1);
            System.out.println(group);

            List<String> targets = new ArrayList<>();
            List<String> sources = new ArrayList<>();

            Matcher interfacesMatcher = interfacesPattern.matcher(file);
            while (interfacesMatcher.find()) {
                String implementing = interfacesMatcher.group(1);
                System.out.println(" > " + implementing);
                targets.add(implementing);
            }

            Matcher referenceMatcher = referencesPattern.matcher(file);
            while (referenceMatcher.find()) {
                String referencing = referenceMatcher.group(2);
                sources.add(referencing);
                System.out.println(" >>> " + referencing);
            }

            sources.stream().forEach(source -> {
                targets.stream().forEach(target -> {
                    if (source.contains("skysail") && target.contains("skysail")) {
                        refs.add(new Ref(source, target, group));
                    }
                });
            });

        }
        return null;
    }

    private List<String> getDSFiles(Bundle bundle) {
        Enumeration<String> entryPathsEnumeration = bundle.getEntryPaths("OSGI-INF");
        if (entryPathsEnumeration == null) {
            return Collections.emptyList();
        }
        List<String> entryPaths = Collections.list(entryPathsEnumeration);
        return entryPaths.stream().filter(path -> path.endsWith(".xml")).map(path -> readBundleFile(bundle, path))
                .collect(Collectors.toList());
    }

    private String readBundleFile(Bundle bundle, String path) {
        return BundleUtils.readResource(bundle, path);
    }

}
