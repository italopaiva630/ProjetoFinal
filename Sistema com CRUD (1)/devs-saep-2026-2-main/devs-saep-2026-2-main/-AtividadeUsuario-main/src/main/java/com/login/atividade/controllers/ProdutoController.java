package com.login.atividade.controllers;

import com.login.atividade.dtos.ProdutoDto;
import com.login.atividade.entities.ProdutoEntity;
import com.login.atividade.services.ProdutoService;
import com.login.atividade.sessoes.SessaoDto;
import com.login.atividade.sessoes.SessaoUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public String listarProdutos(@RequestParam(value = "busca", required = false) String busca,
                                 HttpSession session, Model model) {
        if (SessaoUtil.obterSessao(session) == null) return "redirect:/login";

        List<ProdutoEntity> produtos;
        if (busca != null && !busca.trim().isEmpty()) {
            produtos = produtoService.buscarPorNome(busca);
        } else {
            produtos = produtoService.listarProdutos();
        }

        model.addAttribute("produtos", produtos);
        model.addAttribute("busca", busca);
        return "listasprodutos";
    }

    @GetMapping("/novo")
    public String exibirFormularioCadastro(HttpSession session, Model model) {
        if (SessaoUtil.obterSessao(session) == null) return "redirect:/login";

        model.addAttribute("produto", new ProdutoDto());
        return "cadastroproduto";
    }

    @PostMapping("/salvar")
    public String salvarProduto(@Valid @ModelAttribute("produto") ProdutoDto produtoDto,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                HttpSession session,
                                Model model) {
        if (SessaoUtil.obterSessao(session) == null) return "redirect:/login";

        if (result.hasErrors()) {
            model.addAttribute("erro", "Preencha todos os campos obrigatórios corretamente.");
            return "cadastroproduto";
        }

        try {
            produtoService.cadastrarProduto(produtoDto);
            redirectAttributes.addFlashAttribute("mensagem", "Produto salvo com sucesso!");
            return "redirect:/produtos";
        } catch (RuntimeException e) {
            model.addAttribute("erro", e.getMessage());
            return "cadastroproduto";
        }
    }

    @GetMapping("/editar/{id}")
    public String editarProduto(@PathVariable Long id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (SessaoUtil.obterSessao(session) == null) return "redirect:/login";

        try {
            ProdutoEntity entity = produtoService.buscarPorId(id);
            ProdutoDto dto = ProdutoDto.fromEntity(entity);

            model.addAttribute("produto", dto);
            return "cadastroproduto";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", "Produto não encontrado.");
            return "redirect:/produtos";
        }
    }

    @GetMapping("/deletar/{id}")
    public String deletarProduto(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (SessaoUtil.obterSessao(session) == null) return "redirect:/login";

        try {
            produtoService.deletarProduto(id);
            redirectAttributes.addFlashAttribute("mensagem", "Produto excluído com sucesso do estoque!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao tentar excluir o produto.");
        }
        return "redirect:/produtos";
    }
}