package com.login.atividade.sessoes;

import jakarta.servlet.http.HttpSession;

public class SessaoUtil {

    private static final String SESSAO_USUARIO = "usuarioLogado";

    public static void registrarSessao(HttpSession session, SessaoDto sessaoDto) {
        session.setAttribute(SESSAO_USUARIO, sessaoDto);
    }

    public static SessaoDto obterSessao(HttpSession session) {
        return (SessaoDto) session.getAttribute(SESSAO_USUARIO);
    }

    public static void removerSessao(HttpSession session) {
        session.removeAttribute(SESSAO_USUARIO);
    }
}