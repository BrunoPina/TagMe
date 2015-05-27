package br.com.tagme.auth.helper;

import org.jdom2.Element;

public class FieldMetadata {
	private String fieldName;
	private String label;

	public FieldMetadata(String fieldName, String label) {
		this.fieldName = fieldName;
		this.label = label;
	}

	public Element getElement() {
		Element field = new Element("field");
		field.setAttribute("fieldName", this.fieldName);
		field.setAttribute("label", this.label);
		return field;
	}

	public String getFieldName() {
		return this.fieldName;
	}
	
	public String getLabel() {
		return this.label;
	}
}
