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
import br.com.tagme.commons.model.CheckIn;
import br.com.tagme.commons.model.Pessoa;
import br.com.tagme.commons.spring.ConnectionTemplateFactory;
import br.com.tagme.commons.spring.HttpContextSession;

@Repository
public class CheckInDao implements IDao {

	@Autowired
	private HttpContextSession	contextSession;
	


	public CheckInDao() {
	}

	public CheckIn getCheckInById(final String id) {

		String sql = "SELECT  CODCIN ,CODPES, CODINS, VOUCHER, DHRESERVA, OBSERVACAO "
					+ "from TAGCIN "
					+ "where CODCIN = ?";

		try{
			CheckIn checkIn = ConnectionTemplateFactory.getTemplate().queryForObject(sql, new Object[] { id }, new RowMapper<CheckIn>() {
	
				@Override
				public CheckIn mapRow(ResultSet rs, int rowNum) throws SQLException {
	
					CheckIn checkIn = new CheckIn();
					checkIn.setCodCin(rs.getString("CODCIN"));
					checkIn.setCodPes(rs.getString("CODPES"));
					checkIn.setCodIns(rs.getString("CODINS"));
					checkIn.setVoucher(rs.getString("VOUCHER"));
					checkIn.setDhReserva(rs.getTimestamp("DHRESERVA"));
					checkIn.setObservacao(rs.getString("OBSERVACAO"));
					return checkIn;
				}
	
			});
			return checkIn;
		}catch(EmptyResultDataAccessException e){
			return null;
		}
	}

	public long insertCheckIn(final CheckIn checkIn) {
		
		//TODO Com Foto
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("CODPES"  , checkIn.getCodPes());
		parametros.put("CODINS"   , checkIn.getCodIns());
		parametros.put("VOUCHER"  , checkIn.getVoucher());
		parametros.put("DHRESERVA"  , checkIn.getDhReserva());
		parametros.put("OBSERVACAO" ,checkIn.getObservacao());

		return new SimpleJdbcInsert(ConnectionTemplateFactory.getTemplate())
		.withTableName("TAGCIN")
		.usingGeneratedKeyColumns("CODCIN")
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
