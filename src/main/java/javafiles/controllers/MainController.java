package javafiles.controllers;


import javafiles.interfaces.InitStage;
import javafiles.managers.HistoryManager;
import javafiles.managers.MainManager;
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

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

public class MainController implements InitStage {
    public MainManager mainManager=new MainManager();
    private HistoryManager historyManager=new HistoryManager();//calculates the values for the history page

    @FXML
    private Text dateText;

    @FXML Text totalsum;
    @FXML Text foodsum;
    @FXML Text transport;
    @FXML Text utilsum;
    @FXML Text entersum;
    @FXML Text othersum;

    @FXML
    private Button addexpense;
    @FXML
    private Stage stage;

    @FXML private BarChart chartbar; //the barchart
    @FXML private CategoryAxis xax;
    @FXML private NumberAxis yax;

    @FXML
    public void initialize() {      //update the text field to display the current date
            chartbar.setTitle("Expenses Today:");

            yax.setLabel("Value");
        chartbar.setLegendVisible(false);
            updateChart(mainManager.date);



        dateText.setText("Today's date: " + mainManager.date);
    }
    public void setStage(Stage stage) {  //get the stage for the loader
        this.stage = stage;
    }

    @FXML
    public void goToHistory() throws IOException { //on button pressed update the page to the history one

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/history_window.fxml"));
       Scene historyScene = new Scene(loader.load(), 900,650);
      HistoryController historyController = loader.getController();
      historyController.setStage(stage);
      historyController.setgraph(false);


        this.stage.setScene(historyScene);

    }

    public void gotoBudget() throws IOException { //on button pressed update the page to the history one

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/budget_window.fxml"));
        Scene historyScene = new Scene(loader.load(), 900,650);
        BudgetController budgetController = loader.getController();
        budgetController.setStage(stage);
        this.stage.setScene(historyScene);

    }

    @FXML
    public void gotoLogin() throws IOException { //on button pressed update the page to the history one

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/login_window.fxml"));
        Scene loginScene = new Scene(loader.load(), 900,650);
        LoginController loginController = loader.getController();
        loginController.setStage(stage);

        // historyManager.getmonthdates();
        // historyManager.getsumweek();
//initialize the graph of the history to week
        this.stage.setScene(loginScene);

    }
    @FXML
    public void gotofood() throws IOException { //add one of these for each button
        goToSec1(1);
    }
    @FXML
    public void gototransport() throws IOException { //add one of these for each button
        goToSec1(2);
    }
    @FXML
    public void gotoutilities() throws IOException { //add one of these for each button
        goToSec1(3);
    }
    @FXML
    public void gotoentertainment() throws IOException { //add one of these for each button
        goToSec1(4);
    }
    @FXML
    public void gotoother() throws IOException { //add one of these for each button
        goToSec1(5);
    }



    @FXML
    public void goToSec1(int nr) throws IOException { //on button pressed update the page to the history one

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/sec_window.fxml"));
        Scene secscene = new Scene(loader.load(), 900,650);
        SecController secController = loader.getController();
        secController.init(nr);
        secController.setStage(stage);
        this.stage.setScene(secscene);

    }

    @FXML
    public void updateChart(LocalDate date)
    {  XYChart.Series series1 = new XYChart.Series();
        mainManager.calc_sums(mainManager.date);
        series1.getData().add(new XYChart.Data("total", mainManager.getTotal()));
        series1.getData().add(new XYChart.Data("food", mainManager.getFood()));
        series1.getData().add(new XYChart.Data("transport", mainManager.getTransport()));
        series1.getData().add(new XYChart.Data("utilities", mainManager.getUtilities()));
        series1.getData().add(new XYChart.Data("entertainment", mainManager.getEntertainement()));
        series1.getData().add(new XYChart.Data("other", mainManager.getOther()));
        series1.setName("value in lei");

        chartbar.getData().clear();
        //chartbar.layout();

        chartbar.getData().addAll( series1 );

        totalsum.setText(String.valueOf(mainManager.getTotal()));
        foodsum.setText(String.valueOf(mainManager.getFood()));
        transport.setText(String.valueOf(mainManager.getTransport()));
        utilsum.setText(String.valueOf(mainManager.getUtilities()));
        entersum.setText(String.valueOf(mainManager.getEntertainement()));
        othersum.setText(String.valueOf(mainManager.getOther()));


    }
    @FXML
    public void addExpense()
    {       System.out.println("opened");

            Dialog<ButtonType> dialog = new Dialog<>(); //add a dialog
            dialog.setTitle("Add Expense");                 //set its title
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL); //add buttons ok and cancel
            ComboBox<String> expenseTypeComboBox = new ComboBox<>();  //add a combo box to contain type of expenses
             expenseTypeComboBox.getItems().addAll("food", "transport", "utilities","entertainment", "other"); //added items
        VBox dialogContent = new VBox(10);
        dialogContent.getChildren().add(new Label("Choose Expense Type:"));//add a label
        dialogContent.getChildren().add(expenseTypeComboBox);//add the items to the Vbox
        dialog.getDialogPane().setContent(dialogContent); //set the content to the dialog
           Optional<ButtonType> result = dialog.showAndWait(); //without this it doesent open!!

            String selectedType = expenseTypeComboBox.getValue(); //get the category selected, if not null, compare with this
            Optional<String> inputsum; //get amount of money
            int inputsumint=-1;
            Optional<String> inputdesc=null;

        if (result.isPresent() && result.get() == ButtonType.OK && selectedType != null) // now open a new dialog with 2 inputs: amount and desciption
        {
            TextInputDialog td = new TextInputDialog(); //create a text input dialog with its properties
            td.setTitle("Amount of money");
            td.setHeaderText("Amount of money spent:");
            td.setContentText("Please enter a whole,positive number:");
            inputsum=td.showAndWait();

            if (inputsum.isPresent()) //if a sum was entered
            try //try to check if its a positive integer, else throw an exception
            {   inputsumint=Integer.parseInt(inputsum.get());
                    if (inputsumint<0)
                        throw new NumberFormatException();

                TextInputDialog td2 = new TextInputDialog(); //if the sum was good, add a new text input for the description
                td2.setTitle("Description");
                td2.setHeaderText("Add a description to the expense:");
                inputdesc=td2.showAndWait(); //if the ok button was pressed
                    if (inputdesc.isPresent())
                    {String description = inputdesc.get().trim(); //get the actual value and check if its null
                        if (description.isEmpty())
                        {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Input Error, Need a description");
                            alert.showAndWait();
                        }
                        else//add function to update values in the database, the function from the manager
                        {  mainManager.add_expense(inputsumint,selectedType,description);
                            updateChart(mainManager.date);
                            System.out.println(inputsum.get() + inputdesc.get() + selectedType);
                        }

                    }


            }catch(NumberFormatException e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.showAndWait();
            }








        }



    }


    //add here







}



