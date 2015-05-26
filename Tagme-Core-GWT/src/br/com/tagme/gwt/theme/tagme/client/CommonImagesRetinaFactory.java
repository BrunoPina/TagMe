package br.com.tagme.gwt.theme.tagme.client;

import com.google.gwt.core.client.GWT;

public class CommonImagesRetinaFactory extends CommonImagesFactory{
	
	public @Override CommonImages create() {
		return GWT.create(CommonImages.Retina.class);
	}
	
}
