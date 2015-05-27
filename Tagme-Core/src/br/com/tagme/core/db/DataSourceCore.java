package br.com.tagme.core.db;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

@Component("DataSourceCore")
public class DataSourceCore extends DriverManagerDataSource {

	private static final String driverClassName = "com.mysql.jdbc.Driver";
	/*private static final String username = "snkstore";
	private static final String password = "oivalf1234567";
	private static final String url = "jdbc:mysql://sankhyalibrarydb.cekq3idr0yxc.sa-east-1.rds.amazonaws.com:3306/snklibrarydb";*/
	private static final String username = "tagme";
	private static final String password = "tagme1402";
	private static final String url = "jdbc:mysql://tagme.cbahp3rf5qcg.sa-east-1.rds.amazonaws.com/tagme";
	
	public DataSourceCore() {
		setDriverClassName(driverClassName);
		setUsername(username);
		setPassword(password);
		setUrl(url);
	}
}
