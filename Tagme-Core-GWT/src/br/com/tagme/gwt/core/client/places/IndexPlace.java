package br.com.tagme.gwt.core.client.places;

import br.com.sankhya.place.gwt.mvp.client.DefaultActivity;
import br.com.sankhya.place.gwt.mvp.client.IActivityFactory;
import br.com.sankhya.place.gwt.mvp.client.places.AbstractPlace;
import br.com.sankhya.place.gwt.mvp.client.places.DefaultPlace;
import br.com.sankhya.place.gwt.mvp.client.tokenizer.ChangeHistoryTokenizer;
import br.com.tagme.gwt.core.client.activities.IndexActivity;

import com.google.gwt.place.shared.Place;

public class IndexPlace extends DefaultPlace{
	
	public static final String CONTEXT_APP = "main";
	public static final String PREFIX = "index";
	
	public IndexPlace(String token){
		super(CONTEXT_APP, PREFIX, token, "Início");
	}
	
	public static class Initializer extends ChangeHistoryTokenizer implements IActivityFactory{
		
		@Override
		public Place getPlace(String token) {
			return new IndexPlace(token);
		}
		
		@Override
		public DefaultActivity createActivity(AbstractPlace targetPlace) {
			if(targetPlace instanceof IndexPlace){
				return new IndexActivity((DefaultPlace) targetPlace);
			}
			return null;
		}

	} 
	
}