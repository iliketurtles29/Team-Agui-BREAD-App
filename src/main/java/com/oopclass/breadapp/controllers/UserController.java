package com.oopclass.breadapp.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.oopclass.breadapp.config.StageManager;
import com.oopclass.breadapp.models.User;
import com.oopclass.breadapp.services.impl.UserService;
import com.oopclass.breadapp.views.FxmlView;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 * OOP Class 20-21
 *
 * @author Gerald Villaran
 */
@Controller
public class UserController implements Initializable {

    @FXML
    private Label userId;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private DatePicker dob;

    @FXML
    private ToggleGroup gender;

    @FXML
    private RadioButton rbFemale;

    @FXML
    private RadioButton rbMale;
    
    @FXML
    private RadioButton rbPrivate;

    @FXML
    private Button reset;

    @FXML
    private Button saveUser;
      
    @FXML
    private Button deletePar;
    
    @FXML
    private TextField address;

    @FXML
    private Button back;  
    
    @FXML
    private RadioButton rbMem;

    @FXML
    private ToggleGroup status;
    
    @FXML
    private RadioButton rbSes;
    
    @FXML
    private TextField filterField;
    
    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, Long> colUserId;

    @FXML
    private TableColumn<User, String> colFirstName;

    @FXML
    private TableColumn<User, String> colLastName;

    @FXML
    private TableColumn<User, LocalDate> colDOB;

    @FXML
    private TableColumn<User, String> colGender;
    
    @FXML
    private TableColumn<User, String> colAddress;

    @FXML
    private TableColumn<User, ComboBox> colSchedule;
    
    @FXML
    private TableColumn<User, String> colStatus;

    @FXML
    private TableColumn<User, Boolean> colEdit;
    
    @FXML
    private Label time;
    
    @FXML
    private ComboBox<String> comb;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private UserService userService;

    private ObservableList<User> userList = FXCollections.observableArrayList();

////    @FXML
////    private void exit(ActionEvent event) {
////        Platform.exit();
////    }
//
    @FXML
    void reset(ActionEvent event) {
        clearFields();
    }
    
    @FXML
    void back(ActionEvent event) {
        stageManager.switchScene(FxmlView.MAINMENU);
    }
    
    @FXML
    private void saveUser(ActionEvent event) {

        if (validate("First Name", getFirstName(), "([a-zA-Z]{3,30}\\s*)+")
                && validate("Last Name", getLastName(), "([a-zA-Z]{3,30}\\s*)+")
                && validate("Address", getAddress(), "([a-zA-Z]\\s*)+")
                && emptyValidation("Date of Birth", dob.getEditor().getText().isEmpty()))
        {

            if (userId.getText() == null || "".equals(userId.getText())) {
                if (true) {

                    User user = new User();
                    user.setFirstName(getFirstName());
                    user.setLastName(getLastName());
                    user.setDob(getDob());
                    user.setGender(getGender());
                    user.setAddress(getAddress());
                    user.setSchedule(getSchedule());    
                    user.setStatus(getStatus());

                    User newUser = userService.save(user);

                    saveAlert(newUser);
                }

            } else {
                User user = userService.find(Long.parseLong(userId.getText()));
                user.setFirstName(getFirstName());
                user.setLastName(getLastName());
                user.setDob(getDob());
                user.setGender(getGender());
                user.setAddress(getAddress());
                user.setSchedule(getSchedule());
                user.setStatus(getStatus());
                
                User updatedUser = userService.update(user);
                updateAlert(updatedUser);
            }

            clearFields();
            loadUserDetails();
            search_user();
        }

    }
    private void clearFields() {
        userId.setText(null);
        firstName.clear();
        lastName.clear();
        dob.getEditor().clear();
        rbMale.setSelected(true);
        rbFemale.setSelected(false);
        address.clear();
        comb.valueProperty().set(null);
        rbMem.setSelected(false);
        rbSes.setSelected(false);
        rbPrivate.setSelected(false);
    }

    private void saveAlert(User user) {

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Trainer saved successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The trainer " + user.getFirstName() + " " + user.getLastName() + " has been created and \n" + getGenderTitle(user.getGender()) + " ID is " + user.getId() + ".");
        alert.showAndWait();
    }

    private void updateAlert(User user) {

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Trainer updated successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The trainer " + user.getFirstName() + " " + user.getLastName() + " has been updated.");
        alert.showAndWait();
    }

    private String getGenderTitle(String gender) {
        return (gender.equals("Male")) ? "his" : "her";
    }
    
    private String getStatus(){
        return (rbMem.isSelected() ? "Membership" :
                rbSes.isSelected() ? "Session" : "Private consultation" );
    }

    public String getFirstName() {
        return firstName.getText();
    }

    public String getLastName() {
        return lastName.getText();
    }

    public LocalDate getDob() {
        return dob.getValue();
    }

    public String getGender() {
        return rbMale.isSelected() ? "Male" : "Female";
    }
    
    public String getAddress() {
        return address.getText();
    }
    
