package javafiles.fxmlcomponents;

import javafiles.managers.CalendarManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.YearMonth;
import java.time.LocalDate;
import java.util.Calendar;

public class DaysInMonth {

    private int nrofdays;
    private int month;
    private int year;
    @FXML private GridPane grid;
    @FXML private VBox vbox;
    private CalendarManager manager;


    public GridPane getGrid() {
        return grid;
    }
    public VBox getVbox() {
        return vbox;
    }

       public  DaysInMonth(MonthFXML month,int nr)
        {
            nrofdays = month.getnrofdays();
           // this.grid=creategrid();
            vbox=create_box(nr);

        }

        private GridPane creategrid()
        {

            GridPane grid = new GridPane();
          //  grid.getStylesheets().add(getClass().getResource("D:\\an2\\oop\\ExpenseTracker\\src\\main\\resources\\gridhover.css").toExternalForm());
            grid.setHgap(30);
            grid.setVgap(30);
            int day=1;
            for (int row = 0; day <= nrofdays; row++) {
                for (int col = 0; col < 7 && day <= nrofdays; col++) {

                    VBox backgroundPane = new VBox();
                    backgroundPane.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 30");

                    Text daytext =new Text(String.valueOf(day));
                    daytext.setStyle("-fx-font: 20 arial;");
                  //  daytext.getStyleClass().add("day-cell");
                    backgroundPane.setOnMouseEntered(event -> backgroundPane.setStyle("-fx-background-color: lightblue;-fx-padding: 30"));
                    backgroundPane.setOnMouseExited(event -> backgroundPane.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 30"));
                    backgroundPane.getChildren().add(daytext);
                    grid.add(backgroundPane, col, row);
                    day++;
                }
            }
            return grid;
        }
        private VBox create_box(int nr)
        {
            VBox box = new VBox();
            box.setStyle("-fx-background-color: #f4f4f4;-fx-padding: 30");
            Text daytext =new Text(String.valueOf(nr));
            daytext.setStyle("-fx-font: 20 arial;");
            box.setOnMouseEntered(event -> box.setStyle("-fx-background-color: lightblue;-fx-padding: 30"));
            box.setOnMouseExited(event -> box.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 30"));
            box.getChildren().add(daytext);



            return box;
        }


}
