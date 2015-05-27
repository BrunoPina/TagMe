package br.com.tagme.commons.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import br.com.tagme.commons.http.SWServiceInvoker;
import br.com.tagme.commons.http.SchemeReference;

public class SWServiceHelper {
	
	private final SWServiceInvoker	serviceInvoker	= new SWServiceInvoker(SchemeReference.SKW_URL, SchemeReference.SKW_USERNAME, SchemeReference.SKW_RAWPASSWORD);
	
	private Document doc;
	private Element entity;
	private Element fields;
	private boolean isList = false;
	private Element dataSet;
	private Element dataSetCriteria;
	
	private Element responseEntidades = null;

	private Map<String, String> responseObjMap = new HashMap<String, String>();

	public SWServiceHelper(){
		doc = new Document();
		entity = new Element("entity");
		fields = new Element("fields");
	}

	public void setEntity(String entityName){
		setEntity(entityName, false);
	}
	
	public void setEntity(String entityName, boolean getPresentations){
		entity.setAttribute("name", entityName);
		if(getPresentations)
			entity.setAttribute("getPresentations", Boolean.toString(getPresentations));
		doc.addContent(entity);
	}
	
	public void setEntityOrderBy(String orderByExpression){
		entity.setAttribute("orderby", orderByExpression);
	}
	
	public void setDataSet(String entityName, String orderByExpression){
		dataSet = new Element("dataSet");
		dataSet.setAttribute("rootEntity", entityName);
		dataSet.setAttribute("orderByExpression", orderByExpression);
		dataSet.setAttribute("includePresentationFields", "S");
		dataSet.setAttribute("parallelLoader", "true");
		doc.addContent(dataSet);
	}
	
	public void dataSetEntities(Map<String, String> mapEntities){
		
		int i = 0;
		for(String entityName : mapEntities.keySet()){
			Element entity = new Element("entity");
			String path = entityName;
			if(i==0){
				path = "";
			}
			entity.setAttribute("path", path);
			
			if(mapEntities.get(entityName).contains(",")){
				Element fieldset = new Element("fieldset");
				fieldset.setAttribute("list", mapEntities.get(entityName));
				entity.addContent(fieldset);
			}else{
				Element field = new Element("field");
				field.setAttribute("name", mapEntities.get(entityName));
				entity.addContent(field);
			}
			dataSet.addContent(entity);
			i++;
		}
	}
	
	public void dataSetCriteria(){
		dataSetCriteria = new Element("criteria");
		dataSet.addContent(dataSetCriteria);
	}
	
	public void dataSetExpression(String expression){
		Element expressionElem = new Element("expression");
		expressionElem.setText(expression);
		dataSetCriteria.addContent(expressionElem);
	}
	
	public void dataSetParameter(String type, String parameter){
		Element parameterElem = new Element("parameter");
		parameterElem.setAttribute("type", type);
		parameterElem.setText(parameter);
		dataSetCriteria.addContent(parameterElem);
	}
	
	public void setCriterio(String nome, String valor){
		Element criterio = new Element("criterio");
		criterio.setAttribute("nome", nome);
		criterio.setAttribute("valor", valor);	
		entity.addContent(criterio);
	}
	
	public void setLiteralCriteria(String expression){
		Element literalCriteria = new Element("literalCriteria");
		entity.addContent(literalCriteria);
		
		Element expressionElem = new Element("expression");
		expressionElem.setText(expression);
		literalCriteria.addContent(expressionElem);
	}
	
	public void findField(String fieldName){
		Element field = new Element("field");
		field.setAttribute("name", fieldName);
		fields.addContent(field);
	}
	
	public void findFields(String[] fieldsToFind){
		for(String fieldName : fieldsToFind){
			findField(fieldName);
		}
	}
	
	public void referenceFetch(String referenceInst, String[] fieldsToFind){
		Element referenceFetch = new Element("referenceFetch");
		referenceFetch.setAttribute("name", referenceInst);
		entity.addContent(referenceFetch);
		
		for(String fieldName : fieldsToFind){
			Element field = new Element("field");
			field.setAttribute("name", fieldName);
			referenceFetch.addContent(field);
		}
	}
	
	public List<Element> callCrudFind(){
		
		List<Element> listEntidades = new ArrayList<Element>();
		
		try {
			Document responseDoc = serviceInvoker.call("crud.find", "mge", docToString(doc));
			
			Element serviceResponse = responseDoc.getRootElement();
			Element responseBody = serviceResponse.getChild("responseBody");
			Element entidades = responseBody.getChild("entidades").detach();		
			listEntidades = entidades.getChildren("entidade");
			
		} catch (Exception e) {
			e.printStackTrace();
			listEntidades.add(new Element("entidade"));
		}
		return listEntidades;
	}
	
