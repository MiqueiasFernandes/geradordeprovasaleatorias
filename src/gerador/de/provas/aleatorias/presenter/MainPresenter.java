/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias.presenter;

import gerador.de.provas.aleatorias.model.BancoDeProvas;
import gerador.de.provas.aleatorias.model.TemplateProperties;
import gerador.de.provas.aleatorias.model.WorkDir;
import gerador.de.provas.aleatorias.util.Janela;
import gerador.de.provas.aleatorias.view.MainView;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/**
 *
 * @author conta
 */
public class MainPresenter {

    private MainView view;
    private BancoDeProvas bancoDeProvas;
    private final TemplateProperties properies;
    private final WorkDir workDir;

    public MainPresenter(WorkDir workDir) {
        this.properies = workDir.getProperties();
        this.workDir = workDir;
        view = new MainView();

        view.getNovo_banco_de_provas_menu().addActionListener((e) -> {
            criarBancoDeProvas();
        });
        view.getSalvar_banco_de_dados_menu().addActionListener((e) -> {
            salvarBancoDeProvas();
        });
        view.getSalvar_como_banco_de_dados_menu().addActionListener((e) -> {
            salvarComoBancoDeProvas();
        });
        view.getImportar_banco_de_provas_menu().addActionListener((e) -> {
            importarBancoDeProvas();
        });

        view.getImportar_provas_menu().addActionListener(((e) -> {
            new ImportarPresenter(workDir).getView().addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    salvarBancoDeProvas();
                    verificar_se_tem_banco();
                }
            });
        }));
        view.getGerar_provas_menu().addActionListener(((e) -> {
            new GerarPresenter(workDir, bancoDeProvas).getView().addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    salvarBancoDeProvas();
                    verificar_se_tem_banco();
                }
            });
        }));

        view.getExportar_provas_menu().addActionListener(((e) -> {
            new ExportarPresenter(workDir, bancoDeProvas).getView().addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    try {
                        workDir.limparTemp();
                    } catch (IOException ex) {
                        Logger.getLogger(MainPresenter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }));

        view.setVisible(true);

    }

    public void inicializar() {
        new Thread(() -> {
            verificar_se_tem_banco();
            autoImportarBancoDeProvas();
            verificar_se_tem_banco();
            view.getCarregando_label().setVisible(false);
        }).start();
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
            view.getLocal_prova_label().setText(bancoDeProvas.toString());
            view.getQuestoes_label().setText(bancoDeProvas.getNumQuestoes() + " Questões disponíveis");
            view.getProvas_label().setText(bancoDeProvas.getNumTiposdeProvas() + " tipos de provas disponíveis");
            view.getProvas_geradas_label().setText(bancoDeProvas.getNumProvasGeradas() + " provas geradas");
        }
    }

    void autoImportarBancoDeProvas() {
        //  view.setEnabled(false);
        view.getLocal_prova_label().setText("Carregando banco de provas ...");
        view.getLocal_prova_label().setText(workDir.carregarBanco(view));
        updateBancoDeProvas();
        //  view.setEnabled(true);
    }

    void criarBancoDeProvas() {
        try {
            workDir.criarBanco(view);
            view.getLocal_prova_label().setText("novo banco de provas criado ...");
            salvarBancoDeProvas();
            updateBancoDeProvas();
        } catch (Exception ex) {
            Logger.getLogger(MainPresenter.class.getName()).log(Level.SEVERE, null, ex);
            view.getLocal_prova_label().setText("Falhou ao criar: " + ex);
        }
    }

    void salvarBancoDeProvas() {
        view.getLocal_prova_label().setText(workDir.salvarBanco(view));
    }

    void salvarComoBancoDeProvas() {
        String old = properies.getZIP_PATH();
        try {
            properies.setZIP_PATH("", view);
        } catch (Exception ex) {
            Logger.getLogger(MainPresenter.class.getName()).log(Level.SEVERE, null, ex);
            view.getLocal_prova_label().setText("Falhou ao alterar propriedade: " + ex);
        }
        view.getLocal_prova_label().setText(workDir.salvarBanco(view));
        if ((properies.getZIP_PATH() == null || properies.getZIP_PATH().isEmpty()) && old != null && !old.isEmpty()) {
            try {
                properies.setZIP_PATH(old, view);
            } catch (Exception ex) {
                Logger.getLogger(MainPresenter.class.getName()).log(Level.SEVERE, null, ex);
                view.getLocal_prova_label().setText("Falhou ao alterar propriedade para antiga: " + ex);
            }
        }
    }

    void importarBancoDeProvas() {

        JFileChooser fileChooser = Janela.fileChooser(
                view,
                "Abrir banco de provas",
                properies.getZIP_PATH(),
                "Abrir",
                new String[]{"Banco de Provas", "gpa"},
                false);
        if (fileChooser != null) {
            try {
                properies.setZIP_PATH(fileChooser.getSelectedFile().getAbsolutePath(), view);
                view.getLocal_prova_label().setText(workDir.carregarBanco(view));
            } catch (Exception ex) {
                Logger.getLogger(MainPresenter.class.getName()).log(Level.SEVERE, null, ex);
                view.getLocal_prova_label().setText("Falhou ao importar banco de provas: " + ex);
            }
        }

        updateBancoDeProvas();
    }

    private void updateBancoDeProvas() {
        String zip = workDir.getProperties().getZIP_PATH();
        this.bancoDeProvas = zip != null && !zip.isEmpty() ? new BancoDeProvas(workDir, view) : null;
        verificar_se_tem_banco();
    }

}
