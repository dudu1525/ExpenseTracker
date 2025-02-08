package javafiles.managers;

import java.sql.Connection;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Vector;

public class CalendarManager extends CreditentialsManager {

    private Connection con;
    private String path = getPath();
    private String user = getUser();
    private String password = getPassword();
    private MainManager mainn = new MainManager();
    private Vector<String> items=new Vector<>(100);
    private int nrofitems=0;

    String datestring;
    String day = "";
    String month = "";
    String year;

  public  CalendarManager(int day, int monthindex, String year) {
        Integer intday = Integer.valueOf(day);
        if (day < 10)
            this.day = this.day.concat("0");

        this.day = this.day.concat(intday.toString());
        transform(monthindex);
        this.year = year;
        datestring = "";
        datestring = datestring.concat(this.year);
        datestring = datestring.concat("-");
        datestring = datestring.concat(this.month);
        datestring = datestring.concat("-");
        datestring = datestring.concat(this.day); //here its not the day


    }
    public int getNrofitems()
    {
        return nrofitems;
    }

    public Vector<String> getItems() {
        return items;
    }

    public String getDatestring() {
        return datestring;
    }

    private void transform(int monthindex) {
        int properid = monthindex + 1;
        Integer intmonth = Integer.valueOf(monthindex);
        if (properid < 10) {
            this.month = this.month.concat("0");
        }

        this.month = this.month.concat(intmonth.toString());
    }


    public void doActions()  {
                connect();
    }

    private void connect()  {
        try {
            con = DriverManager.getConnection(path, user, password);
            String getexpenseid = "select id from expenses where date=? and user_id=?";
            PreparedStatement pst = con.prepareStatement(getexpenseid);
            final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            final LocalDate dt = LocalDate.parse(datestring, dtf);
            pst.setObject(1, dt);
            pst.setInt(2, mainn.getUser_id());
            int expenseid = 0;
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    expenseid = rs.getInt("id");
                }
            } catch (Exception e) {
                    System.out.println("something wrong in calendar");
            }

        if (expenseid==0)
        {
            nrofitems=0;
        }
        else
        {
            String getdetails="select name,cost from items where expense_id=?";
            PreparedStatement pst1=con.prepareStatement(getdetails);
            pst1.setInt(1,expenseid);
            try( ResultSet rs1=pst1.executeQuery())
            {
                while (rs1.next()) {
                    String details=rs1.getString("name");
                    Integer cost=rs1.getInt("cost");
                    String coststring=cost.toString();
                    details=details.concat("  -  ");
                    details=details.concat(coststring);
                    details=details.concat(" lei");

                    items.add(details);
                    nrofitems++;

                }
                items.setSize(nrofitems);
            }catch(Exception e)
            {
                System.out.println("something very wrong-calendar");
            }

        }


        }catch (Exception e) {
                System.out.println("couldn't connect to database-calendar");
            }
        }
        //function to make a nice functionality for the vbox

    }
