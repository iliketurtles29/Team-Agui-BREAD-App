package com.oopclass.breadapp.views;

import java.util.ResourceBundle;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * OOP Class 20-21
 *
 * @author Gerald Villaran
 */
public enum FxmlView {

    USER {
        @Override
		public String getTitle() {
            return getStringFromResourceBundle("user.title");
        }

        @Override
		public String getFxmlFile() {
            return "/fxml/User.fxml";
        }
    },
    REPORT {
        @Override
		public String getTitle() {
            return getStringFromResourceBundle("report.title");
        }

        @Override
		public String getFxmlFile() {
            return "/fxml/Report.fxml";
        }
    },
    LOGIN {
        @Override
		public String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
		public String getFxmlFile() {
            return "/fxml/Login.fxml";
        }
    },
    INSTRUCTOR {
        @Override
		public String getTitle() {
            return getStringFromResourceBundle("instructor.title");
        }

        @Override
		public String getFxmlFile() {
            return "/fxml/Instructor.fxml";
        }
    },
    PAYMENT {
        @Override
		public String getTitle() {
            return getStringFromResourceBundle("payment.title");
        }

        @Override
		public String getFxmlFile() {
            return "/fxml/Payment.fxml";
        }
    },
    MAINMENU {
        @Override
		public String getTitle() {
            return getStringFromResourceBundle("mainmenu.title");
        }

        @Override
		public String getFxmlFile() {
            return "/fxml/MainMenu.fxml";
        }
    };

    public abstract String getTitle();

    public abstract String getFxmlFile();

    String getStringFromResourceBundle(String key) {
        return ResourceBundle.getBundle("Bundle").getString(key);
    }

}
