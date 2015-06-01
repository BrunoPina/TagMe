package br.com.tagme.commons.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

import com.amazonaws.util.json.JSONObject;

import br.com.tagme.commons.utils.FieldMetadata;


public class Pessoa  {

	private String	codPes;
	private String nomeCompleto;
	private String profissao; 
	private Timestamp dtNasc; 
	private String cpf;
	private String endereco; 
	private String bairro; 
	private String cep; 
	private String cidade; 
	private String estado;
	private String nacionalidade; 
	private String telefone; 
	private String celular;
	private String sexo;
	private String fumante; 
	private String email;

	
	private static List<FieldMetadata> FIELDS_METADATA = new ArrayList<FieldMetadata>();
	
	static{
		FIELDS_METADATA.add(new FieldMetadata("CODPES", "Cód. Pessoa"));
		FIELDS_METADATA.add(new FieldMetadata("NOMECOMPLETO", "Nome Completo"));
		FIELDS_METADATA.add(new FieldMetadata("EMAIL", "Email"));
		FIELDS_METADATA.add(new FieldMetadata("PROFISSAO", "Profissão"));
		FIELDS_METADATA.add(new FieldMetadata("DTNASC", "Data de Nascimento"));
		FIELDS_METADATA.add(new FieldMetadata("CPF", "CPF"));
		FIELDS_METADATA.add(new FieldMetadata("ENDERECO", "Endereço"));
		FIELDS_METADATA.add(new FieldMetadata("BAIRRO", "Bairro"));
		FIELDS_METADATA.add(new FieldMetadata("CEP", "CEP"));
		FIELDS_METADATA.add(new FieldMetadata("CIDADE", "Cidade"));
		FIELDS_METADATA.add(new FieldMetadata("ESTADO", "Estado"));
		FIELDS_METADATA.add(new FieldMetadata("NACIONALIDADE", "Nacionalidade"));
		FIELDS_METADATA.add(new FieldMetadata("TELEFONE", "Telefone"));
		FIELDS_METADATA.add(new FieldMetadata("CELULAR", "Celular"));
		FIELDS_METADATA.add(new FieldMetadata("SEXO", "Sexo"));
		FIELDS_METADATA.add(new FieldMetadata("FUMANTE", "Fumante"));
	}
	
	
	public String getCodPes() {
		return codPes;
	}
	public void setCodPes(String codPes) {
		this.codPes = codPes;
		
	}
	public String getNomeCompleto() {
		return nomeCompleto;
	}
	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}
	public String getProfissao() {
		return profissao;
	}
	public void setProfissao(String profissao) {
		this.profissao = profissao;
	}
	public String getDtNasc() {
		return dtNasc.toString();
	}
	public void setDtNasc(Timestamp dtNasc) {
		this.dtNasc = dtNasc;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getNacionalidade() {
		return nacionalidade;
	}
	public void setNacionalidade(String nacionalidade) {
		this.nacionalidade = nacionalidade;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public String getCelular() {
		return celular;
	}
	public void setCelular(String celular) {
		this.celular = celular;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public String getFumante() {
		return fumante;
	}
	public void setFumante(String fumante) {
		this.fumante = fumante;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	public static List<FieldMetadata> presentationsFields(){
		return FIELDS_METADATA;
	}
	
	public static Element metadataElement(){
		Element metadata = new Element("metadata");
		
		for( FieldMetadata field : presentationsFields() ){
			metadata.addContent(field.getElement());
		}
		
		return metadata;
	} 
	
	

}
