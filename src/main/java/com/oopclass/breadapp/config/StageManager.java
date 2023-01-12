package com.oopclass.breadapp.config;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Objects;

import org.slf4j.Logger;

import com.oopclass.breadapp.views.FxmlView;
import java.io.IOException;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * OOP Class 20-21
 * @author Gerald Villaran
 */

public class StageManager {

    private static final Logger LOG = getLogger(StageManager.class);
    private final Stage primaryStage;
    private final SpringFXMLLoader springFXMLLoader;

    public StageManager(SpringFXMLLoader springFXMLLoader, Stage stage) {
        this.springFXMLLoader = springFXMLLoader;
        this.primaryStage = stage;
    }

    public void switchScene(final FxmlView view) {
        Parent viewRootNodeHierarchy = loadViewNodeHierarchy(view.getFxmlFile());
        show(viewRootNodeHierarchy, view.getTitle());
    }

    private void show(final Parent rootnode, String title) {
        Scene scene = prepareScene(rootnode);
        //scene.getStylesheets().add("/styles/Styles.css");

        //primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();

        try {
            primaryStage.show();
        } catch (Exception exception) {
            logAndExit("Unable to show scene for title" + title, exception);
        }
    }

    private Scene prepareScene(Parent rootnode) {
        Scene scene = primaryStage.getScene();

        if (scene == null) {
            scene = new Scene(rootnode);
        }
        scene.setRoot(rootnode);
        return scene;
    }

    private Parent loadViewNodeHierarchy(String fxmlFilePath) {
        Parent rootNode = null;
        try {
            rootNode = springFXMLLoader.load(fxmlFilePath);
            Objects.requireNonNull(rootNode, "A Root FXML node must not be null");
        } catch (IOException exception) {
            logAndExit("Unable to load FXML view:" + fxmlFilePath, exception);
        }
        return rootNode;
    }

    private void logAndExit(String errorMsg, Exception exception) {
        LOG.error(errorMsg, exception, exception.getCause());
        Platform.exit();
    }

}
