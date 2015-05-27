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
import br.com.tagme.commons.model.Instituicao;
import br.com.tagme.commons.model.Pessoa;
import br.com.tagme.commons.spring.ConnectionTemplateFactory;
import br.com.tagme.commons.spring.HttpContextSession;

@Repository
public class InstituicaoDao implements IDao {

	@Autowired
	private HttpContextSession	contextSession;

	public InstituicaoDao() {
	}

	public Instituicao getInstituicaoById(final String id) {

		String sql = "SELECT  CODINS ,NOME, NOMEABREV, CNPJ, RAZAOSOCIAL,LAYOUTCHECKIN "
					+ "from TAGINS "
					+ "where CODINS = ?";

		try{
			Instituicao instituicao = ConnectionTemplateFactory.getTemplate().queryForObject(sql, new Object[] { id }, new RowMapper<Instituicao>() {
	
				@Override
				public Instituicao mapRow(ResultSet rs, int rowNum) throws SQLException {
	
					Instituicao instituicao = new Instituicao();
					instituicao.setCodIns(rs.getString("CODINS"));
					instituicao.setNome(rs.getString("NOME"));
					instituicao.setNomeAbrev(rs.getString("NOMEABREV"));
					instituicao.setCnpj(rs.getString("CNPJ"));
					instituicao.setRazaoSocial(rs.getString("RAZAOSOCIAL"));
					instituicao.setLayoutCheckIn(rs.getBinaryStream("LAYOUTCHECKIN"));
					return instituicao;
				}
	
			});
			return instituicao;
		}catch(EmptyResultDataAccessException e){
			return null;
		}
	}

	
	public long insert(final Instituicao instituicao) {
		
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("NOME"  , instituicao.getNome());
		parametros.put("NOMEABREV"     ,instituicao.getNomeAbrev());
		parametros.put("CNPJ"        , instituicao.getCnpj());
		parametros.put("RAZAOSOCIAL"           , instituicao.getRazaoSocial());
		parametros.put("LAYOUTCHECKIN"      , instituicao.getLayoutCheckIn());
		
		return  new SimpleJdbcInsert(ConnectionTemplateFactory.getTemplate())
		.withTableName("TAGINS")
		.usingGeneratedKeyColumns("codInd")
		.executeAndReturnKey(parametros).longValue();
	}
	

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
