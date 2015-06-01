package br.com.tagme.gwt.core.client.widgets;

import java.util.HashMap;

import org.gwtbootstrap3.client.ui.AnchorButton;
import org.gwtbootstrap3.client.ui.DropDownHeader;
import org.gwtbootstrap3.client.ui.Image;

import br.com.sankhya.place.gwt.auth.client.AppAuth;
import br.com.sankhya.place.gwt.auth.client.places.LoginPlace;
import br.com.sankhya.place.gwt.commons.client.components.Alert;
import br.com.sankhya.place.gwt.commons.client.sankhya.SankhyaEnvironment;
import br.com.sankhya.place.gwt.commons.utils.client.EnvironmentUtils;
import br.com.sankhya.place.gwt.commons.utils.client.XMLUtils;
import br.com.sankhya.place.gwt.http.client.ServiceProxyException;
import br.com.sankhya.place.gwt.http.client.XMLCallback;
import br.com.sankhya.place.gwt.http.client.XMLServiceProxy;
import br.com.sankhya.place.gwt.mvp.client.PlaceController;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;

public class LoggedInUserWidget extends Composite {

	private static LoggedInUserWidgetUiBinder uiBinder = GWT.create(LoggedInUserWidgetUiBinder.class);

	@UiField FormPanel logoutForm;
	@UiField DropDownHeader userEmail;
	@UiField AnchorButton userName;
	@UiField Image userImage;
	@UiField DropDownHeader nomeParc;
	
	private static int counter = 0;
	
	interface LoggedInUserWidgetUiBinder extends UiBinder<Widget, LoggedInUserWidget> {
	}
	
	public LoggedInUserWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		
		logoutForm.setAction(EnvironmentUtils.getRelativeURL() + "j_spring_security_logout");
		
		logoutForm.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				AppAuth.getInstance().setLoginNotVerified("Sessão Finalizada");
				PlaceController.goTo(new LoginPlace("home"));
			}
		});
	}
	
	@UiHandler("btnSair")
	public void onBtnSairClick(ClickEvent event){
		logoutForm.submit();
	}
	
	@UiHandler("btnAjustesConta")
	public void onBtnAjustesContaClick(ClickEvent event){
		new AjustesUsuarioForm().show();
		refresh();
	}

	
	public void refresh(){
		counter++;
		userName.setText(AppAuth.getInstance().getName());
		userEmail.setText(AppAuth.getInstance().getUsername());
		XMLServiceProxy service = new XMLServiceProxy(this);

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("getNotAuthzReasons", "true");
		
//		service.call("PORCLI@BuscaInfosContatoService", null, params, new XMLCallback() {
//
//			@Override
//			public void onResponseReceived(Element response) {
//				Element responseElem = XMLUtils.getFirstChild(response, "contato");
//				String strNomeParc = responseElem != null ? XMLUtils.getNodeValue(XMLUtils.getFirstChild(responseElem,"NOMEPARC")) : "";
//				nomeParc.setText(strNomeParc);
//				
//				NodeList listReasonLostAccess = response.getElementsByTagName("RLA");
//				
//				if(listReasonLostAccess.getLength() > 0){
//					
//					String rlaHtml = "<br/>";
//				
//					for(int i = 0; i  < listReasonLostAccess.getLength() ; i++){
//						Element rla = (Element) listReasonLostAccess.item(i);
//						String lostAccess = rla.getAttribute("lostAccess");
//						String reason = rla.getAttribute("reason");
//						if(SankhyaEnvironment.isJiva() && reason.contains("Sankhya")){
//							reason = reason.replaceAll("Sankhya", "Jiva");
//						}
//						rlaHtml += "<br/><span style='text-align:justify;'><b>" + lostAccess + " </b> " + reason + "</span>";
//						
//					}
//					
//					Alert.showInfo("Acessos bloqueados", rlaHtml);
//				
//				}
//				
//			}
//
//			@Override
//			public boolean onError(ServiceProxyException e) {
//				return false;
//			}
//			
//			
//		});
		
		userImage.setUrl(EnvironmentUtils.getRelativeURL() + "image/user/currUser"+counter+".png?w=70&h=70&r=true&s=true");
	}
	
}
