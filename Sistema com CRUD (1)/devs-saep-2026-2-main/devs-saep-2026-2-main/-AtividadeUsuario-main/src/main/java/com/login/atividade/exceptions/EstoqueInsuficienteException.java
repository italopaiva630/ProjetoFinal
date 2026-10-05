package com.login.atividade.exceptions;

/** Lançada quando uma saída pede mais unidades do que existem em estoque. */
public class EstoqueInsuficienteException extends RuntimeException {
    public EstoqueInsuficienteException(String message) {
        super(message);
    }
}
