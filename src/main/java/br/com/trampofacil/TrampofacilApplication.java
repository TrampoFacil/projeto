package br.com.trampofacil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class TrampofacilApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrampofacilApplication.class, args);
		System.out.println(new BCryptPasswordEncoder().encode("123"));
	}
}
