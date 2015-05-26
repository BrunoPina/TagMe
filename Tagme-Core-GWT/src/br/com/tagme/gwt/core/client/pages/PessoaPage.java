package br.com.tagme.gwt.core.client.pages;

import br.com.sankhya.place.gwt.mvp.client.CompositeWithPresenter;
import br.com.sankhya.place.gwt.mvp.client.DefaultActivity;
import br.com.tagme.gwt.core.client.CoreEntryPoint;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class PessoaPage extends CompositeWithPresenter {

	private static IndexPageUiBinder	uiBinder	= GWT.create(IndexPageUiBinder.class);

	interface IndexPageUiBinder extends UiBinder<Widget, PessoaPage> {
	}

	public PessoaPage(DefaultActivity presenter) {
		super(presenter);
		
		initWidget(uiBinder.createAndBindUi(this));
		
		CoreEntryPoint.addBottomNavbar(null);
	}
	
	/*private void addNotEmptyValidator(final TextBoxBase field){
		field.addValidator(new Validator<String>() {

			@Override
			public int getPriority() {
				return Priority.MEDIUM;
			}

			@Override
			public List<EditorError> validate(Editor<String> editor, String value) {
				List<EditorError> result = new ArrayList<EditorError>();
				if(StringUtils.isEmpty(value)){
					result.add(new BasicEditorError(field, value, "Campo requerido"));
				}
				return result;
			}
		});
	}*/
}
