package br.com.tagme.auth.authentication;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import br.com.tagme.auth.remember.SankhyaPlaceRememberMeServices;

@Component("sankhyaPlaceUsernamePasswordFilter")
public class SankhyaPlaceUsernamePasswordFilter extends UsernamePasswordAuthenticationFilter {

	@Autowired
	public SankhyaPlaceUsernamePasswordFilter(@Qualifier("authenticationManager") AuthenticationManager authenticationManager, LoginSuccessHandler successHandler, LoginFailureHandler failureHandler, SankhyaPlaceRememberMeServices rememberMeServices){
		setAuthenticationManager(authenticationManager);
		setAuthenticationSuccessHandler(successHandler);
		setAuthenticationFailureHandler(failureHandler);
		setRememberMeServices(rememberMeServices);		
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
		super.successfulAuthentication(request, response, chain, authResult);
	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		super.unsuccessfulAuthentication(request, response, failed);
	}
	
}
