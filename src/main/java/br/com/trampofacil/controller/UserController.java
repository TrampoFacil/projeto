package br.com.trampofacil.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/usuario")
public class UserController {

	@RequestMapping(value="/login")
	public ModelAndView login() {
		ModelAndView mav = new ModelAndView("autenticacao/login");
		return mav;
	}
	
	@RequestMapping(value="/registrar")
	public ModelAndView registrar() {
		ModelAndView mav = new ModelAndView("autenticacao/registrarUsuario");
		return mav;
	}
	 
}
