package com.login.atividade.services;

import com.login.atividade.dtos.MovimentacaoEstoqueDto;
import com.login.atividade.dtos.ProdutoRespostaDto;
import com.login.atividade.entities.MovimentacaoEstoqueEntity;
import com.login.atividade.entities.ProdutoEntity;
import com.login.atividade.entities.TipoMovimentacaoEstoque;
import com.login.atividade.entities.UsuarioEntity;
import com.login.atividade.exceptions.EstoqueInsuficienteException;
import com.login.atividade.exceptions.ProdutoNaoEncontradoException;
import com.login.atividade.repositories.MovimentacaoEstoqueRepository;
import com.login.atividade.repositories.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovimentacaoEstoqueService {

    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;
    private final ProdutoRepository produtoRepository;

    public MovimentacaoEstoqueService(MovimentacaoEstoqueRepository movimentacaoEstoqueRepository, ProdutoRepository produtoRepository) {
        this.movimentacaoEstoqueRepository = movimentacaoEstoqueRepository;
        this.produtoRepository = produtoRepository;
    }

    public List<ProdutoRespostaDto> listarProdutosCadastrados() {
        return produtoRepository.findAllByOrderByNomeAsc()
                .stream()
                .map(ProdutoRespostaDto::toProdutoRespostaDto)
                .toList();
    }

    public List<MovimentacaoEstoqueEntity> listarHistoricoMovimentacoes() {
        return movimentacaoEstoqueRepository.findAllByOrderByDataMovimentacaoDesc();
    }

    @Transactional
    public void registrarEntrada(MovimentacaoEstoqueDto dto, UsuarioEntity usuarioLogado) {
        ProdutoEntity produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado no sistema."));

        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + dto.getQuantidade());
        produtoRepository.save(produto);

        movimentacaoEstoqueRepository.save(dto.toMovimentacao(produto, usuarioLogado,
                TipoMovimentacaoEstoque.ENTRADA));
    }

    @Transactional
    public void registrarSaida(MovimentacaoEstoqueDto dto, UsuarioEntity usuarioLogado) {
        ProdutoEntity produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado no sistema"));

        if (dto.getQuantidade() > produto.getQuantidadeEstoque()) {
            throw new EstoqueInsuficienteException("Quantidade insuficiente em estoque");
        }

        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - dto.getQuantidade());
        produtoRepository.save(produto);

        movimentacaoEstoqueRepository.save(dto.toMovimentacao(produto, usuarioLogado,
                TipoMovimentacaoEstoque.SAIDA));
    }

    public String verificarAlertaEstoqueMinimo(Long produtoId) {
        ProdutoEntity produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado"));

        if (produto.getQuantidadeEstoque() < produto.getEstoqueMinimo()) {
            return String.format("O produto '%s' ficou abaixo do estoque mínimo (%d unidades).",
                    produto.getNome(), produto.getEstoqueMinimo());
        }
        return null;
    }
}