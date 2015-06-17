package br.com.tagme.gwt.core.client.activities;

import br.com.sankhya.place.gwt.mvp.client.DefaultActivity;
import br.com.sankhya.place.gwt.mvp.client.places.AbstractPlace;
import br.com.tagme.gwt.core.client.pages.NewPersonPage;
import br.com.tagme.gwt.core.client.pages.PessoaPage;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class NewPersonActivity extends DefaultActivity{

	public NewPersonActivity(AbstractPlace contextPlace){
		super(contextPlace);
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		//panel.setWidget(new PessoaPage(this));
		panel.setWidget(new NewPersonPage(this));
	}
	
	@Override
	public String mayStop() {
		return super.mayStop();
	}
	
}
