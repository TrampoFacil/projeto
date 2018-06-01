package br.com.trampofacil.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.trampofacil.model.Usuario;
import br.com.trampofacil.repository.RoleRepository;
import br.com.trampofacil.service.UsuarioService;


@Controller
public class IndexController {
	
	@Autowired
	RoleRepository roleRepositorio;
	
	@Autowired
	UsuarioService usuarioservice;
	
	@RequestMapping(value="/")
	public ModelAndView paginaPrincipal() {
		ModelAndView mav = new ModelAndView("index");
		return mav;
	}
	
	@RequestMapping(value="/login")
	public ModelAndView login() {
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
}
