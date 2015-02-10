package io.skysail.api.favorites;

import java.util.List;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface FavoritesService {

    boolean add(Favorite favorite);

    List<Favorite> get(String username);

    void remove(Favorite favorite);

}
