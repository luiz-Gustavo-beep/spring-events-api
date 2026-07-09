package com.gustavo.eventos_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yaml.snakeyaml.events.Event;

import com.gustavo.eventos_api.entity.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long> {

}