package com.gustavo.eventos_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gustavo.eventos_api.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByEmailAndSenha(String email, String senha);
    boolean existsByEmail(String email);



}
