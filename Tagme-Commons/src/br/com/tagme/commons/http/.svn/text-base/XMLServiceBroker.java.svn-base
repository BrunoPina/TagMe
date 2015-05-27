package br.com.tagme.commons.http;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.tagme.commons.auth.AuthcException;
import br.com.tagme.commons.auth.Role;
import br.com.tagme.commons.auth.SankhyaPlaceUser;
import br.com.tagme.commons.utils.Base64Utils;

/**
 * 
 * @author Gildo Neto
 * 
 */

@Controller
public class XMLServiceBroker {

	private static final String	DEFAULT_ACTION			 = "E";

	private static final int	STATUS_ERROR			 = 0;
	private static final int	STATUS_OK				 = 1;
	private static final int	STATUS_NOT_AUTHORIZED	 = 2;
	private static final int	STATUS_NOT_AUTHENTICATED = 3;

	@Autowired
	private ApplicationContext	context;

	@RequestMapping(value = "/broker/{module}/{serviceName}", method = RequestMethod.POST, consumes = MediaType.TEXT_XML_VALUE, produces = MediaType.TEXT_XML_VALUE)
	public @ResponseBody String broker(HttpServletRequest request, HttpServletResponse response, @RequestBody String requestBodyAsString, @PathVariable("module") String module, @PathVariable("serviceName") String serviceName, @MatrixVariable(pathVar = "serviceName", required = false) Map<String, LinkedList<String>> serviceAttributes) throws UnsupportedEncodingException {

		Document responseDoc = new Document();
		Element serviceResponse = new Element("serviceResponse");
		responseDoc.setRootElement(serviceResponse);

		Element responseBody = new Element("responseBody");
		serviceResponse.addContent(responseBody);

		String serviceId = new StringBuffer(module).append("@").append(serviceName).toString();
		int respStatus = STATUS_NOT_AUTHORIZED;
		String respStatusMessage = "Não autorizado";

		try {

			Document requestDoc = null;
			requestBodyAsString = (requestBodyAsString == null || requestBodyAsString.isEmpty()) ? "<requestBody/>" : requestBodyAsString;
			try {
				requestDoc = new SAXBuilder().build(new StringReader(requestBodyAsString));
			} catch (Exception e) {
				RuntimeException ex = new RuntimeException("Não foi possível parsear o arquivo XML de entrada. Formato de entrada inválido. Serviço: " + serviceId);
				ex.initCause(e);
				throw ex;
			}

			String requestContext = XMLService.DEFAULT_CONTEXT;
			String action = DEFAULT_ACTION;
			if(serviceAttributes != null && serviceAttributes.containsKey("requestContext")){
				LinkedList<String> requestContextList = serviceAttributes.get("requestContext");
				String prefix = requestContextList.poll();
				String locator = requestContextList.poll();
				
				if(prefix != null){
					requestContext = prefix;
				}
				if(locator != null){
					action = locator;
				}
			}
			
			XMLService service = null;
			try {
				service = (XMLService) context.getBean(serviceId);
				if(!service.isRequestContextAllowed(requestContext)){
					throw new AccessDeniedException("Serviço não autorizado.");
				}
			} catch (Exception e) {
				RuntimeException ex = new RuntimeException("Não foi possível encontrar o serviço " + serviceId);
				ex.initCause(e);
				throw ex;
			}

			/**
			 * O serviço deve possuir algum registered context (contextos que o executam) ao mesmo para ser seguro. Caso nao tenha, qualquer um pode executa-lo.
			 */
			if (service.isSecured()) {
				
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				if(authentication == null || !(authentication instanceof SankhyaPlaceUser) || !authentication.isAuthenticated()){
					throw new AuthcException();
				}
				
				SankhyaPlaceUser user = (SankhyaPlaceUser) authentication;
				
				if(service.hasAnyRequiredAuthorities()){
					Role requiredRole = new Role(service.getModuleContext(), requestContext, action);
					/**
					 * O requestContext vem como param, ou seja, o usuario pode mudar na marra e 'burlar' a requiredRole. 
					 * Se ele mudar, o serviço nao será encontrado pois o requestContext nao estará registrado para ele.
					 */
					if(!user.hasAuthority(requiredRole)){
						throw new AccessDeniedException("Serviço não autorizado.");
					}
				}else if(!user.isAuthenticated()){
					throw new AccessDeniedException("Serviço não autorizado.");
				}
				
			}
			
			Element serviceRequest = requestDoc.getRootElement();
			Element requestBody = serviceRequest.getChild("requestBody");
			Element responseElem = service.doPost(request, response, requestBody.detach(), serviceAttributes);

			if (responseElem != null){
				responseBody.addContent(responseElem);
			}
			
			respStatus = STATUS_OK;
			respStatusMessage = null;
				
		} catch (Exception ex) {
			ex.printStackTrace();
			
			if(ex instanceof AuthcException){
				respStatus = STATUS_NOT_AUTHENTICATED;
			}else if(ex instanceof AccessDeniedException){
				respStatus = STATUS_NOT_AUTHORIZED;
			}else{
				respStatus = STATUS_ERROR;
			}
			
			if (ex.getMessage() == null) {
				respStatusMessage = new StringBuffer("Erro interno ao executar serviço ").append(serviceId).toString();
			} else {
				respStatusMessage = new StringBuffer(ex.getMessage()).toString();
			}
		}

		serviceResponse.setAttribute("status", String.valueOf(respStatus));
		if (respStatusMessage != null) {
			Element statusElem = new Element("statusMessage");
			statusElem.setText(Base64Utils.encode(respStatusMessage.getBytes("ISO-8859-1")));
			serviceResponse.addContent(statusElem);
		}

		Format f = Format.getPrettyFormat();
		f.setEncoding("ISO-8859-1");
		XMLOutputter outputter = new XMLOutputter(f);
		return outputter.outputString(responseDoc);
	}

}
