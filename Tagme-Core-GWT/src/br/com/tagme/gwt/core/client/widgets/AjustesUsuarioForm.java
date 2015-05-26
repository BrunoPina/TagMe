package br.com.tagme.gwt.core.client.widgets;

import java.util.Iterator;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.FieldSet;
import org.gwtbootstrap3.client.ui.Image;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.constants.Placement;
import org.gwtbootstrap3.client.ui.constants.ValidationState;
import org.gwtbootstrap3.client.ui.html.Paragraph;

import br.com.sankhya.place.gwt.auth.client.AppAuth;
import br.com.sankhya.place.gwt.commons.client.components.Alert;
import br.com.sankhya.place.gwt.commons.client.components.formitem.FormItem;
import br.com.sankhya.place.gwt.commons.client.components.formitem.FormItemWidget.ValueCommitCommand;
import br.com.sankhya.place.gwt.commons.utils.client.EnvironmentUtils;
import br.com.sankhya.place.gwt.commons.utils.client.StringUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class AjustesUsuarioForm extends Composite implements HasWidgets{

	private static AjustesUsuarioFormUiBinder	uiBinder	= GWT.create(AjustesUsuarioFormUiBinder.class);

	interface AjustesUsuarioFormUiBinder extends UiBinder<Widget, AjustesUsuarioForm> {
	}

	@UiField Modal modal;
	@UiField Image imgUsuario;
	@UiField FlowPanel outerPanel;
	
	@UiField FormItem<String> formEmail;
	@UiField FormItem<String> formNome;
	@UiField FormItem<Boolean> formAlteraSenha;
	@UiField FormItem<String> formSenhaAtual;
	@UiField FormItem<String> formNovaSenha;
	@UiField FormItem<String> formConfSenha;
	@UiField Button btnAlterarFoto;
	@UiField Button btnCancelarFoto;
	@UiField FileUpload formFoto;
	@UiField Paragraph fileName;
	@UiField FormPanel formPanel;
	@UiField ButtonGroup containerBtnFoto;
	@UiField FieldSet fsFormPanel;
	@UiField Button btnSalvar;
	private int counter = 0;
	
	private static final String TYPE_CHANGE_SUCCESS = "1";
	private static final String TYPE_CHANGE_FAIL = "2";
	
	public AjustesUsuarioForm() {
		initWidget(uiBinder.createAndBindUi(this));
		
		modal.getElement().setAttribute("tabindex", "-1");
		modal.setFade(true);
		modal.setRemoveOnHide(true);
		
		formPanel.setAction(EnvironmentUtils.getRelativeURL() + "ajustesConta");
		formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		formPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				btnSalvar.state().reset();
				
				Alert.hideWaitMessage();
				String[] arrResult = event.getResults().split(";");
				if(TYPE_CHANGE_SUCCESS.equals(arrResult[0])){
					AppAuth.getInstance().setName(arrResult[1]);
					AppAuth.getInstance().fireInfosChangeEvent();
					refresh();
					Alert.showSuccess("Ajustes realizados", "Suas alterações foram salvas com sucesso.");
					modal.hide();
				}else{
					Alert.showError("Ajustes não foram salvos",arrResult[1]);
				}
			}
		});
		formPanel.addSubmitHandler(new SubmitHandler() {
			
			@Override
			public void onSubmit(SubmitEvent event) {
				btnSalvar.state().loading();
				Alert.showWaitMessage();
			}
		});
		
		imgUsuario.setUrl(EnvironmentUtils.getRelativeURL() + "image/user/currUser"+counter+".png?w=90&h=90&r=true&s=true");
		
		formEmail.setValue(AppAuth.getInstance().getUsername());
		
		formNome.setValue(AppAuth.getInstance().getName());
		
		formAlteraSenha.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if(event.getValue() == Boolean.TRUE){
					formSenhaAtual.setEnabled(true);
					formNovaSenha.setEnabled(true);
					formConfSenha.setEnabled(true);
				}else{
					formSenhaAtual.setValue("");
					formSenhaAtual.setEnabled(false);
					formSenhaAtual.hideTooltip();
					formNovaSenha.setValue("");
					formNovaSenha.setEnabled(false);
					formNovaSenha.hideTooltip();
					formConfSenha.setValue("");
					formConfSenha.setEnabled(false);
					formConfSenha.hideTooltip();
				}
			}
		});
		
		formConfSenha.addOnValueCommitCommand(new ValueCommitCommand() {
			
			@Override
			public void validate() {
				String novaSenha = formNovaSenha.getValueAsString();
				String senhaConf = formConfSenha.getValueAsString();
				hideAllTooltips();
				if(StringUtils.isEmpty(senhaConf)){
					if(!StringUtils.isEmpty(novaSenha)){
						formConfSenha.createAndShowTooltip("Confirme sua nova senha", ValidationState.ERROR, Placement.TOP);
					}else{
						formNovaSenha.createAndShowTooltip("Informe sua nova senha", ValidationState.ERROR);
					}
				}else{
					if(!senhaConf.equals(novaSenha)){
						formConfSenha.createAndShowTooltip("Senhas não conferem", ValidationState.ERROR, Placement.TOP);
					}else{
						if(StringUtils.isEmpty(formSenhaAtual.getValueAsString())){
							formSenhaAtual.createAndShowTooltip("Informe sua senha atual", ValidationState.ERROR);
						}else{
							formConfSenha.createAndShowTooltip("Ok", ValidationState.SUCCESS, Placement.RIGHT);
						}
					}
				}
				
			}
		});
		
		formSenhaAtual.addOnValueCommitCommand(new ValueCommitCommand() {
			
			@Override
			public void validate() {
				hideAllTooltips();
				if(StringUtils.isEmpty(formSenhaAtual.getValueAsString())){
					formSenhaAtual.createAndShowTooltip("Informe sua senha atual", ValidationState.ERROR);
				}else if(!StringUtils.isEmpty(formNovaSenha.getValueAsString()) && !StringUtils.isEmpty(formConfSenha.getValueAsString()) && formConfSenha.getValueAsString().equals(formNovaSenha.getValueAsString())){
					formConfSenha.createAndShowTooltip("Ok", ValidationState.SUCCESS, Placement.RIGHT);
				}
			}
		});
		
		formFoto.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				String currFileName = formFoto.getFilename().replace("\\", "");
				currFileName = currFileName.replace("C:fakepath", "");
				if(StringUtils.isEmpty(currFileName)){
					fileName.setVisible(false);
				}else{
					fileName.setText(currFileName);
					fileName.setVisible(true);
				}
				btnCancelarFoto.removeFromParent();
				containerBtnFoto.add(btnCancelarFoto);
				btnCancelarFoto.setVisible(true);
			}
		});
	}
	
	@UiHandler("btnAlterarFoto")
	public void onBtnAlterarFotoClick(ClickEvent event){
		if(formFoto.getParent() == null){
			fsFormPanel.add(formFoto);
		}
		click(formFoto.getElement());
	}
	
	@UiHandler("btnCancelarFoto")
	public void onBtnCancelarFotoClick(ClickEvent event){
		formFoto.removeFromParent();
		fileName.setVisible(false);
		btnCancelarFoto.removeFromParent();
	}
	
	public void show(){
		modal.show();
	}

	@Override
	public void add(Widget w) {
	}

	@Override
	public void clear() {
	}

	@Override
	public Iterator<Widget> iterator() {
		return null;
	}

	@Override
	public boolean remove(Widget w) {
		return outerPanel.remove(w);
	}
	
	public static native void click(Element element)/*-{
		element.click();
	}-*/;
	
	private void hideAllTooltips(){
		formSenhaAtual.hideTooltip();
		formConfSenha.hideTooltip();
		formNovaSenha.hideTooltip();
	}
	
	@UiHandler("btnSalvar")
	public void onBtnSalvarClick(ClickEvent e){
		formPanel.submit();
	}
	
	private void refresh(){
		counter++;
		formNome.setValue(AppAuth.getInstance().getName());
		
		fileName.setVisible(false);
		btnCancelarFoto.removeFromParent();
		
		imgUsuario.setUrl(EnvironmentUtils.getRelativeURL() + "image/user/currUser"+counter+".png?w=90&h=90&r=true&s=true");
		
		formAlteraSenha.setValue("N");
		formSenhaAtual.setValue("");
		formSenhaAtual.setEnabled(false);
		formSenhaAtual.hideTooltip();
		formNovaSenha.setValue("");
		formNovaSenha.setEnabled(false);
		formNovaSenha.hideTooltip();
		formConfSenha.setValue("");
		formConfSenha.setEnabled(false);
		formConfSenha.hideTooltip();
	}
	
}
