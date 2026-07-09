package com.gustavo.eventos_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gustavo.eventos_api.dto.InscricaoRequest;
import com.gustavo.eventos_api.entity.Inscricao;
import com.gustavo.eventos_api.service.InscricaoService;

@RestController
@RequestMapping("/api/inscricoes")
public class InscricaoController {

    private final InscricaoService inscricaoService;

    public InscricaoController(InscricaoService inscricaoService) {
        this.inscricaoService = inscricaoService;
    }

    @GetMapping
    public List<Inscricao> listarInscricoes() {
        return inscricaoService.listarInscricoes();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Inscricao> listarInscricoesPorUsuario(@PathVariable Long usuarioId) {
        return inscricaoService.listarInscricoesPorUsuario(usuarioId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Inscricao realizarInscricao(@RequestBody InscricaoRequest request) {
        return inscricaoService.realizarInscricao(request.getUsuarioId(), request.getEventoId());
    }
}
