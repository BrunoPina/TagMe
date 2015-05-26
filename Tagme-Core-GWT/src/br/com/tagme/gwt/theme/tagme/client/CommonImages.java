package br.com.tagme.gwt.theme.tagme.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.RetinaImageResource;

public interface CommonImages extends ClientBundle{
	
	ImageResource logo();
	ImageResource logosankhya();
	ImageResource logomarcasankhya();
	
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
		
		RetinaImageResource logo();
		RetinaImageResource logosankhya();
		RetinaImageResource logomarcasankhya();
		
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
