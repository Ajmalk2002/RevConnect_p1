
package com.revconnect.config;
import java.sql.*;
public class DBConnection {
 static {
  try { Class.forName("oracle.jdbc.driver.OracleDriver"); }
  catch(Exception e){ e.printStackTrace(); }
 }
 public static Connection getConnection() throws SQLException {
  return DriverManager.getConnection(
   "jdbc:oracle:thin:@localhost:1521:XE","system","system");
 }
}
