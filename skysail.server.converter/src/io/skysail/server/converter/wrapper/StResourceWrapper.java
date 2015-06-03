package io.skysail.server.converter.wrapper;

import io.skysail.api.favorites.FavoritesService;
import io.skysail.server.model.ResourceModel;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.ResourceUtils;

import java.util.Set;

public class StResourceWrapper {

    private FavoritesService favoritesService;
    private ResourceModel<SkysailServerResource<?>, ?> model;

    public StResourceWrapper(ResourceModel<SkysailServerResource<?>,?> model, FavoritesService favoritesService) {
        this.model = model;
        this.favoritesService = favoritesService;
    }

    public STApplicationWrapper getApplication() {
        return new STApplicationWrapper(model.getResource());
    }

    @Override
    public String toString() {
        return model.getResource().toString();
    }

    public Set<String> getSupportedMediaTypes() {
        return ResourceUtils.getSupportedMediaTypes(model.getResource(), model.getConvertedSource());
    }


}
