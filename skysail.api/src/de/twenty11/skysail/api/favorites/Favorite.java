package de.twenty11.skysail.api.favorites;

import lombok.Getter;

@Getter
public class Favorite {

	String username, favoriteName, favoriteLink, favoriteImg, favoriteTooltip;

	public Favorite(String username, String name, String link) {
        this.username = username;
        this.favoriteName = name;
        this.favoriteLink = link;
    }
	
	public void setFavoriteImg(String favoriteImg) {
		this.favoriteImg = favoriteImg;
	}

	public void setFavoriteTooltip(String favoriteTooltip) {
		this.favoriteTooltip = favoriteTooltip;
	}
	
	
}
