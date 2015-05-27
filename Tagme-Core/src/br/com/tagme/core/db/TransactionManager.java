package br.com.tagme.core.db;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.tagme.commons.spring.TransactionManagerAdapter;

@Component("TransactionManagerCore")
public class TransactionManager extends TransactionManagerAdapter implements Serializable{

	@Autowired
	public TransactionManager(final DataSourceCore dataSource) {
		super(dataSource);
	}

}
