package javafiles.controllers;


import javafiles.interfaces.InitStage;
import javafiles.managers.CreditentialsManager;
import javafiles.managers.MainManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Arc;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;

public class BudgetController extends CreditentialsManager implements InitStage {
    private MainManager mainn=new MainManager();
    private Connection con;
    private String path=getPath();
    private String user=getUser();
    private String password=getPassword();
    private float budget;
    private float spentmoney;
    private float bfood,btransport,butil,benter,bother;//budget for each category
    private float sfood,stransport,sutil,senter,sother;//spent on each category


    private Stage stage;
    @FXML Text available;
    @FXML Text spent;
    @FXML Label budgetlabel;
    @FXML Button budgetbutton;
    @FXML Label foodbud,transbud,utilbud,enterbud,otherbud; //budget in database for each category
    @FXML Label foodrem,transrem,utilrem,enterrem,otherrem; //remaining money per category
    @FXML VBox  imgfood,imgtrans,imgutil,imgenter,imgother; //images above each category
    @FXML Arc coloredarcred;

    private LocalDate startmonth;
    private LocalDate endmonth;

    public void setStage(Stage stage) {  //get the stage for the loader
        this.stage = stage;
        mainn.getdatabaseBudget();
        setLabels(); //set some labels
        getSpendings();//get the spendings on each category and the total
        updatelabels2();//update coresponding labels
        getcategorybudgets();//get budgets for each category

        updateArc();
        updatecolors();

    }

    @FXML
    public void goBack() throws IOException { //on button pressed update the page to the history one

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/main_window.fxml"));
        Scene mainscene = new Scene(loader.load(), 900,650);
        MainController mainController = loader.getController();
        mainController.setStage(stage);
        this.stage.setScene(mainscene);
    }
    public void setBudget(float m_budget) {

       budget=m_budget;
    }
    public float getBudget() {
        return budget;
    }

