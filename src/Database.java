import com.ibm.db2.jcc.DB2Driver;

import java.sql.*;
import java.util.ArrayList;

class Database {

    private Connection conn;

    // Creates database connection when creating object
    public Database() throws SQLException {
        DriverManager.registerDriver(new DB2Driver());

        conn = DriverManager.getConnection(
                "jdbc:db2://winter2024-comp421.cs.mcgill.ca:50000/COMP421",
                "cs421g49",
                "!Codingisfun"
        );
    }

    // Closes database connection
    public void dbClose() throws SQLException {
        conn.close();
    }

    // Insert or Update values in the database
    public boolean dbInsertOrUpdate(String sqlString) throws SQLException {
        Statement stmt = conn.createStatement();
        boolean result = false;

        try {
            // Execute Statement
            result = stmt.execute(sqlString);
        } catch (SQLException e) {
            // If an exception occurs, close connection and then throw the error
            stmt.close();
            conn.close();
            System.err.println(
                "msg: " + e.getMessage() +
                "code: " + e.getErrorCode() +
                "state: " + e.getSQLState());
            System.out.println("SQL Insert or Update Had Failed");
            System.exit(e.getErrorCode());
        }

        stmt.close();

        return result;
    }

    // Query data from the database
    public ArrayList<ArrayList<String>> dbQuery(String sqlString) throws SQLException {
        Statement stmt = conn.createStatement();

        ArrayList<ArrayList<String>> data = null;
        try {
            // Execute Query
            ResultSet result = stmt.executeQuery(sqlString);

            // Formats query to return
            data = new ArrayList<>();

            while (result.next()) {

                ArrayList<String> row = new ArrayList<>();

                for (int i = 1; i <= result.getMetaData().getColumnCount(); i++) {

                    row.add(result.getObject(i).toString());

                }

                data.add(row);

            }

        } catch (SQLException e) {
            // If an exception occurs, close connection and then throw the error
            stmt.close();
            conn.close();
            System.out.println("ERROR: " + e.getMessage());
            System.out.println("SQL Query Had Failed");
            System.exit(e.getErrorCode());
        }

        stmt.close();

        return data;
    }

}
