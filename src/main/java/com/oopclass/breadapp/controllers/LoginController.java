package com.oopclass.breadapp.controllers;

import org.springframework.stereotype.Controller;
import com.oopclass.breadapp.config.StageManager;
import com.oopclass.breadapp.views.FxmlView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
//import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;


/**
 *
 * @author alvin
 */
@Controller
public class LoginController  {
  
     @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button Login;
    
    @FXML
    private CheckBox checkBox;
    
    @FXML
    private TextField passwordText;
    
    
    @Lazy
    @Autowired
    private StageManager stageManager;
    
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
    void mini(MouseEvent event) {
        Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
        s.setIconified(true);
    }
    
    
    
    @FXML
    void changedVisibility(ActionEvent event) {
        if(checkBox.isSelected()){
            passwordText.setText(password.getText());
            passwordText.setVisible(true);
            password.setVisible(false);
            return;
        }
        else{
            password.setText(passwordText.getText());
            password.setVisible(true);
            passwordText.setVisible(false);
        }
    }
    
    @FXML
    private void Loginact(ActionEvent event){
        if("Admin".equals(username.getText()) && "Password".equals(password.getText()))
        {
           stageManager.switchScene(FxmlView.MAINMENU);
        }
        else
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Login Failed ");
            alert.setHeaderText(null);
            alert.setContentText(" Incorrect username or password, please try again. ");
            alert.showAndWait();
        }
    }   
}
