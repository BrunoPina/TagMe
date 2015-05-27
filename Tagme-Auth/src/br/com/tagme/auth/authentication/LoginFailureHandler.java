package br.com.tagme.auth.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.tagme.auth.helper.AuthStringBuilder;

@Component("loginFailureHandler")
public class LoginFailureHandler implements AuthenticationFailureHandler {

	public static final int STATUS_FAIL = 2;
	
	@Override
	public @ResponseBody void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		
		String exceptionMessage = "";
		
		if(exception instanceof InternalAuthenticationServiceException){
			exceptionMessage = "Erro de autenticação";
		}else if(exception instanceof BadCredentialsException){
			exceptionMessage = "Senha Incorreta";
		}else{
			exceptionMessage = exception.getMessage();
		}

		StringBuffer responseString = new StringBuffer("<html><head></head><body>");
		responseString.append(AuthStringBuilder.buildNotAuthenticated(STATUS_FAIL,exceptionMessage));
		responseString.append("</body></html>");
		
		response.setContentType("text/html");
		response.getWriter().println(responseString.toString());

	}

}
