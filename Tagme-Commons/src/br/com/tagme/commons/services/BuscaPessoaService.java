package br.com.tagme.commons.services;

import java.text.SimpleDateFormat;
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

	public static final int MAXOFFSET = 100;

	@Override
	public Element doPost(HttpServletRequest request,
			HttpServletResponse response, Element requestBody,
			Map<String, LinkedList<String>> params) {

//		long inicio = System.currentTimeMillis();
		Element parametros = requestBody.getChild("parametros");
		int page = Integer.parseInt(parametros.getChildText("page")) - 1;

		int offset = Integer.parseInt(parametros.getChildText("offset"));

		if (offset > MAXOFFSET) {
			offset = MAXOFFSET;
		}

		String searchTerm = "%"
				+ (parametros.getChildText("searchTerm") == null ? ""
						: parametros.getChildText("searchTerm")).toUpperCase()
				+ "%";

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

		// para paginacao

//		long antesQuery = System.currentTimeMillis();

		String sql = " SELECT CODPES, "
				+ " NOMECOMPLETO, "
				+ " DTNASC DTNASC, "
				+ " CELULAR CELULAR, "
				+ " (SELECT  COUNT(*) FROM TAGPES WHERE UPPER(NOMECOMPLETO) LIKE ?  )  TOTAL_RESGISTROS "
				+ " FROM TAGPES " + " WHERE UPPER(NOMECOMPLETO) LIKE ?  "
				+ " LIMIT  ? OFFSET ? ";
		List<Map<String, Object>> rows = ConnectionTemplateFactory
				.getTemplate().queryForList(
						sql,
						new Object[] { searchTerm, searchTerm, offset,
								page * offset });

//		long depoisQuery = System.currentTimeMillis();

		int total = 0;

		
		SimpleDateFormat format = new SimpleDateFormat("dd 'de' MMMMM");
		
		        

		
		for (Map<String, Object> row : rows) {
			Element entidade = new Element("entidade");
			Element codpes = new Element("CODPES");
			codpes.addContent(row.get("CODPES").toString());
			Element nomeCompleto = new Element("NOMECOMPLETO");
			nomeCompleto.addContent(row.get("NOMECOMPLETO").toString());
			Element dtNasc = new Element("DTNASC");
			dtNasc.addContent(format.format(row.get("DTNASC")));
			Element celularElem = new Element("CELULAR");
			String celular = row.get("CELULAR").toString();
			String celularEnd =  celular.length()>4?"... "+celular.substring(celular.length()-4):"n.i";
			celularElem.addContent(celularEnd);
			entidade.addContent(codpes);
			entidade.addContent(nomeCompleto);
			entidade.addContent(dtNasc);
			entidade.addContent(celularElem);
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

//		long fim = System.currentTimeMillis();

//		System.out.println("Query " + Long.toString(depoisQuery - antesQuery));
//		System.out.println("Tudo " + Long.toString(fim - inicio));

		return entidades;

	}

}
