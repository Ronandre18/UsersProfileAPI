package com.system.API.utility;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.google.gson.Gson;

@Configuration
public class JdbcConfig {

	@Autowired
	DbProperties db;
	
	@Bean
	DataSource MssqlDataSource() throws SQLException {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    	dataSource.setUrl(db.getServer() + ";databaseName=" + db.getDbName());
			dataSource.setUsername(db.getUserName());
			dataSource.setPassword(db.getPassWord());
			return dataSource;
		
	}
}
