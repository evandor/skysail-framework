package io.skysail.server.db.test;

import java.text.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomOutEdgesDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testName() throws Exception {
        String input = "{\"id\":\"#16:1291\",\"title\":\"1447785738974\",\"created\":\"2015-11-17 19:42:20\","
                + "\"comments\":[{\"id\":\"#17:1038\",\"out\":\"#16:1291\",\"in\":{\"id\":\"#18:977\",\"comment\":\"Comment #1\",\"comments\":[\"#17:1038\"]}}]}\"";

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(df);

       // Todo readValue = mapper.readValue(input, Todo.class);
        Todo readValue = mapper.reader(Todo.class).readValue(input);
        System.out.println(readValue);
    }

    @Test
    public void testNameWithList() throws Exception {
        String input = "{\"id\":\"#16:1291\",\"title\":\"1447785738974\",\"created\":\"2015-11-17 19:42:20\","
                + "\"comments\":[{\"id\":\"#18:977\",\"comment\":\"Comment #1\"}]}\"";

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(df);

       // Todo readValue = mapper.readValue(input, Todo.class);
        TodoWithList readValue = mapper.reader(TodoWithList.class).readValue(input);
        System.out.println(readValue);
    }

}
