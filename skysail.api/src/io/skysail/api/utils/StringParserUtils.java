package io.skysail.api.utils;

import java.util.regex.Pattern;

public class StringParserUtils {

    public static Pattern placeholderPattern = Pattern.compile("\\{([^\\}]*)\\}");

}
