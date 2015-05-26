package br.com.tagme.gwt.core.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class LoggedOutUserWidget extends Composite {

	private static LoggedOutUserWidgetUiBinder uiBinder = GWT.create(LoggedOutUserWidgetUiBinder.class);

	interface LoggedOutUserWidgetUiBinder extends UiBinder<Widget, LoggedOutUserWidget> {
	}
	
	public LoggedOutUserWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		
	}
	
}
