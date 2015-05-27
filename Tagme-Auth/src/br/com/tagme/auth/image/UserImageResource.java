package br.com.tagme.auth.image;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import org.apache.commons.io.IOUtils;
import br.com.tagme.commons.servlet.ImageDownloader;
import br.com.tagme.commons.servlet.ImageDownloader.ImageResource;
import br.com.tagme.commons.spring.HttpContextSession;
import br.com.tagme.auth.db.ConnectionTemplate;
import br.com.tagme.auth.db.TransactionalAuth;

@Component
@TransactionalAuth
public class UserImageResource implements ImageResource{

	public static final String NO_PHOTO = "/gwt/user_no_photo.png";
	
	@Autowired
	private HttpContextSession contextSession;
	
	@Autowired
	ApplicationContext appContext;
	
	
	public UserImageResource(){
		ImageDownloader.registerImageResource("user", this);
	}
	
	@Override
	public byte[] getImage(String imageId, String extension) {

		try{
			UserImage userImg = getUserImage(imageId);
			
			if (userImg != null && userImg.imagem != null
					&& userImg.imagem.length > 0) {
				return userImg.imagem;
			}
		}catch(Exception e){
		}
		
		Resource resource = appContext.getResource(NO_PHOTO);
		
		InputStream in = null;

		try {
			in = resource.getInputStream();
			return IOUtils.toByteArray(in);
		} catch (IOException ignored) {
		} finally {
			IOUtils.closeQuietly(in);
		}
		
		return null;
	}
	
	private UserImage getUserImage(String imageId){
		
		String codUsu = imageId;
		if(imageId.startsWith("currUser")){
			codUsu = contextSession.getSKPLACE_CODUSU();
		}

		String sql = "SELECT suj.foto, suj.dhalter_foto " +
			  "FROM sec_sujeito suj " +
			  "WHERE suj.cod_sujeito = ?";
		
		try{
			UserImage userImage = ConnectionTemplate.getTemplate().queryForObject(sql, new Object[] { codUsu }, new RowMapper<UserImage>() {
				public UserImage mapRow(ResultSet rs, int rowNum) throws SQLException {
	
					UserImage userImage = new UserImage(rs.getBytes("foto"), rs.getTimestamp("dhalter_foto"));
					
					return userImage;
				}
			});
			return userImage;
		}catch(EmptyResultDataAccessException e){
			return null;
		}
	}
	
	public class UserImage {
		
		byte[] imagem;
		Timestamp dhAlter;

		public UserImage(byte[] imagem, Timestamp dhAlter) {
			this.imagem = imagem;
			this.dhAlter = dhAlter;
		}

	}

	@Override
	public long getLastModified(String imageID, String extension) {
		
		/*try {
			if (!"noPhoto".equalsIgnoreCase(imageID)) {

				UserImage userImg = getUserImage(imageID);

				if (userImg != null && userImg.imagem != null) {
					Timestamp dhAlter = userImg.dhAlter;

					if (dhAlter != null) {
						Calendar cal = new GregorianCalendar();
						cal.setTimeInMillis(dhAlter.getTime());
						cal.set(Calendar.MILLISECOND,
								cal.getMaximum(Calendar.MILLISECOND));

						return cal.getTimeInMillis();
					}
				}
			}

			ServletContext ctx = req.getSession().getServletContext();
			File noPhoto = new File(ctx.getRealPath(NO_PHOTO));

			if (noPhoto != null && noPhoto.exists()) {
				return noPhoto.lastModified();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		
		return 0;
	}

}
