package br.com.tagme.gwt.core.client.widgets;

import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.Lead;
import org.gwtbootstrap3.client.ui.ThumbnailLink;

import br.com.sankhya.place.gwt.commons.utils.client.ColorUtils;
import br.com.sankhya.place.gwt.commons.utils.client.StringUtils;
import br.com.tagme.gwt.core.client.pages.ListaOSPage.Person;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class SearchItemOsWidget extends Composite {

	private static SearchItemOsWidgetUiBinder uiBinder = GWT.create(SearchItemOsWidgetUiBinder.class);

	interface SearchItemOsWidgetUiBinder extends UiBinder<Widget, SearchItemOsWidget> {
	}

	@UiField Lead linkTitulo;
	@UiField HTML strSnipplets;
	@UiField Anchor niverAnchor;
	@UiField Anchor telEndAnchor;
	@UiField ThumbnailLink thumbnailOs;
	

	
	public SearchItemOsWidget(Person person) {
		initWidget(uiBinder.createAndBindUi(this));

		String title = person.toString();
		
		String titleArray[] = title.split(" ");
		String firstWord = getWord(titleArray, true);
		String lastWord = getWord(titleArray, false);
		String iniciais = firstWord.substring(0, firstWord.length() > 1 ? 2 : 1);
		if(!firstWord.equals(lastWord)){
			iniciais = firstWord.substring(0, 1)+lastWord.substring(0, 1);
			if("CU".equals(iniciais.toUpperCase())){
				iniciais = firstWord.substring(0, firstWord.length() > 1 ? 2 : 1);
			}
		}
		
		linkTitulo.setText(iniciais.toUpperCase());
		strSnipplets.setHTML(title);
		
		title = StringUtils.removeSpecialChars(title);
		title = title.toUpperCase();
		title.replaceAll("[^A-Z]", "");
		String color = ColorUtils.textToColor(title);
		linkTitulo.getElement().getStyle().setColor(color);
		
		
		niverAnchor.setText(person.getBrithDay());
		telEndAnchor.setText(person.getTelefoneEnd());
		//TODO:thumbnailOs.setHref(PlaceController.buildHref(new OrdemServicoPlace(numOs+"?item="+numItem+"&pesquisa=S")));
	}
	
	private String getWord(String [] words, boolean isFirst){
		int i = 0;
		if(isFirst){
			while(isRestricted(words[i])){
				i++;
			}
		}else{
			i = words.length-1;
			while(isRestricted(words[i])){
				i--;
			}
		}
		return words[i];
	}
	
	private boolean isRestricted(String word){
		if(word == null || word.isEmpty()){
			return true;
		}
		word = word.toUpperCase();
		
		boolean isNum = false;
		try{
			Integer.valueOf(word);
			isNum = true;
		}catch(Exception ex){
			isNum = false;
		}
		
		if(isNum){
			return true;
		}else if(word.length() < 4){
			return true;
		}else if("-".equals(word) || "/".equals(word) || "*".equals(word) || "ORACLE".equals(word) || "MSSQL".equals(word) || "VIII".equals(word) || "XIII".equals(word) || "XVII".equals(word) || "XVIII".equals(word)){
			return true;
		}
		
		return false;
	}
}
