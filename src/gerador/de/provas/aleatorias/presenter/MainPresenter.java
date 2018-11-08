/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias.presenter;

import gerador.de.provas.aleatorias.model.BancoDeProvas;
import gerador.de.provas.aleatorias.view.MainView;

/**
 *
 * @author conta
 */
public class MainPresenter {

    private MainView view;
    private BancoDeProvas bancoDeProvas;

    public MainPresenter() {
        view = new MainView();
        verificar_se_tem_banco();

        view.setVisible(true);
    }

    void verificar_se_tem_banco() {
        boolean importado = bancoDeProvas != null;
        view.getQuestoes_label().setVisible(importado);
        view.getProvas_label().setVisible(importado);
        view.getProvas_geradas_label().setVisible(importado);
        view.getImportar_provas_menu().setEnabled(importado);
        view.getGerar_provas_menu().setEnabled(importado);
        view.getExportar_provas_menu().setEnabled(importado);
        view.getSalvar_banco_de_dados_menu().setEnabled(importado);
        view.getSalvar_como_banco_de_dados_menu().setEnabled(importado);
        if (importado) {
            view.getLocal_prova_label().setText(bancoDeProvas.getLocal());
        } else {
            view.getLocal_prova_label().setText("");
        }
    }

}
