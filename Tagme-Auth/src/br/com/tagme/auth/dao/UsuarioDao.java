package br.com.tagme.auth.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import br.com.tagme.commons.daointerfaces.GenericObject;
import br.com.tagme.commons.daointerfaces.IDao;
import br.com.tagme.commons.spring.HttpContextSession;
import br.com.tagme.commons.utils.TimeUtils;
import br.com.tagme.auth.db.ConnectionTemplate;
import br.com.tagme.auth.model.Usuario;

@Repository
public class UsuarioDao implements IDao {

	@Autowired
	private HttpContextSession	contextSession;

	public UsuarioDao() {
	}

	public Usuario getUsuarioById(final String id) {

		String sql = "SELECT cod_sujeito as codUsu, usuario as email, senha, foto, dhalter_perfil, dhalter_foto, nome, last_login, ativo " + "FROM sec_sujeito " + "WHERE cod_sujeito = ?";

		try{
			Usuario usuario = ConnectionTemplate.getTemplate().queryForObject(sql, new Object[] { id }, new RowMapper<Usuario>() {
	
				@Override
				public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
	
					Usuario usuario = new Usuario();
					usuario.setCodUsu(id);
					usuario.setEmail(rs.getString("email"));
					usuario.setSenha(rs.getString("senha"));
					usuario.setNome(rs.getString("nome"));
					usuario.setDhalterPerfil(rs.getTimestamp("dhalter_perfil"));
					usuario.setDhalterFoto(rs.getTimestamp("dhalter_foto"));
					usuario.setLastLogin(rs.getTimestamp("last_login"));
					usuario.setFoto(rs.getBytes("foto"));
					usuario.setAtivo("S".equals(rs.getString("ativo")) ? true : false);
	
					return usuario;
				}
	
			});
			return usuario;
		}catch(EmptyResultDataAccessException e){
			return null;
		}
	}

	public Usuario getUsuarioByUsername(final String username) {

		String sql = "SELECT cod_sujeito as codUsu, usuario as email, senha, foto, dhalter_perfil, dhalter_foto, nome, last_login, ativo " + 
					 "FROM sec_sujeito " + 
					 "WHERE usuario = ?";

		try{
			Usuario usuario = ConnectionTemplate.getTemplate().queryForObject(sql, new Object[] { username }, new RowMapper<Usuario>() {
	
				@Override
				public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
	
					Usuario usuario = new Usuario();
					usuario.setCodUsu(rs.getString("codUsu"));
					usuario.setEmail(rs.getString("email"));
					usuario.setSenha(rs.getString("senha"));
					usuario.setNome(rs.getString("nome"));
					usuario.setDhalterPerfil(rs.getTimestamp("dhalter_perfil"));
					usuario.setDhalterFoto(rs.getTimestamp("dhalter_foto"));
					usuario.setLastLogin(rs.getTimestamp("last_login"));
					usuario.setFoto(rs.getBytes("foto"));
					usuario.setAtivo("S".equals(rs.getString("ativo")) ? true : false);
	
					return usuario;
				}
	
			});
			return usuario;
		}catch(EmptyResultDataAccessException e){
			return null;
		}
	}
	
	public Usuario getUsuarioByChave(final String chave){
		
		String sql = "SELECT cod_sujeito as codUsu, usuario as email, senha, foto, nome, dhalter_perfil, dhalter_foto, ativo, dhativacao " + 
					 "FROM sec_sujeito " + 
					 "WHERE chave = ?";
		
		try{
			Usuario usuario = ConnectionTemplate.getTemplate().queryForObject(sql, new Object[] {chave}, new RowMapper<Usuario>(){
	
				@Override
				public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
	
					Usuario usuario = new Usuario();
					usuario.setCodUsu(rs.getString("codUsu"));
					usuario.setEmail(rs.getString("email"));
					usuario.setSenha(rs.getString("senha"));
					usuario.setNome(rs.getString("nome"));
					usuario.setFoto(rs.getBytes("foto"));
					usuario.setDhalterPerfil(rs.getTimestamp("dhalter_perfil"));
					usuario.setDhalterFoto(rs.getTimestamp("dhalter_foto"));
					usuario.setAtivo("S".equals(rs.getString("ativo")) ? true : false);
					usuario.setChave(chave);
					usuario.setDhAtivacao(rs.getTimestamp("dhativacao"));
					
					return usuario;
				}
				
			});
			return usuario;
		}catch(EmptyResultDataAccessException e){
			return null;
		}
		
	}

	public List<Usuario> getUsuariosByUsernameList(Collection<String> colUsernames) {

		String usernames = org.springframework.util.StringUtils.collectionToDelimitedString(colUsernames, ",", "'", "'");

		String sql = "SELECT cod_sujeito as codUsu, usuario as email, senha, foto, dhalter_perfil, dhalter_foto, nome, last_login, ativo " + "FROM sec_sujeito " + "WHERE usuario in (" + usernames + ")";

		List<Map<String, Object>> rows = ConnectionTemplate.getTemplate().queryForList(sql);

		List<Usuario> listUsuarios = new ArrayList<Usuario>();
		
		for (Map<String, Object> row : rows) {
			
			Usuario usuario = new Usuario();
			usuario.setCodUsu(row.get("codUsu").toString());
			usuario.setEmail(row.get("email").toString());
			usuario.setSenha(row.get("senha").toString());
			usuario.setFoto((byte[]) row.get("foto"));
			usuario.setNome(row.get("nome").toString());
			usuario.setDhalterPerfil(TimeUtils.getTimestamp(row.get("dhalter_perfil")));
			usuario.setDhalterFoto(TimeUtils.getTimestamp(row.get("dhalter_foto")));
			usuario.setLastLogin(TimeUtils.getTimestamp(row.get("last_login")));
			usuario.setAtivo("S".equals(row.get("ativo").toString()) ? true : false);
			listUsuarios.add(usuario);
		}
		
		return listUsuarios;
	}

	public boolean updateUsuario(Usuario usuario, boolean updateFoto) {

		String sql = "UPDATE sec_sujeito " + "SET nome = ?, senha = ?, foto = ?, codpes = ? ,dhalter_perfil = CURRENT_TIMESTAMP ";

		if (updateFoto) {
			sql += ", dhalter_foto = CURRENT_TIMESTAMP ";
		}

		sql += "WHERE cod_sujeito = ?";

		int rows = ConnectionTemplate.getTemplate().update(sql, new Object[] { usuario.getNome(), usuario.getSenha(), usuario.getFoto(),usuario.getCodPes(), usuario.getCodUsu() });

		if (rows == 0) {
			return false;
		} else {

			contextSession.setSKPLACE_CODUSU(usuario.getCodUsu());
			contextSession.setSKPLACE_USERNAME(usuario.getEmail());
			contextSession.setSKPLACE_NOME(usuario.getNome());
			contextSession.setSKPLACE_LASTLOGIN(usuario.getLastLogin());

			return true;
		}
	}
	
	public void insertUsuario(Usuario usuario, boolean uploadFoto) {
		
		if(uploadFoto){
			String sql = "INSERT INTO sec_sujeito(usuario, senha, foto, nome, dhalter_perfil, dhalter_foto, ativo, chave) VALUES (?,?,?,?,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,?,?);";
			ConnectionTemplate.getTemplate().update(sql, new Object[] {usuario.getEmail(), usuario.getSenha(), usuario.getFoto(), usuario.getNome(), "N", usuario.getChave()});
		}else{
			String sql = "INSERT INTO sec_sujeito(usuario, senha, nome, dhalter_perfil, ativo, chave) VALUES (?,?,?,CURRENT_TIMESTAMP,?,?);";
			ConnectionTemplate.getTemplate().update(sql, new Object[] {usuario.getEmail(), usuario.getSenha(), usuario.getNome(), "N", usuario.getChave()});
		}
		
	}

	public Date getDataHoraAlteracao(Usuario usuario) {

		String sql = "SELECT dhalter_perfil " + "FROM sec_sujeito " + "WHERE cod_sujeito = ?";

		try{
			Date dhalter = ConnectionTemplate.getTemplate().queryForObject(sql, new Object[] { usuario.getCodUsu() }, new RowMapper<Date>() {
	
				@Override
				public Date mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getDate("dhalter_perfil");
				}
	
			});
			return dhalter;
		}catch(EmptyResultDataAccessException e){
			return null;
		}
	}

	public void setLoggedIn(Usuario usuario) {

		String sql = "UPDATE sec_sujeito " + "SET last_login = CURRENT_TIMESTAMP " + "WHERE cod_sujeito = ?";

		ConnectionTemplate.getTemplate().update(sql, new Object[] { usuario.getCodUsu() });
	}
	
	public boolean ativarUsuario(Usuario usuario){
		
		if(usuario.isAtivo()){
			String sql = "UPDATE sec_sujeito " + "SET ativo = ? , dhativacao = CURRENT_TIMESTAMP " + "WHERE cod_sujeito = ?";
			ConnectionTemplate.getTemplate().update(sql, new Object[] {"S", usuario.getCodUsu()});
			return true;
		}else{
			return false;
		}
		
	}

	@Override
	public GenericObject findByKey(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GenericObject> findByKeys(Collection<String> keys) {
		
		List<Usuario> listUsuarios = getUsuariosByUsernameList(keys);
		
		List<GenericObject> listObjects = new ArrayList<GenericObject>();
		
		for(Usuario usuario : listUsuarios){
			GenericObject object = new GenericObject();
			object.addValue("codUsu", usuario.getCodUsu());
			object.addValue("nome", usuario.getNome());
			object.addValue("email", usuario.getEmail());
			object.addValue("ativo", Boolean.toString(usuario.isAtivo()));
			listObjects.add(object);
		}
		
		return listObjects;
	}


}
