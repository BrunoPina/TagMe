package br.com.tagme.gwt.core.client.widgets;

import java.util.ArrayList;
import java.util.HashMap;

import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.PanelGroup;

import br.com.sankhya.place.gwt.auth.client.AppAuth;
import br.com.sankhya.place.gwt.auth.client.AppAuthEvent;
import br.com.sankhya.place.gwt.auth.client.AppAuthEventHandler;
import br.com.sankhya.place.gwt.mvp.client.ContextAppChangeEvent;
import br.com.sankhya.place.gwt.mvp.client.ContextAppChangeHandler;
import br.com.sankhya.place.gwt.mvp.client.MVPEntryPoint;
import br.com.sankhya.place.gwt.mvp.client.places.AbstractPlace;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class Menu extends Composite{

	private static MenuUiBinder uiBinder = GWT.create(MenuUiBinder.class);

	interface MenuUiBinder extends UiBinder<Widget, Menu> {
	}
	
	@UiField PanelGroup menuAccordion;
	@UiField Heading menuHeading;
	
	private ArrayList<MenuItem> logicalMenuAccordion = new ArrayList<MenuItem>();

	private static HashMap<String, String>          	menuTitles = new HashMap<String, String>();
	private static HashMap<String, IContextAppMenu> 	menuItemRenderers = new HashMap<String, IContextAppMenu>();
	private static IContextAppMenu 						currRenderer;
	
	public Menu() {
		initWidget(uiBinder.createAndBindUi(this));
		
		MVPEntryPoint.addContextAppChangeHandler(new ContextAppChangeHandler() {
			
			@Override
			public void onContextAppChange(ContextAppChangeEvent event) {
				if(!MVPEntryPoint.DEFAULT_CONTEXT_APP.equals(event.getNewContextApp())){
					IContextAppMenu renderer = menuItemRenderers.get(event.getNewContextApp());
					if(renderer != null){
						menuAccordion.clear();
						logicalMenuAccordion = new ArrayList<MenuItem>();
						
						menuHeading.setText(menuTitles.get(event.getNewContextApp()));
						
						currRenderer = renderer;
						currRenderer.render(menuAccordion, logicalMenuAccordion);
						int menuSize = logicalMenuAccordion.size();
						if(menuSize >= 1){
							logicalMenuAccordion.get(menuSize-1).setLast();
						}
					}
				}
			}
		});
		
		AppAuth.getInstance().addAppAuthHandler(new AppAuthEventHandler() {
			
			@Override
			public void onAppAuthChange(AppAuthEvent event) {
				menuAccordion.clear();
				logicalMenuAccordion = new ArrayList<MenuItem>();
				
				if(currRenderer != null){
					currRenderer.render(menuAccordion, logicalMenuAccordion);
					int menuSize = logicalMenuAccordion.size();
					if(menuSize >= 1){
						logicalMenuAccordion.get(menuSize-1).setLast();
					}
				}
			}
			
		});
		
	}

	public void setSelectedItem(AbstractPlace currPlace) {
		MenuItem parentMenuItem = null;
		
		for(MenuItem menuItem : logicalMenuAccordion){
			boolean anyChildSelected = menuItem.setChildSelectedIfTarget(currPlace);
			
			if(anyChildSelected && parentMenuItem == null){
				parentMenuItem = menuItem;
			}
		}
		
		if(parentMenuItem != null){
			parentMenuItem.openMenuChildren();
		}
		
	}
	
	public static void addContextAppMenu(String contextApp, String title, IContextAppMenu renderer){
		menuTitles.put(contextApp, title);
		menuItemRenderers.put(contextApp, renderer);
	}
}
