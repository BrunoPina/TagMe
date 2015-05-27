package br.com.tagme.auth.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import br.com.sankhya.place.validator.CPFValidator;
import br.com.sankhya.place.validator.EmailValidator;
import br.com.tagme.auth.dao.UsuarioDao;
import br.com.tagme.auth.model.Usuario;
import br.com.tagme.commons.dao.PessoaDao;
import br.com.tagme.commons.http.SchemeReference;
import br.com.tagme.commons.model.Pessoa;
import br.com.tagme.commons.spring.HttpContextSession;
import br.com.tagme.commons.utils.StringUtils;

@Controller
public class ContaUsuarioServlet {

	@Autowired
	private HttpContextSession	contextSession;
	
	@Autowired
	private UsuarioDao			usuarioDao;

	@Autowired
	private PessoaDao			pessoaDao;
	
	private static final int	STATUS_SUCCESS	= 1;
	private static final int	STATUS_ERROR	= 2;
	
	@RequestMapping(value = "/ajustesConta", headers = "content-type=multipart/*", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.TEXT_HTML_VALUE, method = RequestMethod.POST)
	public @ResponseBody void ajustesConta(HttpServletRequest request, HttpServletResponse response, @RequestParam("foto") MultipartFile file, @RequestParam Map<String, String> params) throws IOException {

		StringBuffer responseString = new StringBuffer("<html><head></head><body>");

		String nome = params.get("nome");
		String alteraSenha = params.get("alteraSenha");
		String senhaAtual = params.get("senhaAtual");
		String novaSenha = params.get("novaSenha");
		String confSenha = params.get("confSenha");

		PasswordEncoder passwordEncode = new BCryptPasswordEncoder();

		Usuario usuario = usuarioDao.getUsuarioById(contextSession.getSKPLACE_CODUSU());
		usuario.setNome(nome);

		int status = STATUS_SUCCESS;
		boolean updateFoto = false;
		String motivoErro = "";

		if ("on".equals(alteraSenha)) {

			if (passwordEncode.matches(senhaAtual, usuario.getSenha())) {
				if (confSenha.equals(novaSenha)) {
					usuario.setSenha(passwordEncode.encode(novaSenha));
				} else {
					status = STATUS_ERROR;
					motivoErro = "Nova senha e confirmação de senha não conferem.";
				}

			} else {
				status = STATUS_ERROR;
				motivoErro = "Senha atual informada não confere com a do banco de dados";
			}
		}

		if (status == STATUS_SUCCESS) {

			if (!file.isEmpty()) {
				byte[] bytes = file.getBytes();

				/*ByteArrayInputStream in = new ByteArrayInputStream(bytes);
				BufferedImage img = ImageIO.read(in);
				int size = img.getHeight() > img.getWidth() ? img.getHeight() : img.getWidth(); 
				Image scaledImage = img.getScaledInstance(size, size, Image.SCALE_SMOOTH);
				BufferedImage imageBuff = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
				imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0, 0, 0), null);

				ByteArrayOutputStream buffer = new ByteArrayOutputStream();

				ImageIO.write(imageBuff, "jpg", buffer);

				usuario.setFoto(buffer.toByteArray());*/
				usuario.setFoto(bytes);
				updateFoto = true;
			}

			boolean updated = usuarioDao.updateUsuario(usuario, updateFoto);
			if (!updated) {
				status = STATUS_ERROR;
				motivoErro = "Erro ao atualizar banco de dados.";
			}
		}

		responseString.append(String.valueOf(status)).append(";");

		if (status == STATUS_SUCCESS) {
			responseString.append(nome).append(";");
			responseString.append(usuarioDao.getDataHoraAlteracao(usuario).toString());
		} else {
			responseString.append(motivoErro);
		}

		responseString.append("</body></html>");

		response.setContentType("text/html");
		response.getWriter().println(responseString.toString());
	}
	
	@RequestMapping(value = "/criarConta", method = RequestMethod.POST)
	public @ResponseBody void criarConta(HttpServletRequest request, HttpServletResponse response,@RequestParam Map<String, String> params) throws IOException {
	
		
		PasswordEncoder passwordEncode = new BCryptPasswordEncoder();
		
		StringBuffer responseString = new StringBuffer("<html><head></head><body>");
		int status = STATUS_ERROR;
		String motivoErro = "";
		
		String email = params.get("email");
		String novaSenha = params.get("novaSenha");
		String confSenha = params.get("confSenha");
		boolean uploadFoto = false;
		
		if(!(new EmailValidator()).validate(email) ){
			responseString.append(STATUS_ERROR);
			motivoErro = "Formato de Email inválido.";
		}
		
		if(StringUtils.isNotEmpty(email)){
		
			Usuario foundUsuario = usuarioDao.getUsuarioByUsername(email);
			
			if(foundUsuario != null){
				responseString.append(STATUS_ERROR);
				motivoErro = "Já existe uma conta com este e-mail.";
			}else if(StringUtils.isNotEmpty(email) && StringUtils.isNotEmpty(novaSenha) && StringUtils.isNotEmpty(confSenha)){
			
				if(confSenha.equals(novaSenha)){
	
					String chave = null;//link.substring(link.lastIndexOf("/")+1);
					
					Usuario usuario = new Usuario();
					usuario.setAtivo(false);
					usuario.setEmail(email);
					usuario.setSenha(passwordEncode.encode(novaSenha));
					usuario.setChave(chave);
					
					usuarioDao.insertUsuario(usuario, uploadFoto);
					
					responseString.append(STATUS_SUCCESS);
				
				}else{
					responseString.append(STATUS_ERROR);
					motivoErro = "Senhas não conferem";
				}
				
			}else{
				responseString.append(STATUS_ERROR);
				motivoErro = "Todos os campos devem ser informados.";
			}
		
		}
		
		if(status == STATUS_ERROR){
			responseString.append(";").append(motivoErro);
		}
		
		responseString.append("</body></html>");

		response.setContentType("text/html");
		response.getWriter().println(responseString.toString());
		
	}
	
