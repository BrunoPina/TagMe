package br.com.tagme.commons.auth;

import org.springframework.security.core.GrantedAuthority;

import br.com.tagme.commons.types.Resources;

public class Role implements GrantedAuthority{

	private Resources resource = Resources.ALL;
	private String instance = WILDCARD_TOKEN;
	private String action = WILDCARD_TOKEN;

	private static final String WILDCARD_TOKEN = "*";
	private static final String PART_DIVIDER_TOKEN = ":";
	private static final String SUBPART_DIVIDER_TOKEN = ",";
	
	public Role(){}
	
	public Role(String role){
		String[] parts = role.split(PART_DIVIDER_TOKEN);
		if(parts.length == 1){
			setResource(Resources.valueOf(parts[0]));
		}else if(parts.length == 2){
			setResource(Resources.valueOf(parts[0]));
			setInstance(parts[1]);
		}else if(parts.length > 2){
			setResource(Resources.valueOf(parts[0]));
			setInstance(parts[1]);
			setAction(parts[2]);
		}
	}
	
	public Role(Resources resource, String instance, String action) {
		setResource(resource);
		setInstance(instance);
		setAction(action);
	}
	
	public Resources getResource(){
		return this.resource;
	}
	
	public void setResource(Resources resource) {
		if(resource != null){
			this.resource = resource;
		}
	}

	public String getInstance(){
		return this.instance;
	}
	
	public void setInstance(String instance) {
		if(instance != null){
			this.instance = instance;
		}
	}

	public String getAction(){
		return this.action;
	}
	
	public void setAction(String action) {
		if(!"".equals(action) && action != null){
			this.action = action;
		}
	}

	@Override
	public String toString() {
		return new StringBuffer(resource.name()).append(PART_DIVIDER_TOKEN).append(instance).append(PART_DIVIDER_TOKEN).append(action).toString();
	}
	
	public boolean implies(Role impliedRole){
		if((Resources.ALL.equals(resource) || impliedRole.getResource().equals(resource))){
			if((WILDCARD_TOKEN.equals(instance) || impliedRole.getInstance().equals(instance))){
				
				if(action.contains(WILDCARD_TOKEN)){
					return true;
				}else{
					String[] actionsGrantedRole = action.split(SUBPART_DIVIDER_TOKEN);
					String[] actionsImpliedRole = impliedRole.getAction().split(SUBPART_DIVIDER_TOKEN);
					
					for(int i=0;i<actionsImpliedRole.length;i++){
						boolean isActionGranted = false;
						
						for(int j=0;j<actionsGrantedRole.length;j++){
							if(actionsImpliedRole[i].equals(actionsGrantedRole[j])){
								isActionGranted = true;
								break;
							}
						}
						
						if(!isActionGranted){
							return false;
						}
					}
					
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public String getAuthority() {
		return this.toString();
	}

}

