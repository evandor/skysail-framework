package io.skysail.server.ext.favorites.db;

import io.skysail.api.favorites.*;
import io.skysail.server.db.*;

import java.util.*;
import java.util.stream.Collectors;

import aQute.bnd.annotation.component.*;

@Component(immediate = true, properties = "name=FavoritesRepository")
public class DbFavoritesService implements FavoritesService, DbRepository {

    private DbService2 dbService;

    @Activate
    public void activate() {
        dbService.createWithSuperClass("V", FavoriteEntity.class.getSimpleName());
        dbService.register(FavoriteEntity.class);
        // dbService.createUniqueIndex(TodoList.class, "name", "owner");
    }

    @Override
    public boolean add(Favorite favorite) {
        FavoriteEntity entity = new FavoriteEntity(favorite);
        Object persist = dbService.persist(entity);
        return persist != null;
    }

    @Override
    public List<Favorite> get(String username) {
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        List<FavoriteEntity> entities = dbService.findObjects("SELECT FROM FavoriteEntity WHERE username=:username",
                params);
        return entities.stream().map(e -> {
            return new Favorite().setFavoriteName(e.getName()).setUsername(e.getUsername());
        }).collect(Collectors.toList());
    }

    @Override
    public void remove(Favorite favorite) {
        
    }

    public void setDbService(DbService2 dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService2 dbService) {
        this.dbService = null;
    }

}
