package br.com.tagme.auth.helper;

public class AuthStringBuilder {
	
	public static String buildAuthenticated(int status, String username, String name, String authorities){
		
		StringBuffer stringBuffer = new StringBuffer();
		
		stringBuffer.append(status).append(";");
		stringBuffer.append(username).append(";");
		stringBuffer.append(name).append(";");
		stringBuffer.append(authorities);
		
		return stringBuffer.toString();
	}
	
	public static String buildNotAuthenticated(int status, String customMessage){
		StringBuffer stringBuffer = new StringBuffer();
		
		stringBuffer.append(status).append(";");
		stringBuffer.append(customMessage);
		
		return stringBuffer.toString();
	}
	
	public static String buildNotAuthenticated(int status){
		return buildNotAuthenticated(status, "Não Autenticado");
	}

}
