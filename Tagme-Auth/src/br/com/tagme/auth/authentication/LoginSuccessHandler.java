package br.com.tagme.auth.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.tagme.commons.auth.SankhyaPlaceUser;
import br.com.tagme.commons.spring.HttpContextSession;
import br.com.tagme.auth.helper.AuthStringBuilder;

@Component("loginSuccessHandler")
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

	public static final int STATUS_SUCCESS = 1;
	
	@Autowired
	private HttpContextSession contextSession;
	
	@Override
	public @ResponseBody void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

		String authoritiesString = ((SankhyaPlaceUser) authentication).buildAuthoritiesString();
		
		StringBuffer responseString = new StringBuffer("<html><head></head><body>");
		responseString.append(AuthStringBuilder.buildAuthenticated(STATUS_SUCCESS, authentication.getName(), contextSession.getSKPLACE_NOME(), authoritiesString));
		responseString.append("</body></html>");
		
		response.setContentType("text/html");
		response.getWriter().println(responseString.toString());
	}

}
