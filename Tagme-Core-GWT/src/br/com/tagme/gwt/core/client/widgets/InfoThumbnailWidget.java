package br.com.tagme.gwt.core.client.widgets;

import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.html.Paragraph;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class InfoThumbnailWidget extends ThumbnailWidget {

	private static InfoThumbnailWidgetUiBinder	uiBinder	= GWT.create(InfoThumbnailWidgetUiBinder.class);

	@UiField Icon icon;
	@UiField Heading heading;
	@UiField Paragraph paragraph;
	
	interface InfoThumbnailWidgetUiBinder extends UiBinder<Widget, InfoThumbnailWidget> {
	}

	public InfoThumbnailWidget(IconType iconType, String heading, String paragraph) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.icon.setType(iconType);
		this.heading.setText(heading);
		this.paragraph.setText(paragraph);
	}

}
