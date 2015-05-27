package br.com.tagme.commons.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom2.Element;
import org.springframework.stereotype.Service;

import br.com.tagme.commons.http.XMLService;
import br.com.tagme.commons.spring.ConnectionTemplateFactory;

@Service("commons@BuscaPessoaService")
public class BuscaPessoaService extends XMLService {

	@Override
	public Element doPost(HttpServletRequest request,
			HttpServletResponse response, Element requestBody,
			Map<String, LinkedList<String>> params) {

		Element parametros = requestBody.getChild("parametros");
		int page = Integer.parseInt(parametros.getChildText("page")) - 1;

		int offset = 15;

		String searchTerm = "%"
				+ parametros.getChildText("searchTerm").toUpperCase() + "%";

		Element entidades = new Element("entidades");
		entidades.setAttribute("searchTerm", searchTerm);
		entidades.setAttribute("currentPage", parametros.getChildText("page"));

		Element metadata = new Element("metadata");

		Element fieldCodPes = new Element("field");
		fieldCodPes.setAttribute("fieldName", "CODPES");
		fieldCodPes.setAttribute("label", "Código");
		fieldCodPes.setAttribute("visible", "false");

		Element fieldNomeCompleto = new Element("field");
		fieldNomeCompleto.setAttribute("fieldName", "NOMECOMPLETO");
		fieldNomeCompleto.setAttribute("label", "Nome Completo");
		fieldNomeCompleto.setAttribute("visible", "true");

		Element fieldDtnasc = new Element("field");
		fieldDtnasc.setAttribute("fieldName", "DTNASC");
		fieldDtnasc.setAttribute("label", "Aniversário");
		fieldDtnasc.setAttribute("visible", "true");

		metadata.addContent(fieldCodPes);
		metadata.addContent(fieldNomeCompleto);
		metadata.addContent(fieldDtnasc);
		entidades.addContent(metadata);

		String sql = " SELECT CODPES, "
				+ " NOMECOMPLETO, "
				+ " DATE_FORMAT(DTNASC, '%d/%m') DTNASC, "
				+ " (SELECT  COUNT(*) FROM TAGPES WHERE UPPER(NOMECOMPLETO) LIKE ?  )  TOTAL_RESGISTROS "
				+ " FROM TAGPES " + " WHERE UPPER(NOMECOMPLETO) LIKE ?  "
				+ " LIMIT  ? OFFSET ? ";

		List<Map<String, Object>> rows = ConnectionTemplateFactory
				.getTemplate().queryForList(
						sql,
						new Object[] { searchTerm, searchTerm, offset,
								page * offset });

		int total = 0;

		for (Map<String, Object> row : rows) {
			Element entidade = new Element("entidade");
			Element codpes = new Element("CODPES");
			codpes.addContent(row.get("CODPES").toString());
			Element nomeCompleto = new Element("NOMECOMPLETO");
			nomeCompleto.addContent(row.get("NOMECOMPLETO").toString());
			Element dtNasc = new Element("DTNASC");
			dtNasc.addContent(row.get("DTNASC").toString());
			entidade.addContent(codpes);
			entidade.addContent(nomeCompleto);
			entidade.addContent(dtNasc);
			entidades.addContent(entidade);
			if (total == 0) {
				Long totalGeral = (Long) row.get("TOTAL_RESGISTROS");
				entidades.setAttribute("totalResults", totalGeral.toString());
				entidades.setAttribute("totalPages",
						Long.toString(((totalGeral / offset) + 1)));
			}
			total++;
		}

		entidades.setAttribute("total", Integer.toString(total));
		entidades.setAttribute("total", Integer.toString(total));

		return entidades;

	}

}
