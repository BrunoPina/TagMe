package br.com.tagme.gwt.core.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class LoadingBusca extends Composite {

	private static LoadingBuscaUiBinder	uiBinder	= GWT.create(LoadingBuscaUiBinder.class);

	interface LoadingBuscaUiBinder extends UiBinder<Widget, LoadingBusca> {
	}

	public LoadingBusca() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
