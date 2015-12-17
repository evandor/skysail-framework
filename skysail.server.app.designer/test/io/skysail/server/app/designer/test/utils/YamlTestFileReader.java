package io.skysail.server.app.designer.test.utils;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import io.skysail.server.app.designer.application.DbApplication;

public class YamlTestFileReader {

    private static ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    public static DbApplication read(String... subpath) throws IOException {
        List<String> strs = new ArrayList<>();
        strs.add("testinput");
        strs.addAll(Arrays.asList(subpath));
        Path path = Paths.get("resources", strs.toArray(new String[strs.size()]));
        StringBuilder sb = new StringBuilder();
        Files.lines(path).forEach(line -> sb.append(line).append("\n"));
        return mapper.readValue(sb.toString(), DbApplication.class);
    }

}
