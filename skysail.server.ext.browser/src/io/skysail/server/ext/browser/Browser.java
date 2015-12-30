package io.skysail.server.ext.browser;

import javafx.application.Platform;
import javafx.beans.value.*;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Worker.State;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.web.*;
import javafx.scene.web.WebHistory.Entry;
import javafx.util.Callback;
import netscape.javascript.JSObject;

public class Browser extends Region {

    private HBox toolBar;
    private static String[] imageFiles = new String[] { "product.png", "blog.png", "documentation.png", "partners.png",
            "help.png" };
    private static String[] captions = new String[] { "Products", "Blogs", "Documentation", "Partners", "Help" };
    private static String[] urls = new String[] { "http://www.oracle.com/products/index.html",
            "http://blogs.oracle.com/", "http://docs.oracle.com/javase/index.html",
            "http://www.oracle.com/partners/index.html", "http://www.oracle.com/partners/index.html" };
    final ImageView selectedImage = new ImageView();
    final Hyperlink[] hpls = new Hyperlink[captions.length];
    final Image[] images = new Image[imageFiles.length];
    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();
    final Button showPrevDoc = new Button("Toggle Previous Docs");
    final WebView smallView = new WebView();
    final ComboBox comboBox = new ComboBox();
    private boolean needDocumentationButton = false;

    public Browser() {
        // apply the styles
        getStyleClass().add("browser");

        for (int i = 0; i < captions.length; i++) {
            // create hyperlinks
            Hyperlink hpl = hpls[i] = new Hyperlink(captions[i]);
            Image image = images[i] = new Image(getClass().getResourceAsStream(imageFiles[i]));
            hpl.setGraphic(new ImageView(image));
            final String url = urls[i];
            final boolean addButton = (hpl.getText().equals("Documentation"));

            // process event
            hpl.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    needDocumentationButton = addButton;
                    webEngine.load(url);
                }
            });
        }

        comboBox.setPrefWidth(60);

        // create the toolbar
        toolBar = new HBox();
        toolBar.setAlignment(Pos.CENTER);
        toolBar.getStyleClass().add("browser-toolbar");
        toolBar.getChildren().add(comboBox);
        toolBar.getChildren().addAll(hpls);
        toolBar.getChildren().add(createSpacer());

        // set action for the button
        showPrevDoc.setOnAction(new EventHandler() {
            @Override
            public void handle(Event t) {
                webEngine.executeScript("toggleDisplay('PrevRel')");
            }
        });

        smallView.setPrefSize(120, 80);

        // handle popup windows
        webEngine.setCreatePopupHandler(new Callback<PopupFeatures, WebEngine>() {
            @Override
            public WebEngine call(PopupFeatures config) {
                smallView.setFontScale(0.8);
                if (!toolBar.getChildren().contains(smallView)) {
                    toolBar.getChildren().add(smallView);
                }
                return smallView.getEngine();
            }
        });

        // process history
        final WebHistory history = webEngine.getHistory();
        history.getEntries().addListener(new ListChangeListener<WebHistory.Entry>() {
            @Override
            public void onChanged(Change<? extends Entry> c) {
                c.next();
                for (Entry e : c.getRemoved()) {
                    comboBox.getItems().remove(e.getUrl());
                }
                for (Entry e : c.getAddedSubList()) {
                    comboBox.getItems().add(e.getUrl());
                }
            }
        });

        // set the behavior for the history combobox
        comboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent ev) {
                int offset = comboBox.getSelectionModel().getSelectedIndex() - history.getCurrentIndex();
                history.go(offset);
            }
        });

        // process page loading
        webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
            @Override
            public void changed(ObservableValue<? extends State> ov, State oldState, State newState) {
                toolBar.getChildren().remove(showPrevDoc);
                if (newState == State.SUCCEEDED) {
                    JSObject win = (JSObject) webEngine.executeScript("window");
                    win.setMember("app", new JavaApp());
                    if (needDocumentationButton) {
                        toolBar.getChildren().add(showPrevDoc);
                    }
                }
            }
        });

        // load the home page
        webEngine.load("http://localhost:2016");

        // add components
        //getChildren().add(toolBar);
        getChildren().add(browser);
    }

    // JavaScript interface object
    public class JavaApp {

        public void exit() {
            Platform.exit();
        }
    }

    private Node createSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    @Override
    protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        double tbHeight = toolBar.prefHeight(w);
        layoutInArea(browser, 0, 0, w, h - tbHeight, 0, HPos.CENTER, VPos.CENTER);
        layoutInArea(toolBar, 0, h - tbHeight, w, tbHeight, 0, HPos.CENTER, VPos.CENTER);
    }

    @Override
    protected double computePrefWidth(double height) {
        return 750;
    }

    @Override
    protected double computePrefHeight(double width) {
        return 600;
    }
}