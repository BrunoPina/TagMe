package br.com.tagme.gwt.core.client.widgets;

import java.util.HashMap;

import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Breadcrumbs;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.ListGroupItem;
import org.gwtbootstrap3.client.ui.constants.AlertType;

import br.com.sankhya.place.gwt.commons.client.components.PathItem;
import br.com.sankhya.place.gwt.commons.client.sankhya.SankhyaEnvironment;
import br.com.sankhya.place.gwt.mvp.client.CompositeWithPresenter;
import br.com.sankhya.place.gwt.mvp.client.PlaceController;
import br.com.sankhya.place.gwt.mvp.client.places.AbstractPlace;
import br.com.sankhya.place.gwt.mvp.client.places.IBreadcumbPlace;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class DefaultInnerPage extends CompositeWithPresenter {

	private static DefaultInnerPageUiBinder	uiBinder	= GWT.create(DefaultInnerPageUiBinder.class);
	
	interface DefaultInnerPageUiBinder extends UiBinder<Widget, DefaultInnerPage> {
	}
	
	private static HashMap<String, String> 		MESSAGES = new HashMap<String, String>();
	private static String 						DEFAULT_MESSAGE = "Ocorreu um erro ao carregar a página. Por favor, entre em contato com a ";
	private static DefaultInnerPage 			INSTANCE;
	private static boolean 						initialized = false;
	
	@UiField Breadcrumbs breadcrumbs;
	@UiField Column contentContainer;
	@UiField Column menuContainer;
	private Menu menu;
	
	static{
		MESSAGES.put("naoautorizado", "Você não está autorizado a executar esta operação. Solicite acesso ao responsável pelo ERP ");
		MESSAGES.put("naoencontrado", "Não foi possível encontrar o local especificado. Por favor, verifique o link da página.");
		MESSAGES.put("paginanaoencontrada", "Não foi possível encontrar a página especificada. Por favor, verifique o link da página.");
	}
	
	private DefaultInnerPage(Menu menu) {
		super();
		this.menu = menu;
		
		String msgNaoAutorizado = MESSAGES.get("naoautorizado");
		
		if(SankhyaEnvironment.isJiva()){
			DEFAULT_MESSAGE += "franqueadora Jiva.";
			msgNaoAutorizado += "Jiva";
		}else{
			DEFAULT_MESSAGE += "central.";
			msgNaoAutorizado += "Sankhya";
		}
		
		msgNaoAutorizado += " em sua organização.";
		MESSAGES.put("naoautorizado", msgNaoAutorizado);
		
	}
	
	public static DefaultInnerPage getInstance(){
		if(!initialized){
			INSTANCE.initWidget(uiBinder.createAndBindUi(INSTANCE));
			INSTANCE.addMenuOnContainer();
			initialized = true;
		}
		
		return INSTANCE;
	}
	
	public static void setMenu(Menu menu){
		if(INSTANCE == null){
			INSTANCE = new DefaultInnerPage(menu);
		}
	}
	
	public void addMenuOnContainer(){
		menuContainer.add((Widget) menu);
	}
	
	private void buildView(Widget w, String locator){
		clearContainer();
		clearBreadcumb();
		
		if(getPlace() instanceof IBreadcumbPlace){
			IBreadcumbPlace breadcumbPlace = (IBreadcumbPlace) getPlace();
			
			for(int i=0; i<breadcumbPlace.getPathSize(); i++){
				
				final PathItem pathItem = breadcumbPlace.getPathItem(i);
				
				if(pathItem != null && pathItem.getDescriptor() != null){
					
					AnchorListItem anchor = new AnchorListItem(pathItem.getDescriptor());
					anchor.setHref(PlaceController.buildHref(PlaceController.buildPlace(pathItem.getPrefix(), pathItem.getToken())));
					
					/*pathItem.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							AppPlaceController.goTo(targetPlace);
						}
					});*/
					breadcrumbs.add(anchor);
				}
				
			}
			
			ListGroupItem finalPathItem = new ListGroupItem();
			finalPathItem.setText(breadcumbPlace.getDescriptor());
			finalPathItem.setStyleName("active");
			breadcrumbs.add(finalPathItem);
		}
		
		menu.setSelectedItem((AbstractPlace) getPlace());
		
		if(w == null){
			setError(locator);
		}else{
			contentContainer.add(w);
		}
	}
	
	public void setView(Widget w){
		buildView(w, null);
	}
	
	public void setView(String messageLocator){
		buildView(null, messageLocator);
	}
	
	private void setError(String messageLocator){
		Alert notAuthorizedAlert = new Alert(MESSAGES.get(messageLocator) == null ? DEFAULT_MESSAGE : MESSAGES.get(messageLocator));
		notAuthorizedAlert.setType(AlertType.DANGER);
		
		contentContainer.insert(notAuthorizedAlert, 0);
	}
	
	private void clearContainer(){
		int widgetCount = contentContainer.getWidgetCount();
		
		for(int i=0; i < widgetCount; i++){
			contentContainer.remove(0);
		}
	}
	
	private void clearBreadcumb(){
		int widgetCount = breadcrumbs.getWidgetCount();
		
		for(int i=0; i < widgetCount; i++){
			breadcrumbs.remove(0);
		}
	}
	
}
