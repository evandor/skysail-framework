package io.skysail.api.text.test;

import io.skysail.api.text.*;

public class LowerCaseRenderService implements TranslationRenderService {

    public class DefaultTranslation extends Translation {

        public DefaultTranslation(String text) {
            super(text, null,null);
        }

//        @Override
//        public Class<? extends TranslationRenderService> getTranslatedBy() {
//            return LowerCaseRenderService.class;
//        }

    }

//    @Override
//    public Translation getTranslation(String key, ClassLoader cl, Request request, TranslationStore store) {
//        if (store != null) {
//            Optional<String> text = store.get(key);
//            if (text.isPresent()) {
//                return new DefaultTranslation(text.get());
//            }
//            return new DefaultTranslation("");
//        }
//        return new DefaultTranslation(key);
//    }

    @Override
    public String render(Translation translation, Object... substitutions) {
        return translation.getValue().toLowerCase();
    }

//    @Override
//    public Translation getTranslation(String key, ClassLoader cl, Request request) {
//        return null;
//    }

    @Override
    public boolean applicable(String unformattedTranslation) {
        return true;
    }

    @Override
    public String adjustText(String unformatted) {
        return null;
    }

    @Override
    public String addRendererInfo() {
        return null;
    }

}
