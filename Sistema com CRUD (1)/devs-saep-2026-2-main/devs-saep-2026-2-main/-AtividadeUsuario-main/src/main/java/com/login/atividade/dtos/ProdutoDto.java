package com.login.atividade.dtos;

import com.login.atividade.entities.ProdutoEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ProdutoDto {

    private Long id;

    @NotBlank(message = "O nome do produto é obrigatório.")
    private String nome;

    private String descricao;

    @NotNull(message = "O preço é obrigatório.")
    @Positive(message = "O preço deve ser maior que zero.")
    private Double preco;

    @NotNull(message = "A quantidade em estoque é obrigatória.")
    @Min(value = 0, message = "A quantidade em estoque não pode ser negativa.")
    private Integer quantidadeEstoque;

    // Opcional no formulário: se vier nulo, mantém o valor atual (ou 0 em produto novo)
    @Min(value = 0, message = "O estoque mínimo não pode ser negativo.")
    private Integer estoqueMinimo;

    public ProdutoDto() {
    }

    public ProdutoDto(Long id, String nome, String descricao, Double preco,
                      Integer quantidadeEstoque, Integer estoqueMinimo) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
        this.estoqueMinimo = estoqueMinimo;
    }

    public static ProdutoDto fromEntity(ProdutoEntity entity) {
        return new ProdutoDto(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getPreco(),
                entity.getQuantidadeEstoque(),
                entity.getEstoqueMinimo()
        );
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Double getPreco() { return preco; }
    public void setPreco(Double preco) { this.preco = preco; }

    public Integer getQuantidadeEstoque() { return quantidadeEstoque; }
    public void setQuantidadeEstoque(Integer quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }

    public Integer getEstoqueMinimo() { return estoqueMinimo; }
    public void setEstoqueMinimo(Integer estoqueMinimo) { this.estoqueMinimo = estoqueMinimo; }
}
