package br.com.tagme.auth.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import br.com.tagme.auth.db.ConnectionTemplate;
import br.com.tagme.auth.db.TransactionalAuth;
import br.com.tagme.commons.auth.Role;
import br.com.tagme.commons.types.Resources;
import br.com.tagme.commons.utils.StringUtils;
import br.com.tagme.commons.utils.TimeUtils;

/**
 * 
 * @author Gildo Neto
 * 
 */

@Repository
@TransactionalAuth
public class AuthcDao implements IAuthcDao {

	public AuthcDao() {
	}

	@Override
	@TransactionalAuth
	public User getUserByUsername(String username) {

		if(StringUtils.isNotEmpty(username)){
			
			username = username.toLowerCase();
		
			String sql = "SELECT T1.usuario as username, T1.senha as password, T1.ativo as ativo, T1.dhativacao, T2.resource as resource, T2.instance as instance, T2.action as action FROM " 
					+ "(select cod_sujeito, usuario, senha, ativo, dhativacao from sec_sujeito where usuario = ?) T1 "
					+ "LEFT JOIN "
					+ "(select resource, instance, action, (select cod_sujeito from sec_sujeito where usuario = ?) as codusu from sec_perm where cod_sujeito = (select cod_sujeito from sec_sujeito where usuario = ?) or cod_grupo in (select distinct(cod_grupo) from sec_lig_suj_grupo where cod_sujeito = (select cod_sujeito from sec_sujeito where usuario = ?))) T2 "
					+ "ON T1.cod_sujeito = T2.codusu;";
			
			List<Map<String, Object>> rows = ConnectionTemplate.getTemplate().queryForList(sql, new Object[] { username, username, username, username });
			
			List<Role> authorities = new ArrayList<Role>();
					
			String usuario = "";
			String senha = "";
			boolean isAtivo = true;
			String resource;
			String instance;
			String action;
			Timestamp dhativacao = null;
			
			if(rows.size() > 0){
			
				for(Map<String, Object> row : rows){
					usuario = (String) row.get("username");
					senha = (String) row.get("password");
					
					dhativacao = TimeUtils.getTimestamp(row.get("dhativacao"));

					resource = ("*").equals((String) row.get("resource")) ? "ALL" : ((String) row.get("resource"));
					instance = (String) row.get("instance");
					action = (String) row.get("action");
					
					if(StringUtils.isNotEmpty(resource) && StringUtils.isNotEmpty(instance) && StringUtils.isNotEmpty(action)){
						Role role = new Role(Resources.valueOf(resource), instance, action);
						authorities.add(role);
					}
				}
				
				if(!isAtivo){
					if(dhativacao == null){
						throw new UsernameNotFoundException("Valida��o de conta pendente. Verifique seu e-mail.");
					}else{					
						throw new UsernameNotFoundException("Conta n�o est� ativa.");
					}
				}
				
				User user = new User(usuario, senha, authorities);
				return user;
			}else{
				throw new UsernameNotFoundException("Usu�rio n�o encontrado.");
			}
		
		}else{
			throw new UsernameNotFoundException("Usu�rio deve ser informado.");
		}
			
	}

}
