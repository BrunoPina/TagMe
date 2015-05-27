package br.com.tagme.commons.services;

import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom2.Element;
import org.springframework.stereotype.Service;

import br.com.tagme.commons.http.SWServiceInvoker;
import br.com.tagme.commons.http.SchemeReference;
import br.com.tagme.commons.http.XMLService;

@Service("commons@SWProxyLogoutService")
public class SWProxyLogoutService extends XMLService {

	private final SWServiceInvoker	serviceInvoker	= new SWServiceInvoker(SchemeReference.SKW_URL, SchemeReference.SKW_USERNAME, SchemeReference.SKW_RAWPASSWORD);
	
	@Override
	public Element doPost(HttpServletRequest request, HttpServletResponse response, Element requestBody, Map<String, LinkedList<String>> params) {

		Element parametros = requestBody.getChild("parametros");
		String jsessionid = parametros.getChildText("jsessionid");

		serviceInvoker.setJSessionID(jsessionid);
		
		try {
			serviceInvoker.doLogout();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
