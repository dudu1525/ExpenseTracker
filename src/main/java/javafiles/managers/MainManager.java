package javafiles.managers;

import java.time.LocalDate;
import java.sql.*;
public class MainManager extends CreditentialsManager{
   public  LocalDate date=LocalDate.now();

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    private static int user_id;
    private int total=0;
    private int food=0;
    private int transport=0;
    private int utilities=0;
    private int entertainement=0;
    private int other=0;
    private Connection con;
   private String path=getPath();
    private String user=getUser();
    private String password=getPassword();

    private  static float budget;



            public float getBudget()
            {
                return budget;
            }
            public void setBudget(float m_budget)
            {
                budget=m_budget;
            }
          public   int getTotal()
            {
                return food+transport+utilities+entertainement+other;
            }
            public int getFood()
            {
                return food;
            }
            public int getTransport()
            {
                return transport;
            }
            public int getUtilities()
            {
                return utilities;
            }
            public int getEntertainement()
            {
                return entertainement;
            }
            public int getOther()
            {
                return other;
            }
          public   int getUser_id()
            {
                return user_id;
            }
        public    void setUser_id(int m_user_id)
            {
                user_id = m_user_id;
            }


            public void init() //whenever the method is called, a new date is created if it doesent exist in the table
        {

            //try the connection, execute the max id currenty in the table then insert if the values are different
            try  {
                con=DriverManager.getConnection(path,user,password);
                System.out.println("Connected to PostgreSQL database");
                int maxId = getMaxId();
                String verifuniq="select id from expenses where date=? and user_id=?";
                String sqlst="INSERT INTO expenses (id,date,user_id) VALUES (?, ?,?) ON CONFLICT DO NOTHING";

                    PreparedStatement pst=con.prepareStatement(verifuniq);
                    pst.setObject(1,date);
                    pst.setInt(2,user_id);
                    ResultSet rs=pst.executeQuery();
                    if(!rs.next())
                try(PreparedStatement st=con.prepareStatement(sqlst))
                {
                    st.setInt(1,maxId+1);
                    st.setObject(2, date);
                    st.setInt(3, user_id);
                        st.executeUpdate();


                }catch (SQLException e) {
                    e.printStackTrace();
                }
                    else  {
                        System.out.println("User and date combo already exists");

                    }

            } catch (Exception e) {
                System.out.println("sql problem");
                throw new RuntimeException(e);
            }

        }

