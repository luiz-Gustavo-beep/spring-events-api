package com.gustavo.eventos_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gustavo.eventos_api.entity.Evento;
import com.gustavo.eventos_api.repository.EventoRepository;

@Service
public class EventoService {

    private EventoRepository eventoRepository;

    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    public Evento cadastrarEvento(Evento evento) {

        if (evento.getTitulo() == null || evento.getTitulo().isBlank()) {
            throw new IllegalArgumentException(
                "Não é possível deixar o título em branco."
            );
        }

        if (evento.getDataInicio() != null
                && evento.getDataFim() != null
                && evento.getDataInicio().isAfter(evento.getDataFim())) {

            throw new IllegalArgumentException(
                "A data de início não pode ser depois da data de fim."
            );
        }

        if (evento.getVagas() == null || evento.getVagas() <= 0) {
            throw new IllegalArgumentException(
                "As vagas são obrigatórias e devem ser maiores que zero."
            );
        }

        return eventoRepository.save(evento);
    }

    public List<Evento> listarEventos() {
        return eventoRepository.findAll();
    }
}