package io.skysail.server.converter;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
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
