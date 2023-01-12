package com.oopclass.breadapp;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.oopclass.breadapp.config.StageManager;
import com.oopclass.breadapp.views.FxmlView;
import com.sun.java.swing.plaf.windows.resources.windows;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Screen;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import static javafx.stage.WindowEvent.WINDOW_CLOSE_REQUEST;

/**
 * OOP Class 20-21
 * @author Gerald Villaran
 */

@SpringBootApplication
public class Main extends Application {

    protected ConfigurableApplicationContext springContext;
    protected StageManager stageManager;

    public static void main(final String[] args) {
        Application.launch(args);
    }

    @Override
    public void init() throws Exception {
        springContext = springBootApplicationContext();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stageManager = springContext.getBean(StageManager.class, stage);
        stage.initStyle(StageStyle.UNDECORATED);
        displayInitialScene();

        stage.show();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth())/ 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight())/ 2);
    } 

   
   
//    
//    private void closeRequest(Stage stage){
//        Alert alert = new Alert (Alert.AlertType.CONFIRMATION);
//            alert.setHeaderText(null);
//            alert.setContentText(" Are you sure you want to exit the application? ");
//            if(alert.showAndWait().get() == ButtonType.OK){
//                Platform.exit();         
//            }
//    }
    
    
    
    @Override
    public void stop() throws Exception {
        springContext.close();
    }

    protected void displayInitialScene() {
        stageManager.switchScene(FxmlView.LOGIN);
    }

    private ConfigurableApplicationContext springBootApplicationContext() {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(Main.class);
        String[] args = getParameters().getRaw().stream().toArray(String[]::new);
        return builder.run(args);
    }

}
