package javafiles.controllers;

import javafiles.interfaces.InitStage;
import javafiles.managers.HistoryManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Optional;
import java.util.Vector;

public class HistoryController implements InitStage {

    private Stage stage;
    private HistoryManager historyManager=new HistoryManager();
    private static boolean charttype=false;

    @FXML private Button lastweek;
    @FXML private Button lastmonth;
    @FXML private PieChart historychart;
    @FXML  VBox descbox;
    @FXML Text foodtxt,transporttxt,utiltxt,entertxt,othertxt;

    private Vector<String> expensesdesc=new Vector<>(1000);

    public void setStage(Stage stage) {
        this.stage = stage;
        descbox.setSpacing(10);
        descbox.setPadding(new Insets(10,10,10,10));

    }
    public boolean getcharttype()
    {
        return charttype;
    }
    public void setCharttype()
    {
        charttype = !charttype;

    }
    @FXML
    public VBox getDescbox() {
        return descbox;
    }


    public void goBack() throws IOException {//as in main, when back is pressed go back to main window

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/main_window.fxml"));

        Scene mainscene=new Scene(fxmlLoader.load(), 900,650);
        MainController mainController = fxmlLoader.getController();
        mainController.setStage(stage);
        stage.setScene(mainscene);
    }

    public void goCalendar() throws IOException {//as in main, when back is pressed go back to main window

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/calendar_window.fxml"));

        Scene mainscene=new Scene(fxmlLoader.load(), 900,650);
        CalendarController calendarController = fxmlLoader.getController();
        calendarController.setStage(stage);
        stage.setScene(mainscene);
    }
    public void goFilter(LocalDate start, LocalDate end,int id) throws IOException {//as in main, when back is pressed go back to main window

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/filter_window.fxml"));

        Scene mainscene=new Scene(fxmlLoader.load(), 900,650);
        FilterController filterController = fxmlLoader.getController();
        filterController.setDates(start,end);
        filterController.setStage(stage,id);
        stage.setScene(mainscene);
    }

    @FXML
    private void setonweek()
    {
        setgraph(false);
    }
    @FXML
    private void setonmonth()
    {
        setgraph(true);
    }



    @FXML
    public void setgraph(boolean charttype)
    {


        if (charttype==false)
        {//week
            lastweek.getStyleClass().remove("inactive-button");
            lastmonth.getStyleClass().remove("active-button");
            lastweek.getStyleClass().add("active-button");
            lastmonth.getStyleClass().add("inactive-button");

            //lastweek.setStyle("-fx-background-color: cyan;" + "-fx-text-fill: black");
            //lastmonth.setStyle("-fx-background-color: grey;" + "-fx-text-fill: black");

            historyManager.getsumweek();
            foodtxt.setText("Food: "+historyManager.getsumfood() +" lei");
            transporttxt.setText("Transport: "+historyManager.getsumtrans()+" lei");
            utiltxt.setText("Utilities: "+historyManager.getSumutil()+" lei");
            entertxt.setText("Entertainment: "+historyManager.getsumenter()+" lei");
            othertxt.setText("Other: "+historyManager.getsumother()+" lei");
            ObservableList<PieChart.Data> pieChartData =
                    FXCollections.observableArrayList(
                            new PieChart.Data("Food", historyManager.getsumfood()),
                            new PieChart.Data("Transport", historyManager.getsumtrans()),
                            new PieChart.Data("Utilities", historyManager.getSumutil()),
                            new PieChart.Data("Entertainment", historyManager.getsumenter()),
                            new PieChart.Data("Other", historyManager.getsumother()));


            historychart.setData(pieChartData);
            
            expensesdesc.setSize(historyManager.getExpenses().size());
            expensesdesc=historyManager.getExpenses();
            descbox.getChildren().clear();
            //check nr of children before and after, do smth
            for (int i=0;i<expensesdesc.size();i++)
            {
                Text t=new Text(expensesdesc.get(i));
                t.getStyleClass().add("text");

                descbox.getChildren().add(t);
            }




        }
        else
        {//month
            lastweek.getStyleClass().remove("active-button");
            lastmonth.getStyleClass().remove("inactive-button");
            lastweek.getStyleClass().add("inactive-button");
            lastmonth.getStyleClass().add("active-button");

           // lastweek.setStyle("-fx-background-color: grey;" + "-fx-text-fill: black");
            //lastmonth.setStyle("-fx-background-color: cyan;" + "-fx-text-fill: black");
            descbox.getChildren().clear();
            historyManager.getsummonth();
            foodtxt.setText("Food: "+historyManager.getsumfood());
            transporttxt.setText("Transport: "+historyManager.getsumtrans());
            utiltxt.setText("Utilities: "+historyManager.getSumutil());
            entertxt.setText("Entertainment: "+historyManager.getsumenter());
            othertxt.setText("Other: "+historyManager.getsumother());

            ObservableList<PieChart.Data> pieChartData =
                    FXCollections.observableArrayList(
                            new PieChart.Data("Food", historyManager.getsumfood()),
                            new PieChart.Data("Transport", historyManager.getsumtrans()),
                            new PieChart.Data("Utilities", historyManager.getSumutil()),
                            new PieChart.Data("Entertainment", historyManager.getsumenter()),
                            new PieChart.Data("Other", historyManager.getsumother()));


            historychart.setData(pieChartData);
            expensesdesc.setSize(historyManager.getExpenses().size());
            expensesdesc=historyManager.getExpenses();
            descbox.getChildren().clear();
            //check nr of children before and after, do smth
            for (int i=0;i<expensesdesc.size();i++)
            {
                Text t=new Text(expensesdesc.get(i));
                t.getStyleClass().add("text");
                descbox.getChildren().add(t);
            }
        }


    }


