package br.com.trampofacil.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.trampofacil.repository.RoleRepository;

@Service
public class RoleService {
	@Autowired
	RoleRepository roleRepository;
	
	public String getRole(Authentication authentication) {
		Set<String> roles = authentication.getAuthorities().stream().map(r -> r.getAuthority())
				.collect(Collectors.toSet());
		String role = "";
		for (String string : roles) {
			if (string.equals("ROLE_TRABALHAR")) {
				role="Trabalhar";
			}
			if (string.equals("ROLE_ANUNCIAR")) {
				role="Anunciar";
			}
			
		}

		return role;
	}
}
