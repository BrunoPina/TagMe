package br.com.tagme.auth.services;

import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom2.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import br.com.tagme.commons.auth.SankhyaPlaceUser;
import br.com.tagme.commons.http.XMLService;
import br.com.tagme.commons.spring.HttpContextSession;
import br.com.tagme.auth.helper.AuthStringBuilder;

@Service("auth@VerifyAuthzService")
public class VerifyAuthzService extends XMLService{

	public static final int STATUS_LOGIN_VERIFIED 		= 3;
	public static final int STATUS_LOGIN_NOT_VERIFIED 	= 4;
	
	@Autowired
	private HttpContextSession contextSession;
	
	public VerifyAuthzService(){
		unsecured = true;
	}
	
	@Override
	public Element doPost(HttpServletRequest request, HttpServletResponse response, Element requestBody, Map<String, LinkedList<String>> params) {
		Element authzElem = new Element("auth");
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication != null && authentication instanceof SankhyaPlaceUser && authentication.isAuthenticated()){
			SankhyaPlaceUser user = new SankhyaPlaceUser((User) authentication.getPrincipal());
			authzElem.setText(AuthStringBuilder.buildAuthenticated(STATUS_LOGIN_VERIFIED, user.getName(), contextSession.getSKPLACE_NOME(), user.buildAuthoritiesString()));
		}else{
			authzElem.setText(AuthStringBuilder.buildNotAuthenticated(STATUS_LOGIN_NOT_VERIFIED));
		}
		
		return authzElem;
	}

}