    @FXML
    public void setbudgetbox()
    {  Optional<String> inputnum;
        TextInputDialog td = new TextInputDialog(); //create a text input dialog with its properties
        td.setHeaderText("Set a Budget");
        td.setContentText("Please enter a positive number:");
        inputnum= td.showAndWait();
        if (inputnum.isPresent() && !inputnum.get().equals("") ) {
            float floatnum=Float.parseFloat(inputnum.get());
                if (floatnum>0) {
                    updatebudget(floatnum);
                    mainn.getdatabaseBudget();

                    setLabels();
                    updatelabels2();
                    updateArc();
                    updatecolors();
                }

                else
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Input Error");
                    alert.showAndWait();
                }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.showAndWait();
        }

    }

    private void setLabels()
    {
        budget=mainn.getBudget();
        available.setText("Budget: "+budget+" lei");
        budgetbutton.setText("Out of "+budget+" lei");

    }
    private void updatebudget(float newbudget)
    {
        try{con= DriverManager.getConnection(path,user,password);
            String querry="update users set budget=? where id=?";
            PreparedStatement ps=con.prepareStatement(querry);
            ps.setFloat(1,newbudget);
            ps.setInt(2,mainn.getUser_id());
            ps.executeUpdate();

        }catch (Exception e)
        {
            System.out.println("problem in database- updating budget");
        }
    }
    private void setdates()
    { LocalDate now=LocalDate.now();
        startmonth= now.with(TemporalAdjusters.firstDayOfMonth());
        endmonth= now.with(TemporalAdjusters.lastDayOfMonth());

    }
    private void getSpendings()
    {//select cost from items
       // join expenses on expense_id=expenses.id
        //where expenses.date>='2025-01-01' and expenses.date<='2025-01-31' and user_id=1 and cat_id=1
        setdates();


        try{con= DriverManager.getConnection(path,user,password);
            String querry="select cost from items join expenses on expense_id=expenses.id where expenses.date>=? and expenses.date<=? and user_id=? and cat_id=?";
            for (int i=1;i<=5;i++)
            {
                PreparedStatement ps=con.prepareStatement(querry);
                ps.setObject(1,startmonth);
                ps.setObject(2,endmonth);
                ps.setInt(3,mainn.getUser_id());
                ps.setInt(4,i);
                try(ResultSet rs=ps.executeQuery())
                {
                    while (rs.next())
                    {
                        String cost=rs.getString("cost");
                        if (i==1)
                        {sfood+=Float.parseFloat(cost);

                        }else if (i==2)
                        {stransport+=Float.parseFloat(cost);

                        }else if (i==3)
                        {sutil+=Float.parseFloat(cost);
                        }else if (i==4)
                        {senter+=Float.parseFloat(cost);
                        }else
                        {sother+=Float.parseFloat(cost);
                        }


                    }



                }catch (Exception e)
                {
                    System.out.println("error in getting items-in budget");
                }
                

            }
            spentmoney=sfood+stransport+sutil+senter+sother;


        }catch (Exception e)
        {
            System.out.println("problem in database- retrieving spendings");
        }



    }

    private void updatelabels2()
    {
        spent.setText("Spent: "+spentmoney+" lei");
        float remtospend=budget-spentmoney;
        if (remtospend<0)
            remtospend=0;
        budgetlabel.setText(""+remtospend+"lei");

    }

    private void getcategorybudgets()
    {
        try
        {
            con= DriverManager.getConnection(path,user,password);
            String q1="select food_budget from budget where user_id=?";
            PreparedStatement ps1=con.prepareStatement(q1);
            String q2="select transport_budget from budget where user_id=?";
            PreparedStatement ps2=con.prepareStatement(q2);
            String q3="select utilities_budget from budget where user_id=?";
            PreparedStatement ps3=con.prepareStatement(q3);
            String q4="select entertainment_budget from budget where user_id=?";
            PreparedStatement ps4=con.prepareStatement(q4);
            String q5="select other_budget from budget where user_id=?";
            PreparedStatement ps5=con.prepareStatement(q5);

            ps1.setInt(1,mainn.getUser_id());
            ps2.setInt(1,mainn.getUser_id());
            ps3.setInt(1,mainn.getUser_id());
            ps4.setInt(1,mainn.getUser_id());
            ps5.setInt(1,mainn.getUser_id());

            ResultSet rs1=ps1.executeQuery();
            ResultSet rs2=ps2.executeQuery();
            ResultSet rs3=ps3.executeQuery();
            ResultSet rs4=ps4.executeQuery();
            ResultSet rs5=ps5.executeQuery();
                rs1.next();
                rs2.next();
                rs3.next();
                rs4.next();
                rs5.next();
                String foodlabel=rs1.getString("food_budget");
                bfood=Float.parseFloat(foodlabel);
                String transportlabel=rs2.getString("transport_budget");
                btransport=Float.parseFloat(transportlabel);
                String utillabel=rs3.getString("utilities_budget");
                butil=Float.parseFloat(utillabel);
                String enterlabel=rs4.getString("entertainment_budget");
                benter=Float.parseFloat(enterlabel);
                String otherlabel=rs5.getString("other_budget");
                bother=Float.parseFloat(otherlabel);
            foodbud.setText("Budget: "+foodlabel+" lei");
            transbud.setText("Budget: "+transportlabel+" lei");
            utilbud.setText("Budget: "+utillabel+" lei");
            enterbud.setText("Budget: "+enterlabel+" lei");
            otherbud.setText("Budget: "+otherlabel+" lei");

            setspendings();


        }catch (Exception e)
        {
            System.out.println("problem in database- retrieving category budgets");
        }

    }

    private void setspendings()
    {
        float remfood=bfood-sfood;
        float remtransport=btransport-stransport;
        float remutil=butil-sutil;
        float rementer=benter-senter;
        float remother=bother-sother;

        foodrem.setText(""+remfood+" rem");
        transrem.setText(""+remtransport+" rem");
        utilrem.setText(""+remutil+" rem");
        enterrem.setText(""+rementer+" rem");
        otherrem.setText(""+remother+" rem");
    }

    private float updatecatbudget() //text box that appears when pressing one of the modify buttons to enter a sum of money
    {Optional<String> inputnum;
        float floatnum=0;
        TextInputDialog td = new TextInputDialog();
        td.setHeaderText("Set a Budget for the Category");
        td.setContentText("Please enter a positive number:");
        inputnum= td.showAndWait();
        if (inputnum.isPresent() && !inputnum.get().equals("") ) {
             floatnum=Float.parseFloat(inputnum.get());
            if (floatnum>0) {
                return floatnum;
            }

            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.showAndWait();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.showAndWait();
        }
        return floatnum;


    }

    @FXML
    private void updatefoodcat()
    {
        float newbudget=updatecatbudget();
        if (newbudget<=0) //if the input was not good, keep the last budget
            newbudget=bfood;
        try{con= DriverManager.getConnection(path,user,password);
            String querry="update budget set food_budget=? where user_id=?";
            PreparedStatement ps=con.prepareStatement(querry);
            ps.setFloat(1,newbudget);
            ps.setInt(2,mainn.getUser_id());
            ps.executeUpdate();

        }catch (Exception e)
        {
            System.out.println("problem in database- updating budget");
        }
getcategorybudgets();
        updatecolors();
    }
    @FXML
    private void updatetransportcat()
    {
        float newbudget=updatecatbudget();
        if (newbudget<=0)
            newbudget=btransport;
        try{con= DriverManager.getConnection(path,user,password);
            String querry="update budget set transport_budget=? where user_id=?";
            PreparedStatement ps=con.prepareStatement(querry);
            ps.setFloat(1,newbudget);
            ps.setInt(2,mainn.getUser_id());
            ps.executeUpdate();

        }catch (Exception e)
        {
            System.out.println("problem in database- updating budget");
        }
        getcategorybudgets();
        updatecolors();
    }
    @FXML
    private void updateutilcat()
    {
        float newbudget=updatecatbudget();
        if (newbudget<=0)
            newbudget=butil;
        try{con= DriverManager.getConnection(path,user,password);
            String querry="update budget set utilities_budget=? where user_id=?";
            PreparedStatement ps=con.prepareStatement(querry);
            ps.setFloat(1,newbudget);
            ps.setInt(2,mainn.getUser_id());
            ps.executeUpdate();

        }catch (Exception e)
        {
            System.out.println("problem in database- updating budget");
        }
        getcategorybudgets();
        updatecolors();
    }
    @FXML
    private void updateentercat()
    {
        float newbudget=updatecatbudget();
        if (newbudget<=0)
            newbudget=benter;
        try{con= DriverManager.getConnection(path,user,password);
            String querry="update budget set entertainment_budget=? where user_id=?";
            PreparedStatement ps=con.prepareStatement(querry);
            ps.setFloat(1,newbudget);
            ps.setInt(2,mainn.getUser_id());
            ps.executeUpdate();

        }catch (Exception e)
        {
            System.out.println("problem in database- updating budget");
        }
        getcategorybudgets();
        updatecolors();
    }
    @FXML
    private void updateothercat()
    {
        float newbudget=updatecatbudget();
        if (newbudget<=0)
            newbudget=benter;
        try{con= DriverManager.getConnection(path,user,password);
            String querry="update budget set other_budget=? where user_id=?";
            PreparedStatement ps=con.prepareStatement(querry);
            ps.setFloat(1,newbudget);
            ps.setInt(2,mainn.getUser_id());
            ps.executeUpdate();

        }catch (Exception e)
        {
            System.out.println("problem in database- updating budget");
        }
        getcategorybudgets();
        updatecolors();
    }

    private void updateArc()
    {
        float perc=budget-spentmoney;
        if (perc<=0)
            perc=0;
        perc=perc/budget;
         //percentaje to fill
        float len2=perc*360;
        coloredarcred.setLength(len2);
    }
    private void updatecolors()
    {
        //verifici care sunt mai putin si le dai pe rosu culorile la remaining
        //update when changing anything related to budgets
        if (budget < spentmoney) {

            budgetlabel.setStyle("-fx-text-fill: red");
        } else {

            budgetlabel.setStyle("-fx-text-fill: green");

        }

        if (btransport<stransport)
            transrem.setStyle("-fx-text-fill: red");
        else
            transrem.setStyle("-fx-text-fill: green");

        if (butil<sutil)
            utilrem.setStyle("-fx-text-fill: red");
        else
            utilrem.setStyle("-fx-text-fill: green");

        if (bfood<sfood)
            foodrem.setStyle("-fx-text-fill: red");
        else
            foodrem.setStyle("-fx-text-fill: green");

        if (benter<senter)
            enterrem.setStyle("-fx-text-fill: red");
        else
            enterrem.setStyle("-fx-text-fill: green");

        if (bother<sother)
            otherrem.setStyle("-fx-text-fill: red");
        else
            otherrem.setStyle("-fx-text-fill: green");






    }





}
