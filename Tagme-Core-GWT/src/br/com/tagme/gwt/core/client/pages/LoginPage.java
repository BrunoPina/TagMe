package br.com.tagme.gwt.core.client.pages;

import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Row;

import br.com.sankhya.place.gwt.commons.client.components.Alert;
import br.com.sankhya.place.gwt.commons.client.components.Alert.ConfirmTextCallback;
import br.com.sankhya.place.gwt.commons.client.components.formitem.FormItem;
import br.com.sankhya.place.gwt.commons.utils.client.EnvironmentUtils;
import br.com.tagme.gwt.core.client.places.IndexPlace;
import br.com.tagme.gwt.core.client.widgets.ClickableThumbnailWidget;
import br.com.tagme.gwt.core.client.widgets.CriarContaModal;
import br.com.tagme.gwt.core.client.widgets.LoginForm;
import br.com.tagme.gwt.core.client.widgets.ThumbnailWidget;
import br.com.tagme.gwt.theme.tagme.client.CommonImagesFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.Widget;

public class LoginPage extends Composite {

	private static LoginPageUiBinder	uiBinder	= GWT.create(LoginPageUiBinder.class);

	interface LoginPageUiBinder extends UiBinder<Widget, LoginPage> {
	}

	@UiField LoginForm loginForm;
	
	@UiField FormPanel reenviarEmailForm;
	
	@UiField Row thumbnailsContainer;
	
	private static CommonImagesFactory res 	   = GWT.create(CommonImagesFactory.class);
	
	private FormItem<String> formEmail;
	
	public LoginPage() {
		initWidget(uiBinder.createAndBindUi(this));

		reenviarEmailForm.setEncoding(FormPanel.ENCODING_URLENCODED);
		reenviarEmailForm.setAction(EnvironmentUtils.getRelativeURL() + "reenviarEmailValidacao");
		reenviarEmailForm.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				
				Alert.hideWaitMessage();
				
				String[] arrResult = event.getResults().split(";");
				if("1".equals(arrResult[0].trim())){
					Alert.showSuccess("Verifique seu e-mail", "Dentro de alguns instantes você receberá um link para ativação de sua conta.");
				}else{
					Alert.showError("E-mail de confirmação não enviado",arrResult[1]);
				}
			}
		});
		reenviarEmailForm.addSubmitHandler(new SubmitHandler(){

			@Override
			public void onSubmit(SubmitEvent event) {
				Alert.showWaitMessage();
			}
		});
		
		formEmail = new FormItem<String>("E-mail", FormItem.TYPE_TEXTBOX);
		formEmail.setVisible(false);
		formEmail.setName("emailReenv");
		reenviarEmailForm.add(formEmail);
		thumbnailsContainer.add(buildThumbnailColumn(new ClickableThumbnailWidget(res.create().imbc(), "Para Você",      "Sua viagem com mais comodidade", new IndexPlace("a"))));
		thumbnailsContainer.add(buildThumbnailColumn(new ClickableThumbnailWidget(res.create().imgb(), "Registro Único", "Fácil, sempre atualizado e seguro", new IndexPlace("a"))));
		thumbnailsContainer.add(buildThumbnailColumn(new ClickableThumbnailWidget(res.create().imga(), "Para Hotéis",    "Check-in online e descentralizado", new IndexPlace("a"))));
	}
		
	private Column buildThumbnailColumn(ThumbnailWidget thumbnail){
		Column column = new Column("XS_12 SM_6 MD_4 LG_4");
		column.add(thumbnail);
		return column;
	}
	
	@UiHandler("btnMeCadastrar")
	void onBtnMeCadastrarClick(ClickEvent event){
	}
	
	//@UiHandler("btnCriarConta")
	void onBtnCriarContaClick(ClickEvent event){
		CriarContaModal modal = new CriarContaModal();
		modal.show();
	}
	
	//@UiHandler("btnReenviarEmail")
	void onbtnReenviarEmailClick(ClickEvent event){
		Alert.prompt("Reenviar e-mail de confirmação", "Especifique o e-mail utilizado para criar sua conta que reenviaremos um novo link de confirmação.", "E-mail:", new ConfirmTextCallback() {
			
			@Override
			public void onConfirm(String email) {
				formEmail.setValue(email);
				reenviarEmailForm.submit();
			}
			
			@Override
			public void onCancel() {
			}
		});
	}
	
}
