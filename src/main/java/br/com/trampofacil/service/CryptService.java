package br.com.trampofacil.service;

import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CryptService {

	@Transactional
	public String encriptar(String chave) {

		return BCrypt.hashpw(chave, BCrypt.gensalt());

	}

	@Transactional
	public Boolean chaveIsValid(String chave, String hash) {

		return BCrypt.checkpw(chave, hash);
	}

	@Transactional
	public String gerar() {
		UUID uuid = UUID.randomUUID();
		String senhaBase = uuid.toString();		
		return senhaBase.substring(0, 6);
	}
}
