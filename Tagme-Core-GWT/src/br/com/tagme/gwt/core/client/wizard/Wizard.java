package br.com.tagme.gwt.core.client.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.PageHeader;
import org.gwtbootstrap3.client.ui.constants.IconSize;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class Wizard extends Composite implements IWizard {

	private static WizardUiBinder	uiBinder			= GWT.create(WizardUiBinder.class);

	private ArrayList<IStep>		steps;
	private ArrayList<Icon>		    icons;
	private ArrayList<String>		titles;
	private Map<String, Object>		dataWizard;
	private FinishHandler			finishHandler;
	private int						currentStepIndex	= -1;
	
	@UiField Column stepsContainer;
	@UiField Column stepsIconContainer;
	@UiField PageHeader wizardTitle;

	interface WizardUiBinder extends UiBinder<Widget, Wizard> {
	}

	public Wizard() {
		initWidget(uiBinder.createAndBindUi(this));
		steps = new ArrayList<IStep>();
		dataWizard = new HashMap<String, Object>();
		icons = new ArrayList<Icon>();
		titles = new ArrayList<String>();
	}

	public void addStep(IStep step) {
		step.setWizard(this);
		steps.add(step);
		Icon icon = new Icon(step.getIcon());
		icon.setSize(IconSize.TIMES2);
		icons.add(icon);
		titles.add(step.getTitle());
	}

	public Map<String, Object> getDataWizard() {
		return dataWizard;
	}

	public void setDataWizard(Map<String, Object> dataWizard) {
		this.dataWizard = dataWizard;
	}

	public void positionStep(int index) {
		currentStepIndex = index;
		
		IStep currentStep = steps.get(currentStepIndex);
		stepsIconContainer.clear();
		for(int i=0;i<icons.size();i++){
			if(currentStepIndex == i){
				icons.get(i).addStyleName("selected-step");
			}else{
				icons.get(i).removeStyleName("selected-step");
			}
			stepsIconContainer.add(icons.get(i));
		}
		
		wizardTitle.setText(currentStep.getTitle());

		stepsContainer.clear();
		
		stepsContainer.add((Widget) currentStep);
		
		/*if(stackPanel.getWidgetIndex() == -1){
			stackPanel.add((Widget) currentStep);
		}

		stackPanel.setSelected((Widget) currentStep);*/
		
		currentStep.showStep();
		refresh();
	}

	public void refresh() {
		IStep currentStep = steps.get(currentStepIndex);
		int indexNextStep = getNextStepIndex();
		btnVoltar.setEnabled(currentStepIndex > 0);
		btnAvancar.setEnabled(currentStep.canNext() && indexNextStep != -1);
		btnFinalizar.setEnabled(currentStep.canFinish());
		//btnCancelar.setEnabled(currentStep.canCancel());
	}

	public void finish() {
		IStep currentStep = steps.get(currentStepIndex);

		if (currentStep.canNext() || currentStep.canFinish()) {
			currentStep.exitStep();

			if (finishHandler != null) {
				finishHandler.onFinish(dataWizard);
			}
		}
	}

	public void cancel() {
		IStep currentStep = steps.get(currentStepIndex);

		if (!currentStep.canCancel()) {
			return;
		}

		if (finishHandler != null) {
			finishHandler.onCancel();
		}
	}

	public void next() {
		if (currentStepIndex > -1) {
			IStep currentStep = steps.get(currentStepIndex);

			if (!currentStep.canNext()) {
				return;
			}

			currentStep.exitStep();

			/*if (currentStep instanceof HasNextValidation) {
				((HasNextValidation) currentStep).validate(new NextAction() {

					@Override
					public void doNext() {
						executeNext();
					}
				});
			} else {*/
				executeNext();
			//}
		} else {
			executeNext();
		}
	}

	private void executeNext() {
		int nextStepIndex = getNextStepIndex();

		if (nextStepIndex == -1) {
			finish();
			return;
		} else {
			positionStep(nextStepIndex);
		}
	}

	public void previous() {
		int previousStepIndex = getPreviousStepIndex();

		if (previousStepIndex > -1) {
			positionStep(previousStepIndex);
		}
	}

	public int getPreviousStepIndex() {
		for (int i = currentStepIndex - 1; i < steps.size(); i--) {
			if (steps.get(i).isVisible()) {
				return i;
			}
		}

		return -1;
	}

	public int getNextStepIndex() {
		for (int i = currentStepIndex + 1; i < steps.size(); i++) {
			if (steps.get(i).isVisible()) {
				return i;
			}
		}

		return -1;
	}

	public void setFinishHandler(FinishHandler finishHandler) {
		this.finishHandler = finishHandler;
	}

	//@UiField
	//Button		btnCancelar;

	@UiField
	Button		btnVoltar;

	@UiField
	Button		btnAvancar;

	@UiField
	Button		btnFinalizar;

	/*@UiHandler("btnCancelar")
	void closeHandler(ClickEvent e) {
		cancel();
	}*/

	@UiHandler("btnVoltar")
	void previousHandler(ClickEvent e) {
		previous();
	}

	@UiHandler("btnAvancar")
	void nextHandler(ClickEvent e) {
		next();
	}

	@UiHandler("btnFinalizar")
	void finishHandler(ClickEvent e) {
		finish();
	}

	@Override
	public Button getNextButton() {
		return btnAvancar;
	}

	@Override
	public Button getPreviousButton() {
		return btnVoltar;
	}

	@Override
	public Button getFinishButton() {
		return btnFinalizar;
	}

	@Override
	public Button getCancelButton() {
		return null;
		//return btnCancelar;
	}
}
