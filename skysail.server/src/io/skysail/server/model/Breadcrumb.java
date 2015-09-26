package io.skysail.server.model;

import lombok.*;

@Builder
@ToString
public class Breadcrumb {

    @Getter
    private String href;
    @Getter
    private String cssClass;
    @Getter
    private String value;

}
