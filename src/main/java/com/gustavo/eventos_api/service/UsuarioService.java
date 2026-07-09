package com.gustavo.eventos_api.service;

import org.springframework.stereotype.Service;

import com.gustavo.eventos_api.entity.Usuario;
import com.gustavo.eventos_api.repository.UsuarioRepository;


@Service
public class UsuarioService {

    private UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario cadastrarUsuario(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("O email já está em uso.");
        
        } 
        
        return usuarioRepository.save(usuario);
    }

}
