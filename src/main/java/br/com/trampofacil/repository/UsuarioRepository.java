package br.com.trampofacil.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.trampofacil.model.Usuario;
@Repository
public interface UsuarioRepository extends CrudRepository<Usuario,Integer> {
	Usuario findByEmail(String email);
}
