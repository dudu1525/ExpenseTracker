package javafiles;


import com.sun.tools.javac.Main;
import javafiles.controllers.LoginController;
import javafiles.controllers.MainController;
import javafiles.managers.MainManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWind extends Application {
        MainManager mainManager = new MainManager();
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(MainWind.class.getResource("/fxmls/login_window" +
                ".fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 650);
       // MainController controller = fxmlLoader.getController();
        LoginController controller = fxmlLoader.getController();
         controller.setStage(stage);

        stage.setTitle("Expense Tracker");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}

//in the constructor of the secondary page controller, pass as arguments as an int to load the different components of the secondary page
//or somwhere call the some functions in the main function