package br.com.tagme.gwt.core.client.widgets;

import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.Lead;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class BuscaSemResultados extends Composite {

	private static BuscaSemResultadosUiBinder	uiBinder	= GWT.create(BuscaSemResultadosUiBinder.class);

	interface BuscaSemResultadosUiBinder extends UiBinder<Widget, BuscaSemResultados> {
	}

	@UiField Lead customLeadMessage;
	@UiField Icon customIcon;
	
	
	public @UiConstructor BuscaSemResultados(String message, IconType iconType) {
		initWidget(uiBinder.createAndBindUi(this));
		
		customLeadMessage.setText(message);
		customIcon.setType(iconType);
	}
	
	public void setLeadMessage(String message){
		customLeadMessage.setText(message);
	}

}
