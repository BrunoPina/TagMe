package br.com.tagme.gwt.core.client.activities;

import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Container;

import br.com.sankhya.place.gwt.mvp.client.DefaultActivity;
import br.com.sankhya.place.gwt.mvp.client.MVPEntryPoint;
import br.com.sankhya.place.gwt.mvp.client.places.AbstractPlace;
import br.com.tagme.gwt.core.client.CoreEntryPoint;
import br.com.tagme.gwt.core.client.pages.IndexPage;

import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class IndexActivity extends DefaultActivity{

	private static IndexPage indexPage;
	
	public IndexActivity(AbstractPlace place){
		super(place);
		
		if(indexPage == null){
			indexPage = new IndexPage(this, null);
			indexPage.addAttachHandler(new Handler() {
				
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
		CoreEntryPoint.getTopNavbar().setLogoVisible(false);
		panel.setWidget(indexPage);
	}
	
	@Override
	public void onStop() {
		super.onStop();
		CoreEntryPoint.getTopNavbar().setLogoVisible(true);
	}
	
}
