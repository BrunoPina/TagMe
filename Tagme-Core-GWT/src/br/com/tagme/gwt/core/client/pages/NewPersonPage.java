package br.com.tagme.gwt.core.client.pages;

import org.gwtbootstrap3.client.ui.constants.IconType;

import br.com.sankhya.place.gwt.mvp.client.CompositeWithPresenter;
import br.com.sankhya.place.gwt.mvp.client.DefaultActivity;
import br.com.tagme.gwt.core.client.wizard.Wizard;
import br.com.tagme.gwt.core.client.wizard.newperson.BasicInfosStep;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class NewPersonPage extends CompositeWithPresenter  {

	private static NewPersonPageUiBinder	uiBinder	= GWT.create(NewPersonPageUiBinder.class);

	interface NewPersonPageUiBinder extends UiBinder<Widget, NewPersonPage> {
	}

	@UiField Wizard wizard;
	
	public NewPersonPage(DefaultActivity presenter) {
		initWidget(uiBinder.createAndBindUi(this));
		
		wizard.addStep(new BasicInfosStep("1",IconType.AT,"Identificação"));
		
		wizard.addStep(new BasicInfosStep("2",IconType.HEARTBEAT,"Dados Cadastrais"));
		
		wizard.addStep(new BasicInfosStep("3",IconType.MAP_MARKER,"Endereço"));
		
		wizard.addStep(new BasicInfosStep("4",IconType.LIST_ALT,"Documentos"));
		
		wizard.addStep(new BasicInfosStep("5",IconType.CHECK,"Checando Informações"));
		
		wizard.next();
	}

}
