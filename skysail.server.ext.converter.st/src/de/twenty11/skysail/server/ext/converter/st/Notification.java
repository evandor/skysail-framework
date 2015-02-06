package de.twenty11.skysail.server.ext.converter.st;

public class Notification {

    private String msg;
    private String type;

    public Notification(String msg, String type) {
        this.msg = msg;
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }
    
    public String getType() {
        return type;
    }
}
