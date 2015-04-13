package io.skysail.server.converter;

import lombok.Getter;

@Getter
public class RepresentationLink {

    private String name;
    private String img;
    private String href;

    public RepresentationLink(String mediaType) {
        this.name = mediaType;
        this.img = "/static/img/fileicons/16px/" + mediaType + ".png";
        this.href = "?media=" + mediaType;
    }

}
