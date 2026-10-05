package com.login.atividade.controllers;

import com.login.atividade.dtos.MovimentacaoEstoqueDto;
import com.login.atividade.entities.UsuarioEntity;
import com.login.atividade.exceptions.EstoqueInsuficienteException;
import com.login.atividade.exceptions.ProdutoNaoEncontradoException;
import com.login.atividade.services.MovimentacaoEstoqueService;
import com.login.atividade.sessoes.SessaoDto;
import com.login.atividade.sessoes.SessaoUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MovimentacaoController {

    private final MovimentacaoEstoqueService service;

    public MovimentacaoController(MovimentacaoEstoqueService service) {
        this.service = service;
    }

    @PostMapping("/movimentacaocadastrar")
    public String registrarMovimentacao(
            @Valid @ModelAttribute("movimentacaoDto") MovimentacaoEstoqueDto dto,
            BindingResult bindingResult,
            @RequestParam(value = "tipo", required = false) String tipo,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        SessaoDto usuarioSessao = SessaoUtil.obterSessao(session);
        if (usuarioSessao == null) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            String mensagemErroValidacao = bindingResult.getAllErrors().get(0).getDefaultMessage();
            model.addAttribute("mensagemErro", mensagemErroValidacao);

            recarregarListasNaTela(model, usuarioSessao);
            return "movimentacoes/listarmovimentacoes";
        }

        if (tipo == null || tipo.isBlank()) {
            model.addAttribute("mensagemErro", "O tipo de movimentação é obrigatório.");
            recarregarListasNaTela(model, usuarioSessao);
            return "movimentacoes/listarmovimentacoes";
        }

        try {
            UsuarioEntity usuarioLogado = new UsuarioEntity();
            usuarioLogado.setId(usuarioSessao.getUsuarioId());

            if ("ENTRADA".equalsIgnoreCase(tipo)) {
                service.registrarEntrada(dto, usuarioLogado);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Movimentação de entrada registrada com sucesso!");

            } else if ("SAIDA".equalsIgnoreCase(tipo)) {
                service.registrarSaida(dto, usuarioLogado);

                // CORREÇÃO: Utilizando getProdutoId() da nova classe DTO
                String mensagemAlerta = service.verificarAlertaEstoqueMinimo(dto.getProdutoId());
                if (mensagemAlerta != null) {
                    redirectAttributes.addFlashAttribute("alertaMinimo", "Movimentação registrada com sucesso! ATENÇÃO: " + mensagemAlerta);
                } else {
                    redirectAttributes.addFlashAttribute("mensagemSucesso", "Movimentação de saída registrada com sucesso!");
                }
            } else {
                model.addAttribute("mensagemErro", "Tipo de movimentação inválido.");
                recarregarListasNaTela(model, usuarioSessao);
                return "movimentacoes/listarmovimentacoes";
            }

        } catch (EstoqueInsuficienteException e) {
            model.addAttribute("erroQuantidade", e.getMessage());
            recarregarListasNaTela(model, usuarioSessao);
            return "movimentacoes/listarmovimentacoes";
        } catch (ProdutoNaoEncontradoException | IllegalArgumentException e) {
            model.addAttribute("mensagemErro", e.getMessage());
            recarregarListasNaTela(model, usuarioSessao);
            return "movimentacoes/listarmovimentacoes";
        }

        return "redirect:/movimentacoes";
    }

    private void recarregarListasNaTela(Model model, SessaoDto usuarioSessao) {
        model.addAttribute("listaProdutos", service.listarProdutosCadastrados());
        model.addAttribute("listaMovimentacoes", service.listarHistoricoMovimentacoes());
        model.addAttribute("usuarioLogado", usuarioSessao);
    }
}