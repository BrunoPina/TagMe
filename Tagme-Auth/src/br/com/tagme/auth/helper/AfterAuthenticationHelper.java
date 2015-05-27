package br.com.tagme.auth.helper;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.tagme.commons.spring.HttpContextSession;

import br.com.tagme.auth.dao.UsuarioDao;
import br.com.tagme.auth.model.Usuario;

@Service
public class AfterAuthenticationHelper {

	@Autowired
	private HttpContextSession contextSession;

	@Autowired
	private UsuarioDao usuarioDao;
	
	private Usuario usuario;
	
	public AfterAuthenticationHelper(){
		
	}
	
	public void initContextTo(String username){
		
		getSKPlaceUserInfo(username);
		//getSKWUserInfo(username);
		
		setLoggedIn();
	}


	
	private void getSKPlaceUserInfo(String username){
		
		usuario = usuarioDao.getUsuarioByUsername(username);
		
		contextSession.setSKPLACE_CODUSU(usuario.getCodUsu());
		contextSession.setSKPLACE_USERNAME(username);
		contextSession.setSKPLACE_NOME(usuario.getNome());
		contextSession.setSKPLACE_LASTLOGIN(usuario.getLastLogin());
	}
	
	private void setLoggedIn(){
		usuarioDao.setLoggedIn(usuario);
	}
	

}
