package br.com.tagme.commons.spring;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Gildo Neto
 *
 */

@Component(value = "ConnectionTemplateFactory")
public class ConnectionTemplateFactory {

	private static JdbcTemplate	template;

	public ConnectionTemplateFactory() {
	}

	public ConnectionTemplateFactory(DriverManagerDataSource dataSource) {
		if (template == null) {
			template = new JdbcTemplate(dataSource);
		}
	}

	public static JdbcTemplate getTemplate() {
		return template;
	}
}
