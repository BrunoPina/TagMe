package br.com.tagme.gwt.core.client.wizard.newperson;

import org.gwtbootstrap3.client.ui.constants.IconType;

import br.com.tagme.gwt.core.client.wizard.DefaultStep;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class BasicInfosStep extends DefaultStep {

	private static BasicInfosStepUiBinder	uiBinder	= GWT.create(BasicInfosStepUiBinder.class);

	interface BasicInfosStepUiBinder extends UiBinder<Widget, BasicInfosStep> {
	}

	public BasicInfosStep() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	
	private IconType stepIcon;
	private String stepTitle;

	public BasicInfosStep(String firstName, IconType icon, String title) {
		initWidget(uiBinder.createAndBindUi(this));
		this.stepIcon = icon;
		this.stepTitle = title;
	}

	public IconType getIcon() {
		return stepIcon;
	}
	
	public String getTitle() {
		return stepTitle;
	}
	
}
