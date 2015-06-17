package br.com.tagme.gwt.core.client.pages;

import java.util.ArrayList;
import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.extras.typeahead.client.base.Dataset;
import org.gwtbootstrap3.extras.typeahead.client.base.Suggestion;
import org.gwtbootstrap3.extras.typeahead.client.base.SuggestionCallback;
import org.gwtbootstrap3.extras.typeahead.client.ui.Typeahead;

import br.com.sankhya.place.gwt.commons.utils.client.XMLUtils;
import br.com.sankhya.place.gwt.http.client.ServiceProxyException;
import br.com.sankhya.place.gwt.http.client.XMLCallback;
import br.com.sankhya.place.gwt.http.client.XMLServiceProxy;
import br.com.sankhya.place.gwt.mvp.client.CompositeWithPresenter;
import br.com.sankhya.place.gwt.mvp.client.DefaultActivity;
import br.com.sankhya.place.gwt.mvp.client.PlaceController;
import br.com.tagme.gwt.core.client.places.SearchPlace;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.XMLParser;

public class IndexPage extends CompositeWithPresenter {

	private static IndexPageUiBinder uiBinder = GWT
			.create(IndexPageUiBinder.class);

	interface IndexPageUiBinder extends UiBinder<Widget, IndexPage> {
	}

	@UiField(provided = true)
	Typeahead<String> asyncTypeahead;
	@UiField
	Button botaoSearch;

	private final String placeholder = "Enter a name";

	private final List<String> nomes = new ArrayList<String>();

	public IndexPage(DefaultActivity presenter, String contextApp) {
		super(presenter);

		createAsyncTypeahead();

		initWidget(uiBinder.createAndBindUi(this));
		botaoSearch.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				PlaceController.goTo(new SearchPlace("1?busca="
						+ asyncTypeahead.getText()));
			}
		});
		
		asyncTypeahead.setPlaceholder("Busque por um nome");
	}

	private void createAsyncTypeahead() {

		asyncTypeahead = new Typeahead<String>(new Dataset<String>() {

			@Override
			public void findMatches(String query,
					final SuggestionCallback<String> callback) {
				final List<Suggestion<String>> suggestions = new ArrayList<Suggestion<String>>();

				final Dataset thisFinal = this;

				String queryLower = query.toLowerCase();

				if (queryLower.length() < 3) {
					return;
				}
				Document doc = XMLParser.createDocument();

				Element searchTermElem = doc.createElement("searchTerm");
				searchTermElem.appendChild(doc.createTextNode(queryLower));

				doc.appendChild(searchTermElem);

				XMLServiceProxy serviceProxy = new XMLServiceProxy(this);
				serviceProxy.call("commons@TypeaHeadService", doc,
						new XMLCallback() {
							@Override
							public void onResponseReceived(Element response) {
								Element nomesElem = XMLUtils.getFirstChild(
										response, "nomes");

								String json = XMLUtils.getNodeValue(nomesElem);

								JSONValue jsonValue = JSONParser
										.parseStrict(json);
								JSONArray jsonArray = jsonValue.isArray();

								for (int i = 0; i < jsonArray.size(); i++) {
									String nome = jsonArray.get(i).isString().stringValue();
									Suggestion<String> s = Suggestion.create(
											nome, nome, thisFinal);
									suggestions.add(s);
								}

								callback.execute(suggestions);

							}

							@Override
							public boolean onError(ServiceProxyException e) {

								return false;
							}
						});

			}

		});
		asyncTypeahead.setPlaceholder(placeholder);

		asyncTypeahead.addDomHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {

				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					PlaceController.goTo(new SearchPlace("1?busca="
							+ asyncTypeahead.getText()));
				}
			}
		}, KeyDownEvent.getType());
	}
}
