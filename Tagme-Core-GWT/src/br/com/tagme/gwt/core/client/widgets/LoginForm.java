package br.com.tagme.gwt.core.client.widgets;

import java.util.ArrayList;
import java.util.Collection;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Image;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.SubmitButton;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.gwt.CellTable;

import br.com.sankhya.place.gwt.auth.client.AppAuth;
import br.com.sankhya.place.gwt.auth.client.AppAuthEvent;
import br.com.sankhya.place.gwt.auth.client.AppAuthEventHandler;
import br.com.sankhya.place.gwt.commons.client.components.Alert;
import br.com.sankhya.place.gwt.commons.client.components.GenericData;
import br.com.sankhya.place.gwt.commons.utils.client.EnvironmentUtils;
import br.com.sankhya.place.gwt.commons.utils.client.StringUtils;
import br.com.sankhya.place.gwt.mvp.client.PlaceController;
import br.com.tagme.gwt.theme.tagme.client.CommonImagesFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class LoginForm extends Composite{

	private static LoginPageUiBinder	uiBinder	= GWT.create(LoginPageUiBinder.class);
	private static AppAuthEventHandler authHandler;
	private static CommonImagesFactory res 	   = GWT.create(CommonImagesFactory.class);
	
	interface LoginPageUiBinder extends UiBinder<Widget, LoginForm> {
	}

	@UiField SubmitButton btnLogin;
	@UiField FormPanel loginForm;
	@UiField TextBox username;
	@UiField Input password;
	@UiField Image imgLogo;
	
	@UiField Modal modalParceiros;
	@UiField ModalBody containerGridParceiros;
	@UiField Button botaoEscolherParceiro;
	
	private GenericData gridSelectedParceiro;
	
	public LoginForm() {
		initWidget(uiBinder.createAndBindUi(this));
		
		imgLogo.setResource(res.create().logosankhya());
		
		if(authHandler == null){
			authHandler = new AppAuthEventHandler() {
				
				@Override
				public void onAppAuthChange(AppAuthEvent event) {
					
					if(event.typeLoginSuccess()){
						PlaceController.goTo("redirect","lastOrIndex");
					}else if(event.typeLoginFail()){
						String message = event.getMessage();
						Alert.showTempError("Autenticação não realizada", message == null ? "Não foi possível autenticar seu usuário." : message);
					}else if(event.typeLoginMultiplosParceiros()){
						exibeSelecaoParceiros(event.getMessage());
					}
					
					Alert.hideWaitMessage();
					btnLogin.state().reset();
				}
			};
			
			AppAuth.getInstance().addAppAuthHandler(authHandler);
		}
		
		loginForm.setAction(EnvironmentUtils.getRelativeURL() + "j_spring_security_check");
		loginForm.addSubmitHandler(new SubmitHandler() {
			
			@Override
			public void onSubmit(SubmitEvent event) {
				Alert.showWaitMessage();
				btnLogin.state().loading();
			}
		});
		
		loginForm.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				AppAuth.getInstance().tryAuth(event.getResults());
			}
		});
		
	}
	
	public String getUsernameValue(){
		return username.getValue();
	}
	
	public String getPasswordValue(){
		return password.getValue();
	}
	
	private void exibeSelecaoParceiros(String strParceiros){
		
		containerGridParceiros.clear();
		
		ListDataProvider<GenericData> dataProvider = new ListDataProvider<GenericData>();
		
		String[] arrMultiplosParceiros = strParceiros.split("#");
		
		Collection<String> colCodParc = new ArrayList<String>();
		for(int i = 0; i<arrMultiplosParceiros.length; i = i + 4){
			
			String codParc = arrMultiplosParceiros[i];
			String nomeParc = arrMultiplosParceiros[i+1];
			String codContato = arrMultiplosParceiros[i+2];
			String nomeContato = arrMultiplosParceiros[i+3];
			
			GenericData genericData = new GenericData();
			genericData.addValue("CODPARC", codParc);
			genericData.addValue("NOMEPARC", nomeParc);
			genericData.addValue("CODCONTATO", codContato);
			genericData.addValue("NOMECONTATO", nomeContato);
			
			colCodParc.add(codParc);

			dataProvider.getList().add(genericData);
			
		}
		
		CellTable<GenericData> gridMultiplosParceiros = new CellTable<GenericData>();
		
		gridMultiplosParceiros.setBordered(false);
		gridMultiplosParceiros.setCondensed(true);
		gridMultiplosParceiros.setHover(false);
		gridMultiplosParceiros.setStriped(true);
		
		TextColumn<GenericData> colNomeParc = new TextColumn<GenericData>(){

			@Override
			public String getValue(GenericData genericData) {
				return genericData.getValueAsString("NOMEPARC");
			}
			
		};
		gridMultiplosParceiros.addColumn(colNomeParc, "Selecione um dos parceiros para continuar");
		
		if(StringUtils.checkDuplicateUsingAdd(colCodParc)){
			TextColumn<GenericData> colNomeContato = new TextColumn<GenericData>(){
		
				@Override
				public String getValue(GenericData genericData) {
					return genericData.getValueAsString("NOMECONTATO");
				}
				
			};
			gridMultiplosParceiros.addColumn(colNomeContato, "Contato");
		}
		
		dataProvider.addDataDisplay(gridMultiplosParceiros);
		
		containerGridParceiros.add(gridMultiplosParceiros);
		
		gridMultiplosParceiros.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		
		final SingleSelectionModel<GenericData> selectionModel = new SingleSelectionModel<GenericData>();
		gridMultiplosParceiros.setSelectionModel(selectionModel);
		
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				gridSelectedParceiro = selectionModel.getSelectedObject();
			}
		});
		
		modalParceiros.show();
		
	}
	
	@UiHandler("botaoEscolherParceiro")
	void escolherParceiro(ClickEvent e){
		if(gridSelectedParceiro != null){
			modalParceiros.hide();
			String codParc = gridSelectedParceiro.getValueAsString("CODPARC");
			String codContato = gridSelectedParceiro.getValueAsString("CODCONTATO");
			String strUsername = username.getText();
			if(!StringUtils.isEmpty(strUsername)){
				strUsername = strUsername.split(":")[0];
			}
			username.setValue(strUsername + ":" + codParc + ":" + codContato);
			//username.setText(strUsername + ":" + codParc + ":" + codContato);
			loginForm.submit();
		}else{
			Alert.showError("Escolha um parceiro.");
		}
	}
	
}
