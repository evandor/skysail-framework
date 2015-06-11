package io.skysail.server.ext.sse.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

public class TestSseResource extends EntityServerResource<String> {

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public String getEntity() {
       // alert();
        StringBuilder sb = new StringBuilder();
        sb.append("hier:<br><div id='output'></div><script>\n");
        sb.append("var output = document.getElementById('output');\n");
        sb.append(" if (typeof(EventSource) !== 'undefined') {\n");
        sb.append("  var source = new EventSource('/SSE');\n");
        sb.append("   source.onmessage = function(event) {\n");
        sb.append("     alert(event);\n");
        sb.append("     output.innerHTML += event.data + '<br>';\n");
        sb.append("   };\n");
        sb.append("   source.onerror = function(event) {\n");
        sb.append("     output.innerHTML += 'error<br>';\n");
        sb.append("   };\n");
        sb.append("   source.onopen = function(event) {\n");
        sb.append("     output.innerHTML += 'opened...<br>';\n");
        sb.append("   };\n");
        
        sb.append("   source.addEventListener('ping', function(e) {\n");
        sb.append("     var newElement = document.createElement('li');\n");
        sb.append("     var obj = JSON.parse(e.data);\n");
        sb.append("     newElement.innerHTML = 'ping at ' + obj.time;\n");
        sb.append("     eventList.appendChild(newElement);\n");
        sb.append("   }, false);\n");
        sb.append("  }\n");
        sb.append("  </script>\n");
        return sb.toString();
    }

//    private void alert() {
//        new EventHelper(((SseApplication) getApplication()).getEventAdmin().get())//
//                .channel(EventHelper.GUI_MSG)//
//                .info(new Date().toString())//
//                .fire();
//    }
}
