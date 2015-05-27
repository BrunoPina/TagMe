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

@Service("commons@SWProxyLoginService")
public class SWProxyLoginService extends XMLService {

	private final SWServiceInvoker	serviceInvoker	= new SWServiceInvoker(SchemeReference.SKW_URL, SchemeReference.SKW_USERNAME, SchemeReference.SKW_RAWPASSWORD);
	
	@Override
	public Element doPost(HttpServletRequest request, HttpServletResponse response, Element requestBody, Map<String, LinkedList<String>> params) {

		Element mgeSession = new Element("MGESESSION");
		try {
			serviceInvoker.doLogin();
			mgeSession.setText(serviceInvoker.getJSessionID());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mgeSession;
	}

}
