package br.com.tagme.commons.model;

import java.sql.Timestamp;


public class CheckIn {

	private String codCin;
	private String codPes;
	private String codIns;
	private String voucher;
	private Timestamp dhReserva;
	private String observacao;
	public String getCodCin() {
		return codCin;
	}
	public void setCodCin(String codCin) {
		this.codCin = codCin;
	}
	public String getCodPes() {
		return codPes;
	}
	public void setCodPes(String codPes) {
		this.codPes = codPes;
	}
	public String getCodIns() {
		return codIns;
	}
	public void setCodIns(String codIns) {
		this.codIns = codIns;
	}
	public String getVoucher() {
		return voucher;
	}
	public void setVoucher(String voucher) {
		this.voucher = voucher;
	}
	public Timestamp getDhReserva() {
		return dhReserva;
	}
	public void setDhReserva(Timestamp dhReserva) {
		this.dhReserva = dhReserva;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
}
