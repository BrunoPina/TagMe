package br.com.tagme.core.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.tagme.commons.spring.ConnectionTemplateFactory;

@Component(value = "ConnectionTemplateCore")
public class ConnectionTemplate extends ConnectionTemplateFactory {

	@Autowired
	public ConnectionTemplate(DataSourceCore dataSource) {
		super(dataSource);
	}

}
