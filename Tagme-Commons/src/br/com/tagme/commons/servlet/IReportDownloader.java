package br.com.tagme.commons.servlet;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.tagme.commons.dao.InstituicaoDao;
import br.com.tagme.commons.spring.ConnectionTemplateFactory;


@Controller
public class IReportDownloader {
	
	@Autowired
	private InstituicaoDao			instituicaoDao;
	
	@RequestMapping(value = "/imprimirCheckIn/{codPes}/{codIns}.{extension}")
	public @ResponseBody String imprimirCheckIn(
			HttpServletRequest request, 
			HttpServletResponse response,
			@PathVariable("codPes") String codPes,
			@PathVariable("codIns") String codIns,
			@PathVariable("extension") String extension 
		) throws Exception{
		
		
//		<a href="http://www.yahoo.com" target="_blank">Go to Yahoo</a> 
	    ServletOutputStream output = response.getOutputStream();
	    
		JasperReport report = JasperCompileManager.compileReport(instituicaoDao.getInstituicaoById(codIns).getLayoutCheckIn());
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("codPes", codPes);
		parameters.put("codIns", codIns);
		
		JasperPrint print = JasperFillManager.fillReport(report, parameters, ConnectionTemplateFactory.getTemplate().getDataSource().getConnection());

	    output.write(JasperExportManager.exportReportToPdf(print));
	    output.close();
		
		return null;
	}
}
