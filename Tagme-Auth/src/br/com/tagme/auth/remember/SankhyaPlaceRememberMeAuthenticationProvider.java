package br.com.tagme.auth.remember;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import br.com.tagme.commons.auth.SankhyaPlaceUser;
import br.com.tagme.auth.helper.AfterAuthenticationHelper;

@Component("sankhyaPlaceRememberMeAuthenticationProvider")
public class SankhyaPlaceRememberMeAuthenticationProvider extends RememberMeAuthenticationProvider {
	
	@Autowired
	private AfterAuthenticationHelper afterAuthenticationHelper;
	
	public SankhyaPlaceRememberMeAuthenticationProvider(){
		super(SankhyaPlaceRememberMeServices.key);
	}
	
	@Override
	public Authentication authenticate(Authentication providedAuth) throws AuthenticationException {

		RememberMeAuthenticationToken providedRememberMeAuth = (RememberMeAuthenticationToken) providedAuth;
		Authentication auth = super.authenticate(providedRememberMeAuth);
		SankhyaPlaceUser user = new SankhyaPlaceUser((User) auth.getPrincipal());
		user.setAuthenticated(true);
		afterAuthenticationHelper.initContextTo(user.getName());
		return user;
	}
	
}
