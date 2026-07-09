package com.gustavo.eventos_api.entity;

import jakarta.persistence.Entity;

@Entity
public enum StatusInscricao {
    PENDENTE,
    CONFIRMADA,
    CANCELA
    
}