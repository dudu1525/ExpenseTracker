package javafiles.controllers;


import javafiles.interfaces.InitStage;
import javafiles.managers.CreditentialsManager;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class CreateAccController extends CreditentialsManager implements InitStage {
    private String path = getPath();
    private String user = getUser();
    private String password = getPassword();
    private Connection con;
    @FXML
    private Stage stage;
    private LoginController lc = new LoginController();

    @FXML TextField txt1;
    @FXML PasswordField pass1;
    @FXML PasswordField pass2;
    @FXML Button createbutton;


    public void setStage(Stage stage) {  //get the stage for the loader
        this.stage = stage;
        txt1.getStyleClass().add("textfield");
        pass1.getStyleClass().add("textfield");
        pass2.getStyleClass().add("textfield");
        createbutton.getStyleClass().add("button");

    }
    @FXML
    public void gotoLogin() throws IOException { //on button pressed update the page to the history one

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/login_window.fxml"));
        Scene loginScene = new Scene(loader.load(), 900,650);
        LoginController loginController = loader.getController();
        loginController.setStage(stage);


        this.stage.setScene(loginScene);

    }

    @FXML
    private void trycreate()
    {
        try{

            con= DriverManager.getConnection(path, user, password);
            String username=txt1.getText();
            String password1=pass1.getText();
            String password2=pass2.getText();
            String sqlquerry="insert into users (username,password) values (?,?)";
            PreparedStatement ps=con.prepareStatement(sqlquerry);
            if (username.length()>=2 && password1.equals(password2) && password1.length()>5)
            {
                String temppass=lc.passtoothers(password1,lc.getSalt());
                ps.setString(1, username);
                ps.setString(2, temppass);
                int rowsAffected = ps.executeUpdate();
                System.out.print("success");
               gotoLogin();
            }
            else {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Username or password are too short");
                alert.showAndWait();
                System.out.println("user or password incorrect, or some error");
            }


        } catch (Exception e) {
            System.out.println("Unable to create account, sql problem?");
        }

    }



}
