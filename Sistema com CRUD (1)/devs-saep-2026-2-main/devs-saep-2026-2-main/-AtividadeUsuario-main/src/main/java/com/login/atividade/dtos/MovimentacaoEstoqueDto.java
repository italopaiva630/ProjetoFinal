package com.login.atividade.dtos;

import com.login.atividade.entities.MovimentacaoEstoqueEntity;
import com.login.atividade.entities.ProdutoEntity;
import com.login.atividade.entities.TipoMovimentacaoEstoque;
import com.login.atividade.entities.UsuarioEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class MovimentacaoEstoqueDto {

    @NotNull(message = "O produto é obrigatório")
    private Long produtoId;

    @NotNull(message = "A quantidade é obrigatória")
    @Positive(message = "A quantidade deve ser maior que zero")
    private Integer quantidade;

    // Construtor vazio (OBRIGATÓRIO para o Spring conseguir capturar os dados do formulário)
    public MovimentacaoEstoqueDto() {
    }

    // Construtor com argumentos
    public MovimentacaoEstoqueDto(Long produtoId, Integer quantidade) {
        this.produtoId = produtoId;
        this.quantidade = quantidade;
    }

    // --- GETTERS E SETTERS ---

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    // Método para converter no Entity
    public MovimentacaoEstoqueEntity toMovimentacao(ProdutoEntity produto, UsuarioEntity usuario, TipoMovimentacaoEstoque tipo) {
        MovimentacaoEstoqueEntity movimentacao = new MovimentacaoEstoqueEntity();
        movimentacao.setProduto(produto);
        movimentacao.setUsuario(usuario);
        movimentacao.setTipoMovimentacao(tipo);
        movimentacao.setQuantidade(this.quantidade);
        movimentacao.setDataMovimentacao(LocalDateTime.now());
        return movimentacao;
    }
}