    public String getSchedule(){
        return comb.getValue();
    }


//    /*
//	 *  Set All userTable column properties
//     */
    private void setColumnProperties() {
        /* Override date format in table
		 * colDOB.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<LocalDate>() {
			 String pattern = "dd/MM/yyyy";
			 DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
		     @Override 
		     public String toString(LocalDate date) {
		         if (date != null) {
		             return dateFormatter.format(date);
		         } else {
		             return "";
		         }
		     }

		     @Override 
		     public LocalDate fromString(String string) {
		         if (string != null && !string.isEmpty()) {
		             return LocalDate.parse(string, dateFormatter);
		         } else {
		             return null;
		         }
		     }
		 }));*/

        colUserId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colDOB.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colSchedule.setCellValueFactory(new PropertyValueFactory<>("schedule"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colEdit.setCellFactory(cellFactory);
    }
    
    @FXML
    private void deletePar(ActionEvent event) {
        List<User> users = userTable.getSelectionModel().getSelectedItems();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Delete selected item?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            userService.deleteInBatch(users);
        }
        loadUserDetails();
        search_user();
        clearFields();
    }


    Callback<TableColumn<User, Boolean>, TableCell<User, Boolean>> cellFactory
            = new Callback<TableColumn<User, Boolean>, TableCell<User, Boolean>>() {
        @Override
        public TableCell<User, Boolean> call(final TableColumn<User, Boolean> param) {
            final TableCell<User, Boolean> cell = new TableCell<User, Boolean>() {
                Image imgEdit = new Image(getClass().getResourceAsStream("/images/edit.png"));
                final Button btnEdit = new Button();

                @Override
                public void updateItem(Boolean check, boolean empty) {
                    super.updateItem(check, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        btnEdit.setOnAction(e -> {
                            User user = getTableView().getItems().get(getIndex());
                            updateUser(user);
                        });

                        btnEdit.setStyle("-fx-background-color: transparent;");
                        ImageView iv = new ImageView();
                        iv.setImage(imgEdit);
                        iv.setPreserveRatio(true);
                        iv.setSmooth(true);
                        iv.setCache(true);
                        btnEdit.setGraphic(iv);

                        setGraphic(btnEdit);
                        setAlignment(Pos.CENTER);
                        setText(null);
                    }
                }

                private void updateUser(User user) {
                    if (user.getStatus().equals("Membership")) {
                        rbMem.setSelected(true);
                    } else if(user.getStatus().equals("Session")) {
                        rbSes.setSelected(true);
                    } else{
                        rbPrivate.setSelected(true);
                    }
                    userId.setText(Long.toString(user.getId()));
                    firstName.setText(user.getFirstName());
                    lastName.setText(user.getLastName());
                    dob.setValue(user.getDob());
                    address.setText(user.getAddress());
                    comb.setValue(user.getSchedule());
                    if (user.getGender().equals("Male")) {
                        rbMale.setSelected(true);
                    } else {
                        rbFemale.setSelected(true);
                    }
                   
                }
            };
            return cell;
        }
    };

    /*
	 *  Add All users to observable list and update table
     */
    private void loadUserDetails() {
        userList.clear();
        userList.addAll(userService.findAll());

        userTable.setItems(userList);
    }

    /*
	 * Validations
     */
    private boolean validate(String field, String value, String pattern) {
        if (!value.isEmpty()) {
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(value);
            if (m.find() && m.group().equals(value)) {
                return true;
            } else {
                validationAlert(field, false);
                return false;
            }
        } else {
            validationAlert(field, true);
            return false;
        }
    }

    private boolean emptyValidation(String field, boolean empty) {
        if (!empty) {
            return true;
        } else {
            validationAlert(field, true);
            return false;
        }
    }

    private void validationAlert(String field, boolean empty) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        if (field.equals("Role")) {
            alert.setContentText("Please select " + field + ".");
        } else {
            if (empty) {
                alert.setContentText("Please enter " + field + ".");
            } else {
                alert.setContentText("Please enter valid " + field + ".");
            }
        }
        alert.showAndWait();
    }
    
    @FXML
    void search_user(){
        
        colUserId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colDOB.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colSchedule.setCellValueFactory(new PropertyValueFactory<>("schedule"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        userTable.setItems(userList);
        FilteredList<User> filteredData = new FilteredList<>(userList, b -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
        filteredData.setPredicate(person -> {
                        if(newValue == null || newValue.isEmpty()){
                                        return true;
                            }
                                String lowerCaseFilter = newValue.toLowerCase();
                                    
                        if (person.getFirstName().toLowerCase().indexOf(lowerCaseFilter) != -1){
                            return true;
                        } else if (person.getLastName().toLowerCase().indexOf(lowerCaseFilter) != -1){
                            return true;
                        } else if (person.getGender().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        } else if (person.getAddress().toLowerCase().indexOf(lowerCaseFilter) != -1){
                            return true;
                        } else if (person.getStatus().toLowerCase().contains(lowerCaseFilter)){
                            return true;
                        }
                         else
                            return false;
                                    
                });
                                    
        });
        
        SortedList<User> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(userTable.comparatorProperty());
        userTable.setItems(sortedData);
        
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
    void mini(MouseEvent event) {
        Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
        s.setIconified(true);
    }
                
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e ->  
         time.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy hh:mm:ss a")))),
         new KeyFrame(Duration.seconds(1)));
              clock.setCycleCount(Animation.INDEFINITE);
              clock.play();
    
        ObservableList<String> list = FXCollections.observableArrayList("6:00 AM - 8:00 AM", "8:00 AM - 10:00 AM", "10:00 AM - 12:00 PM", "1:00 PM - 3:00 PM", "3:00 PM - 5:00 PM", "5:00 PM - 7:00 PM");
        comb.setItems(list);

        userTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        setColumnProperties();

//         Add all users into table
        loadUserDetails();
        search_user();
        
        
        
    }
    
}
