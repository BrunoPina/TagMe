package br.com.tagme.commons.http;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom2.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import br.com.tagme.commons.types.Resources;

public abstract class XMLService {
	
	@Autowired
	protected ApplicationContext context;
	protected boolean unsecured = false;
	
	protected ArrayList<String> 	authorizedRequestContext   = new ArrayList<String>();
	protected Resources 			moduleContext 			   = Resources.HTTPSERVICE;
	
	public static final String			DEFAULT_CONTEXT			   = "NOCONTEXT";
	
	public abstract Element doPost(HttpServletRequest request, HttpServletResponse response, Element requestBody, Map<String, LinkedList<String>> params);
	
	public boolean isRequestContextAllowed(String currSource){
		boolean allowed = false;
		if(authorizedRequestContext.isEmpty()){
			allowed = true;
		}else if(currSource == null || DEFAULT_CONTEXT.equals(currSource)){
			allowed = false;
		}else{
			allowed = authorizedRequestContext.contains(currSource);
		}
		return allowed;
	}
	
	public boolean isSecured(){
		return !authorizedRequestContext.isEmpty() && !unsecured;
	}
	
	public boolean hasAnyRequiredAuthorities(){
		return !authorizedRequestContext.isEmpty();
	}
	
	public Resources getModuleContext(){
		return moduleContext;
	}
	
}
