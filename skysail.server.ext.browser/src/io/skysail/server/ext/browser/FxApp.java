package io.skysail.server.ext.browser;

import java.util.concurrent.Executors;

import aQute.bnd.annotation.component.*;
import javafx.application.*;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

@Component(immediate = true)
public class FxApp extends Application implements StageProvider {

    private Stage stage;
    private Scene scene;

    @Activate
    public void startBundle() {
        Executors.defaultThreadFactory().newThread(() -> {
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            launch();
        }).start();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;

        stage.setTitle("skysail browser");
        scene = new Scene(new Browser(), 1024, 800, Color.web("#666970"));
        stage.setScene(scene);
        scene.getStylesheets().add("io/skysail/server/ext/browser/BrowserToolbar.css");
        stage.show();
    }

    @Override
    public Stage getStage() {
        return this.stage;
    }

    @Deactivate
    public void stopBundle() {
        Platform.exit();
    }
}
