package br.com.tagme.gwt.core.client.pages;

import java.util.ArrayList;
import java.util.List;

import org.gwtbootstrap3.extras.typeahead.client.base.Dataset;
import org.gwtbootstrap3.extras.typeahead.client.base.Suggestion;
import org.gwtbootstrap3.extras.typeahead.client.base.SuggestionCallback;
import org.gwtbootstrap3.extras.typeahead.client.ui.Typeahead;

import br.com.sankhya.place.gwt.mvp.client.CompositeWithPresenter;
import br.com.sankhya.place.gwt.mvp.client.DefaultActivity;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class IndexPage extends CompositeWithPresenter {

	private static IndexPageUiBinder	uiBinder	= GWT.create(IndexPageUiBinder.class);

	interface IndexPageUiBinder extends UiBinder<Widget, IndexPage> {
	}

	@UiField(provided = true) 
	Typeahead<Person> asyncTypeahead;
	
	private final String placeholder = "Enter a name";
	
	private final List<Person> persons = new ArrayList<Person>();
	
	public IndexPage(DefaultActivity presenter, String contextApp) {
		super(presenter);

		persons.add(new Person("Bill Happernan", 50));
		persons.add(new Person("Bob Wondersteen", 38));
		persons.add(new Person("Bobak Longfield", 24));
		persons.add(new Person("Dawton Kernigham", 27));
		persons.add(new Person("Eumon Wonderburgh", 39));
		persons.add(new Person("Frank Dinkelstein", 66));
		persons.add(new Person("Gene Razelhaze", 42));
		persons.add(new Person("Gus Peppermint", 31));
		persons.add(new Person("Jebediah Franklin", 57));
		persons.add(new Person("Kirrim Proplov", 73));
		persons.add(new Person("Linus Weenkol", 103));
		persons.add(new Person("Mortimer Jackendale", 24));
		persons.add(new Person("Walt Crinistool", 43));
		persons.add(new Person("Wernher Agusto", 52));
		persons.add(new Person("Julia Sarah Sandy Margret Johanna Cathrin Jenkins", 34));
		
		createAsyncTypeahead();
		
		initWidget(uiBinder.createAndBindUi(this));
	}

	private void createAsyncTypeahead() {
		asyncTypeahead = new Typeahead<Person>(new Dataset<Person>() {
			
			@Override
			public void findMatches(String query, SuggestionCallback<Person> callback) {
				List<Suggestion<Person>> suggestions = new ArrayList<Suggestion<Person>>();

				String queryLower = query.toLowerCase();
				for (Person person : persons) {
					if (person.name.toLowerCase().contains(queryLower)) {
						Suggestion<Person> s = Suggestion.create(person.name, person, this);
						suggestions.add(s);
					}
				}

				callback.execute(suggestions);
			}

		});
		asyncTypeahead.setPlaceholder(placeholder);
	}

	private class Person {
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
