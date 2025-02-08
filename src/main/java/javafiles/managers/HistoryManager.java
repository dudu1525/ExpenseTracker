package javafiles.managers;

import com.sun.tools.javac.Main;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.sql.*;
import java.time.ZonedDateTime;
import java.util.Vector;

public class HistoryManager extends CreditentialsManager {
    private Vector<String> expenses=new Vector<>(1000);
    private int indexdesc=0;
    private MainManager mainn;
    private static int user_id;
    private int sumfood=0;
    private int sumtrans=0;
    private int sumutil=0;
    private int sumenter=0;
    private int sumother=0;
    private static LocalDate startweek;
    private static LocalDate endweek;
    private static LocalDate startmonth;
    private static LocalDate endmonth;
    private Connection con;
    private String path=getPath();
    private String user=getUser();
    private String password=getPassword();




    public int getsumfood()
    {
        return sumfood;
    }
    public void setsumfood(int sumfood)
    {
        this.sumfood = sumfood;
    }
    public int getsumtrans()
    {
        return sumtrans;
    }
    public void setsumtrans(int sumtrans)
    {
        this.sumtrans = sumtrans;
    }
    public int getSumutil()
    {
        return sumutil;
    }
    public void setsumutil(int sumutil)
    {
        this.sumutil = sumutil;
    }
    public int getsumenter()
    {
        return sumenter;
    }
    public void setsumenter(int sumenter)
    {
        this.sumenter = sumenter;
    }
    public int getsumother()
    {
        return sumother;
    }
    public void setsumother(int sumother)
    {
        this.sumother = sumother;
    }
    public Vector<String> getExpenses()
    {
        return expenses;
    }
    //give the dates for this
   // select name,cost from items join expenses on items.expense_id = expenses.id where date>='2024-12-15' and date<='2024-12-18' and user_id=2 and items.cat_id=1
        //function to calculate the dates of the last week and get the expenses on these

    private void getweekdates()
    {  mainn = new MainManager();
        user_id=mainn.getUser_id();
         ZonedDateTime input = ZonedDateTime.now();
         ZonedDateTime startOfLastWeek = input.minusWeeks(1).with(DayOfWeek.MONDAY);
                startweek=startOfLastWeek.toLocalDate();
         ZonedDateTime endOfLastWeek = startOfLastWeek.plusDays(6);
         endweek=endOfLastWeek.toLocalDate();
       // System.out.println(start+" "+end);

    }

    private void getmonthdates()
    {
        LocalDate now = LocalDate.now();
        LocalDate firstDayOfCurrentMonth = now.withDayOfMonth(1);
        startmonth= firstDayOfCurrentMonth.minusMonths(1);
        endmonth = firstDayOfCurrentMonth.minusDays(1);
    }
    private void initsums()
    {
        sumfood=0;
        sumtrans=0;
        sumutil=0;
        sumenter=0;
        sumother=0;
        indexdesc=0;
    }
    public void getsumweek() //function to calculate the sums of the last week
    {initsums();
        getweekdates();
        try{
            con=DriverManager.getConnection(path,user,password);
            String sqlquerry="select name,cost,expenses.date from items join expenses on items.expense_id = expenses.id where date>=? and date<=? and user_id=? and items.cat_id=?";
            PreparedStatement ps=con.prepareStatement(sqlquerry);
            ps.setObject(1,startweek);
            ps.setObject(2,endweek);
           // System.out.println(mainn.getUser_id());
            ps.setInt(3,user_id);
            for (int i=1;i<=5;i++)
            {ps.setInt(4,i);
                try{
                    ResultSet rs=ps.executeQuery();
                    while (rs.next())
                    {   indexdesc++;
                        //insert description here for the vbox
                        int nr=rs.getInt("cost");
                        String cost=rs.getString("cost");
                        String desc=rs.getString("name");
                        String date="   ";
                         date=date.concat(rs.getString("date"));
                            date=date.concat(" -    ");
                            date=date.concat(cost);
                            date=date.concat(" lei,    ");
                            date=date.concat(desc);
                                expenses.add(date);

                        if (i==1)
                            sumfood+=nr;
                        else if (i==2)
                            sumtrans+=nr;
                        else if (i==3)
                            sumutil+=nr;
                        else if (i==4)
                            sumenter+=nr;
                        else
                            sumother+=nr;
                    }

                }
                catch(Exception e)
                {   System.out.println(e);
                    System.out.println("something wrong in the loop of week sum");
                }
        expenses.setSize(indexdesc);
            }


        } catch (Exception e) {
            System.out.println("something wrong in history, week sum calculation problem.");
        }
          //  System.out.println(startweek+" "+endweek);
            //System.out.println(sumfood+" "+sumtrans+" "+sumutil+" "+sumenter+" "+sumother);
    }
    public void getsummonth() //function to calculate the sums of the last month
    {initsums();
        getmonthdates();
        try{
            con=DriverManager.getConnection(path,user,password);
            String sqlquerry="select name,cost,expenses.date from items join expenses on items.expense_id = expenses.id where date>=? and date<=? and user_id=? and items.cat_id=?";
            PreparedStatement ps=con.prepareStatement(sqlquerry);
            ps.setObject(1,startmonth);
            ps.setObject(2,endmonth);
            ps.setInt(3,user_id);
            for (int i=1;i<=5;i++)
            {ps.setInt(4,i);
                try{
                    ResultSet rs=ps.executeQuery();
                    while (rs.next())
                    {indexdesc++;
                        //insert description here for the vbox
                        int nr=rs.getInt("cost");
                        String cost=rs.getString("cost");
                        String desc=rs.getString("name");
                        String date=rs.getString("date");
                        date=date.concat("-     ");
                        date=date.concat(cost);
                        date=date.concat(" lei,   ");
                        date=date.concat(desc);
                        expenses.add(date);

                        if (i==1)
                            sumfood+=nr;
                        else if (i==2)
                            sumtrans+=nr;
                        else if (i==3)
                            sumutil+=nr;
                        else if (i==4)
                            sumenter+=nr;
                        else
                            sumother+=nr;
                    }

                }
                catch(Exception e)
                {
                    System.out.println("something wrong in the loop of week sum");
                }
                expenses.setSize(indexdesc);

            }


        } catch (Exception e) {
            System.out.println("something wrong in history, week sum calculation problem.");
        }

    }

}
