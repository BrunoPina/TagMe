package br.com.tagme.auth.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.tagme.auth.helper.AfterAuthenticationHelper;
import br.com.tagme.commons.auth.SankhyaPlaceUser;

@Component("sankhyaPlaceAuthenticationProvider")
public class SankhyaPlaceAuthenticationProvider extends DaoAuthenticationProvider {

	static final PasswordEncoder	passwordEncoder	= new BCryptPasswordEncoder();
	
	@Autowired
	private AfterAuthenticationHelper afterAuthenticationHelper;
	
	
	@Autowired
	public SankhyaPlaceAuthenticationProvider(SankhyaPlaceUserDetailService userDetailService) {
		setUserDetailsService(userDetailService);
		setPasswordEncoder(passwordEncoder);
	}

	@Override
	public Authentication authenticate(Authentication providedAuth) throws AuthenticationException{
		
		UsernamePasswordAuthenticationToken providedUsernamePassword = (UsernamePasswordAuthenticationToken) providedAuth;
		User foundUser = (User) super.retrieveUser(providedAuth.getName(), providedUsernamePassword);
		super.additionalAuthenticationChecks(foundUser, providedUsernamePassword);
		SankhyaPlaceUser user = new SankhyaPlaceUser(foundUser);
		user.setAuthenticated(true);
		afterAuthenticationHelper.initContextTo(user.getName());
		return user;
	}
}