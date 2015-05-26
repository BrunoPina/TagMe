package br.com.tagme.gwt.core.client.activities;

import br.com.sankhya.place.gwt.mvp.client.DefaultActivity;
import br.com.sankhya.place.gwt.mvp.client.places.AbstractPlace;
import br.com.sankhya.place.gwt.mvp.client.places.ErrorPlace;
import br.com.sankhya.place.gwt.mvp.client.places.IBreadcumbPlace;
import br.com.tagme.gwt.core.client.widgets.DefaultInnerPage;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class DefaultInnerPageActivity extends DefaultActivity{

	public DefaultInnerPageActivity(IBreadcumbPlace place){
		super((AbstractPlace) place);
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		DefaultInnerPage.getInstance().setPresenter(this);
		
		if(getContextPlace() instanceof ErrorPlace){
			DefaultInnerPage.getInstance().setView(getContextLocator());
		}
		
		panel.setWidget(DefaultInnerPage.getInstance());
	}
	
}
