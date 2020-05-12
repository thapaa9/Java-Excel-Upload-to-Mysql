
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pavilion '& B0'
 */
public class Data {
     public static Connection getCon() throws ClassNotFoundException
    {
        Connection con;
       try
       {
           Class.forName("com.mysql.jdbc.Driver");
          con=DriverManager.getConnection("jdbc:mysql://localhost:3306/sol??useUnicode=yes&characterEncoding=UTF-8","pashupati","myname");
          return con;
       }
       catch (SQLException e)
       {
           return null;
       }
    }
}
