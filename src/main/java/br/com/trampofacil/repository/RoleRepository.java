package br.com.trampofacil.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.trampofacil.model.Role;
@Repository
public interface RoleRepository extends CrudRepository<Role,String> {
	
}
