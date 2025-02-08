package javafiles.fxmlcomponents;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.YearMonth;
import java.time.LocalDate;
public class MonthFXML extends Pane {

    private  String name;
    private  int monthIndex;
    private  VBox monthView;
    private int nrofdays;
    private String year;

    public MonthFXML(String name, int monthIndex, String year) {
        this.name = name;
        this.monthIndex = monthIndex;
        this.year = year;
        this.monthView = createMonthView();


    }
    public VBox getMonthView() {
        return monthView;
    }
    public String getName() {
        return name;
    }
    public int getnrofdays() {
        return nrofdays;
    }
    public String getYear() {
        return year;
    }
    public int getMonthIndex() {
        return monthIndex;
    }

    private VBox createMonthView() {

        VBox vbox=new VBox(5); //create a vbox of padding 5pixels
        Label monthLabel = new Label(name);
        monthLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16; -fx-text-fill: black;"); //set label

       //grid for putting the days of the month
        GridPane daysGrid = new GridPane();
        daysGrid.setHgap(5);
        daysGrid.setVgap(5);
        populateDaysGrid(daysGrid);


        vbox.getChildren().addAll(monthLabel, daysGrid);
        return vbox;
    }

    private void populateDaysGrid(GridPane daysGrid) {
        int yearint=Integer.parseInt(year);
        YearMonth yearMonth = YearMonth.of(yearint, monthIndex);

        //YearMonth yearMonth = YearMonth.of(java.time.LocalDate.now().getYear(), monthIndex);
        int daysInMonth = yearMonth.lengthOfMonth(); //get days of the month index
        nrofdays=daysInMonth;
        int day = 1; //populate the days grid
        for (int row = 0; day <= daysInMonth; row++) {
            for (int col = 0; col < 7 && day <= daysInMonth; col++) {
                Text daytext =new Text(String.valueOf(day));
                daysGrid.add(daytext, col, row);
                day++;
            }
        }
    }




}
