package br.com.tagme.auth.db;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.tagme.commons.spring.TransactionManagerAdapter;

@Component("TransactionManagerAuth")
public class TransactionManager extends TransactionManagerAdapter implements Serializable{

	@Autowired
	public TransactionManager(DataSourceAuth dataSource) {
		super(dataSource);
	}

}
