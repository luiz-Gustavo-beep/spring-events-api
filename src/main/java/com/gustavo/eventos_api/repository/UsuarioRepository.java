package com.gustavo.eventos_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gustavo.eventos_api.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByEmail(String email);
    boolean existsByEmail(String email);



}
