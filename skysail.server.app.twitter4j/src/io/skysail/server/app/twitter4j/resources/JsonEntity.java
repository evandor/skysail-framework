package io.skysail.server.app.twitter4j.resources;

import org.json.JSONArray;
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

    @Field
    private String location;
    
    @Field(htmlPolicy = HtmlPolicy.DEFAULT_HTML)
    private String msg;
    
    public JsonEntity(String endpoint, String location, JSONObject json) {
        try {
            this.location = location;
            String formatted = JsonFormatter.format(endpoint, json);
            this.msg = formatted.replace(",", ",<br>,");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    public JsonEntity(String endpoint, String location, JSONArray jsonArray) {
        try {
            this.location = location;
            String formatted = JsonFormatter.format(endpoint, jsonArray.getJSONObject(0));
            this.msg = formatted.replace(",", ",<br>,");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

}