	public List<Element> callLoadRecords(){
		
		List<Element> listEntidades = new LinkedList<Element>();
		
		try {
			
			Document responseDoc = serviceInvoker.call("CRUDServiceProvider.loadRecords", "mge", docToString(doc));
			
			Element serviceResponse = responseDoc.getRootElement();
			Element responseBody = serviceResponse.getChild("responseBody");
			Element entities = responseBody.getChild("entities");
			Element metadata = entities.getChild("metadata");
			Element fields = metadata.getChild("fields");
			List<Element> listFields = fields.getChildren("field");
			
			List<String> fieldsName = new LinkedList<String>(); 
			for(Element field : listFields){
				fieldsName.add(field.getAttributeValue("name"));
			}
			
			List<Element> listEntity = entities.getChildren("entity");
			for(Element entity : listEntity){
				
				Element entidade = new Element("entidade");
				
				List<Element> fieldsValue = entity.getChildren();

				int fieldCounter = 0;
				for(String fieldName : fieldsName){
					Element field = new Element(fieldName);
					field.setText(fieldsValue.get(fieldCounter).getText());
					entidade.addContent(field);
					fieldCounter++;
				}
				
				listEntidades.add(entidade);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return listEntidades;
	}
	
	public String docToString(Document doc){
		XMLOutputter xmlOut = new XMLOutputter(Format.getPrettyFormat().setOmitDeclaration(true));
		return xmlOut.outputString(doc);
	}
	
	public void prepareResponseForList(List<PresentationField> presentationFields, List<Element> listEntidades){
		prepareResponseInfo(listEntidades.size());
		setMetadata(presentationFields);
		isList = true;
		for(Element entidade : listEntidades){
			prepareResponseForObject(presentationFields,entidade);
		}
	}
	
	public void prepareResponseForObject(List<PresentationField> presentationFields, Element entidade){
		
		if(!isList){
			prepareResponseInfo(1);
			setMetadata(presentationFields);
		}
		
		List<Element> listFields = entidade.getChildren();
		
		for(String fieldName : responseObjMap.keySet()){
			Element outerObjElem = new Element(fieldName);
			outerObjElem.setText(responseObjMap.get(fieldName));
			listFields.add(outerObjElem);
		}

		Element responseEntidade = new Element("entidade");
		
		for(Element field : listFields){
			for(PresentationField currentField : presentationFields){ 
				if(field.getName().equals(currentField.getRawFieldName())){
					Element newField = new Element(currentField.getPresentationFieldName());
					String content = field.getText();
					newField.setText(content);
					responseEntidade.addContent(newField);
				}
			}
		}
		responseEntidades.addContent(responseEntidade);
	}
	
	public void addResponseObj(String fieldName, String fieldValue){
		responseObjMap.put(fieldName, fieldValue);
	}
	
	private void prepareResponseInfo(int numEntities){
		if(responseEntidades == null){
			responseEntidades = new Element("entidades");
			responseEntidades.setAttribute("total", String.valueOf(numEntities));
		}
	}
	
	public Element getResponse(){
		if(responseEntidades == null){
			responseEntidades = new Element("entidades");
			responseEntidades.setAttribute("total","0");
		}
		return responseEntidades;
	}
	
	private void setMetadata(List<PresentationField> presentationFields){
		
		Element metadata = new Element("metadata");
		
		for(PresentationField currentField : presentationFields){
			Element newField = new Element("field");
			newField.setAttribute("fieldName", currentField.getPresentationFieldName());
			newField.setAttribute("label", currentField.getFieldLabel());
			newField.setAttribute("visible", Boolean.toString(currentField.isVisible()));
			metadata.addContent(newField);
		}
		responseEntidades.addContent(metadata);
	}
	
	public Element callModuleService(String serviceName, String module, Document requestDoc){
		
		Element responseBody = new Element("responseBody");
		
		try {
			Document responseDoc = serviceInvoker.call(serviceName, module, docToString(requestDoc));
			Element serviceResponse = responseDoc.getRootElement();
			responseBody = serviceResponse.getChild("responseBody").detach();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseBody;
	}
	
	public Element responseError(String msg){
		Element erro = new Element("erro");
		erro.setText(msg);
		return erro;
	}
	
	public Element callCrudSave(List<Campo> listCampos){
		for(Campo campo : listCampos){
			Element campoElem = new Element("campo");
			campoElem.setAttribute("nome", campo.getNome());
			campoElem.setText(campo.getValor());
			entity.addContent(campoElem);
		}
		
		Element responseBody = new Element("responseBody");
		
		try {
			
			Document responseDoc = serviceInvoker.call("crud.save", "mge", docToString(doc));
			
			Element serviceResponse = responseDoc.getRootElement();
			responseBody = serviceResponse.getChild("responseBody").detach();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return responseBody;
		
	}
	
	public void doLogin(){
		try {
			serviceInvoker.doLogin();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getSKWSession(){
		
		String sessionid = serviceInvoker.getJSessionID(); 
		if("".equals(sessionid) || sessionid == null){
			doLogin();
			sessionid = serviceInvoker.getJSessionID();
		}
		return sessionid;
	}
	
	public static class Campo{
		
		private String nome;
		private String valor;
		
		public Campo(String nome, String valor){
			this.nome = nome;
			this.valor = valor;
		}

		public String getNome() {
			return nome;
		}

		public String getValor() {
			return valor;
		}
		
	}
	
	public static class PresentationField {

		private String	rawFieldName;
		private String	presentationFieldName;
		private String	fieldLabel;
		private boolean visible;

		public PresentationField(String rawFieldName, String presentationFieldName, String fieldLabel, boolean visible){
			this.rawFieldName = rawFieldName;
			this.presentationFieldName = presentationFieldName;
			this.fieldLabel = fieldLabel;
			this.visible = visible;
		}

		public PresentationField(String rawFieldName, String presentationFieldName, String fieldLabel){
			this(rawFieldName, presentationFieldName, fieldLabel, true);
		}
		
		public PresentationField(String rawFieldName, String fieldLabel, boolean visible){
			this(rawFieldName, rawFieldName, fieldLabel, visible);
		}
		
		public PresentationField(String rawFieldName, String fieldLabel){
			this(rawFieldName, rawFieldName, fieldLabel);
		}
		
		public String getRawFieldName() {
			return rawFieldName;
		}

		public String getPresentationFieldName() {
			return presentationFieldName;
		}

		public String getFieldLabel() {
			return fieldLabel;
		}
		
		public boolean isVisible(){
			return visible;
		}
	}
	
}
