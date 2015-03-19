package io.skysail.api.favorites;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString(of = { "username", "favoriteName", "favoriteLink" })
public class Favorite {

    String username;

    String favoriteName;

    String favoriteLink;

    String favoriteImg;

    String favoriteTooltip;

}
