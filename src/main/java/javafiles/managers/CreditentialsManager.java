package javafiles.managers;

import java.io.FileReader;
import java.io.InputStream;
import java.util.Properties;
//class made to provide the creditentials for the postgres database
public class CreditentialsManager {

    private String passwordp;
    private String userp;
    private String pathp;

    public String getPassword() {
        return passwordp;
    }
    public String getUser() {
        return userp;

    }
    public String getPath() {
        return pathp;
    }

        public CreditentialsManager() {
        initialize();
        }

    private void initialize()
    {Properties properties=new Properties();
        try(InputStream in =getClass().getClassLoader().getResourceAsStream("creditentials")){
            properties.load(in);
            userp=properties.getProperty("username");

            passwordp=properties.getProperty("password");
            pathp=properties.getProperty("path");

        }catch (Exception e) {
            System.out.println(e);
            System.out.println("something wrong in getting file");
        }



    }



}
