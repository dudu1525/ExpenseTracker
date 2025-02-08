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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class LoginController extends CreditentialsManager implements Initializable,InitStage {
    private String path = getPath();
    private String user =getUser();
    private String password = getPassword();
    private Connection con;
    private String salt="parola";
    private String hashpass;

    public String getSalt()
    {
        return salt;
    }


    @FXML
    private PasswordField passfield;

    @FXML
    private TextField userfield;

    @FXML
    private Button loginbutton;

    @FXML CheckBox checkremember;

    @FXML
    private Stage stage;

    @FXML Text invistext;



    public void setStage(Stage stage)  {  //get the stage for the loader
        this.stage = stage;
        loginbutton.getStyleClass().add("button");
        userfield.getStyleClass().add("textfield");
        passfield.getStyleClass().add("textfield");


    }

    @FXML
    public void gotoMain(int id) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/main_window.fxml"));
        Scene mainscene = new Scene(loader.load(), 900,650);
        MainController mainController = loader.getController();
        mainController.mainManager.setUser_id(id);
        System.out.println(mainController.mainManager.getUser_id());
        mainController.mainManager.init();
       mainController.updateChart(mainController.mainManager.date);
        mainController.setStage(stage);
        this.stage.setScene(mainscene);

    }

    @FXML
    public void gotoCreate() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/create_acc_window.fxml"));
        Scene loginScene = new Scene(loader.load(), 900,650);
        CreateAccController loginController = loader.getController();
        loginController.setStage(stage);
        this.stage.setScene(loginScene);

    }

    private  String gethashpassword(String password, String salt)  {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] hashedPassword = md.digest(password.getBytes());

            return Base64.getEncoder().encodeToString(hashedPassword);
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }
    public String passtoothers(String password, String salt)
    {
        return gethashpassword(password,salt);
    }

    public void initlogin() {
        try {
            con = DriverManager.getConnection(path, user, password);
            String user = userfield.getText();
            String pass = passfield.getText();
            hashpass=gethashpassword(pass,salt);

            //numai ii dai encrypt si in rest la fel?
            String sqlquerry = "select id from users where username=? and password=?";
            PreparedStatement ps = con.prepareStatement(sqlquerry);
            ps.setString(1, user);
            ps.setString(2, hashpass);

            try (ResultSet rs = ps.executeQuery()) {//if an id was found with the user and password entered, go to main
                if (rs.next()) {
                            int id = rs.getInt("id");
                            saveRememberMePreferences();
                    gotoMain(id);

                }
                else//if not
                {System.out.println("password or user incorrect");
                    PauseTransition pt=new PauseTransition(Duration.seconds(2));
                    invistext.setVisible(true);
                    pt.setOnFinished(event -> invistext.setVisible(false));
                    pt.play();
                }
                  } catch (SQLException e) {
                System.out.println("password or user error");
                  }


    }catch(Exception e)

    {
        System.out.println("login error");
    }


    }



    @FXML
    void handleRememberUser(ActionEvent event) {
        saveRememberMePreferences();
    }

    private void saveRememberMePreferences() {
        Preferences pref = Preferences.userRoot();

        if (checkremember.isSelected()) {
            // save username and remember me state
            pref.put("username", userfield.getText());
            pref.putBoolean("rememberMe", true);
        } else {

            pref.remove("username");
            pref.putBoolean("rememberMe", false);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Preferences pref = Preferences.userRoot();

        // set username field with stored username
        userfield.setText(pref.get("username", ""));

        // set checkbox state based on saved preferences
        checkremember.setSelected(pref.getBoolean("rememberMe", false));
    }


}
