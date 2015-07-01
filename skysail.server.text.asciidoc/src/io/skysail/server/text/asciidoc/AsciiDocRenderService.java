package io.skysail.server.text.asciidoc;

import io.skysail.api.text.*;
import io.skysail.server.utils.CompositeClassLoader;

import java.util.*;

import org.asciidoctor.Asciidoctor;
import org.jruby.RubyInstanceConfig;
import org.jruby.javasupport.JavaEmbedUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.*;

@Component(immediate = true, properties = { org.osgi.framework.Constants.SERVICE_RANKING + "="
        + AsciiDocRenderService.SERVICE_RANKING })
public class AsciiDocRenderService implements TranslationRenderService {

    public static final String SERVICE_RANKING = "200";
    public static final String PREFIX_IDENTIFIER = "renderer:asciidoc ";

    private volatile Asciidoctor asciidoctor;

    @Activate
    public void activate(ComponentContext ctx) {

        Bundle[] allBundles = ctx.getBundleContext().getBundles();
        Optional<Bundle> jrubyBundle = Arrays.stream(allBundles).filter(b -> {
            return b.getSymbolicName().equals("org.jruby.jruby");
        }).findFirst();
        if (jrubyBundle.isPresent()) {
            
            ClassLoader loader = jrubyBundle.get().adapt(BundleWiring.class).getClassLoader();
            ClassLoader originalLoader = Thread.currentThread().getContextClassLoader();
            
            CompositeClassLoader ccl = new CompositeClassLoader();
            ccl.addClassLoader(loader);
            ccl.addClassLoader(originalLoader);
            ccl.addClassLoader(this.getClass().getClassLoader());
            
            RubyInstanceConfig config = new RubyInstanceConfig();
            config.setLoader(ccl);//this.getClass().getClassLoader());

            JavaEmbedUtils.initialize(Arrays.asList("META-INF/jruby.home/lib/ruby/2.0", "gems/asciidoctor-1.5.2/lib"),
                    config);

            try {
                Thread.currentThread().setContextClassLoader(loader);
                asciidoctor = org.asciidoctor.Asciidoctor.Factory.create(originalLoader);//this.getClass().getClassLoader());
              } finally {
                Thread.currentThread().setContextClassLoader(originalLoader);
              }
        }
        // asciidoctor = org.asciidoctor.Asciidoctor.Factory.create(Arrays
        // .asList("C:\\git\\skysail-framework\\skysail.server.text.asciidoc\\resources\\jruby"));
        System.out.println(asciidoctor);
        // asciidoctor =
        // org.asciidoctor.Asciidoctor.Factory.create(ClassLoader.getSystemClassLoader());
    }

    @Deactivate
    public void deactivate() {
        asciidoctor = null;
    }

    @Override
    public String render(String in, Object... substitutions) {
        return asciidoctor.convert(in, new HashMap<String, Object>());
    }

    @Override
    public String render(Translation translation, Object... substitutions) {
        return asciidoctor.convert(translation.getValue(), new HashMap<String, Object>());
    }

    @Override
    public String adjustText(String unformatted) {
        return unformatted.trim().substring(PREFIX_IDENTIFIER.length());
    }

    @Override
    public boolean applicable(String unformattedTranslation) {
        return (unformattedTranslation.trim().startsWith(PREFIX_IDENTIFIER));
    }

    @Override
    public String addRendererInfo() {
        return PREFIX_IDENTIFIER;
    }

}
