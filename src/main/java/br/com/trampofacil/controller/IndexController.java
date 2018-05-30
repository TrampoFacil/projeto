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


@Controller
@RequestMapping("/")
public class IndexController {
	
	@Autowired
	RoleRepository roleRepositorio;
	
	@RequestMapping()
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
	
	@RequestMapping(value="/registrar/salvar",method = RequestMethod.POST)
	public ModelAndView salvar(@Valid Usuario usuario, BindingResult result, RedirectAttributes attributes) {
		ModelAndView mav;
		
		//ERROR NO MODEL//
		if(result.hasErrors()) {
			mav = new ModelAndView("autenticacao/registrarUsuario");
			mav.addObject(usuario);
			return mav;
		}
		//Não encontrou erros

		System.out.println("Usuário não existe, criando...");
		System.out.println(usuario.getNome());
		System.out.println(usuario.getSobrenome());
		System.out.println(usuario.getEmail());
		System.out.println(usuario.getSenha());
		
		return new ModelAndView("redirect:/login");

		
	}
	
}
