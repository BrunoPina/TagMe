package br.com.tagme.gwt.core.client.activities;

import br.com.sankhya.place.gwt.mvp.client.DefaultActivity;
import br.com.sankhya.place.gwt.mvp.client.places.AbstractPlace;
import br.com.tagme.gwt.core.client.pages.ListaOSPage;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class PesquisaPessoasActivity extends DefaultActivity{

	public PesquisaPessoasActivity(AbstractPlace contextPlace){
		super(contextPlace);
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(new ListaOSPage(this));
	}
	
}
