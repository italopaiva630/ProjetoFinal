package com.login.atividade.dtos;

import com.login.atividade.entities.ProdutoEntity;

/** Versão "enxuta" do produto usada na tela de movimentações (select + estoque atual). */
public class ProdutoRespostaDto {

    private final Long id;
    private final String nome;
    private final Integer quantidadeEstoque;
    private final Integer estoqueMinimo;

    public ProdutoRespostaDto(Long id, String nome, Integer quantidadeEstoque, Integer estoqueMinimo) {
        this.id = id;
        this.nome = nome;
        this.quantidadeEstoque = quantidadeEstoque;
        this.estoqueMinimo = estoqueMinimo;
    }

    public static ProdutoRespostaDto toProdutoRespostaDto(ProdutoEntity produto) {
        return new ProdutoRespostaDto(
                produto.getId(),
                produto.getNome(),
                produto.getQuantidadeEstoque(),
                produto.getEstoqueMinimo()
        );
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public Integer getQuantidadeEstoque() { return quantidadeEstoque; }
    public Integer getEstoqueMinimo() { return estoqueMinimo; }

    /** Alias de compatibilidade para templates que usam o nome antigo "estoqueAtual". */
    public Integer getEstoqueAtual() { return quantidadeEstoque; }
}
