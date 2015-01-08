package de.twenty11.skysail.api.favorites;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class Favorite {

	String username, favoriteName, favoriteLink, favoriteImg, favoriteTooltip;

}
