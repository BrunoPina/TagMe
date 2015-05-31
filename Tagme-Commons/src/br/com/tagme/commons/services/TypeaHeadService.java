package br.com.tagme.commons.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom2.Element;
import org.springframework.stereotype.Service;

import com.amazonaws.util.json.JSONArray;

import br.com.tagme.commons.http.XMLService;
import br.com.tagme.commons.spring.ConnectionTemplateFactory;

@Service("commons@TypeaHeadService")
public class TypeaHeadService extends XMLService {

	public static final int TYPEAHEADLIMIT = 10;

	@Override
	public Element doPost(HttpServletRequest request,
			HttpServletResponse response, Element requestBody,
			Map<String, LinkedList<String>> params) {

		String searchTerm = "%"
				+ (requestBody.getChildText("searchTerm") == null ? ""
						: requestBody.getChildText("searchTerm")).toUpperCase()
				+ "%";

		Element nomes = new Element("nomes");

		String sql = " SELECT " + " NOMECOMPLETO " + " FROM TAGPES "
				+ " WHERE UPPER(NOMECOMPLETO) LIKE ?  " + " LIMIT  ? ";

		List<Map<String, Object>> rows = ConnectionTemplateFactory
				.getTemplate().queryForList(sql,
						new Object[] { searchTerm, TYPEAHEADLIMIT });

		int total = 0;

		JSONArray nomesJS = new JSONArray();

		for (Map<String, Object> row : rows) {
			nomesJS.put(row.get("NOMECOMPLETO").toString());
			total++;
		}

		nomes.setText(nomesJS.toString());
		nomes.setAttribute("total", Integer.toString(total));

		return nomes;

	}

}
