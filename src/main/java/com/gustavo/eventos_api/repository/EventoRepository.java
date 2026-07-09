package com.gustavo.eventos_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gustavo.eventos_api.entity.Evento;

public interface EventoRepository extends JpaRepository<Evento, Long> {

}