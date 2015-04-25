package io.skysail.server.ext.favorites.db;

import io.skysail.api.favorites.Favorite;
import lombok.*;

@NoArgsConstructor
@Getter
public class FavoriteEntity {

    private String name;
    private String username;

    public FavoriteEntity(Favorite favorite) {
        name = favorite.getFavoriteName();
        username = favorite.getUsername();
    }

}
