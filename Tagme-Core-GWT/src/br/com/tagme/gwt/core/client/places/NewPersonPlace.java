package br.com.tagme.gwt.core.client.places;

import br.com.sankhya.place.gwt.mvp.client.DefaultActivity;
import br.com.sankhya.place.gwt.mvp.client.IActivityFactory;
import br.com.sankhya.place.gwt.mvp.client.places.AbstractPlace;
import br.com.sankhya.place.gwt.mvp.client.places.DefaultPlace;
import br.com.sankhya.place.gwt.mvp.client.tokenizer.ChangeHistoryTokenizer;
import br.com.tagme.gwt.core.client.activities.NewPersonActivity;

import com.google.gwt.place.shared.Place;

public class NewPersonPlace extends DefaultPlace{
	
	public static final String CONTEXT_APP = "main";
	public static final String PREFIX = "new";
	
	public NewPersonPlace(String token){
		super(CONTEXT_APP, PREFIX, token);
	}
	
	public static class Initializer extends ChangeHistoryTokenizer implements IActivityFactory{
		
		@Override
		public Place getPlace(String token) {
			return new NewPersonPlace(token);
		}
		
		@Override
		public DefaultActivity createActivity(AbstractPlace targetPlace) {
			if(targetPlace instanceof NewPersonPlace){
				return new NewPersonActivity(targetPlace);
			}
			return null;
		}

	} 
	
}