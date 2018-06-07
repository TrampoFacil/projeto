package br.com.trampofacil.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.trampofacil.model.Usuario;
import br.com.trampofacil.repository.RoleRepository;
import br.com.trampofacil.repository.UsuarioRepository;
import br.com.trampofacil.service.CryptService;
import br.com.trampofacil.service.RoleService;
import br.com.trampofacil.service.SmtpMailSender;
import br.com.trampofacil.service.UsuarioService;


@Controller
public class IndexController {
	
	@Autowired
	RoleRepository roleRepositorio;
	
	@Autowired
	UsuarioRepository usuarioRepo;
	
	@Autowired
	UsuarioService usuarioservice;
	
	@Autowired
	SmtpMailSender smtpMailSender;
	
	@Autowired
	CryptService cryptservice;
	
	@Autowired
	RoleService roleService;
	
	@GetMapping(value="/")
	public ModelAndView paginaPrincipal() {
		ModelAndView mav = new ModelAndView("index");
		return mav;
	}
	
	
	
	@RequestMapping(value="/login")
	public ModelAndView paginaLogin() {
		ModelAndView mav = new ModelAndView("autenticacao/login");
		return mav;
	}
	
	@RequestMapping(value="/registrar")
	public ModelAndView registrar(Usuario usuario) {
		ModelAndView mav = new ModelAndView("autenticacao/registrarUsuario");
		mav.addObject("roles",roleRepositorio.findAll());	
		mav.addObject(usuario);
		return mav;
	}
	
	@RequestMapping(value="/registrar",method = RequestMethod.POST)
	public ModelAndView salvar(@Valid Usuario usuario, BindingResult result, RedirectAttributes attributes) {
		ModelAndView mav;
		
		//ERROR NO MODEL//
		if(result.hasErrors()) {
			mav = new ModelAndView("autenticacao/registrarUsuario");
			mav.addObject(usuario);
			return mav;
		}
		boolean statussalvar = usuarioservice.salvar(usuario,result);
		
		//Se true nao existe usuario cadasraso, se nao usuario ja ta cadastrado com mesmo email(login)
		if (statussalvar == true) {
				
				attributes.addFlashAttribute("mensagem", "Usuário registrado, favor para ter acesso faça login.");
				mav = new ModelAndView("redirect:/login");
				return mav;
		
		}else {
			mav = new ModelAndView("autenticacao/registrarUsuario");
			mav.addObject("mensagem", "Email já cadastrado");
			mav.addObject("roles",roleRepositorio.findAll());
			mav.addObject(usuario);
			return mav;
		}
	
	}
	
	
	@RequestMapping(value="/resetar")
	public ModelAndView resetarSenha() {
		
		ModelAndView mav = new ModelAndView("autenticacao/resetarSenha");
		return mav;
	
	}
	@RequestMapping(value="/resetar/enviar")
	public ModelAndView resetarSenha(@RequestParam("emailReset") String emailReset,RedirectAttributes attributes) throws MessagingException {
		Usuario usuario = null;
		
		if(emailReset == null || emailReset.isEmpty()) {
			return redirecionaComParametro("msgError","Entre com o e-mail", "/resetar", attributes);
		}else {
			usuario = usuarioRepo.findByEmail(emailReset);
			if(usuario == null) {
				attributes.addFlashAttribute("msgError", "E-mail não cadastrado no sistema");
				return redirecionaComParametro("email",emailReset,"/resetar", attributes);
			}else {
				
				String senha = cryptservice.gerar();
				usuario.setSenha(cryptservice.encriptar(senha));
				usuario.setResetada(true);
				usuarioRepo.save(usuario);
				String destinatario = emailReset;
				String assunto = "Reset de senha TrampoFacil ";
				String corpoEmail = "Entre com o seu login e com a sua senha temporária de acesso no TrampoFácil" + "\nSenha: " + senha;
				smtpMailSender.send(destinatario, assunto, corpoEmail);
			}
		}
		return redirecionaComParametro("msgEmailEnviado", "Um E-mail foi enviado para "+usuario.getEmail()+", confira sua caixa de entrada.","/login", attributes);
		
	
	}
	
	private ModelAndView redirecionaComParametro(String variavel, String valor, String caminho, RedirectAttributes attributes) {
		attributes.addFlashAttribute(variavel, valor);
		return new ModelAndView("redirect:" + caminho);
	}
	
	
	@RequestMapping(value = "/painel")
	public ModelAndView painel(HttpSession session,RedirectAttributes attributes) {
		ModelAndView mav = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuarioAutenticado = usuarioservice.procurarPorEmail(authentication.getName());				
		session.setAttribute("hs_usuario", usuarioAutenticado);
		session.setAttribute("ses_role", roleService.getRole(authentication));
		
		if(usuarioAutenticado !=null) {
			if(usuarioAutenticado.isResetada()) {			
				mav = new ModelAndView("autenticacao/resetarNovaSenha");
				mav.addObject("usuario",usuarioAutenticado);
			}		
			else {
				mav = new ModelAndView("/interno/painel");
			}
		}else {
			mav = new ModelAndView("redirect:/login");
		}
		
		return mav;
	}
	
	@RequestMapping(value = "/resetar/alterar", method = RequestMethod.POST)
	public ModelAndView alterarSenha(Usuario usuarioView, HttpSession sessao, RedirectAttributes attributes) throws MessagingException  {
		ModelAndView mav = new ModelAndView("acesso");
		Usuario usuarioSessao = (Usuario) sessao.getAttribute("hs_usuario");
		String email = usuarioSessao.getEmail();
		String senhaView = usuarioView.getSenha();
		Usuario usuario = usuarioRepo.findByEmail(email);
		if (usuario != null) {
			String senhaAlterada = cryptservice.encriptar(senhaView);
			usuario.setSenha(senhaAlterada);
			usuario.setResetada(false);
			usuarioRepo.save(usuario);
			String destinatario = usuario.getEmail();
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			String assunto = "Senha alterada com sucesso em " + df.format(new Date());
			String corpoEmail = "Sua senha foi alterada com sucesso, acesse o sistema com sua nova senha: "+ senhaView;
			
			smtpMailSender.send(destinatario, assunto, corpoEmail);
			attributes.addFlashAttribute("msgSucessoAlt", "Alteração de senha realizada com sucesso");
			
		}
		return mav;
	}
	
}
