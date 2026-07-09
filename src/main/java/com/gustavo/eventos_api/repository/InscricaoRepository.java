package com.gustavo.eventos_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gustavo.eventos_api.entity.Inscricao;

public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {

    boolean existsByUsuario_IdAndEvento_Id(Long usuarioId, Long eventoId);
    List<Inscricao> findByUsuario_Id(Long usuarioId);

}