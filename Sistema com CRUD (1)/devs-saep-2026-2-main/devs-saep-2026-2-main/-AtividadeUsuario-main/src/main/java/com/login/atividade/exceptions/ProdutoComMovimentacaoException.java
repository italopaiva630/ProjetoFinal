package com.login.atividade.exceptions;

/** Lançada ao tentar excluir um produto que já possui movimentações de estoque. */
public class ProdutoComMovimentacaoException extends RuntimeException {
    public ProdutoComMovimentacaoException(String message) {
        super(message);
    }
}
