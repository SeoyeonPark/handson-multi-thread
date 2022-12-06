package org.sample.infrastructure;

import org.sample.domain.models.Employee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeDao {

    private static String dburl = "jdbc:mysql:aws://localhost:3306/trucking?useSSL=false&allowPublicKeyRetrieval=true";
//    private static String dburl = "jdbc:h2:tcp://localhost/~/test;AUTO_SERVER=TRUE";
    private static String dbUser = "trucking";
    private static String dbpasswd = "welcome";

    public void createDatabase() {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getConnection();
            String queryCreateTable = "CREATE TABLE IF NOT EXISTS employee_multithread" +
                    "(employeeId INTEGER not NULL ," +
                    "NAME VARCHAR(200))";
            ps = conn.prepareStatement(queryCreateTable);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeAll(conn, ps);
        }
    }


    public int insert(Employee employee) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getConnection();
            String query = "INSERT INTO employee_multithread VALUES (?, ?)";
            ps = conn.prepareStatement(query);
            ps.setInt(1, employee.getEmployeeId());
            ps.setString(2, employee.getName());
            int count = ps.executeUpdate();
            if (count > 0) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeAll(conn, ps);
        }
        return 0;
    }

    private Connection getConnection() throws Exception {
//        Class.forName("org.h2.Driver");
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection(dburl, dbUser, dbpasswd);
        return conn;
    }

    private void closeAll(Connection conn, PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}