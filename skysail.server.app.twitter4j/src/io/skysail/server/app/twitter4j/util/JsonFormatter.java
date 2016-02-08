package io.skysail.server.app.twitter4j.util;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * see
 * http://stackoverflow.com/questions/8596161/json-string-tidy-formatter-for-
 * java
 *
 */
public class JsonFormatter {

    public static String format(String endpoint, final JSONObject object) throws JSONException {
        final JsonVisitor visitor = new JsonVisitor(4, ' ');
        visitor.visit(object, 0, endpoint);
        return visitor.toString();
    }

    private static class JsonVisitor {

        private final StringBuilder builder = new StringBuilder();
        private final int indentationSize;
        private final char indentationChar;

        public JsonVisitor(final int indentationSize, final char indentationChar) {
            this.indentationSize = indentationSize;
            this.indentationChar = indentationChar;
        }

        private void visit(final JSONArray array, final int indent, String endpoint) throws JSONException {
            final int length = array.length();
            if (length == 0) {
                write("[]", indent);
            } else {
                write("[", indent);
                for (int i = 0; i < length; i++) {
                    visit(array.get(i), indent + 1, endpoint);
                }
                write("]", indent);
            }

        }

        private void visit(final JSONObject obj, final int indent, String endpoint) throws JSONException {
            final int length = obj.length();
            if (length == 0) {
                write("{}", indent);
            } else {
                write("{", indent);
                final Iterator<String> keys = obj.keys();
                while (keys.hasNext()) {
                    final String key = keys.next();
                    write("<b>" + key + "</b> :", indent + 1);
                    visit(obj.get(key), indent + 1, endpoint);
                    if (keys.hasNext()) {
                        write(",", indent + 1);
                    }
                }
                write("}", indent);
            }

        }

        private void visit(Object object, int indent, String endpoint) throws JSONException {
            if (object instanceof JSONArray) {
                visit((JSONArray) object, indent, endpoint);
            } else if (object instanceof JSONObject) {
                visit((JSONObject) object, indent, endpoint);
            } else {
                if (object instanceof String) {
                    String aString = (String) object;
                    if (aString.startsWith("http")) {
                        write("\"<a href='?url=" + endpoint + aString.replace(endpoint, "") + "'>"+aString+"</a>\"", indent);
                    } else {
                        write("\"" + aString + "\"", indent);
                    }
                } else {
                    write(String.valueOf(object), indent);
                }
            }

        }

        private void write(final String data, final int indent) {
            for (int i = 0; i < (indent * indentationSize); i++) {
                builder.append(indentationChar);
            }
            builder.append(data).append('\n');
        }

        @Override
        public String toString() {
            return builder.toString();
        }

    }

}
