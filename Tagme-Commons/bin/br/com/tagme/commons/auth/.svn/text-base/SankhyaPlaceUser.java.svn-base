package br.com.tagme.commons.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class SankhyaPlaceUser implements Authentication {
	
	private User user;
	private boolean isAuthenticated;	
		
	public SankhyaPlaceUser(User user){
		this.user = user;
	}
	
	@Override
	public String getName() {
		return user.getUsername();
	}

	@Override
	public Object getCredentials() {
		return user.getPassword();
	}

	@Override
	public Object getDetails() {
		return user;
	}

	@Override
	public Object getPrincipal() {
		return user;
	}

	@Override
	public boolean isAuthenticated() {
		return isAuthenticated && user.isAccountNonExpired() && user.isAccountNonLocked() && user.isCredentialsNonExpired() && user.isEnabled();
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		this.isAuthenticated = isAuthenticated;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return user.getAuthorities();
	}
	
	public boolean hasAuthority(String authorityToVerify) {
		if(!isAuthenticated()){
			return false;
		}

		if(user.getAuthorities() != null){
			for (GrantedAuthority authority : user.getAuthorities()) {
				Role role = null;
				
				if(authority instanceof Role){
					role = (Role) authority;
					if (role.implies(new Role(authorityToVerify))) {
						return true;
					}
				}
			}
		}

		return false;
	}
	
	public boolean hasAuthority(Role authorityToVerify) {
		return hasAuthority(authorityToVerify.toString());
	}
	
	public boolean hasAllAuthority(ArrayList<Role> authoritiesToVerify){
		for(Role authorityToVerivy : authoritiesToVerify){
			boolean authorized = hasAuthority(authorityToVerivy);
			if(!authorized){
				return false;
			}
		}
		return true;
	}
	
	public String buildAuthoritiesString(){
		StringBuffer authoritiesString = new StringBuffer();
		
		for(Iterator<?> ite = getAuthorities().iterator(); ite.hasNext();){
			GrantedAuthority role = (GrantedAuthority) ite.next();
			
			authoritiesString.append(role.toString());
			if(ite.hasNext()){
				authoritiesString.append(";");
			}
		}
		return authoritiesString.toString();
	}

}
