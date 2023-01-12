package com.oopclass.breadapp.controllers;

import com.oopclass.breadapp.config.StageManager;
import com.oopclass.breadapp.models.Instructor;
import com.oopclass.breadapp.services.impl.InstructorService;
import com.oopclass.breadapp.views.FxmlView;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

/**
 * FXML Controller class
 *
 * @author alvin
 */
@Controller
public class InstructorController implements Initializable {
    
    @FXML
    private Label userId;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private DatePicker dob;

    @FXML
    private RadioButton rbFemale;

    @FXML
    private RadioButton rbMale;

    @FXML
    private Button reset;

    @FXML
    private Button saveUser;
      
    @FXML
    private Button deletePar;
    
    @FXML
    private ToggleGroup classtype;
    
    @FXML
    private RadioButton rbYoga;

    @FXML
    private RadioButton rbZumba;
    
    @FXML
    private RadioButton rbCF;
    
    @FXML
    private ToggleGroup genderist;
    
    @FXML
    private TextField address;

    @FXML
    private TextField number;

    @FXML
    private Button back;  
    
    @FXML
    private TextField filterField;
    
    @FXML
    private TableView<Instructor> instructorTable;

    @FXML
    private TableColumn<Instructor, Long> colUserId;

    @FXML
    private TableColumn<Instructor, String> colFirstName;

    @FXML
    private TableColumn<Instructor, String> colLastName;

    @FXML
    private TableColumn<Instructor, LocalDate> colDOB;

    @FXML
    private TableColumn<Instructor, String> colGender;
    
    @FXML
    private TableColumn<Instructor, String> colAddress;

    @FXML
    private TableColumn<Instructor, Long> colNumber;
    
    @FXML
    private TableColumn<Instructor, String> colClass;

    @FXML
    private TableColumn<Instructor, Boolean> colEdit;
    
    @FXML
    private Label time;
    
    

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private InstructorService instructorService;

    private ObservableList<Instructor> instructorList = FXCollections.observableArrayList();

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
                && validate("Mobile Number", number.getText(), "[0-9]*")
                && emptyValidation("Mobile Number", number.getText().isEmpty())
                && emptyValidation("Date of Birth", dob.getEditor().getText().isEmpty()))
        {

            if (userId.getText() == null || "".equals(userId.getText())) {
                if (true) {

                    Instructor instructor = new Instructor();
                    instructor.setFirstName(getFirstName());
                    instructor.setLastName(getLastName());
                    instructor.setDob(getDob());
                    instructor.setGender(getGender());
                    instructor.setAddress(getAddress());
                    instructor.setNumber(getNumber());
                    instructor.setGymClass(getGymClass());

                    Instructor newInstructor = instructorService.save(instructor);

                    saveAlert(newInstructor);
                }

            } else {
                Instructor instructor = instructorService.find(Long.parseLong(userId.getText()));
                instructor.setFirstName(getFirstName());
                instructor.setLastName(getLastName());
                instructor.setDob(getDob());
                instructor.setGender(getGender());
                instructor.setAddress(getAddress());
                instructor.setNumber(getNumber());
                instructor.setGymClass(getGymClass());
                
                Instructor updatedInstructor = instructorService.update(instructor);
                updateAlert(updatedInstructor);
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
        number.clear();
        rbCF.setSelected(true);
        rbYoga.setSelected(false);
        rbZumba.setSelected(false);
//        gymClass.clear();
    }

    private void saveAlert(Instructor instructor) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Instructor saved successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The instructor " + instructor.getFirstName() + " " + instructor.getLastName() + " has been created and \n" + getGenderTitle(instructor.getGender()) + " ID is " + instructor.getId() + ".");
        alert.showAndWait();
    }

    private void updateAlert(Instructor instructor) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Instructor updated successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The instructor " + instructor.getFirstName() + " " + instructor.getLastName() + " has been updated.");
        alert.showAndWait();
    }

    private String getGenderTitle(String gender) {
        return (gender.equals("Male")) ? "his" : "her";
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
    
    private long getNumber(){
        return Long.parseLong(number.getText());
    }
    
    public String getGymClass() {
        return rbCF.isSelected() ? "Cross-Fit" :        
               rbYoga.isSelected() ? "Yoga" : "Zumba";
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
        colNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
        colClass.setCellValueFactory(new PropertyValueFactory<>("gymClass"));
        colEdit.setCellFactory(cellFactory);
    }
    
    @FXML
    private void deletePar(ActionEvent event) {
        List<Instructor> instructor = instructorTable.getSelectionModel().getSelectedItems();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Delete selected item?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            instructorService.deleteInBatch(instructor);
        }
        loadUserDetails();
        clearFields();
        search_user();
    }


    Callback<TableColumn<Instructor, Boolean>, TableCell<Instructor, Boolean>> cellFactory
            = new Callback<TableColumn<Instructor, Boolean>, TableCell<Instructor, Boolean>>() {
        @Override
        public TableCell<Instructor, Boolean> call(final TableColumn<Instructor, Boolean> param) {
            final TableCell<Instructor, Boolean> cell = new TableCell<Instructor, Boolean>() {
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
                            Instructor instructor = getTableView().getItems().get(getIndex());
                            updateUser(instructor);
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

                private void updateUser(Instructor instructor) {
                    if(instructor.getGymClass().equals("Cross-Fit")){
                        rbCF.setSelected(true);
                    }
                    else if(instructor.getGymClass().equals("Yoga")){
                        rbYoga.setSelected(true);
                    }
                    else{
                        rbZumba.setSelected(true);
                    }
                    userId.setText(Long.toString(instructor.getId()));
                    firstName.setText(instructor.getFirstName());
                    lastName.setText(instructor.getLastName());
                    dob.setValue(instructor.getDob());
                    address.setText(instructor.getAddress());
                    number.setText(Long.toString(instructor.getNumber()));
                    if (instructor.getGender().equals("Male")) {
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
        instructorList.clear();
        instructorList.addAll(instructorService.findAll());

        instructorTable.setItems(instructorList);
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
        Alert alert = new Alert(Alert.AlertType.WARNING);
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
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colClass.setCellValueFactory(new PropertyValueFactory<>("gymClass"));
        
        instructorTable.setItems(instructorList);
        FilteredList<Instructor> filteredData = new FilteredList<>(instructorList, b -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
        filteredData.setPredicate(person -> {
                                    if(newValue == null || newValue.isEmpty()){
                                        return true;
                                    }
                                    String lowerCaseFilter = newValue.toLowerCase();
                                    
                                    if (person.getFirstName().toLowerCase().contains(lowerCaseFilter)){
                                        return true;
                                    }else if (person.getLastName().toLowerCase().contains(lowerCaseFilter)){
                                        return true;
                                    }else if (person.getGender().toLowerCase().contains(lowerCaseFilter)){
                                        return true;
                                    } else if (person.getAddress().toLowerCase().contains(lowerCaseFilter)){
                                        return true;
                                    }
                                       else if (person.getGymClass().toLowerCase().contains(lowerCaseFilter)){
                                        return true;
                                       }
                                          else
                                              return false;
                                    
                });
                                    
        });
        
        SortedList<Instructor> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(instructorTable.comparatorProperty());
        instructorTable.setItems(sortedData);
        
        
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
         time.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy hh:mm:ss a")))
    ),
         new KeyFrame(Duration.seconds(1))
    );
    clock.setCycleCount(Animation.INDEFINITE);
    clock.play();

        instructorTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        setColumnProperties();

//         Add all users into table
        loadUserDetails();
        search_user();
    } 
    
}
