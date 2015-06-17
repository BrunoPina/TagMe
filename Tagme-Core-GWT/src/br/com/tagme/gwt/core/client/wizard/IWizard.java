package br.com.tagme.gwt.core.client.wizard;

import java.util.Map;

import org.gwtbootstrap3.client.ui.Button;

public interface IWizard {

	void addStep(IStep step);

	Map<String, Object> getDataWizard();

	void setDataWizard(Map<String, Object> dataWizard);

	void positionStep(int index);

	void refresh();

	void finish();

	void cancel();

	void next();

	void previous();

	int getPreviousStepIndex();

	int getNextStepIndex();

	void setFinishHandler(FinishHandler finishHandler);

	Button getNextButton();

	Button getPreviousButton();

	Button getFinishButton();

	Button getCancelButton();

	public interface FinishHandler {
		public void onFinish(Map<String, Object> data);

		public void onCancel();
	}
	
	public static interface NextAction{
		void doNext();
	}
}
