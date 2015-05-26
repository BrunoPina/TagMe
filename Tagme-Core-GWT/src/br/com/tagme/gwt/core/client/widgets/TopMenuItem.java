package br.com.tagme.gwt.core.client.widgets;

import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.ThumbnailLink;
import org.gwtbootstrap3.client.ui.ThumbnailPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class TopMenuItem extends Composite {

	private static TopMenuItemUiBinder	uiBinder	= GWT.create(TopMenuItemUiBinder.class);
	
	interface TopMenuItemUiBinder extends UiBinder<Widget, TopMenuItem> {
	}

	@UiField Heading heading;
	@UiField Image	 icon;	
	@UiField ThumbnailPanel menuItem;
	@UiField ThumbnailLink menuLink;
	private boolean hideMenuBar = true; 
	
	public TopMenuItem(String menuName, String customStyle, ImageResource image, String href, ClickHandler onClick, boolean hideMenuBar) {
		initWidget(uiBinder.createAndBindUi(this));
		
		heading.setText(menuName);
		icon.setResource(image);
		this.hideMenuBar = hideMenuBar;
		
		if(href != null){
			menuLink.setHref(href);
		}
		menuLink.setTarget("_self");
		if(onClick != null){
			menuLink.addClickHandler(onClick);
		}
	}
	
	public TopMenuItem(String menuName, String customStyle, ImageResource image, String href, boolean hideMenuBar){
		this(menuName, customStyle, image, href, null, hideMenuBar);
	}
	
	public TopMenuItem(String menuName, String customStyle, ImageResource image, ClickHandler onClick, boolean hideMenuBar) {
		this(menuName, customStyle, image, null, onClick, hideMenuBar);
	}

	public boolean isHideMenuBar() {
		return hideMenuBar;
	}
	
	public void setSelected() {
		menuLink.addStyleName("selected");
	}
	
	public void removeSelection() {
		menuLink.removeStyleName("selected");
	}

}
