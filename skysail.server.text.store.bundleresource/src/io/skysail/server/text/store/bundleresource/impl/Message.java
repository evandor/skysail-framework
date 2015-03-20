package io.skysail.server.text.store.bundleresource.impl;

import io.skysail.api.forms.Field;

public class Message {

    @Field
    private String msgKey;

    private String msg;

    public Message(String msgKey) {
        this.msgKey = msgKey;
    }

    public Message(String msgKey, String msg) {
        this.msgKey = msgKey;
        this.msg = msg.replace("'{'", "{").replace("'}'", "}");

    }

    public String getMsgKey() {
        return msgKey;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
