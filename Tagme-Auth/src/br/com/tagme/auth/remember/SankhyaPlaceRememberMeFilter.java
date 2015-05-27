package br.com.tagme.auth.remember;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.stereotype.Component;

import br.com.tagme.auth.authentication.LoginSuccessHandler;

@Component("sankhyaPlaceRememberMeFilter")
public class SankhyaPlaceRememberMeFilter extends RememberMeAuthenticationFilter {
	
	@Autowired
	public SankhyaPlaceRememberMeFilter(@Qualifier("authenticationManager") AuthenticationManager authenticationManager, SankhyaPlaceRememberMeServices rememberMeServices, LoginSuccessHandler successHandler){
		super(authenticationManager, rememberMeServices);
	}
	
	@Override
	protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) {
		super.onSuccessfulAuthentication(request, response, authResult);
	}

}
