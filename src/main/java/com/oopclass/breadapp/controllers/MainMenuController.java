
package com.oopclass.breadapp.controllers;

import com.oopclass.breadapp.config.StageManager;
import com.oopclass.breadapp.views.FxmlView;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

/**
 *
 * @author alvin
 */
@Controller
public class MainMenuController implements Initializable{
    
    
    
    
    @Lazy
    @Autowired
    private StageManager stageManager;
    
    @FXML
    void addUser(MouseEvent event) {
        stageManager.switchScene(FxmlView.USER);

    }
    
    @FXML
    private Label time;
    
    @FXML
    private Label instructorNo;

    @FXML
    private Label reportNo;

    @FXML
    private Label traineeNo;
    
    
    @FXML
    void addInstructor(MouseEvent event) {
        stageManager.switchScene(FxmlView.INSTRUCTOR);
    }
    
    @FXML
    void addReport(MouseEvent event) {
        stageManager.switchScene(FxmlView.REPORT);
    }
    
    
    
    @FXML
    void back(ActionEvent event) {
    Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to logout" + "?", ButtonType.YES, ButtonType.NO);
    alert.setHeaderText(null);
    alert.showAndWait();

    if (alert.getResult() == ButtonType.YES) {
        stageManager.switchScene(FxmlView.LOGIN);
        }     
    }  
    
    @FXML
    void close(MouseEvent event) {
        Alert alert = new Alert (Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setContentText(" Are you sure you want to exit the application? ");
            if(alert.showAndWait().get() == ButtonType.OK){
                Platform.exit();         
            }
    }
    @FXML
    void addPayment(MouseEvent event) {
        stageManager.switchScene(FxmlView.PAYMENT);
    }

    @FXML
    void mini(MouseEvent event) {
        Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
        s.setIconified(true);
    }

   
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/breadapp", "root", "");
          
        ResultSet rs = con.createStatement().executeQuery( "SELECT COUNT(*) FROM users");
        ResultSet rd = con.createStatement().executeQuery( "SELECT COUNT(*) FROM instructors");
        ResultSet rf = con.createStatement().executeQuery( "SELECT COUNT(*) FROM report");
        while(rs.next() && rd.next() && rf.next()){ 
            int rowCount = rs.getInt(1);
            int rowCountd = rd.getInt(1);
            int rowCountf = rf.getInt(1);
            
            traineeNo.setText(String.valueOf(rowCount));
            instructorNo.setText(String.valueOf(rowCountd));
            reportNo.setText(String.valueOf(rowCountf));
            
        }
                }catch(Exception e){
                    e.printStackTrace();
                }
        


        
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e ->  
         time.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy hh:mm:ss a")))
    ),
         new KeyFrame(Duration.seconds(1))
    );
    clock.setCycleCount(Animation.INDEFINITE);
    clock.play();
    }
}
