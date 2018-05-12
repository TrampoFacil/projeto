package br.com.trampofacil.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/")
public class IndexController {
	
	
	@RequestMapping()
	public ModelAndView paginaPrincipal() {
		ModelAndView mav = new ModelAndView("index");
		return mav;
	}
	
	@RequestMapping(value="/registrar")
	public ModelAndView registrar() {
		ModelAndView mav = new ModelAndView("registrarUsuario");
		return mav;
	}
}
