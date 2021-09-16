import org.testng.Assert;
import org.testng.annotations.Test;


import java.io.IOException;
import java.sql.*;

public class InsertDataTest extends ServerConnector {

    public static int gID;
    public static String gFirstName;
    public static String gLastName;
    public static String gPhone;

    @Test
    public static void SetData() {

        try (Statement cstmt = getConnection().createStatement();) {

//             Use autosaved false mode is set in ServerConnector class

            String sql0 = "SELECT firstName, lastName, phone " + "FROM students";
            ResultSet rs0 = cstmt.executeQuery(sql0);
            ResultSetMetaData rsmd = rs0.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            //            Insert in database students new row

            String sql = "INSERT INTO students(ID,firstName,lastName,phone) " + "VALUES (?,?,?,?)";
            PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1,1004);
            pstmt.setString(2,"George");
            pstmt.setString(3,"Hamilton");
            pstmt.setString(4,"5558578965");


            int rowAffected = pstmt.executeUpdate();
            System.out.println(rowAffected);

              if (rowAffected == 1) {

                int NewColumnsNumber = rsmd.getColumnCount();
//                Validate that row wasn't created, new and old column numbers are same because it's not committed
                Assert.assertEquals(NewColumnsNumber,columnsNumber,"wrong number of columns");

//                Call commit()
                pstmt.getConnection().commit();


                   ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    gID = rs.getInt("ID");
                    System.out.println(gID);
                    gFirstName = rs.getString("firstName");
                    gLastName = rs.getString("lastName");
                    gPhone = rs.getString("phone");
                }

//                  Validate all the values of inserted row using TestNG
                Assert.assertEquals(gID, 1004);
                Assert.assertEquals(gFirstName, "George");
                Assert.assertEquals(gLastName, "Hamilton");
                Assert.assertEquals(gPhone, "5558578965");

                // column numbers are different after commit

                  Assert.assertNotEquals(columnsNumber, NewColumnsNumber, "wrong new number of columns");
//                Update firstName of added student

                String sql1 = "UPDATE students firstName='Lewis' WHERE ID=1004";
                PreparedStatement pstmt1 = getConnection().prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
                  pstmt1.getConnection().commit();
                int newRowsAffected = pstmt1.executeUpdate();
                Assert.assertEquals(newRowsAffected, 1);
                  pstmt1.getConnection().commit();
                ResultSet rs1 = pstmt1.getGeneratedKeys();


//                  Validate updated firstName using TestNG
                  if (rs1.next()) {
                    gFirstName = rs1.getString("firstName");
                    System.out.println("new first name: " + gFirstName);
                    Assert.assertEquals("Lewis",gFirstName);
                }
            }

              else {
                  System.out.println("executeUpdate not working");

              }


        }catch (SQLException | IOException throwable) {
            throwable.printStackTrace();
            System.out.println("Exception !!!");
        }


    }
}
