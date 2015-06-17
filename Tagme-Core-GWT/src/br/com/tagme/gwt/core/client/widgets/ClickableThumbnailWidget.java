package br.com.tagme.gwt.core.client.widgets;

import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.Image;
import org.gwtbootstrap3.client.ui.ThumbnailLink;
import org.gwtbootstrap3.client.ui.Tooltip;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.html.Paragraph;

import br.com.sankhya.place.gwt.mvp.client.ActivityController;
import br.com.sankhya.place.gwt.mvp.client.PlaceController;
import br.com.sankhya.place.gwt.mvp.client.places.AbstractPlace;
import br.com.sankhya.place.gwt.theme.sankhya.client.CommonImagesFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class ClickableThumbnailWidget extends ThumbnailWidget {

	private static ClickableThumbnailWidgetUiBinder	uiBinder	= GWT.create(ClickableThumbnailWidgetUiBinder.class);

	@UiField ThumbnailLink thumbnailLink;
	@UiField Tooltip tooltip;
	final private AbstractPlace targetPlace;
	
	private HandlerRegistration thumbnailLinkRegistration;
	
	
	
	@UiField Heading heading;
	@UiField Paragraph paragraph;
	@UiField Image imgItem;	
	
	interface ClickableThumbnailWidgetUiBinder extends UiBinder<Widget, ClickableThumbnailWidget> {
	}

	public ClickableThumbnailWidget(ImageResource imageRes, String heading, String paragraph, AbstractPlace targetPlace) {
		this(imageRes, heading, paragraph, targetPlace, null);
	}
	
	public ClickableThumbnailWidget(ImageResource imageRes, String heading, String paragraph, AbstractPlace targetPlace, ClickHandler handler) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.imgItem.setResource(imageRes);
		this.heading.setText(heading);
		this.paragraph.setText(paragraph);
		this.targetPlace = targetPlace;
		
		if(handler == null){
			thumbnailLink.setHref(PlaceController.buildHref(targetPlace));
		}else{
			thumbnailLinkRegistration = thumbnailLink.addClickHandler(handler);
		}

		if(!ActivityController.hasAccess(targetPlace)){
			removeLink();
		}
	 	
	}
	
	public AbstractPlace getTargetPlace(){
		return targetPlace;
	}
	
	public void removeLink(){
		if(thumbnailLinkRegistration != null){
			thumbnailLinkRegistration.removeHandler();
		}
		thumbnailLink.setHref("#");
		thumbnailLink.addStyleName("disabled");
		tooltip.setTitle("Não Autorizado");
	}
	
}
