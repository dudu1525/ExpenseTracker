package javafiles.controllers;


import javafiles.interfaces.InitStage;
import javafiles.managers.FilterManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import java.util.Vector;
//here you control the month window and the filter by date window
//here, the starting dates are given, if month was chosen, just give the date of that month in that year
//if exact date was given, it gets the starting date

public class FilterController  {
    private LocalDate startdate;
    private  LocalDate enddate;
    public Stage stage;

    @FXML private Text mainlabel;
    @FXML private Label foodspent,transportspent,utilspent,entertainmentspent,otherspent;
    @FXML private Text totall;
    @FXML private VBox box1;
    @FXML private VBox box2;
    @FXML private VBox box3;
    @FXML private VBox box4;
    @FXML private VBox box5;

    private Vector<String> foodlist=new Vector<>(1000);
    private Vector<String> translist=new Vector<>(1000);
    private Vector<String> utillist=new Vector<>(1000);
    private Vector<String> enterlist=new Vector<>(1000);
    private Vector<String> otherlist=new Vector<>(1000);

    FilterManager fm;


    public void setDates(LocalDate startdate, LocalDate enddate) {
        this.startdate = startdate;
        this.enddate = enddate;


        populateboxes();
        ScrollPane sp1 = new ScrollPane(box1);
        sp1.setFitToWidth(true); // Ensures the ScrollPane resizes properly
        sp1.setPrefHeight(416);

    }

    public void setStage(Stage stage,int id) {
        this.stage = stage;
        systemout();
        if (id==1)
        {  mainlabel.setText("Money spent from "+startdate+" to "+enddate+":");
        }else
        {
            mainlabel.setText("Money spent from "+startdate+" to "+enddate+":");
        }
    }
    private  void systemout()
    {
        System.out.println(startdate.toString()+"  "+enddate.toString());

    }

    public void goBack() throws IOException { //on button pressed update the page to the history one

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/history_window.fxml"));
        Scene hscene = new Scene(loader.load(), 900,650);
        HistoryController hc = loader.getController();
        hc.setStage(stage);
        hc.setgraph(false);
        this.stage.setScene(hscene);

    }

    private void populateboxes()
    {
        fm=new FilterManager(startdate,enddate);
        fm.doOperations();

        foodlist.setSize(fm.getFoodlist().size());
        translist.setSize(fm.getTranslist().size());
        utillist.setSize(fm.getUtillist().size());
        enterlist.setSize(fm.getEnterlist().size());
        otherlist.setSize(fm.getOtherlist().size());
        foodlist=fm.getFoodlist();
        translist=fm.getTranslist();
        utillist=fm.getUtillist();
        enterlist=fm.getEnterlist();
        otherlist=fm.getOtherlist();

        foodspent.setText("Total spent: "+fm.getTotalfood());
        transportspent.setText("Total spent: "+fm.getTotaltrans());
        utilspent.setText("Total spent:"+fm.getTotalutil());
        entertainmentspent.setText("Total spent: "+fm.getTotalenter());
        otherspent.setText("Total spent: "+fm.getTotalother());

        totall.setText("Total spent in the period: "+ fm.getTotal());


        for (int i=0;i<fm.getFoodlist().size();i++)
        {
           Label l1=new Label(fm.getFoodlist().get(i));
           box1.getChildren().add(l1);
        }
        for (int i=0;i<fm.getTranslist().size();i++)
        {
            Label l1=new Label(fm.getTranslist().get(i));
            box2.getChildren().add(l1);
        }
        for (int i=0;i<fm.getUtillist().size();i++)
        {
            Label l1=new Label(fm.getUtillist().get(i));
            box3.getChildren().add(l1);
        }
        for (int i=0;i<fm.getEnterlist().size();i++)
        {
            Label l1=new Label(fm.getEnterlist().get(i));
            box4.getChildren().add(l1);
        }
        for(int i=0;i<fm.getOtherlist().size();i++)
        {
            Label l1=new Label(fm.getOtherlist().get(i));
            box5.getChildren().add(l1);
        }




    }
}
