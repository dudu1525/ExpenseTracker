package javafiles.managers;

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
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import java.time.LocalDate;
import java.util.Vector;

public class FilterManager extends CreditentialsManager {
    private LocalDate startdate;
    private  LocalDate enddate;

    private Connection con;
    private String path = getPath();
    private String user = getUser();
    private String password = getPassword();
    private MainManager mainn = new MainManager();

    private Vector<String> foodlist=new Vector<>(1000);
    private Vector<String> translist=new Vector<>(1000);
    private Vector<String> utillist=new Vector<>(1000);
    private Vector<String> enterlist=new Vector<>(1000);
    private Vector<String> otherlist=new Vector<>(1000);

    private int totalfood=0,totaltrans=0,totalutil=0,totalenter=0,totalother=0;
    private int total=0;

   public FilterManager(LocalDate startdate, LocalDate enddate) {
        this.startdate = startdate;
        this.enddate = enddate;
    }

    public Integer getTotalfood() {
        return totalfood;
    }
    public void setTotalfood(Integer totalfood) {
        this.totalfood = totalfood;
    }
    public Integer getTotaltrans() {
        return totaltrans;
    }
    public void setTotaltrans(Integer totaltrans) {
        this.totaltrans = totaltrans;

    }
    public Integer getTotalutil() {
        return totalutil;

    }
    public void setTotalutil(Integer totalutil) {
        this.totalutil = totalutil;
    }
    public Integer getTotalenter() {
        return totalenter;
    }
    public void setTotalenter(Integer totalenter) {
        this.totalenter = totalenter;
    }
    public Integer getTotalother() {
        return totalother;
    }
    public void setTotalother(Integer totalother) {
        this.totalother = totalother;
    }

    public Vector<String> getFoodlist() {
        return foodlist;
    }
    public Vector<String> getTranslist() {
        return translist;
    }
    public Vector<String> getUtillist() {
        return utillist;
    }
    public Vector<String> getEnterlist() {
        return enterlist;
    }
    public Vector<String> getOtherlist() {
        return otherlist;
    }
    public int getTotal() {
        return total;
    }


    public void doOperations()
    {
        try{
            con=DriverManager.getConnection(path, user, password);
            String sqlscript="select name,cost from items join expenses on items.expense_id=expenses.id where expenses.date>=? and expenses.date<=? and user_id=? and cat_id=?";
            for (int i=1;i<=5;i++)
            {
                PreparedStatement pst=con.prepareStatement(sqlscript);
                pst.setObject(1,startdate);
                pst.setObject(2,enddate);
                pst.setInt(3,mainn.getUser_id());
                pst.setInt(4,i);
                try(ResultSet rs=pst.executeQuery())
                {int index=0;
                    while (rs.next())
                    {
                        String name=rs.getString("name");
                        String cost=rs.getString("cost");
                        String addedtogether=name+" "+cost;
                     //   System.out.println(addedtogether);
                        if (i==1)
                        {
                            foodlist.add(addedtogether);
                            totalfood=totalfood+Integer.parseInt(cost);
                        }else if (i==2)
                        {translist.add(addedtogether);
                            totaltrans=totaltrans+Integer.parseInt(cost);

                        }else if (i==3)
                        {utillist.add(addedtogether);
                            totalutil=totalutil+Integer.parseInt(cost);
                        }else if (i==4)
                        {enterlist.add(addedtogether);
                            totalenter=totalenter+Integer.parseInt(cost);
                        }else
                        {totalother=totalother+Integer.parseInt(cost);
                            otherlist.add(addedtogether);
                        }

                        index++;
                    }

                    if (i==1)
                foodlist.setSize(index);
                    else if (i==2)
                            translist.setSize(index);
                            else if (i==3)
                                utillist.setSize(index);
                                else if (i==4)
                                    enterlist.setSize(index);
                                    else
                                        otherlist.setSize(index);

                }catch (Exception e)
                {
                    System.out.println("error in getting items");
                }


            }


        }
        catch(Exception e){
            System.out.println("couldnt connect in filter manager");
        }
        calctotals();
    }

    private void calctotals()
    {

        total=totalenter+totalfood+totalother+totaltrans+totalutil;

    }





    //select name,cost from items join expenses on
    //    items.expense_id = expenses.id where expenses.date>='2024-12-15' and expenses.date<='2024-12-15'
    //  and user_id=1
}
