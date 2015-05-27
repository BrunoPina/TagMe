package br.com.tagme.auth.authentication;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import br.com.tagme.commons.auth.Role;
import br.com.tagme.commons.auth.SankhyaPlaceUser;
import br.com.tagme.commons.types.Resources;

@Service
public class SankhyaPlaceAdditionalChecks {
	
	public void skwAdditionalChecks(SankhyaPlaceUser user) throws AuthenticationException {
		
		if(!user.hasAuthority(new Role(Resources.SKW, "portalcliente", "ATIVO"))){
			user.setAuthenticated(false);
			throw new DisabledException("Contato não está ativo no Sankhya-W.");
		}
		
	}
	
}