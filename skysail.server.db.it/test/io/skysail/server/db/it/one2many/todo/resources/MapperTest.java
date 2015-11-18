//package io.skysail.server.db.it.one2many.todo.resources;
//
//import io.skysail.server.db.it.one2many.todo.Todo;
//
//import java.text.*;
//
//import org.junit.Test;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//public class MapperTest {
//
//    private ObjectMapper mapper = new ObjectMapper();
//
//    @Test
//    public void testName() throws Exception {
//        String input = "{\"id\":\"#16:1291\",\"title\":\"1447785738974\",\"created\":\"2015-11-17 19:42:20\","
//                + "\"comments\":[{\"id\":\"#17:1038\",\"out\":\"#16:1291\",\"in\":{\"id\":\"#18:977\",\"comment\":\"Comment #1\",\"comments\":[\"#17:1038\"]}}]}\"";
//
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        mapper.setDateFormat(df);
//
//       // Todo readValue = mapper.readValue(input, Todo.class);
//        Todo readValue = mapper.reader(Todo.class).readValue(input);
//
//
//        System.out.println(readValue);
//
//    }
//}
