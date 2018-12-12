/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias.presenter;

import gerador.de.provas.aleatorias.model.BancoDeProvas;
import gerador.de.provas.aleatorias.model.Prova;
import gerador.de.provas.aleatorias.model.WorkDir;
import gerador.de.provas.aleatorias.model.importar.Pagina;
import gerador.de.provas.aleatorias.model.pdf.PDF;
import gerador.de.provas.aleatorias.util.Janela;
import gerador.de.provas.aleatorias.util.Singleton;
import gerador.de.provas.aleatorias.util.Utils;
import gerador.de.provas.aleatorias.view.GerarView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author conta
 */
class Vetor {

    ArrayList<Integer> provas = new ArrayList<>();

    void add(int i) {
        provas.add(i);
    }

    boolean ja_tem_prova() {
        return !provas.isEmpty();
    }

}

public class GerarPresenter {

    private final GerarView view;
    private final WorkDir workDir;
    private final BancoDeProvas bancoDeProvas;
    private DefaultTableModel model;
    private String[] tipos_de_provas;
    private ArrayList<Prova> provas_geradas = new ArrayList<>();

    GerarPresenter(WorkDir workDir, BancoDeProvas bancoDeProvas) {

        this.workDir = workDir;
        this.bancoDeProvas = bancoDeProvas;
        this.view = new GerarView();

        if (bancoDeProvas == null || bancoDeProvas.getNumQuestoes() < 1) {
            JOptionPane.showMessageDialog(view, "Importe provas primeiro!");
            view.dispose();
            return;
        }

        view.getLimpar_btn().addActionListener((e) -> {
            limpar();
        });

        view.getCriar_btn().addActionListener((e) -> {
            criar();
        });

        model = (DefaultTableModel) view.getTabela().getModel();
        tipos_de_provas = bancoDeProvas.getTiposDeProvas();

        if (tipos_de_provas == null) {
            JOptionPane.showMessageDialog(view, "Não há nenhum tipo de prova!");
            view.dispose();
            return;
        }

        resetTabela();
        view.getCabecalho_select().setSelectedItem(null);

        File[] cabs = bancoDeProvas.listarCabecalhos();
        if (cabs != null) {
            view.getCabecalho_existente_radio().setSelected(true);
            view.getCabecalho_txt().setEnabled(false);
            for (File cab : cabs) {
                view.getCabecalho_select().addItem(cab.getName());
            }
        }
        view.getImportar_btn().addActionListener((e) -> {
            importarCabecalho();
        });

        view.getCabecalho_personalizado_radio().addChangeListener((e) -> {
            view.getCabecalho_txt().setEnabled(view.getCabecalho_personalizado_radio().isSelected());
        });

        view.getQuestao_aparecer_chk().setSelected(true);
        view.getSalvar_btn().setEnabled(false);

        view.getSalvar_btn().addActionListener((e) -> {
            salvar();
        });

        view.getNome_da_prova_txt().setText(getNomeDef());

        view.setVisible(true);
    }

