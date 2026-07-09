package com.gustavo.eventos_api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.gustavo.eventos_api.entity.Perfil;
import com.gustavo.eventos_api.entity.Usuario;
import com.gustavo.eventos_api.repository.UsuarioRepository;

@SpringBootApplication
public class EventosApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventosApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner criarAdminPadrao(UsuarioRepository usuarioRepository) {
		return args -> {
			String email = "admin@eventos.com";
			if (usuarioRepository.findByEmail(email).isEmpty()) {
				Usuario admin = new Usuario();
				admin.setNome("Administrador");
				admin.setEmail(email);
				admin.setSenha("Admin123");
				admin.setPerfil(Perfil.ADMIN);
				usuarioRepository.save(admin);
			}
		};
	}

}
