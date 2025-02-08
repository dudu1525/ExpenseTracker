package javafiles.controllers;


import javafiles.interfaces.InitStage;
import javafiles.managers.CreditentialsManager;
import javafiles.managers.MainManager;
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

public class SecController extends CreditentialsManager implements InitStage {
    private MainManager mainManager=new MainManager();
    private Connection con;
    private String path=getPath();
    private String user=getUser();
    private String password=getPassword();

    private Stage stage;

    @FXML  Text backb;
    @FXML VBox textbox;
    @FXML VBox costbox;

    public void setStage(Stage stage) {
        this.stage = stage;
        backb.getStyleClass().add("header-label");
    }

    public void goBack() throws IOException {//as in main, when back is pressed go back to main window

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/main_window.fxml"));

        Scene mainscene=new Scene(fxmlLoader.load(), 900,650);
        MainController mainController = fxmlLoader.getController();

        mainController.setStage(stage);
        stage.setScene(mainscene);
    }

    public void init(int id)     //the main claass that updates the whole page and calls private functions
    {
        dosmth(id);

    }

    private void dosmth(int id)
    {       //connect to database
        try {
            con= DriverManager.getConnection(path,user,password);
            String getexpenseid="select id from expenses where date=? and user_id=?";
            PreparedStatement pst=con.prepareStatement(getexpenseid);
            pst.setObject(1,mainManager.date);
            pst.setInt(2,mainManager.getUser_id());
            int expenseid=0;
            try( ResultSet rs=pst.executeQuery())
            { if (rs.next()) {
                expenseid=rs.getInt("id");
            }

            }catch (SQLException e) {System.out.println("couldnt get expense id, in secondary controller!!");}

            if (id == 1) //FOOD part
            {
                backb.setText("Detailed food expenses:");

                String getdetailsfood="select name,cost from items where cat_id=1 and expense_id=?";
                PreparedStatement pst1=con.prepareStatement(getdetailsfood);
                pst1.setInt(1,expenseid);
                System.out.println("aici:"+expenseid);
                try( ResultSet rs1=pst1.executeQuery())
                {   int hasmembers=0;
                    while (rs1.next()) {hasmembers++;
                        String details=rs1.getString("name");
                        Integer cost=rs1.getInt("cost");
                        Text tx1 = new Text(details);
                        tx1.getStyleClass().add("label");
                         textbox.getChildren().add(tx1);
                         Text tx2 = new Text(cost.toString());
                         tx2.getStyleClass().add("label");
                         costbox.getChildren().add(tx2);

                    }
                    if (hasmembers==0)
                    {Text tx1 = new Text("There are no expenses for this category yet.");
                        textbox.getChildren().add(tx1);

                    }
                } catch (SQLException e) {
                   System.out.println("secondary controller problem, could not get results");
                }

            } else if (id == 2) {
                backb.setText("Detailed transport expenses:");

                String getdetailstransport="select name,cost from items where cat_id=2 and expense_id=?";
                PreparedStatement pst1=con.prepareStatement(getdetailstransport);
                pst1.setInt(1,expenseid);
                try( ResultSet rs1=pst1.executeQuery())
                { int hasmembers=0;
                    while (rs1.next()) {
                        hasmembers++;
                        String details=rs1.getString("name");
                        Integer cost=rs1.getInt("cost");
                        Text tx1 = new Text(details);
                        textbox.getChildren().add(tx1);
                        Text tx2 = new Text(cost.toString());
                        costbox.getChildren().add(tx2);

                    }
                    if (hasmembers==0)
                    {Text tx1 = new Text("There are no expenses for this category yet.");
                        textbox.getChildren().add(tx1);

                    }
                } catch (SQLException e) {
                    System.out.println("secondary controller problem, could not get results");
                }
            } else if (id == 3) {
                backb.setText("Detailed utilities expenses:");
                String getdetailsutilities="select name,cost from items where cat_id=3 and expense_id=?";
                PreparedStatement pst1=con.prepareStatement(getdetailsutilities);
                pst1.setInt(1,expenseid);
                try( ResultSet rs1=pst1.executeQuery())
                { int hasmembers=0;
                    while (rs1.next()) {
                        hasmembers++;
                        String details=rs1.getString("name");
                        Integer cost=rs1.getInt("cost");
                        Text tx1 = new Text(details);
                        textbox.getChildren().add(tx1);
                        Text tx2 = new Text(cost.toString());
                        costbox.getChildren().add(tx2);

                    }
                    if (hasmembers==0)
                    {Text tx1 = new Text("There are no expenses for this category yet.");
                        textbox.getChildren().add(tx1);

                    }
                } catch (SQLException e) {
                    System.out.println("secondary controller problem, could not get results");
                }

            } else if (id == 4) {
                backb.setText("Detailed entertainment expenses:");
                String getdetailsenter="select name,cost from items where cat_id=4 and expense_id=?";
                PreparedStatement pst1=con.prepareStatement(getdetailsenter);
                pst1.setInt(1,expenseid);
                try( ResultSet rs1=pst1.executeQuery())
                { int hasmembers=0;
                    while (rs1.next()) {
                        hasmembers++;
                        String details=rs1.getString("name");
                        Integer cost=rs1.getInt("cost");
                        Text tx1 = new Text(details);
                        textbox.getChildren().add(tx1);
                        Text tx2 = new Text(cost.toString());
                        costbox.getChildren().add(tx2);

                    }
                    if (hasmembers==0)
                    {Text tx1 = new Text("There are no expenses for this category yet.");
                        textbox.getChildren().add(tx1);

                    }
                } catch (SQLException e) {
                    System.out.println("secondary controller problem, could not get results");
                }

            } else if (id == 5) {
                backb.setText("Other expenses:");
                String getdetailsother="select name,cost from items where cat_id=5 and expense_id=?";
                PreparedStatement pst1=con.prepareStatement(getdetailsother);
                pst1.setInt(1,expenseid);
                try( ResultSet rs1=pst1.executeQuery())
                {     int hasmembers=0;
                    while (rs1.next()) {
                        hasmembers++;
                        String details=rs1.getString("name");
                        Integer cost=rs1.getInt("cost");
                        Text tx1 = new Text(details);
                        textbox.getChildren().add(tx1);
                        Text tx2 = new Text(cost.toString());
                        costbox.getChildren().add(tx2);

                    }
                    if (hasmembers==0)
                    {Text tx1 = new Text("There are no expenses for this category yet.");
                        textbox.getChildren().add(tx1);

                    }
                } catch (SQLException e) {
                    System.out.println("secondary controller problem, could not get results");
                }

            }
        }catch (Exception e) {
            System.out.println("problem in getting detailed descriptions");

        }


    }



}
