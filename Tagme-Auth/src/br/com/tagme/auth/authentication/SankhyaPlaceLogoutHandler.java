package br.com.tagme.auth.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.tagme.auth.remember.SankhyaPlaceRememberMeServices;
import br.com.tagme.auth.remember.SankhyaPlaceTokenRepository;

@Component("sankhyaPlaceLogoutHandler")
public class SankhyaPlaceLogoutHandler implements LogoutSuccessHandler {
	
	private SankhyaPlaceTokenRepository tokenRepository;
	
	@Autowired
	public SankhyaPlaceLogoutHandler(SankhyaPlaceTokenRepository tokenRepository){
		this.tokenRepository = tokenRepository;
	}
	
	@Override
	public @ResponseBody void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		CookieClearingLogoutHandler cookieClearingLogoutHandler = new CookieClearingLogoutHandler(SankhyaPlaceRememberMeServices.SKPLACE_COOKIE);
		SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
		if(authentication != null){
			cookieClearingLogoutHandler.logout(request, response, authentication);
			securityContextLogoutHandler.logout(request, response, authentication);
			tokenRepository.removeUserTokens(authentication.getName());
		}
		
		response.setContentType("text/html");
		response.getWriter().println(
				"<html><head></head><body>" + 
				"</body></html>"
		);
	}

}
