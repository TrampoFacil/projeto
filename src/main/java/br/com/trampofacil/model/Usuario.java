package br.com.trampofacil.model;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name="usuario")
public class Usuario implements UserDetails,Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@NotEmpty(message = "Informe seu Nome")
	private String nome;
	@NotEmpty(message = "Informe seu Sobrenome")
	private String sobrenome;
	@Email(message = "O e-mail informado é inválido")
	@NotEmpty(message = "Informe o e-mail do usuário")
	private String email;
	@NotEmpty(message = "Informe a senha do usuário")
	private String senha;
	
	private boolean resetada = false;
	
	@Transient
	private String contratante;
	
	@Transient
	private String contratado;
	
	@ManyToMany(fetch= FetchType.EAGER)
	@JoinTable(name="usuario_roles", joinColumns=@JoinColumn(name="usuario_id", referencedColumnName="id"),
	inverseJoinColumns=@JoinColumn(name="role_id",referencedColumnName="tipo"))
	private List<Role> roles;
	
	
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	
	
	public boolean isResetada() {
		return resetada;
	}
	public void setResetada(boolean resetada) {
		this.resetada = resetada;
	}
	public String getContratante() {
		return contratante;
	}
	public void setContratante(String contratante) {
		this.contratante = contratante;
	}
	public String getContratado() {
		return contratado;
	}
	public void setContratado(String contratado) {
		this.contratado = contratado;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSobrenome() {
		return sobrenome;
	}
	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.senha;
	}
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.email;
	}
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
	
	
}
