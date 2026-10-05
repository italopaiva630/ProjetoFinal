package com.login.atividade.repositories;

import com.login.atividade.entities.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Long> {
    List<ProdutoEntity> findByNomeContainingIgnoreCase(String nome);

    // Método adicionado para permitir a listagem ordenada de produtos no serviço
    List<ProdutoEntity> findAllByOrderByNomeAsc();
}