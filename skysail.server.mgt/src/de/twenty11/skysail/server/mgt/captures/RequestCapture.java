package de.twenty11.skysail.server.mgt.captures;

public class RequestCapture {

    private Integer count;
    private String key;

    public RequestCapture(String key, Integer count) {
        this.key = key;
        this.count = count;
    }

    public String getKey() {
        return key;
    }

    public Integer getCount() {
        return count;
    }

}
