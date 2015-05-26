package br.com.tagme.gwt.core.client.widgets;

import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Pagination;
import org.gwtbootstrap3.client.ui.constants.ColumnOffset;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;

import br.com.sankhya.place.gwt.commons.utils.client.NumberUtils;
import br.com.sankhya.place.gwt.mvp.client.PlaceController;
import br.com.sankhya.place.gwt.mvp.client.places.AbstractPlace;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class PaginationWidget extends Composite{

	private static PaginationWidgetUiBinder uiBinder = GWT.create(PaginationWidgetUiBinder.class);
	
	interface PaginationWidgetUiBinder extends UiBinder<Widget, PaginationWidget> {
	}
	
	@UiField Column pagContainer;
	@UiField Pagination pagination;
	
	private AbstractPlace place;
	private int nextPage = 1;
	
	public PaginationWidget(AbstractPlace currPlace, final int totalPages) {
		initWidget(uiBinder.createAndBindUi(this));
		this.place = currPlace;
		
		pagContainer.setSize(ColumnSize.XS_12, ColumnSize.SM_9);
		pagContainer.setOffset(ColumnOffset.SM_3);
		
		int pageNum = NumberUtils.isInteger(currPlace.getLocator()) ? Integer.valueOf(currPlace.getLocator()) : 1;
		pageNum = pageNum > totalPages ? 1 : pageNum;
		
		AnchorListItem firstLink = pagination.addPreviousLink();
		firstLink.setEnabled(false);
		if(pageNum != 1){
			firstLink.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					place.setLocator(String.valueOf(1));
					PlaceController.goTo(place.clone());
				}
			});
			firstLink.setEnabled(true);
		}
		
		buildInnerPages(pageNum, totalPages);
		
		AnchorListItem lastLink = pagination.addNextLink();
		lastLink.setEnabled(false);
		if(pageNum != totalPages){
			lastLink.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					place.setLocator(String.valueOf(totalPages));
					PlaceController.goTo(place.clone());
				}
			});
			lastLink.setEnabled(true);
		}
	}
	
	private void buildInnerPages(int currPage, final int totalPages){
		AnchorListItem etcLink = new AnchorListItem("...");
		etcLink.setEnabled(false);
		
		if(currPage+2 >= totalPages && currPage > 3){
			AnchorListItem pageLink = new AnchorListItem(String.valueOf(1));
			pageLink.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					place.setLocator(String.valueOf(1));
					PlaceController.goTo(place.clone());
				}
			});
			pagination.add(pageLink);
			
			pagination.add(etcLink);
		}
		
		for(int i=2;i>=1;i--){
			final int finalLinkPage = currPage - i;
			if(finalLinkPage <= 0){
				continue;
			}
			AnchorListItem pageLink = new AnchorListItem(String.valueOf(finalLinkPage));
			pageLink.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					place.setLocator(String.valueOf(finalLinkPage));
					PlaceController.goTo(place.clone());
				}
			});
			pagination.add(pageLink);
		}
		
		AnchorListItem currLink = new AnchorListItem(String.valueOf(currPage));
		currLink.setActive(true);
		pagination.add(currLink);
		
		for(int i=1;i<3;i++){
			final int finalLinkPage = currPage+i;
			if(finalLinkPage > totalPages){
				break;
			}
			
			AnchorListItem pageLink = new AnchorListItem(String.valueOf(finalLinkPage));
			pageLink.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					place.setLocator(String.valueOf(finalLinkPage));
					PlaceController.goTo(place.clone());
				}
			});
			pagination.add(pageLink);
		}
		
		if(currPage+2 < totalPages){
			pagination.add(etcLink);
			
			AnchorListItem pageLink = new AnchorListItem(String.valueOf(totalPages));
			pageLink.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					place.setLocator(String.valueOf(totalPages));
					PlaceController.goTo(place.clone());
				}
			});
			pagination.add(pageLink);
		}
	}
	
}
