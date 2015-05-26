package br.com.tagme.gwt.core.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class BottomNavbar extends Composite {

	private static TopNavbarUiBinder uiBinder = GWT.create(TopNavbarUiBinder.class);

	interface TopNavbarUiBinder extends UiBinder<Widget, BottomNavbar> {
	}
	
	public BottomNavbar(Widget w) {
		initWidget(uiBinder.createAndBindUi(this));
		
	}
	
}
