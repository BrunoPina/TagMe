package br.com.tagme.gwt.core.client.wizard;

import org.gwtbootstrap3.client.ui.constants.IconType;

public interface IStep {
	boolean canNext();
	boolean isVisible();
	boolean canFinish();
	boolean canCancel();
	void showStep();
	void exitStep();
	void setWizard(IWizard wizard);
	IconType getIcon();
	String getTitle();
}
