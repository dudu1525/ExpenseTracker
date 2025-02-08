package javafiles.controllers;


import javafiles.fxmlcomponents.DaysInMonth;
import javafiles.fxmlcomponents.MonthFXML;
import javafiles.interfaces.InitStage;
import javafiles.managers.CalendarManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.awt.*;
import java.io.IOException;
import java.time.Month;
import java.util.Vector;

public class CalendarController implements InitStage {


   @FXML public ComboBox ybox;
    private Stage stage;
    private MonthFXML[ ] month=new MonthFXML[12];
    private DaysInMonth[] daysmonth =new DaysInMonth[31];

    private Vector<String> items=new Vector<>(100);
    @FXML Button navigatebtn;
    @FXML StackPane stckpane;
    @FXML AnchorPane secondanchor;

    public void setStage(Stage stage) {
        this.stage = stage;
        navigatebtn.setVisible(false);
        secondanchor.setStyle("-fx-border-color: #aaaaaa; " + // Blue border color
                "-fx-border-width: 3; " +      // Border width
                "-fx-border-radius: 10; " +    // Rounded corners
                "-fx-border-style: solid;");


        ybox.getItems().addAll(
                "2022","2023","2024","2025","2026"
        );
        ybox.getSelectionModel().select(2);
        initialize();

    }



    public void goBack() throws IOException {//as in main, when back is pressed go back to main window

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/history_window.fxml"));

        Scene mainscene=new Scene(fxmlLoader.load(), 900,650);
        HistoryController mainController = fxmlLoader.getController();
        mainController.setgraph(false);

        mainController.setStage(stage);
        stage.setScene(mainscene);
    }

    public void goBackOnce() throws IOException {
        //add ifs beased on stackpane current pane
        stckpane.getChildren().clear();
        navigatebtn.setVisible(false);
       initialize();

    }
    public void initialize() {
        Object selectedItem = ybox.getSelectionModel().getSelectedItem();
        String selectedYear = (selectedItem != null) ? selectedItem.toString() : "2024";
        if (selectedItem == null) {
            ybox.getSelectionModel().select(selectedYear);
        }

        GridPane yearView = createYearView(selectedYear); //initialize the 4x3 grid pane for the months
        ybox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
           // System.out.println("Selection changed from " + oldValue + " to " + newValue);
            stckpane.getChildren().clear();
            GridPane yrv2=createYearView(newValue.toString());
            stckpane.getChildren().add(yrv2);
            navigatebtn.setVisible(false);

            navigate();
        });
        stckpane.getChildren().add(yearView);

        navigate();
    }

    private GridPane createYearView(String year) {
        GridPane yearView = new GridPane();
        yearView.setHgap(30); // Gap between mini-calendars
        yearView.setVgap(20);
        yearView.setStyle(" -fx-alignment: center;");

        String[] months = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };

        for (int i = 0; i < 12; i++) {

            //MonthFXML month = new MonthFXML(months[i], i + 1);
            month[i]=new MonthFXML(months[i],i+1,year);
            month[i].getMonthView().getStyleClass().add("grid-cell");
            yearView.add(month[i].getMonthView(), i % 4, i / 4); // 4 rows, 3 columns

        }

        return yearView;
    }
    private void navigate() //navigate the elements of the first stackpane
    {
        for (int i=0;i<12;i++)
        {   final int monthindex=i;

            month[i].getMonthView().setOnMouseClicked(event -> {

                dosmth(month[monthindex]);
            });
        }

    }
    private void navigate2(int nrofdays,MonthFXML month) //navigate the elements of the second stack pane
    {
        for (int i=0;i<nrofdays;i++)
        {   final int daynr=i+1;
            daysmonth[i].getVbox().setOnMouseClicked(event -> {
               dosmth2(daynr,month);
            });
        }
    }

    private void dosmth(MonthFXML month)//create the second layer of gridpane
    {       stckpane.getChildren().clear();
            navigatebtn.setVisible(true);

            VBox days=new VBox();
         Label monthLabel = new Label(month.getName());
        monthLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 22; -fx-text-fill: black; -fx-padding: 10 10 20 30");
        GridPane grid=new GridPane();

        int day=1;

        for (int row = 0; day <= month.getnrofdays(); row++) {
            for (int col = 0; col < 7 && day <=month.getnrofdays(); col++)
            {   daysmonth[day-1]=new DaysInMonth(month,day);
                    VBox temp=daysmonth[day-1].getVbox();
                grid.add(temp,col,row);
                day++;
            }}


            days.getChildren().add(monthLabel);
        days.getChildren().add(grid);


            stckpane.getChildren().add(days);

            navigate2(month.getnrofdays(),month);

    }

    private void dosmth2(int nr,MonthFXML month)
    {
        VBox itemsbox=new VBox(15);

        CalendarManager cm=new CalendarManager(nr,month.getMonthIndex(), month.getYear());
        cm.doActions();
        items.setSize(cm.getNrofitems());
        items=cm.getItems();
        for (int i=0;i<items.size();i++)
        {
            Text tx=new Text(items.get(i));
            tx.setStyle("-fx-font-family: Arial; -fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #333333; -fx-text-align: center; -fx-padding: 10px;" );
            itemsbox.getChildren().add(tx);
        }

        stckpane.getChildren().clear();
        Label monthLabel = new Label();
        if (items.size()==0)
         monthLabel = new Label("There are no expenses for this date");
        stckpane.getChildren().add(itemsbox);
        stckpane.getChildren().add(monthLabel);

    }





}
