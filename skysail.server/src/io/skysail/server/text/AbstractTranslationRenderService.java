//package io.skysail.server.text;
//
//import io.skysail.api.text.*;
//
//import org.restlet.Request;
//
//public abstract class AbstractTranslationRenderService implements TranslationRenderService {
//
//  //  protected List<TranslationStoreHolder> stores = new ArrayList<>();
//
//   // protected abstract Translation createTranslation(StoreAndTranslation t);
//
////    @Override
////    public Translation getTranslation(String key, ClassLoader cl, Request request, TranslationStore store) {
////        return createTranslation(new StoreAndTranslation(store, store.get(key, cl, request)));
////    }
//
//    /**
//     * Here, no specific store is provided, so the available translation stores
//     * are sorted by their service ranking and asked for a translation.
//     */
//    @Override
//    public Translation getTranslation(String key, ClassLoader cl, Request request) {
//
////        List<TranslationStoreHolder> sortedStores = getSortedTranslationStores();
////
////        Optional<Translation> bestTranslation = sortedStores.stream().filter(store -> {
////            return store.getStore().get() != null;
////        }).map(store -> {
////            return new StoreAndTranslation(store.getStore().get(), store.getStore().get().get(key, cl, request));
////        }).filter( storeAndTranslation -> {
////            return storeAndTranslation.getTranslation().isPresent();
////        }).map(storeAndTranslation -> {
////            return createTranslation(storeAndTranslation);
////        }).findFirst();
////
////        if (bestTranslation.isPresent()) {
////            return bestTranslation.get();
////        }
//        return null;
//
//    }
//
//    
//}
