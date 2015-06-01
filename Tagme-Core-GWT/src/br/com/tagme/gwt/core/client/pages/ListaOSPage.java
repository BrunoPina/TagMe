package br.com.tagme.gwt.core.client.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.TextBox;

import br.com.sankhya.place.gwt.commons.utils.client.NumberUtils;
import br.com.sankhya.place.gwt.commons.utils.client.StringUtils;
import br.com.sankhya.place.gwt.commons.utils.client.XMLUtils;
import br.com.sankhya.place.gwt.http.client.ServiceProxyException;
import br.com.sankhya.place.gwt.http.client.XMLCallback;
import br.com.sankhya.place.gwt.http.client.XMLServiceProxy;
import br.com.sankhya.place.gwt.mvp.client.CompositeWithPresenter;
import br.com.sankhya.place.gwt.mvp.client.DefaultActivity;
import br.com.sankhya.place.gwt.mvp.client.PlaceController;
import br.com.sankhya.place.gwt.mvp.client.places.AbstractPlace;
import br.com.tagme.gwt.core.client.places.SearchPlace;
import br.com.tagme.gwt.core.client.widgets.BuscaSemResultados;
import br.com.tagme.gwt.core.client.widgets.LoadingBusca;
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
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class ListaOSPage extends CompositeWithPresenter {

	private static ListaOSPageUiBinder uiBinder = GWT
			.create(ListaOSPageUiBinder.class);

	interface ListaOSPageUiBinder extends UiBinder<Widget, ListaOSPage> {
	}

	@UiField
	TextBox txbSearchTerm;
	@UiField
	Button botaoSearch;
	@UiField
	Row searchResultsContainer;
	@UiField
	Row paginationContainer;
	@UiField
	BuscaSemResultados buscaSemResultados;
	@UiField
	LoadingBusca loadingBusca;
	private AbstractPlace contextPlace;

	private final List<Person> people = new ArrayList<Person>();

	public ListaOSPage(DefaultActivity presenter) {
		super(presenter);
		initWidget(uiBinder.createAndBindUi(this));

		this.contextPlace = presenter.getContextPlace();

		handleSearchResults(false, -1);
		String locator = presenter.getContextLocator();
		HashMap<String, String> params = presenter.getContextParams();
		final String page = NumberUtils.isInteger(locator) ? locator : "1";

		botaoSearch.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				consultaOs(txbSearchTerm.getText(), page);
				PlaceController.goTo(new SearchPlace("1?busca="
						+ txbSearchTerm.getText()));
			}
		});

		txbSearchTerm.addDomHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {

				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					
					consultaOs(txbSearchTerm.getText(), page);
					PlaceController.goTo(new SearchPlace("1?busca="
							+ txbSearchTerm.getText()));
				}
			}
		}, KeyDownEvent.getType());

		if (params != null && params.containsKey("busca")) {
			String termoBusca = params.get("busca");
			txbSearchTerm.setText(termoBusca);
			
			consultaOs(txbSearchTerm.getText(), page);
		}
	}

	private void consultaOs(String searchTerm, String page) {
		if (StringUtils.isEmpty(searchTerm)) {
			// TODO:PlaceController.goTo(new OrdensServicoPlace("home"));
			return;
		}
		handleSearchResults(true, 10);
		String queryLower = searchTerm.toLowerCase();

		Document doc = XMLParser.createDocument();

		Element parametros = doc.createElement("parametros");

		Element offsetElem = doc.createElement("offset");
		offsetElem.appendChild(doc.createTextNode("10"));
		parametros.appendChild(offsetElem);

		Element pageElem = doc.createElement("page");
		pageElem.appendChild(doc.createTextNode(page));
		parametros.appendChild(pageElem);

		Element searchTermElem = doc.createElement("searchTerm");
		searchTermElem.appendChild(doc.createTextNode(queryLower));
		parametros.appendChild(searchTermElem);

		doc.appendChild(parametros);

		XMLServiceProxy serviceProxy = new XMLServiceProxy(this);
		serviceProxy.call("commons@BuscaPessoaService", doc, new XMLCallback() {
			@Override
			public void onResponseReceived(Element response) {
				paginationContainer.clear();
				searchResultsContainer.clear();

				Element entidadesElem = XMLUtils.getFirstChild(response,
						"entidades");

				NodeList nodes = entidadesElem.getChildNodes();


				for (int i = 0; i < nodes.getLength(); i++) {

					if (nodes.item(i).getNodeName()
							.equalsIgnoreCase("entidade")) {

						Element codElement = XMLUtils.getFirstChild(
								(Element) nodes.item(i), "CODPES");

						String codPes = XMLUtils.getNodeValue(codElement);
						
						Element nomeElement = XMLUtils.getFirstChild(
								(Element) nodes.item(i), "NOMECOMPLETO");

						String nome = XMLUtils.getNodeValue(nomeElement);
						Element ninverElement = XMLUtils.getFirstChild(
								(Element) nodes.item(i), "DTNASC");
						String ninver = XMLUtils.getNodeValue(ninverElement);
						
						Element telElement = XMLUtils.getFirstChild(
								(Element) nodes.item(i), "CELULAR");
						String endTelefone = XMLUtils.getNodeValue(telElement);

						
						
						Person person = new Person(Long.parseLong(codPes) ,nome, ninver,endTelefone);
						SearchItemOsWidget searchItemOsWidget = new SearchItemOsWidget(
								person);
						searchResultsContainer.add(searchItemOsWidget);

					}
				}

				handleSearchResults(false, Integer.parseInt(entidadesElem.getAttribute("total")));

			}

			@Override
			public boolean onError(ServiceProxyException e) {

				return false;
			}
		});

	}

	private void handleSearchResults(boolean searchStart, int totResultados) {
		if (searchStart) {
			buscaSemResultados.setVisible(false);
			loadingBusca.setVisible(true);
		} else {
			if (totResultados <= 0) {
				buscaSemResultados.setVisible(true);
				if (totResultados == 0) {
					buscaSemResultados
							.setLeadMessage("Nenhum resultado encontrado.");
				}
			} else {
				buscaSemResultados.setVisible(false);
			}
			loadingBusca.setVisible(false);

		}
	}

	public class Person {
		final long cod;
		final String name;
		final String brithDay;
		final String telefoneEnd;

		Person(final long cod ,final String name, final String brithDay, final String telefoneEnd) {
			this.cod = cod;
			this.name = name;
			this.brithDay = brithDay;
			this.telefoneEnd = telefoneEnd;
		}

		@Override
		public String toString() {
			return name;
		}

		public long getCod() {
			return cod;
		}
		
		public String getName() {
			return name;
		}

		public String getBrithDay() {
			return brithDay;
		}

		public String getTelefoneEnd() {
			return telefoneEnd;
		}
	}

}
