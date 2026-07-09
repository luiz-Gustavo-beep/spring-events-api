package com.gustavo.eventos_api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gustavo.eventos_api.entity.Evento;
import com.gustavo.eventos_api.entity.Inscricao;
import com.gustavo.eventos_api.entity.StatusInscricao;
import com.gustavo.eventos_api.entity.Usuario;
import com.gustavo.eventos_api.repository.EventoRepository;
import com.gustavo.eventos_api.repository.InscricaoRepository;
import com.gustavo.eventos_api.repository.UsuarioRepository;

@Service
public class InscricaoService {

    private UsuarioRepository usuarioRepository;
    private EventoRepository eventoRepository;
    private InscricaoRepository inscricaoRepository;

    public InscricaoService(UsuarioRepository usuarioRepository, EventoRepository eventoRepository,
         InscricaoRepository inscricaoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.eventoRepository = eventoRepository;
        this.inscricaoRepository = inscricaoRepository;
    }

    public Inscricao realizarInscricao(Long usuarioId, Long eventoId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Evento evento = eventoRepository.findById(eventoId)
            .orElseThrow(() -> new IllegalArgumentException("Evento não encontrado"));

        if (inscricaoRepository.existsByUsuario_IdAndEvento_Id(usuarioId, eventoId)) {
            throw new IllegalArgumentException(
                "O usuário já está inscrito neste evento"
            );
        }

        if (evento.getVagas() == null || evento.getVagas() <= 0) {
            throw new IllegalArgumentException(
                "Não há vagas disponíveis para este evento"
            );
        }
        Inscricao inscricao = new Inscricao();
        inscricao.setUsuario(usuario);
        inscricao.setEvento(evento);
        inscricao.setDataHoraInscricao(LocalDateTime.now());
        inscricao.setStatus(StatusInscricao.PENDENTE);
        evento.setVagas(evento.getVagas() - 1);
        eventoRepository.save(evento);
        return inscricaoRepository.save(inscricao);
    }

    public List<Inscricao> listarInscricoes() {
        return inscricaoRepository.findAll();
    }

    public List<Inscricao> listarInscricoesPorUsuario(Long usuarioId) {
        return inscricaoRepository.findByUsuario_Id(usuarioId);
    }

    public List<Evento> listarEventosDisponiveisParaUsuario(Long usuarioId) {
        Set<Long> inscritos = listarInscricoesPorUsuario(usuarioId).stream()
            .map(inscricao -> inscricao.getEvento().getId())
            .collect(Collectors.toSet());

        return eventoRepository.findAll().stream()
            .filter(evento -> !inscritos.contains(evento.getId()))
            .collect(Collectors.toList());
    }
}
