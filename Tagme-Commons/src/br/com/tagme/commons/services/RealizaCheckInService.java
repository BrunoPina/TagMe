package br.com.tagme.commons.services;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom2.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.tagme.commons.dao.CheckInDao;
import br.com.tagme.commons.http.XMLService;
import br.com.tagme.commons.model.CheckIn;
import br.com.tagme.commons.model.Pessoa;

@Service("commons@RealizaCheckInService")
public class RealizaCheckInService extends XMLService {

	@Autowired
	private CheckInDao			checkInDao;

	
	@Override
	public Element doPost(HttpServletRequest request,
			HttpServletResponse response, Element requestBody,
			Map<String, LinkedList<String>> params) {

		Element parametros = requestBody.getChild("parametros");
		
		String codPes =  parametros.getChildText("codPes") ;
		String codIns =  parametros.getChildText("codIns") ;
		String voucher =  parametros.getChildText("voucher") ;
		String dhReserva =  parametros.getChildText("dhReserva") ;
		
		final String FORMATO_DATA = "dd/MM/yyyy hh:mm";
		
		DateFormat formatter = new SimpleDateFormat(FORMATO_DATA);
		Date dhReservaDate = null;
		try {
			dhReservaDate = formatter.parse(dhReserva);
		} catch (ParseException e) {
			RuntimeException ex = new RuntimeException("Formato de data inválido "+ dhReserva);
			ex.initCause(e);
			throw ex;
		};
		
		String observacao =  parametros.getChildText("observacao") ;
		
		CheckIn checkIn = new CheckIn();
		
		checkIn.setCodPes(codPes);
		checkIn.setCodIns(codIns);
		checkIn.setVoucher(voucher);
		checkIn.setDhReserva(new Timestamp(dhReservaDate.getTime()));
		checkIn.setObservacao(observacao);
		
		String codCin = Long.toString(checkInDao.insertCheckIn(checkIn));
		
		Element checkElement = new Element("CheckIn");
		checkElement.setAttribute("codCin",codCin);
		return checkElement;

	}

}
