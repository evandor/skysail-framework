package de.twenty11.skysail.api.favorites;

public class Favorite {

	String username, favoriteName, favoriteLink, favoriteImg, favoriteTooltip;

	public Favorite(String username, String name, String link) {
        this.username = username;
        this.favoriteName = name;
        this.favoriteLink = link;
    }
	
	public String getUsername() {
		return username;
	}

	public String getFavoriteName() {
		return favoriteName;
	}

	public String getFavoriteLink() {
		return favoriteLink;
	}

	public String getFavoriteImg() {
		return favoriteImg;
	}

	public void setFavoriteImg(String favoriteImg) {
		this.favoriteImg = favoriteImg;
	}

	public String getFavoriteTooltip() {
		return favoriteTooltip;
	}

	public void setFavoriteTooltip(String favoriteTooltip) {
		this.favoriteTooltip = favoriteTooltip;
	}
	
	
}
