package br.com.tagme.gwt.theme.tagme.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;

public interface CommonStyles extends ClientBundle{
	
	public static final CommonStyles INSTANCE =  GWT.create(CommonStyles.class);
	
	@Source("CommonStyles.css")
	@NotStrict
	CssResource css();
	
}
