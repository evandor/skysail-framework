package io.skysail.server.app.designer.codegen.templates;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.osgi.framework.Bundle;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.stringtemplate.v4.ST;

import io.skysail.server.stringtemplate.STGroupBundleDir;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(immediate = true, service = TemplateProvider.class)
public class TemplateProvider {

    private Bundle bundle;
    private Map<String, STGroupBundleDir> stGroups = new HashMap<>();
    private Map<String, Object> defaultSubstitutions = new HashMap<>();

    @Activate
    public void activate(ComponentContext context) {
        bundle = context.getBundleContext().getBundle();
        addBundleDir("/code");
    }

    @Deactivate
    public void deactivate(ComponentContext context) { // NOSONAR
        bundle = null;
        stGroups = new HashMap<>();
    }

    private STGroupBundleDir addBundleDir(String key) {
        return stGroups.put(key, new STGroupBundleDir(bundle, key));
    }

    public ST templateFor(String path) {
        StPath normalizedPath = new StPath(path);
        String bundleDir = normalizedPath.getBundleDir();
        STGroupBundleDir stGroupBundleDir = stGroups.get(bundleDir);
        ST template;
        if (stGroupBundleDir == null) {
            log.info("template not found, adding path '{}' and trying again", bundleDir);
            addBundleDir(bundleDir);
            stGroupBundleDir = stGroups.get(bundleDir);
            template = stGroupBundleDir.getInstanceOf(normalizedPath.getTemplateName());
        } else {
            template = stGroupBundleDir.getInstanceOf(normalizedPath.getTemplateName());

        }
        for (Entry<String, Object> entry : defaultSubstitutions.entrySet()) {
            template.add(entry.getKey(), entry.getValue());
        }
        return template;
    }

    public TemplateProvider add(String key, Object value) {
        defaultSubstitutions.put(key, value);
        return this;
    }

}