    private int getMaxId() {
        String sql = "SELECT MAX(id) AS max_id FROM expenses";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("max_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    private int getMaxId2()
    {String sql = "SELECT MAX(main_id) AS max_id FROM items";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("max_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;

    }

   // select id from expenses where date = '2024-12-05' querry to get index based on date and also the user needs
    public void add_expense(int value, String type, String desc)
    {       try {   con=DriverManager.getConnection(path,user,password);
            String getexpenseid="select id from expenses where date=? and user_id=?";
            PreparedStatement pst=con.prepareStatement(getexpenseid);
            pst.setObject(1,date);
            pst.setInt(2,user_id);
            int expenseid=0;
           try( ResultSet rs=pst.executeQuery())
           { if (rs.next()) {
               expenseid=rs.getInt("id");
           }

           }catch (SQLException e) {System.out.println("couldnt get expense id!!");}



        if (type.equals("food")) {
            String sqlst = "INSERT INTO items (main_id,expense_id,cat_id,name,cost) VALUES (?,?,?,?, ?) ON CONFLICT DO NOTHING";
            try (PreparedStatement st = con.prepareStatement(sqlst)) {
                int maxid = getMaxId2();
                st.setInt(1, maxid + 1);
                st.setInt(2, expenseid);
                st.setInt(3, 1);
                st.setString(4, desc);
                st.setInt(5, value);

                st.executeUpdate();
              // calc_sums(date);

            } catch (SQLException e) {
                System.out.println("sql problem??");
                e.printStackTrace();
            }

        } else if (type.equals("transport"))
        {String sqlst = "INSERT INTO items (main_id,expense_id,cat_id,name,cost) VALUES (?,?,?,?, ?) ON CONFLICT DO NOTHING";
            try (PreparedStatement st = con.prepareStatement(sqlst)) {
                int maxid = getMaxId2();
                st.setInt(1, maxid + 1);
                st.setInt(2, expenseid);
                st.setInt(3, 2);
                st.setString(4, desc);
                st.setInt(5, value);

                st.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }


        } else if (type.equals("utilities"))
        {String sqlst = "INSERT INTO items (main_id,expense_id,cat_id,name,cost) VALUES (?,?,?,?, ?) ON CONFLICT DO NOTHING";
            try (PreparedStatement st = con.prepareStatement(sqlst)) {
                int maxid = getMaxId2();
                st.setInt(1, maxid + 1);
                st.setInt(2, expenseid);
                st.setInt(3, 3);
                st.setString(4, desc);
                st.setInt(5, value);

                st.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }


        } else if (type.equals("entertainment"))
        {String sqlst = "INSERT INTO items (main_id,expense_id,cat_id,name,cost) VALUES (?,?,?,?, ?) ON CONFLICT DO NOTHING";
            try (PreparedStatement st = con.prepareStatement(sqlst)) {
                int maxid = getMaxId2();
                st.setInt(1, maxid + 1);
                st.setInt(2, expenseid);
                st.setInt(3, 4);
                st.setString(4, desc);
                st.setInt(5, value);

                st.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }


        } else if (type.equals("other"))
        {String sqlst = "INSERT INTO items (main_id,expense_id,cat_id,name,cost) VALUES (?,?,?,?, ?) ON CONFLICT DO NOTHING";
            try (PreparedStatement st = con.prepareStatement(sqlst)) {
                int maxid = getMaxId2();
                st.setInt(1, maxid + 1);
                st.setInt(2, expenseid);
                st.setInt(3, 5);
                st.setString(4, desc);
                st.setInt(5, value);

                st.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }


        } else
            throw new RuntimeException("Unknown expense type");
    }catch (SQLException e) {
        e.printStackTrace();
    }

    }


    public void calc_sums(LocalDate date)
    {       int id_date=0;  //gets the date corresponding to the user id;

        try{  con=DriverManager.getConnection(path,user,password);
            String sqlscript1="select id from expenses where date= ? and user_id=?";
            try(PreparedStatement st=con.prepareStatement(sqlscript1))
            {
                st.setObject(1, date);
                st.setInt(2, user_id);
                ResultSet rs=st.executeQuery();
                if (rs.next())  //if true, there exists something
                  id_date = rs.getInt("id");


            }catch (SQLException e) {
                System.out.println("sql problem on calc, no date found");
                e.printStackTrace();
            }
            String sql1 = "SELECT SUM(cost) AS total_cost1 FROM items WHERE expense_id = ? AND cat_id = 1";
            String sql2 = "SELECT SUM(cost) AS total_cost2 FROM items WHERE expense_id = ? AND cat_id = 2";
            String sql3 = "SELECT SUM(cost) AS total_cost3 FROM items WHERE expense_id = ? AND cat_id = 3";
            String sql4 = "SELECT SUM(cost) AS total_cost4 FROM items WHERE expense_id = ? AND cat_id = 4";
            String sql5 = "SELECT SUM(cost) AS total_cost5 FROM items WHERE expense_id = ? AND cat_id = 5";
                try{
                    PreparedStatement st1=con.prepareStatement(sql1);
                    PreparedStatement st2=con.prepareStatement(sql2);
                    PreparedStatement st3=con.prepareStatement(sql3);
                    PreparedStatement st4=con.prepareStatement(sql4);
                    PreparedStatement st5=con.prepareStatement(sql5);
                    st1.setInt(1, id_date);
                    st2.setInt(1, id_date);
                    st3.setInt(1, id_date);
                    st4.setInt(1, id_date);
                    st5.setInt(1, id_date);
                    int costtotal=0, food=0,transport=0,utilities=0,entertainement=0,other=0;
                    ResultSet rs1=st1.executeQuery();
                    ResultSet rs2=st2.executeQuery();
                    ResultSet rs3=st3.executeQuery();
                    ResultSet rs4=st4.executeQuery();
                    ResultSet rs5=st5.executeQuery();
                                       // System.out.println("as");
                    if (rs1.next()) {
                            this.food=rs1.getInt("total_cost1");
                        if (rs1.wasNull()) { // Check if the result was NULL
                            this.food=0;
                        }}
                    if (rs2.next()) {
                        this.transport=rs2.getInt("total_cost2");
                        if (rs2.wasNull()) { // Check if the result was NULL
                            this.transport=0;
                        }}
                    if (rs3.next()) {
                        this.utilities=rs3.getInt("total_cost3");
                        if (rs3.wasNull()) { // Check if the result was NULL
                            this.utilities=0;
                        }}
                    if (rs4.next()) {
                        this.entertainement=rs4.getInt("total_cost4");
                        if (rs4.wasNull()) { // Check if the result was NULL
                            this.entertainement=0;
                        }}
                    if (rs5.next()) {
                        this.other=rs5.getInt("total_cost5");
                        if (rs5.wasNull()) { // Check if the result was NULL
                            this.other=0;
                        }}
                    this.total=this.food+this.transport+this.utilities+this.entertainement+this.other;
                 //   System.out.println(food+","+transport+","+utilities+","+entertainement+","+other);

                }catch (Exception e)
                {
                    System.out.println("problem problem");
                }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getdatabaseBudget()
    {
        try
        {con=DriverManager.getConnection(path,user,password);
            String querry="select budget from users where id=?";
            PreparedStatement st=con.prepareStatement(querry);
            st.setInt(1, user_id);
            try{
                ResultSet rs=st.executeQuery();
                if (rs.next()) {
                    float localbudget = rs.getFloat("budget");
                    setBudget(localbudget);
                }
            }catch (Exception e) {
                System.out.println("problem getting user");
            }

        }catch(Exception e)
        {
            System.out.println("problem in database regarding  budget");
        }
    }


    public void setConnection(Connection mockConnection) {
                this.con=mockConnection;
    }
}
