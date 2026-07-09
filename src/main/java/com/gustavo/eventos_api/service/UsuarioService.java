package com.gustavo.eventos_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gustavo.eventos_api.entity.Perfil;
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

        usuario.setPerfil(Perfil.USUARIO);
        return usuarioRepository.save(usuario);
    }

    public Usuario criarUsuarioAdmin(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("O email já está em uso.");
        }

        if (usuario.getPerfil() == null) {
            usuario.setPerfil(Perfil.USUARIO);
        }

        return usuarioRepository.save(usuario);
    }

    public Usuario login(String email, String senha) {
        return usuarioRepository.findByEmailAndSenha(email, senha)
            .orElseThrow(() -> new IllegalArgumentException("Email ou senha inválidos."));
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

}
