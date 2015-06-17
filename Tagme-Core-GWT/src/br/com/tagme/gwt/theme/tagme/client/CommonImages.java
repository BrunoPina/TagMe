package br.com.tagme.gwt.theme.tagme.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.RetinaImageResource;

public interface CommonImages extends ClientBundle{
	
	ImageResource logo();
	ImageResource logosankhya();
	ImageResource logomarcasankhya();
	ImageResource logosmall();
	
	ImageResource imga();
	ImageResource imgb();
	ImageResource imbc();
	
	ImageResource oscar();
	ImageResource rosalina();
	
	ImageResource boy();
	ImageResource globe();
	ImageResource server();
	ImageResource archive();
	ImageResource diploma();
	ImageResource brain();
	ImageResource megaphone();
	ImageResource chat();
	ImageResource brochure();
	ImageResource shopping();
	ImageResource key();
	ImageResource lock();
	
	public interface Retina extends CommonImages {
		
		RetinaImageResource imga();
		RetinaImageResource imgb();
		RetinaImageResource imbc();
		
		RetinaImageResource logo();
		RetinaImageResource logosankhya();
		RetinaImageResource logomarcasankhya();
		RetinaImageResource logosmall();
		
		RetinaImageResource oscar();
		RetinaImageResource rosalina();
		
		RetinaImageResource boy();
		RetinaImageResource globe();
		RetinaImageResource server();
		RetinaImageResource archive();
		RetinaImageResource diploma();
		RetinaImageResource brain();
		RetinaImageResource megaphone();
		RetinaImageResource chat();
		RetinaImageResource brochure();
		RetinaImageResource shopping();
		RetinaImageResource key();
		RetinaImageResource lock();
	}
	
}