    void importarCabecalho() {
        JFileChooser fileChooser = Janela.fileChooser(
                view,
                "Abrir arquivo de cabeçalho", "", "Abrir",
                new String[]{"aruivo PDF", "pdf", "PDF"}, false);
        if (fileChooser != null) {
            try {
                enable(false);
                Pagina page = new PDF(
                        fileChooser.getSelectedFile().getAbsolutePath(),
                        null, 0, 0).getPages().get(0);

                page.eliminarFim();

                float[] bounds = {
                    0,
                    0,
                    page.getpDPage().getMediaBox().getWidth(),
                    page.getMarcadores().first().getY_pdf()};

                page.savePartAsPDF(bounds,
                        workDir.getProperties().getCABECALHOS_DIR() + "/" + fileChooser.getSelectedFile().getName());
                view.getCabecalho_select().addItem(fileChooser.getSelectedFile().getName());
                view.getCabecalho_select().setSelectedItem(fileChooser.getSelectedFile().getName());
                bancoDeProvas.salvar();
            } catch (IOException ex) {
                Logger.getLogger(GerarPresenter.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                enable(true);
            }
        }
    }

    String getNomeDef() {
        return "Prova " + Utils.getDateTime();
    }

    void limpar() {
        view.getNome_da_prova_txt().setText(getNomeDef());
        view.getQuantidade_provas_num().setValue(0);
        view.getQuantidade_questoes_num().setValue(0);
        view.getQuestao_aparecer_chk().setSelected(false);
        view.getCabecalho_existente_radio().setSelected(false);
        view.getCabecalho_select().setSelectedItem(null);
        view.getCabecalho_txt().setText("");
        view.getEquilibrar_quantidade().setSelected(true);
        resetTabela();
    }

    void enable(boolean enable) {
        view.getNome_da_prova_txt().setEnabled(enable);
        view.getQuantidade_provas_num().setEnabled(enable);
        view.getQuantidade_questoes_num().setEnabled(enable);
        view.getQuestao_aparecer_chk().setEnabled(enable);
        view.getCabecalho_existente_radio().setEnabled(enable);
        view.getCabecalho_select().setEnabled(enable);
        view.getCabecalho_txt().setEnabled(view.getCabecalho_personalizado_radio().isSelected() && enable);
        view.getTabela().setEnabled(enable);
        view.getLimpar_btn().setEnabled(enable);
        view.getCriar_btn().setEnabled(enable);
        view.getCabecalho_personalizado_radio().setEnabled(enable);
        view.getImportar_btn().setEnabled(enable);
        view.getEquilibrar_quantidade().setEnabled(enable);
        view.getSalvar_btn().setEnabled(enable && verificarSePodeSalvar());
    }

    boolean verificarSePodeSalvar() {
        return provas_geradas.size() == (int) view.getQuantidade_provas_num().getValue();
    }

    public GerarView getView() {
        return view;
    }

    boolean verificar() {

        int prov_a_gerar = (int) view.getQuantidade_provas_num().getValue();
        int qst_a_gerar = (int) view.getQuantidade_questoes_num().getValue();

        if (prov_a_gerar < 1 || qst_a_gerar < 1) {
            JOptionPane.showMessageDialog(view, "Numero de provas e questões deve ser maior que zero");
            return false;
        }
        int num_qst_sel = getNumQstSelecionadas();

        if (num_qst_sel < 1) {
            JOptionPane.showMessageDialog(view, "Selecione pelo menos " + qst_a_gerar + " na tabela.");
            return false;
        }

        if (num_qst_sel < qst_a_gerar) {
            JOptionPane.showMessageDialog(view, "Numero de questões selecionada na tabela"
                    + " insuficiente para gerar " + qst_a_gerar + " questões por prova");
            return false;
        }

        int a_gerar = prov_a_gerar * qst_a_gerar;
        if ((a_gerar > num_qst_sel)
                && !view.getQuestao_aparecer_chk().isSelected()) {
            JOptionPane.showMessageDialog(view, "Habilite repetir questões");
            return false;
        }

        int cab = view.getCabecalho_select().getSelectedIndex();

        if (view.getCabecalho_existente_radio().isSelected() && cab < 0) {
            JOptionPane.showMessageDialog(view, "Selecione um cabeçalho existente");
            return false;
        }

        return true;
    }

    void criar() {
        if (!verificar()) {
            return;
        }
        try {

            view.getLog().setText("gerando provas ...\n");
            enable(false);

            String nome = view.getNome_da_prova_txt().getText().replaceAll("[^a-zA-Z0-9]", "_");
            Prova p = bancoDeProvas.getProvaByName(nome);
            if (p != null) {
                if (Janela.showDialogisYes(view, "Já existe uma prova com esse nome", "Deseja excluir a prova existente e gerar outra agora?")) {
                    p.getFile().delete();
                }
                enable(true);
                return;
            }

            boolean repet = view.getQuestao_aparecer_chk().isSelected();

            HashMap<String, Vetor[]> divisao_das_questoes = new HashMap<>();

            for (String tipo : tipos_de_provas) {
                int qtd = bancoDeProvas.getNumQuestaoOfTipo(tipo);
                Vetor[] vs = new Vetor[qtd];
                for (int i = 0; i < qtd; i++) {
                    vs[i] = new Vetor();
                }
                divisao_das_questoes.put(tipo, vs);
            }

            int prov_a_gerar = (int) view.getQuantidade_provas_num().getValue();
            int qst_a_gerar = (int) view.getQuantidade_questoes_num().getValue();

            provas_geradas.clear();

            for (int prov = 0; prov < prov_a_gerar; prov++) {

                int ja_add = 0;
                ///garantir o minimo a gerar
                for (int i = 0; i < model.getRowCount(); i++) {
                    String tipo = (String) model.getValueAt(i, 0);
                    int qst = bancoDeProvas.getNumQuestaoOfTipo(tipo);
                    Vetor[] vetor = divisao_das_questoes.get(tipo);
                    int min_dessa_p = Math.min(qst, Math.max(0, (int) model.getValueAt(i, 1)));
                    if (min_dessa_p > qst) {
                        throw new Exception("erro 0");
                    }
                    for (int j = 0; j < min_dessa_p; j++) {
                        boolean tem_disp = false;
                        for (Vetor vet : vetor) {
                            if (!vet.ja_tem_prova()) {
                                tem_disp = true;
                            }
                        }
                        if (!tem_disp && !repet) {
                            throw new Exception("erro 1");
                        }

                        boolean add_qst = false;
                        int count = 1 + vetor.length;
                        int pos_aleat = Singleton.getInstance().getNextRandomInt(vetor.length);

                        while (!add_qst && count-- > 0) {
                            if (vetor[pos_aleat].ja_tem_prova() && !repet || vetor[pos_aleat].provas.contains(prov)) {
                                pos_aleat = (pos_aleat + 1) % vetor.length;
                            } else {
                                vetor[pos_aleat].provas.add(prov);
                                add_qst = true;
                                ja_add++;
                            }
                        }

                        if (!add_qst) {
                            throw new Exception("erro 2 qst:" + count);
                        }
                    }
                }

                int prova = 0;
                for (int qst = ja_add; qst < qst_a_gerar; qst++) {
                    if (!view.getEquilibrar_quantidade().isSelected()) {
                        prova += Singleton.getInstance().getNextRandomInt(tipos_de_provas.length);
                    }
                    // adicionar questoes ausentes respeitando REPET e LIMIT por prova
                    Vetor[] questoes;
                    boolean valido;
                    do {
                        valido = false;
                        int tipo_index = prova++ % tipos_de_provas.length;
                        String tipo = tipos_de_provas[tipo_index];
                        questoes = divisao_das_questoes.get(tipo);
                        int ja_tem = 0;

                        ArrayList<Vetor> disp = new ArrayList<>();

                        for (Vetor quest : questoes) {
                            if (quest.provas.contains(prov)) {
                                ja_tem++;
                            } else {
                                disp.add(quest);
                            }
                        }
                        if (ja_tem < Math.min(bancoDeProvas.getNumQuestaoOfTipo(tipo), (int) model.getValueAt(tipo_index, 2))) {
                            /// quantidade de questoes ainda nao chegou ao limit superior dessa prova
                            int sel = Singleton.getInstance().getNextRandomInt(questoes.length);

                            if ((questoes[sel].ja_tem_prova() && !repet) || questoes[sel].provas.contains(prov)) {
                                /// tentar outra questao
                                int min = 100000;
                                Vetor qstao = null;
                                for (Vetor vetor : disp) {
                                    /// pegar questao q foi add menor numero de provas
                                    if (vetor.ja_tem_prova() && !repet) {
                                    } else {
                                        if (vetor.provas.size() < min) {
                                            qstao = vetor;
                                        }
                                    }
                                }
                                if (qstao == null) {
                                    throw new Exception("erro 5 qst:" + ja_add);
                                }
                                qstao.provas.add(prov);
                            } else {
                                questoes[sel].provas.add(prov);
                            }

                            ja_add++;
                        }
                    } while (!valido && prova < 10000 && qst == ja_add);

                    if (!valido && prova > 999) {
                        throw new Exception("erro 4 qst:" + ja_add);
                    }

                }
                prova++;

                Prova prova_gerada = new Prova(new File(workDir.getProperties().getPROVAS_DIR() + "/"
                        + nome), -1 * (provas_geradas.size() + 1));
                prova_gerada.setNome(view.getNome_da_prova_txt().getText());
                prova_gerada.setQuantidade_de_sub_provas(prov_a_gerar);
                prova_gerada.setCabecalhoAsFile(view.getCabecalho_existente_radio().isSelected());
                prova_gerada.setCabecalho(view.getCabecalho_existente_radio().isSelected()
                        ? (String) view.getCabecalho_select().getSelectedItem()
                        : view.getCabecalho_txt().getText()
                );

                for (String tipo_de_prova : divisao_das_questoes.keySet()) {
                    ArrayList<Integer> qst = new ArrayList<>();

                    Vetor[] vet = divisao_das_questoes.get(tipo_de_prova);

                    for (int i = 0; i < vet.length; i++) {
                        if (vet[i].provas.contains(prov)) {
                            /// o numero da quetão é base 1
                            qst.add(i + 1);
                        }
                    }

                    Integer[] shuff = new Integer[qst.size()];
                    for (int i = 0; i < shuff.length; i++) {
                        Integer rm = qst.get(Singleton.getInstance().getNextRandomInt(qst.size()));
                        shuff[i] = rm;
                        qst.remove(rm);
                    }

                    for (int i = 0; i < model.getRowCount(); i++) {
                        if (model.getValueAt(i, 0).equals(tipo_de_prova)) {
                            prova_gerada.addQuestoes(
                                    tipo_de_prova,
                                    shuff,
                                    (int) model.getValueAt(i, 3));
                        }
                    }
                }
                provas_geradas.add(prova_gerada);
            }

            view.getLog().setText("");
            provas_geradas.forEach((prova_gerada) -> {
                view.getLog().setText(view.getLog().getText() + prova_gerada.toString() + "\n");
            });

            view.getSalvar_btn().setEnabled(verificarSePodeSalvar());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Houve um erro: " + ex);
            Logger.getLogger(GerarPresenter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            enable(true);
        }

    }

    private void resetTabela() {
        model.setNumRows(0);
        int row = 0;
        for (String tipo : tipos_de_provas) {
            model.addRow(new Object[]{
                tipo,
                0,
                bancoDeProvas.getNumQuestaoOfTipo(tipo),
                ++row
            });
        }

    }

    private int getNumQstSelecionadas() {
        int rows = model.getRowCount();
        int cont = 0;
        for (int i = 0; i < rows; i++) {
            int ate = Math.max(0, Math.min(bancoDeProvas.getNumQuestaoOfTipo((String) model.getValueAt(i, 0)), (int) model.getValueAt(i, 2)));
            int pelo_menos = Math.max(0, Math.min(bancoDeProvas.getNumQuestaoOfTipo((String) model.getValueAt(i, 0)), (int) model.getValueAt(i, 1)));
            cont += Math.max(ate, pelo_menos);
        }
        return cont;
    }

    private void salvar() {
        String[] strs = view.getLog().getText().split("\n");
        view.getLog().setText("Salvando provas ...");
        boolean falhou = false;
        File local = null;
        for (Prova prova : provas_geradas) {
            local = new File(prova.getFile().getAbsolutePath());
            try {
                prova.salvar();
            } catch (IOException ex) {
                falhou = true;
                view.getLog().setText("Ocorreu um erro ao salvar a prova " + prova.getSub_prova() + "\n" + ex);
                Logger.getLogger(GerarPresenter.class.getName()).log(Level.SEVERE, null, ex);
                break;
            }
        }

        if (!falhou) {
            view.getLog()
                    .setText("Todas provas foram salvas com sucesso...");
            try {
                Prova prova = new Prova(local);
                for (Prova sub_prova : prova.getSub_provas()) {
                    view.getLog()
                            .setText(view.getLog().getText() + "\n" + sub_prova.toString());
                    if (!strs[sub_prova.getSub_prova() - 1].equals(sub_prova.toString())) {
                        throw new Exception("Prova " + strs[sub_prova.getSub_prova()] + " != " + sub_prova.toString());
                    }
                }
            } catch (Exception ex) {
                falhou = true;
                Logger.getLogger(GerarPresenter.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (!falhou) {
                view.getLog()
                        .setText("Todas provas geradas foram importadas com sucesso...");
            } else if (local != null) {
                local.delete();
                view.getLog()
                        .setText("A prova foi excluída porque não pode ser importada ...");
            }
        } else if (local != null) {
            local.delete();
            view.getLog()
                    .setText("A prova foi excluída porque não pode ser salva ...");
        }
    }

}
