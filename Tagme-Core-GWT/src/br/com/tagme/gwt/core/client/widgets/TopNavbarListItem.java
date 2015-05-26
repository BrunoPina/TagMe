package br.com.tagme.gwt.core.client.widgets;

import org.gwtbootstrap3.client.ui.AnchorListItem;

import br.com.sankhya.place.gwt.mvp.client.PlaceController;
import br.com.sankhya.place.gwt.mvp.client.places.AbstractPlace;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class TopNavbarListItem extends AnchorListItem{
	
	private AbstractPlace targetPlace;
	
	public TopNavbarListItem(String text){
		this(text, null);
	}
	
	public TopNavbarListItem(String text, AbstractPlace place){
		super(text);
		this.targetPlace = place;
		
		if(place != null){
			addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					if(targetPlace != null){
						PlaceController.goTo(targetPlace);
					}
				}
			});
		}
	}
	
	public AbstractPlace getTartetPlace(){
		return targetPlace;
	}
	
}