	@RequestMapping(value = "/criarPessoa", method = RequestMethod.POST)
	public @ResponseBody void criarPessoa(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> params) throws IOException {
		
		StringBuffer responseString = new StringBuffer("<html><head></head><body>");
		int status = STATUS_SUCCESS;

		String motivoErro = "";
		
		String nomeCompleto = params.get("nomeCompleto");
		String profissao = params.get("profissao");
		String dtNasc = params.get("dtNasc");
		String cpf = params.get("cpf");
		String endereco = params.get("endereco");
		String bairro = params.get("bairro");
		String cep = params.get("cep");
		String cidade = params.get("cidade");
		String estado = params.get("estado");
		String nacionalidade = params.get("nacionalidade");
		String telefone = params.get("telefone");
		String celular = params.get("celular");
		String sexo = params.get("sexo");
		String fumante = params.get("fumante");
		String email = params.get("email");

		boolean uploadFoto = false;
		
		
		final String FORMATO_DATA = "dd/MM/yyyy";
	
		DateFormat formatter = new SimpleDateFormat(FORMATO_DATA);
		Date dtNascDate = null;
		try {
			dtNascDate = formatter.parse(dtNasc);
		} catch (ParseException e) {
			status = STATUS_ERROR;
			motivoErro = "Formado da 'Data de nascimento' inváido.";
		}
		
		EmailValidator emailValidator = new EmailValidator();
		if(!emailValidator.validate(email)){
			status = STATUS_ERROR;
			motivoErro = "Formado do 'Email' inváido.";
		}
		
		
		if(!CPFValidator.validate(cpf)){
			status = STATUS_ERROR;
			motivoErro = "'CPF' inváido.";
		}
		
		Pessoa pessoa = new Pessoa();
		pessoa.setNomeCompleto(nomeCompleto);
		pessoa.setProfissao(profissao);
		pessoa.setDtNasc(new Timestamp(dtNascDate.getTime()));
		pessoa.setCpf(cpf);
		pessoa.setEndereco(endereco);
		pessoa.setBairro(bairro);
		pessoa.setCep(cep);
		pessoa.setCidade(cidade);
		pessoa.setEstado(estado);
		pessoa.setNacionalidade(nacionalidade);
		pessoa.setTelefone(telefone);
		pessoa.setCelular(celular);
		pessoa.setSexo(sexo);
		pessoa.setFumante(fumante);
		pessoa.setEmail(email);
		
		if (status == STATUS_SUCCESS) {
			pessoaDao.insertPessoa(pessoa, uploadFoto);
		}

		responseString.append(String.valueOf(status)).append(";");

		if (status == STATUS_SUCCESS) {
			responseString.append(pessoa.getNomeCompleto()).append(";");
			//responseString.append(pessoa.getDataHoraAlteracao(usuario).toString());
		} else {
			responseString.append(motivoErro);
		}

		responseString.append("</body></html>");

		response.setContentType("text/html");
		response.getWriter().println(responseString.toString());
		
	}	
	
	@RequestMapping(value = "/validarConta/{chave}", produces = MediaType.TEXT_HTML_VALUE, method = RequestMethod.GET)
	public @ResponseBody void validarConta(HttpServletRequest request, HttpServletResponse response, @PathVariable("chave") String chave) throws IOException, ServletException{
		
		String motivoErro = "";
		boolean isValid = false;
		
		if(StringUtils.isNotEmpty(chave)){
		
			Usuario usuario = usuarioDao.getUsuarioByChave(chave);
			if(usuario != null){
			
				if(usuario.isAtivo()){	
					motivoErro = "Conta já está ativa.";
				}else if(usuario.getDhAtivacao() != null){
					motivoErro = "Conta já foi ativada.";
				}else{
					usuario.setAtivo(true);
					if(usuarioDao.ativarUsuario(usuario)){
						isValid = true;
					}else{
						motivoErro = "Erro ao ativar conta.";
					}
				}
			}else{
				motivoErro = "Conta não encontrada.";
			}
		}else{
			motivoErro = "Chave de validação não foi informada.";
		}

		StringBuffer responseString = new StringBuffer("<html><head></head>");
		responseString.append("<script type='text/javascript'>function placeDelayer(){");
		
		if(isValid){
			responseString.append("document.getElementById('msg').innerHTML='Conta validada, redirecionando para página de login...';");
			responseString.append("setTimeout('placeRedirect()',3000);");
		}else{
			responseString.append("document.getElementById('msg').innerHTML='").append(motivoErro).append("';");
		}
		
		responseString.append("} function placeRedirect(){ document.location = '" + SchemeReference.SKP_RELATIVEURL +"'; }</script>");
		responseString.append("<body onload=\"setTimeout('placeDelayer()',1000)\">");
		responseString.append("Validando usuário... <br/> <div id='msg'></div>");
		responseString.append("</body></html>");
		response.setContentType("text/html");
		response.getWriter().println(responseString.toString());
		
	}
}
