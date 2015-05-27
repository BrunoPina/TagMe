package br.com.tagme.auth.remember;

import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.stereotype.Component;

import br.com.tagme.auth.db.ConnectionTemplate;

@Component
public class SankhyaPlaceTokenRepository extends JdbcTokenRepositoryImpl{
	
	public SankhyaPlaceTokenRepository(){
		setCreateTableOnStartup(false);
		setJdbcTemplate(ConnectionTemplate.getTemplate());
	}

}
