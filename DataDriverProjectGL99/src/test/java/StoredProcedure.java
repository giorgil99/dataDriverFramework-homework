import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StoredProcedure extends ServerConnector {


    public static int ID = 1004;

    @Test
    public static void getStoredProcedure() {

// Use localhost with port 1433 in database url - set in db.properties file

        CallableStatement clstmt = null;
        String firstName = null;
        String lastName;
        try {
//            Create stored procedure that receives student id as a parameter and returns lastname

            String query = "{ call SearchByID(?) }";
            clstmt = getConnection().prepareCall(query);
            clstmt.setInt(1,ID);

//            Call procedure from java class

          ResultSet rs = clstmt.executeQuery();


          if (rs.next())
          {

            firstName = rs.getString(1);
            lastName =rs.getString(2);

           System.out.println(">>> " + firstName +" " + lastName +" with ID: "+ ID);
          }

            if(firstName==null){
                System.out.println("Query failed ");
            }

          




        } catch (SQLException | IOException throwable) {
            throwable.printStackTrace();
            if (clstmt != null) {
                try {
                    clstmt.getConnection().rollback();
                    System.out.println("Exception with rollback");
                }

                catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Exception without rollback !!!");

                }
            }

        }


    }
}
