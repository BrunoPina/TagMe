package br.com.tagme.gwt.core.client.activities;

import br.com.sankhya.place.gwt.mvp.client.DefaultActivity;
import br.com.sankhya.place.gwt.mvp.client.IActivityFactory;
import br.com.sankhya.place.gwt.mvp.client.places.AbstractPlace;
import br.com.sankhya.place.gwt.mvp.client.places.BreadcumbPlace;
import br.com.sankhya.place.gwt.mvp.client.places.ErrorPlace;

public class CoreActivityFactory implements IActivityFactory{

		@Override
		public DefaultActivity createActivity(AbstractPlace targetPlace) {
			if(targetPlace instanceof ErrorPlace){
				return new DefaultInnerPageActivity((BreadcumbPlace) targetPlace);
			}
			return null;
		}
		
}
