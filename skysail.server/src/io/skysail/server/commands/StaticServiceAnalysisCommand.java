package io.skysail.server.commands;

import io.skysail.server.utils.BundleUtils;

import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

import org.apache.felix.service.command.CommandProcessor;
import org.osgi.framework.Bundle;
import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.*;

@Component(properties = {
        CommandProcessor.COMMAND_SCOPE + ":String=skysail",
        CommandProcessor.COMMAND_FUNCTION + ":String=analysis", },
    provide = Object.class)
public class StaticServiceAnalysisCommand { // NO_UCD (unused code)

    private ComponentContext ctx;

    private Pattern implementationPattern = Pattern.compile("implementation class=\"(.*)\"");
    private Pattern interfacesPattern = Pattern.compile("provide interface=\"(.*)\"");
    private Pattern referencesPattern = Pattern.compile("reference (.)*interface=\"([^\"]*)\"");

    private Map<String, List<String>> mapping = new HashMap<>();

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
            files.stream().forEach(
                    file -> parse(file)
            );
        });
        System.out.println("digraph {");
        mapping.keySet().stream().forEach(key -> {
            mapping.get(key).stream().forEach(line -> {
                System.out.println(key.replace(".","") + " -> " + line.replace(".","") + "[label=\"test\"];");
            });
        });
        System.out.println("}");

    }

    private Object parse(String file) {
        Matcher matcher = implementationPattern.matcher(file);
        while(matcher.find()) {
            String group = matcher.group(1);
            System.out.println(group);

            Matcher interfacesMatcher = interfacesPattern.matcher(file);
            while(interfacesMatcher.find()) {
                String implementing = interfacesMatcher.group(1);
                System.out.println(" > " + implementing);
                if (mapping.get(group) == null) {
                    List<String> newList = new ArrayList<>();
                    newList.add(implementing);
                    mapping.put(group, newList);
                } else {
                    List<String> list = mapping.get(group);
                    list.add(implementing);
                }
            }

            Matcher referenceMatcher = referencesPattern.matcher(file);
            while(referenceMatcher.find()) {
                String referencing = referenceMatcher.group(2);
                System.out.println(" >>> " + referencing);
                if (mapping.get(group) == null) {
                    List<String> newList = new ArrayList<>();
                    newList.add(referencing);
                    mapping.put(group, newList);
                } else {
                    List<String> list = mapping.get(group);
                    list.add(referencing);
                }
            }

        }
        return null;
    }

    private List<String> getDSFiles(Bundle bundle) {
        Enumeration<String> entryPathsEnumeration = bundle.getEntryPaths("OSGI-INF");
        if (entryPathsEnumeration == null) {
            return Collections.emptyList();
        }
        List<String> entryPaths = Collections.list(entryPathsEnumeration);
        return entryPaths.stream()
            .filter(path -> path.endsWith(".xml"))
            .map(path -> readBundleFile(bundle, path))
            .collect(Collectors.toList());
    }

    private String readBundleFile(Bundle bundle, String path) {
        return BundleUtils.readResource(bundle, path);
    }

}
