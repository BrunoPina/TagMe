package br.com.tagme.auth.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.tagme.commons.spring.ConnectionTemplateFactory;

@Component("ConnectionTemplateAuth")
@TransactionalAuth
public class ConnectionTemplate extends ConnectionTemplateFactory {

	@Autowired
	public ConnectionTemplate(DataSourceAuth dataSource) {
		super(dataSource);
	}

}
