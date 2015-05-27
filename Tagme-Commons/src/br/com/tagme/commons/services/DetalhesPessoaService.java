package br.com.tagme.commons.services;

import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom2.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.tagme.commons.dao.PessoaDao;
import br.com.tagme.commons.http.XMLService;
import br.com.tagme.commons.model.Pessoa;

@Service("commons@DetalhesPessoaService")
public class DetalhesPessoaService extends XMLService {

	@Autowired
	private PessoaDao			pessoaDao;

	
	@Override
	public Element doPost(HttpServletRequest request,
			HttpServletResponse response, Element requestBody,
			Map<String, LinkedList<String>> params) {

		Element parametros = requestBody.getChild("parametros");
		
		String codPes =  parametros.getChildText("codPes") ;

		Element entidades = new Element("entidades");

		Element metadata = br.com.tagme.commons.model.Pessoa.getMetadataElement();
		entidades.addContent(metadata);
		
		Pessoa pesssoa =  pessoaDao.getPessoaById(codPes);
		
		entidades.addContent(pesssoa.getElement());

		return entidades;

	}

}
