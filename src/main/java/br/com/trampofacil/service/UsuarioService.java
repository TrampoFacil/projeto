package br.com.trampofacil.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.trampofacil.model.Usuario;
import br.com.trampofacil.repository.UsuarioRepository;


@Service
public class UsuarioService implements UserDetailsService{
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
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
	
	public boolean salvar(Usuario usuario) {
		if(usuario.getId()==null) {
			usuario.setSenha(cryptService.encriptar(usuario.getSenha()));	
			usuarioRepository.save(usuario);
			return true;
		}else {
			return false;
		}
	}

}
