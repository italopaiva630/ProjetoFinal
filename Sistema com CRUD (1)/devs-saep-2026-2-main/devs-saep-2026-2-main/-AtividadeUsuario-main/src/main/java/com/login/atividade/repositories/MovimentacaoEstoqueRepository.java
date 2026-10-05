package com.login.atividade.repositories;

import com.login.atividade.entities.MovimentacaoEstoqueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoqueEntity,Long> {

    List<MovimentacaoEstoqueEntity> findAllByOrderByDataMovimentacaoDesc();

    boolean existsByProdutoId(Long produtoId);
}