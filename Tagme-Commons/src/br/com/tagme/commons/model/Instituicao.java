package br.com.tagme.commons.model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

import br.com.tagme.commons.utils.FieldMetadata;

public class Instituicao {

	private String codIns;
	private String nome;
	private String nomeAbrev;
	private String razaoSocial;
	private String cnpj;
	private InputStream layoutCheckIn;

	private static List<FieldMetadata> FIELDS_METADATA = new ArrayList<FieldMetadata>();

	static {
		FIELDS_METADATA.add(new FieldMetadata("CODINS", "Cód. Instituição"));
		FIELDS_METADATA.add(new FieldMetadata("NOME", "Nome"));
		FIELDS_METADATA.add(new FieldMetadata("NOMEABREV", "Nome Abreviado"));
		FIELDS_METADATA.add(new FieldMetadata("RAZAOSOCIAL", "Razão Social"));
		FIELDS_METADATA.add(new FieldMetadata("CNPJ", "CNPJ"));
		FIELDS_METADATA.add(new FieldMetadata("LAYOUTCHECKIN", "Layout Check In"));
	}

	public Element getElement() {
		Element entidade = new Element("entidade");
		entidade.addContent(new Element("CODINS").addContent(getCodIns()));
		entidade.addContent(new Element("NOME").addContent(getNome()));
		entidade.addContent(new Element("NOMEABREV").addContent(getNomeAbrev()));
		entidade.addContent(new Element("RAZAOSOCIAL")
				.addContent(getRazaoSocial()));
		entidade.addContent(new Element("CNPJ").addContent(getCnpj()));
		return entidade;
	}

	public static List<FieldMetadata> getPresentationsFields() {
		return FIELDS_METADATA;
	}

	public static Element getMetadataElement() {
		Element metadata = new Element("metadata");

		for (FieldMetadata field : getPresentationsFields()) {
			metadata.addContent(field.getElement());
		}

		return metadata;
	}

	public String getCodIns() {
		return codIns;
	}

	public void setCodIns(String codIns) {
		this.codIns = codIns;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomeAbrev() {
		return nomeAbrev;
	}

	public void setNomeAbrev(String nomeAbrev) {
		this.nomeAbrev = nomeAbrev;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public InputStream getLayoutCheckIn() {
		return layoutCheckIn;
	}

	public void setLayoutCheckIn(InputStream layoutCheckIn) {
		this.layoutCheckIn = layoutCheckIn;
	}

}
