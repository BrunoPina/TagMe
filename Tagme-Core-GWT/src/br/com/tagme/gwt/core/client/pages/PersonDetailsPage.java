package br.com.tagme.gwt.core.client.pages;

import java.util.Date;

import org.gwtbootstrap3.client.ui.Radio;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.extras.datetimepicker.client.ui.DateTimePicker;
import org.gwtbootstrap3.extras.select.client.ui.Select;

import br.com.sankhya.place.gwt.commons.utils.client.NumberUtils;
import br.com.sankhya.place.gwt.commons.utils.client.XMLUtils;
import br.com.sankhya.place.gwt.http.client.ServiceProxyException;
import br.com.sankhya.place.gwt.http.client.XMLCallback;
import br.com.sankhya.place.gwt.http.client.XMLServiceProxy;
import br.com.sankhya.place.gwt.mvp.client.CompositeWithPresenter;
import br.com.sankhya.place.gwt.mvp.client.DefaultActivity;
import br.com.sankhya.place.gwt.mvp.client.places.AbstractPlace;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.XMLParser;

public class PersonDetailsPage extends CompositeWithPresenter {


	private static PersonDetailsPageUiBinder	uiBinder	= GWT.create(PersonDetailsPageUiBinder.class);

	interface PersonDetailsPageUiBinder extends UiBinder<Widget, PersonDetailsPage> {
	}
	
	@UiField
	TextBox txbNome;
	@UiField
	DateTimePicker nascimento;
	@UiField
	Radio radioSexoM;
	@UiField
	Radio radioSexoF;
	@UiField
	Select selectEstadoCivil;
	@UiField
	TextBox rg;
	@UiField
	TextBox cpf;
	@UiField
	Select tipoLogradouro;
	@UiField
	TextBox endereco;
	@UiField
	TextBox numero;
	@UiField
	TextBox bairro;
	@UiField
	TextBox cep;
	@UiField
	TextBox cidade;
	@UiField
	Select estado;
	@UiField
	TextBox residencial;
	@UiField
	TextBox celular;
	@UiField
	TextBox email;
	
	private AbstractPlace contextPlace;

	public PersonDetailsPage(DefaultActivity presenter) {
		super(presenter);
		
		initWidget(uiBinder.createAndBindUi(this));
		//CoreEntryPoint.addBottomNavbar(null);
		
		this.contextPlace = presenter.getContextPlace();
		
		String locator = presenter.getContextLocator();
		final String codpes = NumberUtils.isInteger(locator) ? locator : "1";

		Document doc = XMLParser.createDocument();

		Element codPesElem = doc.createElement("codPes");
		codPesElem.appendChild(doc.createTextNode(codpes));

		doc.appendChild(codPesElem);

		XMLServiceProxy serviceProxy = new XMLServiceProxy(this);
		serviceProxy.call("commons@DetalhesPessoaService", doc, new XMLCallback() {
			@Override
			public void onResponseReceived(Element response) {
				
				Element pessoaElem = XMLUtils.getFirstChild(response,"pessoa");
				String pessoaStr = XMLUtils.getNodeValue(pessoaElem);
				JSONValue jsonValue = JSONParser.parseStrict(pessoaStr);
				JSONObject pessoa = jsonValue.isObject();
				txbNome.setText( pessoa.get("nomeCompleto").isString().stringValue());
				nascimento.setValue(new Date());//colocar data correta
				radioSexoM.setValue("M".equals(pessoa.get("sexo").isString().stringValue()));
				radioSexoF.setValue("F".equals(pessoa.get("sexo").isString().stringValue()));
				//selectEstadoCivil
				//rg
				cpf.setText(pessoa.get("cpf").isString().stringValue());
				//tipoLogradouro
				endereco.setText(pessoa.get("endereco").isString().stringValue());
				//numero
				bairro.setText(pessoa.get("bairro").isString().stringValue());
				cep.setText(pessoa.get("cep").isString().stringValue());
				cidade.setText(pessoa.get("cidade").isString().stringValue());
				//estado
				residencial.setText(pessoa.get("telefone").isString().stringValue());
				celular.setText(pessoa.get("celular").isString().stringValue());
				email.setText(pessoa.get("email").isString().stringValue());
				nascimento.setEnabled(false);
				radioSexoM.setEnabled(false);
				radioSexoF.setEnabled(false);
				selectEstadoCivil.setEnabled(false);
				rg.setEnabled(false);
				cpf.setEnabled(false);
				tipoLogradouro.setEnabled(false);
				endereco.setEnabled(false);
				numero.setEnabled(false);
				bairro.setEnabled(false);
				cep.setEnabled(false);
				cidade.setEnabled(false);
				estado.setEnabled(false);
				residencial.setEnabled(false);
				celular.setEnabled(false);
				email.setEnabled(false);

			}

			@Override
			public boolean onError(ServiceProxyException e) {
				// TODO Auto-generated method stub
				return false;
			}
		
		});
		
	}

}
