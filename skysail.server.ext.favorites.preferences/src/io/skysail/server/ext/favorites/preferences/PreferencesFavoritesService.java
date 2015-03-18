package io.skysail.server.ext.favorites.preferences;

import io.skysail.api.favorites.Favorite;
import io.skysail.api.favorites.FavoritesService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.osgi.service.prefs.PreferencesService;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

@Component(immediate = true)
@Slf4j
public class PreferencesFavoritesService implements FavoritesService {

    public static final String FAVORITES_NODE = "favorites";

    public static final String FAVORITE_NAME = "name";
    public static final String FAVORITE_IMG = "img";
    public static final String FAVORITE_LINK = "link";
    public static final String FAVORITE_TOOLTIP = "tooltip";

    private PreferencesService preferencesService;

    @Activate
    public void activate() {
        System.out.println("***init***");
    }

    @Override
    public synchronized boolean add(Favorite favorite) {
        if (preferencesService == null) {
            log.warn("no preference Service available!");
            return false;
        }
        Preferences preferences = preferencesService.getUserPreferences(favorite.getUsername());
        Preferences favoritesNode = preferences.node(FAVORITES_NODE);
        try {
            Preferences favoriteNode;
            if (favoritesNode.nodeExists(favorite.getFavoriteName())) {
                favoriteNode = favoritesNode.node(favorite.getFavoriteName());
                log.info("updating favorite '{}' for user '{}'", favorite.getFavoriteName(), favorite.getUsername());
            } else {
                favoriteNode = favoritesNode.node(favorite.getFavoriteName());
                log.info("adding new favorite '{}' for user '{}'", favorite.getFavoriteName(), favorite.getUsername());
            }
            favoriteNode.put(FAVORITE_LINK, favorite.getFavoriteLink());
            favoriteNode.put(FAVORITE_TOOLTIP,
                    favorite.getFavoriteTooltip() == null ? "" : favorite.getFavoriteTooltip());
            favoriteNode.put(FAVORITE_IMG, favorite.getFavoriteImg() == null ? "" : favorite.getFavoriteImg());
            return true;
        } catch (BackingStoreException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public List<Favorite> get(String username) {
        if (preferencesService == null) {
            log.warn("no preference Service available!");
            return Collections.emptyList();
        }
        Preferences preferences = preferencesService.getUserPreferences(username);
        Preferences favoritesNode = preferences.node(FAVORITES_NODE);
        try {
            return Arrays.stream(favoritesNode.childrenNames())
                    .map(fav -> createFavorite(favoritesNode, username, fav)).collect(Collectors.toList());
        } catch (BackingStoreException e) {
            log.error("Could not retrieve favorites for user '" + username + "'", e);
            return Collections.emptyList();
        }
    }

    @Override
    public void remove(Favorite favorite) {
        String username = favorite.getUsername();
        Preferences preferences = preferencesService.getUserPreferences(username);
        Preferences favoritesNode = preferences.node(FAVORITES_NODE);
        try {
            Optional<Preferences> pref = Arrays.stream(favoritesNode.childrenNames())
                    .map(name -> getFavoriteNode(favoritesNode, name)).filter(f -> find(f, favorite)).findFirst();
            pref.ifPresent(p -> favoritesNode.remove(p.name()));
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }

    private boolean find(Preferences f, Favorite favorite) {
        if (!(f.get(FAVORITE_LINK, "").equals(favorite.getFavoriteLink()))) {
            return false;
        }
        return true;
    }

    private Preferences getFavoriteNode(Preferences node, String name) {
        return node.node(name);
    }

    private Favorite createFavorite(Preferences favoritesNode, String username, String favName) {
        Preferences node = favoritesNode.node(favName);
        Favorite favorite = new Favorite().setUsername(username).setFavoriteName(favName)
                .setFavoriteLink(node.get(FAVORITE_LINK, ""));
        favorite.setFavoriteImg(node.get(FAVORITE_IMG, ""));
        favorite.setFavoriteTooltip(node.get(FAVORITE_TOOLTIP, ""));
        return favorite;
    }

    @Reference(dynamic = true, optional = false, multiple = false)
    public void setPreferencesService(PreferencesService preferencesService) {
        this.preferencesService = preferencesService;
    }

    public void unsetPreferencesService(PreferencesService preferencesService) {
        this.preferencesService = null;
    }

}
