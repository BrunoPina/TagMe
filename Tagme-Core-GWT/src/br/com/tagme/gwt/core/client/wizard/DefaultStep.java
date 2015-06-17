package br.com.tagme.gwt.core.client.wizard;

import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.user.client.ui.Composite;

public abstract class DefaultStep extends Composite implements IStep {

	protected IWizard	wizard;

	public boolean canNext() {
		return true;
	}

	public boolean canFinish() {
		return false;
	}

	public boolean canCancel() {
		return true;
	}

	public void showStep() {
	}

	public void exitStep() {
	}

	public boolean isVisible() {
		return true;
	}

	public void setWizard(IWizard wizard) {
		this.wizard = wizard;
	}

}
