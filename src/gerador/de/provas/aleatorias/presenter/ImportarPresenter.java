/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias.presenter;

import gerador.de.provas.aleatorias.model.importar.Marcador;
import gerador.de.provas.aleatorias.model.importar.ModoPagina;
import gerador.de.provas.aleatorias.model.importar.Pagina;
import gerador.de.provas.aleatorias.model.pdf.PDF;
import gerador.de.provas.aleatorias.util.Janela;
import gerador.de.provas.aleatorias.view.ImportarView;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author conta
 */
public class ImportarPresenter {

    private ImportarView view;

    private ArrayList<PDF> arquivos = new ArrayList<>();
    private ArrayList<File> questoes = new ArrayList<>();
    private ArrayList<File> gabaritos = new ArrayList<>();
    private ArrayList<Pagina> paginas = new ArrayList<>();
    private ArrayList<Integer> questao_excluida = new ArrayList<>();
    private Pagina current;

    boolean modo_gabarito_em_outro_arquivo = false;

    public ImportarPresenter() {
        view = new ImportarView();

        view.getFirst_page_nav_button().addActionListener((e) -> {
            first();
        });
        view.getPrevious_page_nav_button().addActionListener((e) -> {
            prev();
        });
        view.getNext_page_nav_button().addActionListener((e) -> {
            next();
        });
        view.getLast_page_nav_button().addActionListener((e) -> {
            last();
        });

        view.getAtual_page_spinner().addChangeListener((e) -> {
            setPageH((int) view.getAtual_page_spinner().getValue());
        });

        view.setVisible(true);

        if (carregarArquivos() < 1) {
            view.dispose();
            return;
        }

        view.getGabarito_no_outro_arquivo().addActionListener((e) -> {
            if (view.getGabarito_no_outro_arquivo().isSelected()) {
                if (modo_em_outro_arquivo_ok(questoes, gabaritos)) {
                    modo_gabarito_em_outro_arquivo = true;
                } else {
                    JOptionPane.showMessageDialog(view,
                            "Para utilizar esta opção você deve inserir quantidade"
                            + " igual de arquivos de provas e gabaritos \n"
                            + "É necessário tambem que o nome do arquivo de prova "
                            + "seja o mesmo nome do arquivo de gabarito terminando com ' gabarito'");
                    view.getGabarito_apos_cada_questao().setSelected(true);
                    modo_gabarito_em_outro_arquivo = false;
                }
            }
        });

        view.getGabarito_apos_cada_questao().addActionListener((e) -> {
            modo_gabarito_em_outro_arquivo = !view.getGabarito_apos_cada_questao().isSelected();
        });

        view.getCarregar_mais_arquivos().addActionListener((e) -> {
            carregarArquivos();
        });

        view.getFolha().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                view.getLog_area().setText(current.getLog());
            }

        });

        view.getAdicionar_marcador().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                view.getAdicionar_marcador().setText("");
            }
        });
        view.getRemover_marcador().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                view.getRemover_marcador().setText("");
            }
        });
        view.getOcultar_regiao_do_marcador_text().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                view.getOcultar_regiao_do_marcador_text().setText("");
            }
        });
        view.getEliminar_questao().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                view.getEliminar_questao().setText("");
            }
        });

        view.getAplicar_btn().addActionListener((e) -> {
            aplicar();
        });

        view.getMarcadores_btn().addActionListener((e) -> {

            if (Janela.showDialogisYes(view, "Remover Marcadores", "Deseja remover TODOS marcadores desta PÁGINA?")) {
                if (current != null) {
                    current.removerTodosMarcadores();
                    if (Janela.showDialogisYes(view, "Remover Marcadores", "Deseja remover TODOS marcadores deste ARQUIVO?")) {
                        current.getPdf().getPages().forEach((t) -> {
                            t.removerTodosMarcadores();
                        });
                        if (Janela.showDialogisYes(view, "Remover Marcadores", "Deseja remover TODOS marcadores DE TODOS ARQUIVO?")) {
                            current.getPdf().getPages().forEach((t) -> {
                                t.removerTodosMarcadores();
                            });
                        }
                    }
                }
                update();
            }
        });
    }

    private int carregarArquivos() {
        JFileChooser fileChooser
                = Janela.fileChooser(view, "Abrir provas", "", "Abrir", new String[]{"arquivos PDF", "pdf"}, true);

        if (fileChooser != null && fileChooser.getSelectedFiles().length > 0) {

            File[] arquivos_importados = fileChooser.getSelectedFiles();

            List<File> todos_arquivos = Arrays.asList(arquivos_importados);
            List<File> arquivos_de_questoes = new ArrayList<>(Arrays.asList(arquivos_importados));
            arquivos_de_questoes.removeIf((t) -> {
                return isGabarito(t);
            });
            List<File> arquivos_de_gabaritos = new ArrayList<>(Arrays.asList(arquivos_importados));
            arquivos_de_gabaritos.removeIf((t) -> {
                return isQuestao(t);
            });

            questoes.addAll(arquivos_de_questoes);
            gabaritos.addAll(arquivos_de_gabaritos);

            if (questoes.size() == gabaritos.size()
                    && Janela.showDialogisYes(view, "Mudar para gbarito em outro arquivo",
                            "As questoes estão em arquivos diferente dos gabaritos?")
                    && modo_em_outro_arquivo_ok(questoes, gabaritos)) {
                modo_gabarito_em_outro_arquivo = true;
            } else {
                modo_gabarito_em_outro_arquivo = false;
            }

            view.setEnabled(false);
            new Thread(() -> {
                for (File arquivo : todos_arquivos) {

                    boolean add = true;

                    for (PDF pdf : arquivos) {
                        if (pdf.getArquivo().equals(arquivo.getAbsolutePath())
                                && !Janela.showDialogisYes(view, "Importar arquivo?", "Este arquivo já foi importado, deseja importar novamente?")) {
                            add = false;
                            break;
                        }
                    }

                    if (!add) {
                        view.setEnabled(true);
                        return;
                    }

                    try {
                        PDF pdf = new PDF(arquivo.getAbsolutePath(), view.getProgressbar(), arquivos.size() + 1, paginas.size());
                        arquivos.add(pdf);
                        paginas.addAll(pdf.getPages());
                        pdf.getPages().forEach((t) -> {
                            t.setQuestao_excluida(questao_excluida);
                        });
                        setModoPagina();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(view, "Impossivel importar arquivo " + arquivo.getName());
                        Logger.getLogger(ImportarPresenter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                view.setEnabled(true);
                first();
            }).start();

        } else {
            return 0;
        }

        return fileChooser.getSelectedFiles().length;
    }

    private boolean isGabarito(File arquivo) {
        return arquivo.getName().endsWith(" gabarito.pdf");
    }

    private boolean isQuestao(File arquivo) {
        return !arquivo.getName().endsWith(" gabarito.pdf");
    }

    private boolean modo_em_outro_arquivo_ok(List<File> arquivos_de_questoes, List<File> arquivos_de_gabaritos) {
        if (arquivos_de_questoes.size() != arquivos_de_gabaritos.size()) {
            JOptionPane.showMessageDialog(view,
                    "Quantidade de arquivos de provas e gabaritos diferente");
            return false;
        } else {
            boolean algum_arquivo_de_prova_NAO_tem_gabarito = false;
            ArrayList<String> gabaritos_certos = new ArrayList<>();
            for (File questao : arquivos_de_questoes) {
                boolean gabarito_correspondente_encontrado = false;
                for (File gabarito : arquivos_de_gabaritos) {
                    if ((questao.getName().replace(".pdf", "") + " gabarito.pdf").equals(gabarito.getName())) {
                        System.out.println(questao.getName() + " => " + gabarito.getName());
                        gabarito_correspondente_encontrado = true;
                        gabaritos_certos.add(gabarito.getName().replace(".pdf", ""));
                        break;
                    }
                }
                if (!gabarito_correspondente_encontrado) {
                    algum_arquivo_de_prova_NAO_tem_gabarito = true;
                }
            }
            if (algum_arquivo_de_prova_NAO_tem_gabarito) {

                List<String> todos_gabaritos = arquivos_de_gabaritos.parallelStream().map((t) -> {
                    return t.getName().replace(".pdf", "");
                }).collect(Collectors.toList());
                todos_gabaritos.removeAll(gabaritos_certos);

                JOptionPane.showMessageDialog(view,
                        "Nome de gabaritos inconsistente com nome do"
                        + " arquivo de provas:\n" + Arrays.toString(todos_gabaritos.toArray(new String[]{})));
                return false;
            }
            return true;
        }
    }

//   #################  FUNÇÕES DE NAVEGAÇÃO NAS PÁGINAS  #####################
    void first() {
        setPage(0);
    }

    void prev() {
        setPage(getPage() - 1);
    }

    void next() {
        setPage(getPage() + 1);
    }

    void last() {
        setPage(10000000);
    }

    void setPage(int i) {
        if (paginas.size() < 1) {
            return;
        }
        current = paginas.get(Math.max(0, Math.min(paginas.size() - 1, i)));
        int p = getPage();
        if (p == 0) {
            view.getFirst_page_nav_button().setEnabled(false);
            view.getPrevious_page_nav_button().setEnabled(false);
        } else {
            view.getFirst_page_nav_button().setEnabled(true);
            view.getPrevious_page_nav_button().setEnabled(true);
        }
        if (p >= (paginas.size() - 1)) {
            view.getNext_page_nav_button().setEnabled(false);
            view.getLast_page_nav_button().setEnabled(false);
        } else {
            view.getNext_page_nav_button().setEnabled(true);
            view.getLast_page_nav_button().setEnabled(true);
        }

        view.getAtual_page_spinner().setValue(getPageH());

        update();
    }

    public void setPageH(int i) {
        setPage(i - 1);
    }

    int getPage() {
        return current != null ? current.getIndex_global() : -1;
    }

    public int getPageH() {
        return getPage() + 1;
    }

    void setModoPagina() {
        if (!modo_gabarito_em_outro_arquivo) {
            paginas.forEach((t) -> {
                t.setModoPagina(ModoPagina.MISTO);
            });
        } else {
            for (File questao : questoes) {
                for (PDF arquivo : arquivos) {
                    if (questao.getAbsolutePath().equals(arquivo.getFile().getAbsolutePath())) {
                        arquivo.setModoPagina(ModoPagina.QUESTAO);
                    }
                }
            }
            for (File gabarito : gabaritos) {
                for (PDF arquivo : arquivos) {
                    if (gabarito.getAbsolutePath().equals(arquivo.getFile().getAbsolutePath())) {
                        arquivo.setModoPagina(ModoPagina.GABARITO);
                    }
                }
            }
        }
    }

    int countQuestions() {
        int cont = 0;
        int cont2 = 0;
        if (!modo_gabarito_em_outro_arquivo) {
            for (Pagina pagina : paginas) {
                cont = Math.max(cont, pagina.setIndex_questao(cont));
            }
        } else {
            for (File questao : questoes) {
                for (PDF arquivo : arquivos) {
                    if (questao.getAbsolutePath().equals(arquivo.getFile().getAbsolutePath())) {
                        PDF questoes_arquivo = arquivo;
                        PDF gabaritos_arquivo = getGabaritoCorrespondente(questoes_arquivo);
                        if (gabaritos_arquivo == null) {
                            JOptionPane.showMessageDialog(view, "ERRO: gabarito correspondente de " + questoes_arquivo.getArquivo() + " não encontrado.");
                        } else {
                            for (Pagina pagina : questoes_arquivo.getPages()) {
                                cont = Math.max(cont, pagina.setIndex_questao(cont));
                            }
                            for (Pagina pagina : gabaritos_arquivo.getPages()) {
                                /// conta para atualizar o numero do gabarito na pagina
                                cont2 = Math.max(cont2, pagina.setIndex_questao(cont2));
                            }
                        }
                    }
                }
            }
        }
        return cont;
    }

    public PDF getGabaritoCorrespondente(PDF questao) {
        for (PDF arquivo : arquivos) {
            if (questao.getFile().getName().replace(".pdf", " gabarito.pdf").equals(arquivo.getFile().getName())) {
                return arquivo;
            }
        }
        return null;
    }

    void update() {
        int numq = countQuestions();
        int cont = 0;

        for (Integer num : questao_excluida) {
            if (num <= numq) {
                cont++;
            }
        }

        view.getNum_questoes_importadas_label().setText((numq - cont) + " questões importadas em todos arquivos");
        view.getLog_area().setText(current.getLog());
        view.getTotal_page_nav_label().setText(" - " + paginas.size());
        view.getPdf_info_lbl().setText(
                current.getPdf().getFile().getName().replace(".pdf", "") + " => "
                + (current.getModoPagina() == ModoPagina.MISTO
                ? "Questão seguida de gabarito"
                : ("Página de " + (current.getModoPagina() == ModoPagina.QUESTAO
                ? "questões" : "gabaritos"))));
        view.getFolha().setPageView(current);
        view.getFolha().repaint();
        view.invalidate();
    }

//   ################# FIM FUNÇÕES DE NAVEGAÇÃO NAS PÁGINAS  #####################
    private void aplicar() {

        if (current == null) {
            return;
        }

        String add = view.getAdicionar_marcador().getText();
        String rem = view.getRemover_marcador().getText();
        String elim = view.getEliminar_questao().getText();
        String ocul = view.getOcultar_regiao_do_marcador_text().getText();

        if (add != null && add.equals("posições y: 356,1030,50")) {
            add = null;
        }
        if (rem != null && rem.equals("nomes: M1,M2,M3")) {
            rem = null;
        }
        if (elim != null && elim.equals("numeros: 1,2,3,4")) {
            elim = null;
        }
        if (ocul != null && ocul.equals("nomes: M1,M3,M5")) {
            ocul = null;
        }

        if (rem != null && !rem.isEmpty()) {
            for (String m : rem.split(",")) {
                if (m != null && !m.isEmpty()) {
                    m = m.trim();
                    Marcador marcador = current.getMarcadorByID(m);

                    if (m == null) {
                        JOptionPane.showMessageDialog(view, "Marcador inexistente nessa página: " + m);
                    } else {
                        current.removeMarcador(marcador);
                    }

                }
                view.getRemover_marcador().setText("");
            }
        }

        if (ocul != null && !ocul.isEmpty()) {
            for (String m : ocul.split(",")) {
                if (m != null && !m.isEmpty()) {
                    m = m.trim();
                    Marcador marcador = current.getMarcadorByID(m);

                    if (m == null) {
                        JOptionPane.showMessageDialog(view, "Marcador inexistente nessa página: " + m);
                    } else {
                        marcador.getArea().ocultar();
                    }

                }
                view.getOcultar_regiao_do_marcador_text().setText("");
            }
        }

        if (add != null && !add.isEmpty()) {
            for (String y : add.split(",")) {
                if (y != null && !y.isEmpty()) {
                    y = y.trim();
                    try {
                        int pos = Integer.parseInt(y);
                        current.addMarcador(pos);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(view, "Numero de Y invalido para marcador: " + y + " => marcador não adicionado");
                    }
                }
            }
            view.getAdicionar_marcador().setText("");
        }
        if (elim != null && !elim.isEmpty()) {
            for (String y : elim.split(",")) {
                if (y != null && !y.isEmpty()) {
                    y = y.trim();
                    try {
                        questao_excluida.add(Integer.parseInt(y));
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(view, "Numero da Questão invalido: " + y);
                    }
                }
            }
            view.getEliminar_questao().setText("");
        }

        update();
    }
}
