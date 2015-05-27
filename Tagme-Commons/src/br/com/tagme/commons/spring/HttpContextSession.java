package br.com.tagme.commons.spring;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("HttpContextSession")
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class HttpContextSession implements Serializable{

	private Map<String, Object>	attributes	= new HashMap<String, Object>();
	private long				initTime;
	private long				timeOut;
	private String				currentUsername;
	private Authentication		currentUser;
	
	private String SKPLACE_CODUSU;
	private String SKPLACE_USERNAME;
	private String SKPLACE_NOME;
	private Timestamp SKPLACE_LASTLOGIN;
	
	private String SKW_CODPARC;
	private String SKW_NOMEPARC;
	private String SKW_CODPARCMATRIZ;
	private String SKW_CODCONTATO;
	
	private String SKW_DTATUAL;

	public String getCurrentUsername() {
		return currentUsername;
	}
	
	public void setCurrentUser(Authentication currentUser){
		this.currentUser = currentUser;
		this.currentUsername = currentUser.getName();
	}
	
	public Authentication getCurrentUser(){
		return this.currentUser;
	}
	
	public boolean isCurrentUserAuthenticated(){
		return currentUser.isAuthenticated();
	}

	public String getSKPLACE_CODUSU() {
		return SKPLACE_CODUSU;
	}

	public void setSKPLACE_CODUSU(String sKPLACE_CODUSU) {
		SKPLACE_CODUSU = sKPLACE_CODUSU;
	}

	public String getSKPLACE_USERNAME() {
		return SKPLACE_USERNAME;
	}

	public void setSKPLACE_USERNAME(String sKPLACE_USERNAME) {
		SKPLACE_USERNAME = sKPLACE_USERNAME;
	}

	public String getSKPLACE_NOME() {
		return SKPLACE_NOME;
	}

	public void setSKPLACE_NOME(String sKPLACE_NOME) {
		SKPLACE_NOME = sKPLACE_NOME;
	}

	public Timestamp getSKPLACE_LASTLOGIN() {
		return SKPLACE_LASTLOGIN;
	}

	public void setSKPLACE_LASTLOGIN(Timestamp sKPLACE_LASTLOGIN) {
		SKPLACE_LASTLOGIN = sKPLACE_LASTLOGIN;
	}

	public String getSKW_CODPARC() {
		return SKW_CODPARC;
	}

	public void setSKW_CODPARC(String sKW_CODPARC) {
		SKW_CODPARC = sKW_CODPARC;
	}
	
	public String getSKW_NOMEPARC() {
		return SKW_NOMEPARC;
	}

	public void setSKW_NOMEPARC(String sKW_NOMEPARC) {
		SKW_NOMEPARC = sKW_NOMEPARC;
	}

	public String getSKW_CODPARCMATRIZ() {
		return SKW_CODPARCMATRIZ;
	}

	public void setSKW_CODPARCMATRIZ(String sKW_CODPARCMATRIZ) {
		SKW_CODPARCMATRIZ = sKW_CODPARCMATRIZ;
	}

	public String getSKW_CODCONTATO() {
		return SKW_CODCONTATO;
	}

	public void setSKW_CODCONTATO(String sKW_CODCONTATO) {
		SKW_CODCONTATO = sKW_CODCONTATO;
	}
	
	public String getSKW_DTATUAL() {
		return SKW_DTATUAL;
	}

	public void setSKW_DTATUAL(String sKW_DTATUAL) {
		SKW_DTATUAL = sKW_DTATUAL;
	}
	
	/////////////
	
	public HttpContextSession() {
		this.initTime = getNowInSeconds();
		this.timeOut = 600; // 10 minutos (default)
	}

	public void setAttribute(String key, Object value) {
		this.attributes.put(key, value);
	}

	public Object getAttribute(String key) {
		return this.attributes.get(key);
	}
	
	public void clearAttributes(){
		this.attributes.clear();
	}

	/**
	 * 
	 * @param timeOut
	 *            {@literal - Time Out em segundos}
	 */
	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}

	public void updateInitTime() {
		this.initTime = getNowInSeconds();
	}

	public boolean hasTimedOut() {
		if ((getNowInSeconds() - this.initTime) > this.timeOut)
			return true;
		return false;
	}

	private long getNowInSeconds() {
		return (System.currentTimeMillis() / 1000);
	}

}
