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
import com.oopclass.breadapp.models.Instructor;
import com.oopclass.breadapp.models.Report;
import com.oopclass.breadapp.services.impl.ReportService;
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
 *
 * @author alvin
 */

@Controller
public class ReportController implements Initializable  {

    @FXML
    private Button back;
    
    @FXML
    private DatePicker creation;
    
    @FXML
    private TextField filterField;

    @FXML
    private TableColumn<Report, Boolean> colEdit;

    @FXML
    private TableColumn<Report, String> colFirstName;

    @FXML
    private TableColumn<Report, String> colLastName;

    @FXML
    private TableColumn<Report, Long> colNumber;

    @FXML
    private TableColumn<Report, String> colReason;
   
    @FXML
    private TableColumn<Report, String> colStatus;
    
    @FXML
    private TableColumn<Report, LocalDate> colCreation;

    @FXML
    private TableColumn<Report, Long> colUserId;

    @FXML
    private Button deletePar;
    
    @FXML
    private Label userId;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField number;

    @FXML
    private RadioButton rbApproved;

    @FXML
    private RadioButton rbDeclined;
    
    @FXML
    private RadioButton rbUnder;

    @FXML
    private TextField reason;

    @FXML
    private TableView<Report> reportTable;

    @FXML
    private Button reset;

    @FXML
    private Button saveUser;

    @FXML
    private ToggleGroup status;
    
    @FXML
    private Label time;
    
    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private ReportService reportService;
    
    private ObservableList<Report> reportList = FXCollections.observableArrayList();

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
                && validate("Reason", getReason(), "([a-zA-Z]\\s*)+"))
        {

            if (userId.getText() == null || "".equals(userId.getText())) {
                if (true) {

                    Report report = new Report();
                    report.setFirstName(getFirstName());
                    report.setLastName(getLastName());
                    report.setNumber(getNumber());
                    report.setReason(getReason());
                    report.setStatus(getStatus());
                    report.setCreation(getCreation());

                    Report newReport = reportService.save(report);

                    saveAlert(newReport);
                }

            } else {
                Report report = reportService.find(Long.parseLong(userId.getText()));
                report.setFirstName(getFirstName());
                report.setLastName(getLastName());
                report.setNumber(getNumber());
                report.setReason(getReason());
                report.setStatus(getStatus());
                report.setCreation(getCreation());
                
                Report updatedReport = reportService.update(report);
                updateAlert(updatedReport);
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
        rbApproved.setSelected(false);
        rbDeclined.setSelected(false);
        rbUnder.setSelected(true);
        reason.clear();
        number.clear();
    }

    private void saveAlert(Report report) {

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Report saved successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The report made by" + report.getFirstName() + " " + report.getLastName() + " has been created on "+ report.getCreation()  +" and is\n" + getStatusTitle(report.getStatus()) + ".");
        alert.showAndWait();
    }

    private void updateAlert(Report report) {

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Report updated successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The report made by " + report.getFirstName() + " " + report.getLastName() + " has been updated.");
        alert.showAndWait();
    }

    private String getStatusTitle(String status) {
        return (status.equals("Approved")) ? "Approved" :
                (status.equals("Declined")? "Declined" : "Under consideration");
    }

    public String getFirstName() {
        return firstName.getText();
    }

    public String getLastName() {
        return lastName.getText();
    }
    
    public long getNumber(){
        return Long.parseLong(number.getText());
    }

    public String getStatus() {
        return (rbApproved.isSelected() ? "Approved" :
               rbDeclined.isSelected() ? "Declined" : "Under consideration");
    }
    
    public String getReason() {
        return reason.getText();
    }
    
    public LocalDate getCreation() {
        return creation.getValue();
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
        colNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
        colReason.setCellValueFactory(new PropertyValueFactory<>("reason"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colCreation.setCellValueFactory(new PropertyValueFactory<>("creation"));
        colEdit.setCellFactory(cellFactory);
    }
    
    @FXML
    private void deletePar(ActionEvent event) {
        List<Report> report = reportTable.getSelectionModel().getSelectedItems();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Delete selected item?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            reportService.deleteInBatch(report);
        }
        loadUserDetails();
        clearFields();
        search_user();
    }


    Callback<TableColumn<Report, Boolean>, TableCell<Report, Boolean>> cellFactory
            = new Callback<TableColumn<Report, Boolean>, TableCell<Report, Boolean>>() {
        @Override
        public TableCell<Report, Boolean> call(final TableColumn<Report, Boolean> param) {
            final TableCell<Report, Boolean> cell = new TableCell<Report, Boolean>() {
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
                            Report report = getTableView().getItems().get(getIndex());
                            updateUser(report);
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

                private void updateUser(Report report) {
                    userId.setText(Long.toString(report.getId()));
                    firstName.setText(report.getFirstName());
                    lastName.setText(report.getLastName());
                    number.setText(Long.toString(report.getNumber()));
                    reason.setText(report.getReason());
                    if (report.getStatus().equals("Approved")) {
                        rbApproved.setSelected(true);
                    } else if(report.getStatus().equals("Declined")){
                        rbDeclined.setSelected(true);
                    }
                    else{
                        rbUnder.setSelected(true);
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
        reportList.clear();
        reportList.addAll(reportService.findAll());

        reportTable.setItems(reportList);
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
        colNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
        colReason.setCellValueFactory(new PropertyValueFactory<>("reason"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colCreation.setCellValueFactory(new PropertyValueFactory<>("creation"));
        
        reportTable.setItems(reportList);
        FilteredList<Report> filteredData = new FilteredList<>(reportList, b -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
        filteredData.setPredicate(person -> {
                                    if(newValue == null || newValue.isEmpty()){
                                        return true;
                                    }
                                    String lowerCaseFilter = newValue.toLowerCase();
                                    
                                    if (person.getFirstName().toLowerCase().indexOf(lowerCaseFilter) != -1){
                                        return true;
                                    }else if (person.getLastName().toLowerCase().indexOf(lowerCaseFilter) != -1){
                                        return true;
                                    } else if (person.getStatus().toLowerCase().contains(lowerCaseFilter)){
                                        return true;
                                    }
                                    else if (person.getReason().toLowerCase().contains(lowerCaseFilter)){
                                        return true;
                                    }
                                          else
                                              return false;
                                    
                });
                                    
        });
        
        SortedList<Report> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(reportTable.comparatorProperty());
        reportTable.setItems(sortedData);
        
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
        
        creation.setValue(LocalDate.now());
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e ->  
         time.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy hh:mm:ss a")))
    ),
         new KeyFrame(Duration.seconds(1))
    );
    clock.setCycleCount(Animation.INDEFINITE);
    clock.play();

        reportTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        setColumnProperties();

//         Add all users into table
        loadUserDetails();
        search_user();
    }
}
