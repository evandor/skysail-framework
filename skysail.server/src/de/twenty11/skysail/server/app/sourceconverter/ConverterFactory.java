package de.twenty11.skysail.server.app.sourceconverter;

import java.util.ArrayList;
import java.util.List;

import org.restlet.representation.Variant;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

@Component(immediate = true)
public class ConverterFactory {

    private static List<SourceConverter> converters = new ArrayList<>();
    private static SourceConverter fallBackConverter = new NoopConverter();

    @Reference(dynamic = true, multiple = true, optional = true)
    public void addSourceConverter(SourceConverter sourceConverter) {
        converters.add(sourceConverter);
    }

    public void removeSourceConverter(SourceConverter sourceConverter) {
        converters.remove(sourceConverter);
    }

    public static SourceConverter getConverter(Object source, Variant target) {
        fallBackConverter.init(source, target); // todo init only if needed...
                                                // or throw Exception if nothing
                                                // was found
        return converters.stream().map(c -> {
            c.init(source, target);
            return c;
        }).filter(c -> {
            return c.isCompatible();
        }).findFirst().orElse(fallBackConverter);
    }
}
