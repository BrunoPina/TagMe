package br.com.tagme.gwt.core.client.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.TextBox;

import br.com.sankhya.place.gwt.commons.client.components.GenericData;
import br.com.sankhya.place.gwt.commons.client.xml.EntityParser;
import br.com.sankhya.place.gwt.commons.client.xml.EntityParser.FormItemBuilder;
import br.com.sankhya.place.gwt.commons.client.xml.EntityParser.PaginationResult;
import br.com.sankhya.place.gwt.commons.utils.client.NumberUtils;
import br.com.sankhya.place.gwt.commons.utils.client.StringUtils;
import br.com.sankhya.place.gwt.http.client.ServiceProxyException;
import br.com.sankhya.place.gwt.http.client.XMLCallback;
import br.com.sankhya.place.gwt.http.client.XMLServiceProxy;
import br.com.sankhya.place.gwt.mvp.client.CompositeWithPresenter;
import br.com.sankhya.place.gwt.mvp.client.DefaultActivity;
import br.com.sankhya.place.gwt.mvp.client.places.AbstractPlace;
import br.com.tagme.gwt.core.client.widgets.BuscaSemResultados;
import br.com.tagme.gwt.core.client.widgets.LoadingBusca;
import br.com.tagme.gwt.core.client.widgets.PaginationWidget;
import br.com.tagme.gwt.core.client.widgets.SearchItemOsWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.XMLParser;

public class ListaOSPage extends CompositeWithPresenter {

	private static ListaOSPageUiBinder uiBinder = GWT.create(ListaOSPageUiBinder.class);

	interface ListaOSPageUiBinder extends UiBinder<Widget, ListaOSPage> {
	}

	@UiField TextBox txbSearchTerm;
	@UiField Button botaoSearch;
	@UiField Row searchResultsContainer;
	@UiField Row paginationContainer;
	@UiField BuscaSemResultados buscaSemResultados;
	@UiField LoadingBusca loadingBusca;
	private AbstractPlace contextPlace;
	
	private final List<Person> people = new ArrayList<Person>();
	
	public ListaOSPage(DefaultActivity presenter) {
		super(presenter);
		initWidget(uiBinder.createAndBindUi(this));
		
		this.contextPlace = presenter.getContextPlace();
		
		people.add(new Person("Bill Happernan", 50));
		people.add(new Person("Bob Wondersteen", 38));
		people.add(new Person("Bobak Longfield", 24));
		people.add(new Person("Dawton Kernigham", 27));
		people.add(new Person("Eumon Wonderburgh", 39));
		people.add(new Person("Frank Dinkelstein", 66));
		people.add(new Person("Gene Razelhaze", 42));
		people.add(new Person("Gus Peppermint", 31));
		people.add(new Person("Jebediah Franklin", 57));
		people.add(new Person("Kirrim Proplov", 73));
		people.add(new Person("Linus Weenkol", 103));
		people.add(new Person("Mortimer Jackendale", 24));
		people.add(new Person("Walt Crinistool", 43));
		people.add(new Person("Wernher Agusto", 52));
		people.add(new Person("Julia Sarah Sandy Margret Johanna Cathrin Jenkins", 34));
		
		handleSearchResults(false, -1);
		String locator = presenter.getContextLocator();
		HashMap<String, String> params = presenter.getContextParams();
		final String page =  NumberUtils.isInteger(locator) ? locator : "1";
		
		botaoSearch.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				handleSearchResults(true, 10);
				consultaOs(txbSearchTerm.getText(), page);
				//TODO:PlaceController.goTo(new OrdensServicoPlace("1?busca="+txbSearchTerm.getText()));
			}
		});
		
		txbSearchTerm.addDomHandler(new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				
				if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
					handleSearchResults(true, 10);
					consultaOs(txbSearchTerm.getText(), page);
					//TODO:PlaceController.goTo(new OrdensServicoPlace("1?busca="+txbSearchTerm.getText()));
				}
			}
		}, KeyDownEvent.getType());
		
		if(params != null && params.containsKey("busca")){
			String termoBusca = params.get("busca");
			txbSearchTerm.setText(termoBusca);
			consultaOs(txbSearchTerm.getText(), page);
		}
	}
	
	private void consultaOs(String searchTerm, String page){
		if(StringUtils.isEmpty(searchTerm)){
			//TODO:PlaceController.goTo(new OrdensServicoPlace("home"));
			return;
		}
		handleSearchResults(false, 10);
		for(Person person : people){
			SearchItemOsWidget searchItemOsWidget = new SearchItemOsWidget(person);
			searchResultsContainer.add(searchItemOsWidget);
		}
	}
	
	private void handleSearchResults(boolean searchStart, int totResultados){
		if(searchStart){
			buscaSemResultados.setVisible(false);
			loadingBusca.setVisible(true);
		}else{
			if(totResultados <= 0){
				buscaSemResultados.setVisible(true);
				if(totResultados == 0){
					buscaSemResultados.setLeadMessage("Nenhum resultado encontrado.");
				}
			}else{
				buscaSemResultados.setVisible(false);
			}
			loadingBusca.setVisible(false);
			paginationContainer.clear();
			searchResultsContainer.clear();
		}
	}
	
	public class Person {
		final String	name;
		final int		age;

		Person(final String name, final int age) {
			this.name = name;
			this.age = age;
		}

		@Override
		public String toString() {
			return name;
		}
	}

}
