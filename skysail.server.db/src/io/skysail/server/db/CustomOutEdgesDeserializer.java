package io.skysail.server.db;

import io.skysail.server.db.test.Comment;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.node.*;

public class CustomOutEdgesDeserializer extends JsonDeserializer<OutEdges<?>> implements ResolvableDeserializer {

    @Override
    public OutEdges<?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
            JsonProcessingException {

        ArrayNode node = jp.getCodec().readTree(jp);
        OutEdges<Comment> outEdges = new OutEdges<>();
        for (int i = 0; i < node.size(); i++) {
            JsonNode jsonNode = node.get(i);
            //String id = ((TextNode) jsonNode.get("id")).asText();
            //String out = ((TextNode) jsonNode.get("out")).asText();
            ObjectNode inNode = (ObjectNode) jsonNode.get("in");
            Comment treeToValue = jp.getCodec().treeToValue(inNode, Comment.class);
            outEdges.add(treeToValue);
        }
         return outEdges;
    }

    @Override
    public void resolve(DeserializationContext ctxt) throws JsonMappingException {
        System.out.println("hier");
    }


}
