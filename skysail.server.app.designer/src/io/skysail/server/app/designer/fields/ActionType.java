package io.skysail.server.app.designer.fields;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

@Getter
public enum ActionType {

    @SuppressWarnings("serial")
    CREATION_DATE(Date.class, new HashMap<String,String>() {{
        put("postEntity#addEntity", "set$Methodname$(new Date());");
    }});
    
    private Map<String, String> codes;
    private Class<?> type;

    private ActionType(Class<?> type, Map<String,String> codes) {
        this.type = type;
        this.codes = codes;
    }

}
