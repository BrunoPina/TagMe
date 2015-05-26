package br.com.tagme.gwt.core.client.widgets;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.html.Small;

import br.com.sankhya.place.gwt.commons.client.components.Alert;
import br.com.sankhya.place.gwt.commons.client.sankhya.SankhyaEnvironment;
import br.com.sankhya.place.gwt.commons.utils.client.StringUtils;
import br.com.sankhya.place.gwt.http.client.ServiceProxyException;
import br.com.sankhya.place.gwt.http.client.XMLCallback;
import br.com.sankhya.place.gwt.http.client.XMLServiceProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.XMLParser;

public class Footer extends Composite{

	private static FooterUiBinder	uiBinder	= GWT.create(FooterUiBinder.class);
	
	@UiField Small email;
	@UiField Small site;
	
	@UiField Button botaoFeedback;
	@UiField Modal modal;
	@UiField TextArea txaFeedback;
	@UiField Button botaoEnviar;
	@UiField Button botaoFechar;
	
	
	interface FooterUiBinder extends UiBinder<Widget, Footer> {
	}

	public Footer() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("botaoFeedback")
	void openModal(ClickEvent e){
		modal.show();
	}
	
	@UiHandler("botaoEnviar")
	void enviarFeedback(ClickEvent e){
			
		String strFeedback = txaFeedback.getText();
		
		if(StringUtils.isEmpty(strFeedback)){
			
			Alert.showTempError("Atenção", "Feedback não pode ser vazio.");
			
		}else{
		
			XMLServiceProxy service = new XMLServiceProxy(this);
			
			Document doc = XMLParser.createDocument();
			
			Element parametros = doc.createElement("parametros");
			
			Element feedbackElem = doc.createElement("FEEDBACK");
			feedbackElem.appendChild(doc.createTextNode(strFeedback));
			parametros.appendChild(feedbackElem);
			
			doc.appendChild(parametros);
			
			service.call("commons@EnviarFeedbackService", doc, new XMLCallback(){

				@Override
				public void onResponseReceived(Element response) {
					txaFeedback.clear();
					modal.hide();
					Alert.showTempSuccess("Enviado com sucesso", "Agradecemos seu feedback.");
				}

				@Override
				public boolean onError(ServiceProxyException e) {
					// TODO Auto-generated method stub
					return false;
				}
				
			});
		
		}
			
	}

	@UiHandler("botaoFechar")
	void fecharModal(ClickEvent e){
		txaFeedback.clear();
		modal.hide();
	}
	
}
