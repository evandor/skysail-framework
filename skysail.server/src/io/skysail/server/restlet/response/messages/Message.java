package io.skysail.server.restlet.response.messages;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Message {

    private MessageType type;
    private String msg;

    public Message(MessageType type, String msg) {
        this.type = type;
        this.msg = msg;
    }

}
