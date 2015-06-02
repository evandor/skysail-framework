package io.skysail.server.model;

import lombok.Getter;

@Getter
public class RepresentationLink {

    private String name;
    private String img;
    private String href;
    private Object entity;

    public RepresentationLink(String mediaType, Object entity) {
        this.name = mediaType.replace("/*","");
        this.entity = entity;
        this.img = "/static/img/fileicons/16px/" + mediaType.replace("/*","") + ".png";
        this.href = "?media=" + mediaType;
    }
    
}
