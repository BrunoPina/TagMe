package br.com.tagme.auth.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Usuario {

	private String		codUsu;
	private String		nome;
	private String		email;
	private String		senha;
	private byte[]		foto;
	private Timestamp	dhalter_perfil;
	private Timestamp	dhalter_foto;
	private Timestamp	last_login;
	private boolean		isAtivo;
	private String 		chave;
	private Timestamp	dhativacao;
	private BigDecimal  codPes; 		

	public String getCodUsu() {
		return codUsu;
	}

	public void setCodUsu(String codUsu) {
		this.codUsu = codUsu;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public Timestamp getDhalterPerfil() {
		return dhalter_perfil;
	}

	public void setDhalterPerfil(Timestamp dhalter_perfil) {
		this.dhalter_perfil = dhalter_perfil;
	}

	public Timestamp getDhalterFoto() {
		return dhalter_foto;
	}

	public void setDhalterFoto(Timestamp dhalter_foto) {
		this.dhalter_foto = dhalter_foto;
	}

	public Timestamp getLastLogin() {
		return last_login;
	}

	public void setLastLogin(Timestamp last_login) {
		this.last_login = last_login;
	}

	public boolean isAtivo() {
		return isAtivo;
	}

	public void setAtivo(boolean isAtivo) {
		this.isAtivo = isAtivo;
	}
	
	public String getChave(){
		return this.chave;
	}
	
	public void setChave(String chave){
		this.chave = chave;
	}
	
	public Timestamp getDhAtivacao(){
		return this.dhativacao;
	}
	
	public void setDhAtivacao(Timestamp dhativacao){
		this.dhativacao = dhativacao;
	}
	
	public BigDecimal getCodPes(){
		return this.codPes;
	}
	
	public void setCodPes(BigDecimal codPes){
		this.codPes = codPes;
	}
	
	
}
