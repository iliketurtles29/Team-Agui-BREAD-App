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
import com.oopclass.breadapp.models.Payment;
import com.oopclass.breadapp.services.impl.PaymentService;
import com.oopclass.breadapp.views.FxmlView;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
public class PaymentController implements Initializable{
  
    @Autowired
    private PaymentService paymentService;

    private ObservableList<Payment> paymentList = FXCollections.observableArrayList();
    
    @FXML
    private Label userId;
    
    @FXML
    private ComboBox<String> comb;
    
    @FXML
    private TextField filterField;

    @FXML
    private Label time;

    @FXML
    private DatePicker paydate;

    @FXML
    private TextField amount;

    @FXML
    private Button saveUser;

    @FXML
    private Button reset;

    @FXML
    private Button back;
    
    @FXML
    private Button deletePar;

    @FXML
    private TableView<Payment> paymentTable;

    @FXML
    private TableColumn<Payment, Long> colUserId;

    @FXML
    private TableColumn<Payment, String> colFullName;

    @FXML
    private TableColumn<Payment, LocalDate> colpaydate;

    @FXML
    private TableColumn<Payment, Double> colAmount;

    @FXML
    private TableColumn<Payment, Boolean> colEdit;

    @FXML
    void back(ActionEvent event) {
        stageManager.switchScene(FxmlView.MAINMENU);
    }
    
    public String getTrainee() {
        return comb.getValue();
    }

    public LocalDate getPaydate() {
        return paydate.getValue();
    }
    
    private Double getAmount(){
        return Double.valueOf(amount.getText());
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
    
    private void clearFields() {
        userId.setText(null);
        comb.valueProperty().set(null);
        paydate.getEditor().clear();
        amount.clear();
        
    }
    
    @FXML
    void deletePar(ActionEvent event) {
        List<Payment> users = paymentTable.getSelectionModel().getSelectedItems();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Delete selected item?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {
            paymentService.deleteInBatch(users);
        }
        loadUserDetails();
        search_user();
        clearFields();
    }
    
    private void saveAlert(Payment payment) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Payment saved successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The payment for " + payment.getTrainee() + " has been created" +  ".");
        alert.showAndWait();
    }
    
    private void updateAlert(Payment payment) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Payment updated successfully.");
        alert.setHeaderText(null);
        alert.setContentText("The payment for " + payment.getTrainee() + " has been updated.");
        alert.showAndWait();
    }
    
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
        colFullName.setCellValueFactory(new PropertyValueFactory<>("trainee"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colpaydate.setCellValueFactory(new PropertyValueFactory<>("paydate"));
        colEdit.setCellFactory(cellFactory);
    }
        
    Callback<TableColumn<Payment, Boolean>, TableCell<Payment, Boolean>> cellFactory
            = new Callback<TableColumn<Payment, Boolean>, TableCell<Payment, Boolean>>() {
        @Override
        public TableCell<Payment, Boolean> call(final TableColumn<Payment, Boolean> param) {
            final TableCell<Payment, Boolean> cell = new TableCell<Payment, Boolean>() {
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
                            Payment payment = getTableView().getItems().get(getIndex());
                            updateUser(payment);
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

                private void updateUser(Payment payment) {
                    userId.setText(Long.toString(payment.getId()));
                    comb.setValue(payment.getTrainee());
                    paydate.setValue(payment.getPaydate());
                    amount.setText(Double.toString(payment.getAmount()));                
                }
                
            };
            return cell;
        }
    };

    /*
	 *  Add All users to observable list and update table
     */
    private void loadUserDetails() {
        paymentList.clear();
        paymentList.addAll(paymentService.findAll());

        paymentTable.setItems(paymentList);
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
        colpaydate.setCellValueFactory(new PropertyValueFactory<>("paydate"));
        colFullName.setCellValueFactory(new PropertyValueFactory<>("trainee"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        
        paymentTable.setItems(paymentList);
        FilteredList<Payment> filteredData = new FilteredList<>(paymentList, b -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
        filteredData.setPredicate(person -> {
                                    if(newValue == null || newValue.isEmpty()){
                                        return true;
                                    }
                                    String lowerCaseFilter = newValue.toLowerCase();
                                    
                                    if (person.getTrainee().toLowerCase().contains(lowerCaseFilter)){
                                        return true;
                                    }
                                    else if(person.getPaydate().toString().contains(lowerCaseFilter)){
                                        return true;
                                    } 
                                     else
                                       return false;
                                    
                });
                                    
        });
        
        SortedList<Payment> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(paymentTable.comparatorProperty());
        paymentTable.setItems(sortedData);
        
        
    }

    @FXML
    void mini(MouseEvent event) {
        Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
        s.setIconified(true);
    }

    @FXML
    void reset(ActionEvent event) {
        clearFields();
    }

    @FXML
    void saveUser(ActionEvent event) {
        
            if (userId.getText() == null || "".equals(userId.getText())) {
                if (true) {

                    Payment payment = new Payment();
                    payment.setTrainee(getTrainee());
                    payment.setPaydate(getPaydate());
                    payment.setAmount(getAmount());

                    Payment newPayment = paymentService.save(payment);

                    saveAlert(newPayment);
                }

            } else {
                Payment payment = paymentService.find(Long.parseLong(userId.getText()));
                payment.setTrainee(getTrainee());
                payment.setPaydate(getPaydate());
                payment.setAmount(getAmount());
                
                Payment updatedPayment = paymentService.update(payment);
                updateAlert(updatedPayment);
            }

            clearFields();
            loadUserDetails();
            search_user();
        
    }
    
    @Lazy
    @Autowired
    private StageManager stageManager;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/breadapp", "root", "");
          
        ResultSet rs = con.createStatement().executeQuery( "SELECT last_name, first_name from users WHERE status = 'Membership' OR status = 'Private Consultation'");
        ObservableList userList = FXCollections.observableArrayList();
        while(rs.next()){ 
            
            userList.add((rs.getString(1))+ ", " + rs.getString(2));
            
        }
        comb.setItems(userList);
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

        paymentTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        setColumnProperties();

//         Add all users into table
        loadUserDetails();
        search_user();
    } 
    }
   
