package br.com.tagme.commons.http;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.codec.binary.Base64;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SWServiceInvoker {
	private String	domain;
	private String	user;
	private String	pass;
	private boolean	debug;
	private boolean	silentMode;
	private String jsessionid;

	public SWServiceInvoker(String domain, String user, String pass) {
		this.domain = domain;
		this.user = user;
		this.pass = pass;
	}

	public void setSilentMode(boolean silentMode) {
		this.silentMode = silentMode;
	}

	public void setDebugMode() {
		this.debug = true;
	}

	public org.jdom2.Document call(String serviceName, String module, String body) throws Exception {
		doLogin();
		
		URLConnection conn = openConn(serviceName, module, jsessionid);

		Document docResp = callService(conn, body, serviceName);

		doLogout();

		return w3cToJdom2(docResp);
	}
	
	public org.jdom2.Document call2(String serviceName, String module, String body, String jsessionid) throws Exception {

		URLConnection conn = openConn(serviceName, module, jsessionid);

		Document docResp = callService(conn, body, serviceName);

		return w3cToJdom2(docResp);
	}

	public void doLogin() throws Exception {
		URLConnection conn = openConn("MobileLoginSP.login", "mge", null);

		StringBuffer bodyBuf = new StringBuffer();

		bodyBuf.append("<NOMUSU>").append(user).append("</NOMUSU>");
		bodyBuf.append("<INTERNO>").append(pass).append("</INTERNO>");

		Document docResp = callService(conn, bodyBuf.toString(), "MobileLoginSP.login");

		Node jsessionNode = (Node) xpath(docResp, "//jsessionid", XPathConstants.NODE);

		jsessionid = jsessionNode.getTextContent().trim();
		
	}

	public void doLogout() throws Exception {
		try {
			URLConnection conn = openConn("MobileLoginSP.logout", "mge", jsessionid);

			callService(conn, null, "MobileLoginSP.logout");
		} catch (Exception e) {
			e.printStackTrace(); // pode ser ignorado
		}
	}
	
	public String getJSessionID(){
		return this.jsessionid;
	}
	
	public void setJSessionID(String jsessionid){
		this.jsessionid = jsessionid;
	}

	private void checkResultStatus(Node sr) throws Exception {
		Node statusNode = sr.getAttributes().getNamedItem("status");

		String status = statusNode.getTextContent().trim();

		if (!"1".equals(status) && !silentMode) {
			String msg = getChildNode("statusMessage", sr).getTextContent();
			throw new Exception(decodeB64(msg.trim()));
		}
	}

	private Node getChildNode(String name, Node parent) {
		NodeList l = parent.getChildNodes();

		for (int i = 0; i < l.getLength(); i++) {
			Node n = l.item(i);

			if (n.getNodeName().equalsIgnoreCase(name)) {
				return n;
			}
		}

		return null;
	}

	private String decodeB64(String s) {
		return new String(Base64.decodeBase64(s.getBytes()));
	}

	private Object xpath(Document d, String query, QName type) throws Exception {
		XPath xp = XPathFactory.newInstance().newXPath();

		XPathExpression xpe = xp.compile(query);
		return xpe.evaluate(d, type);
	}

	private void printNode(Node n) {
		System.out.println(n.toString());

		NodeList l = n.getChildNodes();

		if (l != null && l.getLength() > 0) {
			for (int i = 0; i < l.getLength(); i++) {
				printNode(l.item(i));
			}
		}
	}

	private Document callService(URLConnection conn, String body, String serviceName) throws Exception {
		OutputStream out = null;
		InputStream inp = null;

		try {
			out = conn.getOutputStream();
			OutputStreamWriter wout = new OutputStreamWriter(out, "ISO-8859-1");

			String requestBody = buildRequestBody(body, serviceName);

			if (debug) {
				System.out.println(requestBody);
			}
			wout.write(requestBody);

			wout.flush();

			inp = conn.getInputStream();

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();

			Document doc = null;
			NodeList nodes = null;

			try {
				doc = db.parse(inp);
				
				if(debug) {
					printDocument(doc);
				}

				nodes = doc.getElementsByTagName("serviceResponse");
				
				if(debug) {
					printNode(nodes.item(0));
				}
			
				if (nodes == null || nodes.getLength() == 0) {
					throw new Exception("XML de resposta não possui um elemento de resposta");
				}
			} catch (Exception e) {
				Exception error = new Exception("Erro ao interpretar resposta do servidor");
				error.initCause(e);
				throw error;
			}

			checkResultStatus(nodes.item(0));

			return doc;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
				}
			}

			if (inp != null) {
				try {
					inp.close();
				} catch (Exception e) {
				}
			}
		}
	}

	private String buildRequestBody(String body, String serviceName) {
		StringBuffer buf = new StringBuffer();

		buf.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
		buf.append("<serviceRequest serviceName=\"").append(serviceName).append("\">\n");
		buf.append("<requestBody>\n");
		buf.append(body == null ? "" : body);
		buf.append("</requestBody>\n");
		buf.append("</serviceRequest>");

		return buf.toString();
	}

	private URLConnection openConn(String serviceName, String module, String sessionID) throws Exception {
		StringBuffer buf = new StringBuffer();

		buf.append(SchemeReference.SKW_SIMPLE_URL).append(module == null ? "mge" : module).append("/service.sbr");

		/*if (sessionID != null) {
			buf.append(";JSESSIONID=").append(sessionID);
		}*/

		buf.append("?serviceName=").append(serviceName);

		if (sessionID != null) {
			buf.append("&mgeSession=").append(sessionID);
		}

		URL u = new URL(buf.toString());

		URLConnection uc = u.openConnection();
		HttpURLConnection connection = (HttpURLConnection) uc;
		
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("content-type", "text/xml");
		
		if(sessionID != null) {
			connection.setRequestProperty("Cookie", "JSESSIONID=" + sessionID);
		}
		
		connection.setRequestProperty("User-Agent", "SWServiceInvoker");

		return connection;
	}

	public void createEntity(Element dataSet, String entityPath, String fieldName){
		
		Element entity = new Element("entity");
		entity.setAttribute("path", entityPath);
		
		Element field = new Element("field");
		field.setAttribute("name", fieldName);
		entity.addContent(field);
		
		dataSet.addContent(entity);
		
	}
	
	public void createField(Element fields, String fieldName){
		
		Element field = new Element("field");
		field.setAttribute("name", fieldName);
		fields.addContent(field);

	}
	
	public void createCampo(Element entity, String nome, String valor){
		
		Element campo = new Element("campo");
		campo.setAttribute("nome", nome);
		campo.setText(valor);
		entity.addContent(campo);
		
	}
	
	public Element createReferenceFetch(Element entity, String referenceInst){
		
		Element referenceFetch = new Element("referenceFetch");
		referenceFetch.setAttribute("name", referenceInst);
		entity.addContent(referenceFetch);
		
		return referenceFetch;
		
	}
	
	private static void printDocument(Document doc) throws Exception {
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		StreamResult result = new StreamResult(new StringWriter());
		DOMSource source = new DOMSource(doc);
		transformer.transform(source, result);
		String xmlString = result.getWriter().toString();
		System.out.println("----inicio---");
		System.out.println(xmlString);
		System.out.println("----fim-----");
	}
	
	private org.jdom2.Document w3cToJdom2(Document doc) throws Exception {
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		StreamResult result = new StreamResult(new StringWriter());
		DOMSource source = new DOMSource(doc);
		transformer.transform(source, result);
		String xmlString = result.getWriter().toString();
		return new SAXBuilder().build(new StringReader(xmlString));
	}
	
	public String docToString(org.jdom2.Document doc){
		XMLOutputter xmlOut = new XMLOutputter(Format.getPrettyFormat().setOmitDeclaration(true));
		return xmlOut.outputString(doc);
	}
}
