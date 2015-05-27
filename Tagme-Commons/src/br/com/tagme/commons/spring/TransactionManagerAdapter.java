package br.com.tagme.commons.spring;

import java.io.Serializable;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class TransactionManagerAdapter extends DataSourceTransactionManager implements Serializable{

	public TransactionManagerAdapter(DriverManagerDataSource dataSource) {
		setDataSource(dataSource);
	}

}
