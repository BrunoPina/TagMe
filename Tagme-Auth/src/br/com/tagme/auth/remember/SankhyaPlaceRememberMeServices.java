package br.com.tagme.auth.remember;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Component;

import br.com.tagme.auth.authentication.SankhyaPlaceUserDetailService;

@Component("sankhyaPlaceRememberMeServices")
public class SankhyaPlaceRememberMeServices extends PersistentTokenBasedRememberMeServices {

	protected static final String	key				= "7c472fd5c656bd5381dea552901ab651";
	public static final String		SKPLACE_COOKIE	= "SKPLACE_REMEMBER_ME";

	@Autowired
	public SankhyaPlaceRememberMeServices(SankhyaPlaceTokenRepository tokenRepository, SankhyaPlaceUserDetailService userDetailService) {
		super(key, userDetailService, tokenRepository);
		setCookieName(SKPLACE_COOKIE);
		setAlwaysRemember(false);
		setTokenValiditySeconds(1209600); // 2 semanas
	}

}
