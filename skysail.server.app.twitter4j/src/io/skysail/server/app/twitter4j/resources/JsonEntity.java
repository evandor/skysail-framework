package io.skysail.server.app.twitter4j.resources;

import org.json.JSONException;
import org.json.JSONObject;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.HtmlPolicy;
import io.skysail.server.app.twitter4j.util.JsonFormatter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JsonEntity implements Identifiable {

    private String id;
    
    @Field(htmlPolicy = HtmlPolicy.DEFAULT_HTML)
    private String msg;
    
    public JsonEntity(String endpoint, JSONObject json) {
        try {
            String formatted = JsonFormatter.format(endpoint, json);
            System.out.println(formatted);
            this.msg = formatted.replace(",", ",<br>,");
            System.out.println(this.msg);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

}
