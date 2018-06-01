package br.com.trampofacil.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import br.com.trampofacil.model.Role;
import br.com.trampofacil.model.Usuario;
import br.com.trampofacil.repository.RoleRepository;
import br.com.trampofacil.repository.UsuarioRepository;


@Service
public class UsuarioService implements UserDetailsService{
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private RoleRepository rolerepository;	
	
	@Autowired
	CryptService cryptService;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByEmail(email);
		if(usuario==null) {
			throw new UsernameNotFoundException("Usuário não encontrado");
		}
		return usuario;
	}
	
	public boolean salvar(Usuario usuario,BindingResult result) {
		if(usuario.getId()==null) {
			if (usuarioRepository.findByEmail(usuario.getEmail()) != null) {			
				result.rejectValue("email","emailMessageCode","Erro ao cadastrar: Email já cadastrado. ");
				return false;
			}else {
				usuario.setSenha(cryptService.encriptar(usuario.getSenha()));	
				usuarioRepository.save(usuario);
				return true;
			}
		}else {
			return false;
		}
	}

}
