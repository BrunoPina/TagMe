package br.com.tagme.gwt.core.client.activities;

import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Container;

import br.com.sankhya.place.gwt.mvp.client.DefaultActivity;
import br.com.sankhya.place.gwt.mvp.client.MVPEntryPoint;
import br.com.sankhya.place.gwt.mvp.client.places.AbstractPlace;
import br.com.tagme.gwt.core.client.pages.LoginPage;

import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class LoginActivity extends DefaultActivity{

	private static LoginPage loginPage;
	
	public LoginActivity(AbstractPlace contextPlace){
		super(contextPlace);
		
		if(loginPage == null){
			loginPage = new LoginPage();
			loginPage.addAttachHandler(new Handler() {
				
				@Override
				public void onAttachOrDetach(AttachEvent event) {
					Container outerContainer = MVPEntryPoint.getOuterContainer();
					
					if(event.isAttached()){
						if(outerContainer != null){
							outerContainer.addStyleName("centered-content-page");
						}
						try{
							((Column)MVPEntryPoint.getMainApplicationContainer()).addStyleName("min-height-login-page");
						}catch(Exception e){}
						
					}else{
						if(outerContainer != null){
							outerContainer.removeStyleName("centered-content-page");
						}
						try{
							((Column)MVPEntryPoint.getMainApplicationContainer()).removeStyleName("min-height-login-page");
						}catch(Exception e){}
					}
				}
			});
		}
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(loginPage);
	}
	
}
