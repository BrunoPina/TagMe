package br.com.tagme.auth.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import br.com.tagme.auth.dao.IAuthcDao;

@Component("sankhyaPlaceUserDetailService")
public class SankhyaPlaceUserDetailService implements UserDetailsService{

	@Autowired
	private IAuthcDao	daoUsuario;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = daoUsuario.getUserByUsername(username);
		return user;
	}

}
