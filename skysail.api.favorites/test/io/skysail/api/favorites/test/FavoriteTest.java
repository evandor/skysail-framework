package io.skysail.api.favorites.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.api.favorites.Favorite;

import org.junit.Test;

public class FavoriteTest {

    @Test
    public void pojo_test() throws Exception {
        Favorite favorite = new Favorite().setFavoriteImg("img").setFavoriteLink("link").setFavoriteName("name")
                .setFavoriteTooltip("tooltip").setUsername("username");

        assertThat(favorite.getFavoriteImg(), is(equalTo("img")));
        assertThat(favorite.getFavoriteLink(), is(equalTo("link")));
        assertThat(favorite.getFavoriteName(), is(equalTo("name")));
        assertThat(favorite.getFavoriteTooltip(), is(equalTo("tooltip")));
        assertThat(favorite.getUsername(), is(equalTo("username")));
    }

}
