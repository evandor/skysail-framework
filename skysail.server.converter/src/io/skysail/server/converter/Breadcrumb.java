package io.skysail.server.converter;

import lombok.*;

@Builder
@ToString
@Deprecated // use class from skysail.server
public class Breadcrumb {

    @Getter
    private String href;
    @Getter
    private String cssClass;
    @Getter
    private String value;

    private Boolean favorite;


    public boolean isFavoriteEmpty() {
        return favorite != null && !favorite;
    }

    public boolean isFavoriteFull() {
        return favorite != null && favorite;
    }

}