package com.login.atividade.services;

import com.login.atividade.dtos.ProdutoDto;
import com.login.atividade.entities.ProdutoEntity;
import com.login.atividade.exceptions.ProdutoComMovimentacaoException;
import com.login.atividade.exceptions.ProdutoNaoEncontradoException;
import com.login.atividade.repositories.ProdutoRepository;
import com.login.atividade.repositories.MovimentacaoEstoqueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    public ProdutoService(ProdutoRepository produtoRepository, MovimentacaoEstoqueRepository movimentacaoEstoqueRepository) {
        this.produtoRepository = produtoRepository;
        this.movimentacaoEstoqueRepository = movimentacaoEstoqueRepository;
    }

    public Optional<ProdutoEntity> buscarPorIdOptional(Long id) {
        return produtoRepository.findById(id);
    }

    public ProdutoEntity buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado"));
    }

    public List<ProdutoEntity> listarProdutos() {
        return produtoRepository.findAll();
    }

    public List<ProdutoEntity> buscarPorNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Transactional
    public ProdutoDto cadastrarProduto(ProdutoDto produtoDto) {
        if (produtoDto.getNome() == null || produtoDto.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do produto é obrigatório.");
        }
        if (produtoDto.getPreco() == null || produtoDto.getPreco() <= 0) {
            throw new IllegalArgumentException("O preço deve ser maior que zero.");
        }
        if (produtoDto.getQuantidadeEstoque() == null || produtoDto.getQuantidadeEstoque() < 0) {
            throw new IllegalArgumentException("A quantidade em estoque não pode ser negativa.");
        }

        ProdutoEntity produto = new ProdutoEntity();

        if (produtoDto.getId() != null) {
            produto = buscarPorId(produtoDto.getId());
        }

        produto.setNome(produtoDto.getNome());
        produto.setDescricao(produtoDto.getDescricao());
        produto.setPreco(produtoDto.getPreco());
        produto.setQuantidadeEstoque(produtoDto.getQuantidadeEstoque());
        produto.setEstoqueMinimo(produtoDto.getEstoqueMinimo());

        produto = produtoRepository.save(produto);
        produtoDto.setId(produto.getId());

        return produtoDto;
    }

    @Transactional
    public void deletarProduto(Long id) {
        if (movimentacaoEstoqueRepository.existsByProdutoId(id)) {
            throw new ProdutoComMovimentacaoException("Não é possível excluir o produto pois ele possui movimentações no histórico.");
        }

        ProdutoEntity produto = buscarPorId(id);
        produtoRepository.delete(produto);
    }
}