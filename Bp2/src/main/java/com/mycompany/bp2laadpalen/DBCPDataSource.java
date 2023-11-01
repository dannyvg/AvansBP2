package com.mycompany.bp2laadpalen;

import java.sql.SQLException;
import java.sql.Connection;
import org.apache.commons.dbcp2.BasicDataSource;

public class DBCPDataSource {
    private static BasicDataSource ds = new BasicDataSource();
    static {
        ds.setUrl("");
        ds.setUsername("");
        ds.setPassword("");
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxOpenPreparedStatements(100);
    }
    
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
    
    private DBCPDataSource(){
        
    }
}
