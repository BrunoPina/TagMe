package br.com.tagme.auth.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;

import br.com.tagme.commons.auth.Role;
import br.com.tagme.commons.types.Resources;
import br.com.tagme.commons.utils.SWServiceHelper;

public class AuthzContatoSkw implements IAuthzProvider {

	private static final String[]			fieldsToFind	= { "ATIVO", "AD_ABREOS", "AD_CHAT", "AD_DOWNLOAD", /*"POSSUIACESSOBT",*/ "RECEBEBOLETOEMAIL", /*"RECEBENOTAEMAIL", "AD_RECAVICOB",*/ "AD_LIBERACNPJ", "AD_ALTERACONTATOS", "AD_ALTERAINFOPARCEIRO" };

	private static final Map<String, Role[]>	mapContatoRoles	= new HashMap<String, Role[]>();

	private static final String ACTION_EXECUTE = "E";
	
	static {
		mapContatoRoles.put("ATIVO", new Role[]{new Role(Resources.SKW, "portalcliente", "ATIVO")});
		mapContatoRoles.put("AD_ABREOS", new Role[]{new Role(Resources.SKW, "inseriros", ACTION_EXECUTE), new Role(Resources.SKW, "consultaos", ACTION_EXECUTE)});
		mapContatoRoles.put("AD_CHAT", new Role[]{new Role(Resources.SKW, "chatonline", ACTION_EXECUTE)});
		mapContatoRoles.put("AD_DOWNLOAD", new Role[]{new Role(Resources.PORCLI, "", ACTION_EXECUTE)});
		//mapContatoRoles.put("POSSUIACESSOBT", new Role[]{new Role(Resources.PORCLI, "", ACTION_EXECUTE)});
		mapContatoRoles.put("RECEBEBOLETOEMAIL", new Role[]{new Role(Resources.SKW, "boleto", ACTION_EXECUTE)});
		//mapContatoRoles.put("RECEBENOTAEMAIL", new Role[]{new Role(Resources.PORCLI, "", ACTION_EXECUTE)});
		//mapContatoRoles.put("AD_RECAVICOB", new Role[]{new Role(Resources.PORCLI, "", ACTION_EXECUTE)});
		mapContatoRoles.put("AD_LIBERACNPJ", new Role[]{new Role(Resources.SKW,"cnpjlic", ACTION_EXECUTE)});
		mapContatoRoles.put("AD_ALTERACONTATOS", new Role[]{new Role(Resources.SKW, "cttaut", ACTION_EXECUTE)});
		mapContatoRoles.put("AD_ALTERAINFOPARCEIRO", new Role[]{new Role(Resources.SKW, "infcad", ACTION_EXECUTE)});
	}

	@Override
	public List<Role> getAuthorities(String username) {

		SWServiceHelper serviceHelper = new SWServiceHelper();
		serviceHelper.setEntity("Contato");
		serviceHelper.setCriterio("EMAIL", username);
		serviceHelper.findFields(fieldsToFind);
		serviceHelper.referenceFetch("Parceiro", new String[] {"ATIVO"});

		List<Role> authoritiesContato = new ArrayList<Role>();

		List<Element> listEntidades = serviceHelper.callCrudFind();
		
		//não busca acessos se tiver email repetido
		if(listEntidades.size() == 1){
		
			Element entidade = listEntidades.get(0).detach();
	
				if("S".equals(entidade.getChildText("ATIVO"))){
					for (String field : fieldsToFind) {
						if ("S".equals(entidade.getChildText(field))) {
							for(Role role : mapContatoRoles.get(field)){
								authoritiesContato.add(role);
							}
						}
					}
				}
		}

		return authoritiesContato;
	}

}
