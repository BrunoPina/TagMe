package br.com.tagme.gwt.core.client.widgets;

import org.gwtbootstrap3.client.ui.Container;

import br.com.sankhya.place.gwt.mvp.client.OneWidgetColumn;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class UIContentWrapper extends Composite {

	private static UIContentWrapperUiBinder uiBinder = GWT.create(UIContentWrapperUiBinder.class);

	@UiField OneWidgetColumn mainContainer;
	@UiField Container outerContainer;
	
	interface UIContentWrapperUiBinder extends UiBinder<Widget, UIContentWrapper> {
	}
	
	public UIContentWrapper() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public OneWidgetColumn getMainContainer(){
		return mainContainer;
	}
	
	public Container getOuterContainer(){
		return outerContainer;
	}
	
}
