package com.chat.springboot.common.jdbc;

import org.springframework.beans.factory.annotation.Value;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBCConnectionUtils {

//    @Value("${jdbc.driverClassName}")
    private static String DRIVER_CLASS_NAME="com.mysql.cj.jdbc.Driver";

//    @Value("${jdbc.url}")
    private static String URL="jdbc:mysql://127.0.0.1:3306/qxerp?useUnicode=true&characterEncoding=utf8&useSSL=true&serverTimezone=GMT";

//    @Value("${jdbc.userName}")
    private static String USER_NAME="root";

//    @Value("${jdbc.password}")
    private static String PASSWORD="root";

    private static Connection conn = null;

    private static PreparedStatement pst = null;

    public static PreparedStatement getPreparedStatement(String sql){
        try {
            Class.forName(DRIVER_CLASS_NAME);
            conn = DriverManager.getConnection(URL,USER_NAME,PASSWORD);
            pst = conn.prepareStatement(sql);
        } catch (ClassNotFoundException
                | SQLException e) {
            e.printStackTrace();
        }
        return pst;
    }

    public static void closeConnection(){
        try {
            if (null != conn){
                conn.close();
            }
            if (null != pst){
                pst.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
