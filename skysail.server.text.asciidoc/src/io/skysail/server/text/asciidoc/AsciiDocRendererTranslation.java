//package io.skysail.server.text.asciidoc;
//
//import io.skysail.api.text.Translation;
//import io.skysail.api.text.TranslationRenderService;
//
//import java.util.Optional;
//
//public class AsciiDocRendererTranslation extends Translation {
//
//    public AsciiDocRendererTranslation(Optional<String> text) {
//        super(text, null, Integer.valueOf(AsciiDocRenderService.SERVICE_RANKING));
//    }
//
//    @Override
//    public Class<? extends TranslationRenderService> getTranslatedBy() {
//        return AsciiDocRenderService.class;
//    }
//
//}
