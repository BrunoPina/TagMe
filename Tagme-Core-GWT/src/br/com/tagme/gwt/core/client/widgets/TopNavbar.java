package br.com.tagme.gwt.core.client.widgets;

import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.NavbarCollapse;
import org.gwtbootstrap3.client.ui.NavbarText;

import br.com.sankhya.place.gwt.auth.client.AppAuth;
import br.com.sankhya.place.gwt.auth.client.AppAuthEvent;
import br.com.sankhya.place.gwt.auth.client.AppAuthEventHandler;
import br.com.sankhya.place.gwt.mvp.client.ContextAppChangeEvent;
import br.com.sankhya.place.gwt.mvp.client.ContextAppChangeHandler;
import br.com.sankhya.place.gwt.mvp.client.MVPEntryPoint;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class TopNavbar extends Composite {

	private static TopNavbarUiBinder uiBinder = GWT.create(TopNavbarUiBinder.class);

	interface TopNavbarUiBinder extends UiBinder<Widget, TopNavbar> {
	}
	
	@UiField NavbarText logoText;
	@UiField NavbarCollapse navbarContainer;
	@UiField Anchor logoAnchor;
	
	private Composite currUserWidget;
	private LoggedInUserWidget loggedInUser;
	private LoggedOutUserWidget loggedOutUser = new LoggedOutUserWidget();
	
	public TopNavbar() {
		initWidget(uiBinder.createAndBindUi(this));
		
		logoAnchor.setHref("#index");
		
		MVPEntryPoint.addContextAppChangeHandler(new ContextAppChangeHandler() {
			
			@Override
			public void onContextAppChange(ContextAppChangeEvent event) {
				if(!MVPEntryPoint.DEFAULT_CONTEXT_APP.equals(event.getNewContextApp())){
				}
			}
		});
		
		AppAuth.getInstance().addAppAuthHandler(new AppAuthEventHandler() {
			
			@Override
			public void onAppAuthChange(AppAuthEvent event) {
				if(event.typeInfosChanged()){
					if(loggedInUser != null){
						loggedInUser.refresh();
					}
					return;
				}
				
				if(currUserWidget != null){
					navbarContainer.remove(navbarContainer.getWidgetIndex(currUserWidget));
				}
				
				if(event.typeLoginSuccess() || event.typeLoginVerified()){
					loggedInUser =  new LoggedInUserWidget();
					currUserWidget = loggedInUser;
					loggedInUser.refresh();
				}else if(event.typeLoginFail() || event.typeLoginNotVerified()){
					currUserWidget = loggedOutUser;
				}
				
				navbarContainer.add(currUserWidget);
				
			}
		});
	}
	
	public void setLogoVisible(boolean visible){
		logoText.setVisible(visible);
	}
	
}