    public void opendialog(ActionEvent actionEvent) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Choose filter type");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        ComboBox<String> filter = new ComboBox<>();
        filter.getItems().addAll("Filter by day", "Filter by month", "Filter by exact date");
        VBox dialogContent = new VBox(10);
        dialogContent.getChildren().add(new Label("Choose one filter"));
        dialogContent.getChildren().add(filter);
        dialog.getDialogPane().setContent(dialogContent);
        Optional<ButtonType> result = dialog.showAndWait();

        String selectedType = filter.getValue();

        if (result.isPresent() && result.get() == ButtonType.OK && selectedType != null) {
            if (selectedType.equals("Filter by day")) //go to Calendar
            {
                try {
                    goCalendar();
                } catch (IOException e) {
                    System.out.println("Could not go to calendar, error in branch 1");
                    throw new RuntimeException(e);
                }
            } else if (selectedType.equals("Filter by month")) //Choose a month, go to Filter
            {
                SelectMonth();
            }
            //Choose 2 valid dates, go to Filter
            else {
                SelectedDates();

            }
        }


    }


    public void SelectMonth()
    {   Dialog<ButtonType> dialog2 = new Dialog<>();
        dialog2.setTitle("Choose a month");
        dialog2.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        ComboBox<String> months = new ComboBox<>();
        months.getItems().addAll("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
        VBox dialogContent2 = new VBox(10);
        dialogContent2.getChildren().add(new Label("Choose one month"));
        dialogContent2.getChildren().add(months);
        dialog2.getDialogPane().setContent(dialogContent2);
        Optional<ButtonType> result2 = dialog2.showAndWait();

        String selectedMonth = months.getValue();

        if (result2.isPresent() && result2.get() == ButtonType.OK && selectedMonth != null) {
            int monthNumber = months.getItems().indexOf(selectedMonth) + 1;
            YearMonth yearMonth = YearMonth.of(LocalDate.now().getYear(), monthNumber);
            LocalDate startOfMonth = yearMonth.atDay(1);
            LocalDate endOfMonth = yearMonth.atEndOfMonth();


            try{
                goFilter(startOfMonth,endOfMonth,1);
            } catch (IOException e) {
                System.out.println("Could not go to calendar, error in branch 2");
                throw new RuntimeException(e);
            }


        }

    }

    public void SelectedDates()
    {Dialog<ButtonType> dialog2 = new Dialog<>();
        dialog2.setTitle("Choose a month");
        dialog2.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        VBox dialogcon=new VBox(10);
        Label l1=new Label("Choose a starting date");
        DatePicker dstart=new DatePicker();
        Label l2=new Label("Choose a ending date");
        DatePicker dend=new DatePicker();
        dialogcon.getChildren().addAll(l1,dstart,l2,dend);
        dialog2.getDialogPane().setContent(dialogcon);
        Optional<ButtonType> result = dialog2.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
             LocalDate startDate = dstart.getValue();
             LocalDate endDate = dend.getValue();
                if (startDate!=null && endDate!=null)
             {
                if (startDate.isAfter(endDate)) {
                    LocalDate aux=startDate;
                    startDate=endDate;
                    endDate=aux;
                }
                 try{
                     goFilter(startDate,endDate,2);
                 } catch (IOException e) {
                     System.out.println("Could not go to calendar, error in branch 3");
                     throw new RuntimeException(e);
                 }


            }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Input Error, one of the dates was not chosen");
                    alert.showAndWait();
                }

        }



    }



}
