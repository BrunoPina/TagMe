package br.com.tagme.gwt.core.client.widgets;

import java.util.ArrayList;

import org.gwtbootstrap3.client.ui.PanelGroup;

public interface IContextAppMenu {
	public void render(PanelGroup menuAccordion, ArrayList<MenuItem> logicalMenu);
}
