package br.com.tagme.commons.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import br.com.tagme.commons.daointerfaces.GenericObject;
import br.com.tagme.commons.daointerfaces.IDao;
import br.com.tagme.commons.model.Pessoa;
import br.com.tagme.commons.spring.ConnectionTemplateFactory;
import br.com.tagme.commons.spring.HttpContextSession;

@Repository
public class PessoaDao implements IDao {

	@Autowired
	private HttpContextSession	contextSession;
	


	public PessoaDao() {
	}

	public Pessoa getPessoaById(final String id) {

		String sql = "SELECT  CODPES ,nomecompleto, profissao, dtnasc, cpf, endereco,bairro,cep,cidade,ESTADO, NACIONALIDADE, TELEFONE, CELULAR, SEXO, FUMANTE, EMAIL "
					+ "from TAGPES "
					+ "where CODPES = ?";

		try{
			Pessoa pessoa = ConnectionTemplateFactory.getTemplate().queryForObject(sql, new Object[] { id }, new RowMapper<Pessoa>() {
	
				@Override
				public Pessoa mapRow(ResultSet rs, int rowNum) throws SQLException {
	
					Pessoa pessoa = new Pessoa();
					pessoa.setCodPes(rs.getString("codpes"));
					pessoa.setNomeCompleto(rs.getString("nomeCompleto"));
					pessoa.setProfissao(rs.getString("profissao"));
					pessoa.setDtNasc(rs.getTimestamp("dtnasc"));
					pessoa.setCpf(rs.getString("cpf"));
					pessoa.setEndereco(rs.getString("endereco"));
					pessoa.setBairro(rs.getString("bairro"));
					pessoa.setCep(rs.getString("cep"));
					pessoa.setCidade(rs.getString("cidade"));
					pessoa.setEstado(rs.getString("estado"));
					pessoa.setNacionalidade(rs.getString("nacionalidade"));
					pessoa.setTelefone(rs.getString("telefone"));
					pessoa.setCelular(rs.getString("celular"));
					pessoa.setSexo(rs.getString("sexo"));
					pessoa.setFumante(rs.getString("fumante"));
					pessoa.setEmail(rs.getString("email"));
					return pessoa;
				}
	
			});
			return pessoa;
		}catch(EmptyResultDataAccessException e){
			return null;
		}
	}

	
	public long insertPessoa(final Pessoa pessoa, boolean uploadFoto) {
		
		//TODO Com Foto
		
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("nomeCompleto"  , pessoa.getNomeCompleto());
		parametros.put("profissao"     , pessoa.getProfissao());
		parametros.put("dtNasc"        , pessoa.getDtNasc());
		parametros.put("cpf"           , pessoa.getCpf());
		parametros.put("endereco"      , pessoa.getEndereco());
		parametros.put("bairro"        , pessoa.getBairro());
		parametros.put("cep"           , pessoa.getCep());
		parametros.put("cidade"        , pessoa.getCidade());
		parametros.put("estado"        , pessoa.getEstado());
		parametros.put("nacionalidade" , pessoa.getNacionalidade());
		parametros.put("telefone"      , pessoa.getTelefone());
		parametros.put("celular"       , pessoa.getCelular());
		parametros.put("sexo"          , pessoa.getSexo());
		parametros.put("fumante"       , pessoa.getFumante());
		parametros.put("email"         , pessoa.getEmail());
		
		return new SimpleJdbcInsert(ConnectionTemplateFactory.getTemplate())
		.withTableName("TAGPES")
		.usingGeneratedKeyColumns("codpes")
		.executeAndReturnKey(parametros).longValue();
		
		//vincularUsuario(codPes);		
		
	}
	/*
	private void vincularUsuario(long codPes){
		Usuario usuario = usuarioDao.getUsuarioById(contextSession.getSKPLACE_CODUSU());
		usuario.setCodPes(new BigDecimal(codPes));
		usuarioDao.updateUsuario(usuario, false);
	}*/
	
	
	
	

	@Override
	public GenericObject findByKey(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GenericObject> findByKeys(Collection<String> key) {
		// TODO Auto-generated method stub
		return null;
	}

}
