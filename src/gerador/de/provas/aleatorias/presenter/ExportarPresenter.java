/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias.presenter;

import gerador.de.provas.aleatorias.model.BancoDeProvas;
import gerador.de.provas.aleatorias.model.Prova;
import gerador.de.provas.aleatorias.model.WorkDir;
import gerador.de.provas.aleatorias.model.exportar.IObservable;
import gerador.de.provas.aleatorias.model.exportar.PaginaSubProva;
import gerador.de.provas.aleatorias.model.exportar.SubProva;
import gerador.de.provas.aleatorias.model.pdf.PDF;
import gerador.de.provas.aleatorias.util.Janela;
import gerador.de.provas.aleatorias.view.ExportarView;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 *
 * @author conta
 */
enum Font {
    TIMES,
    TIMES_BOLD,
    HELVETICA,
    HELVETICA_BOLD,
    COURIER,
    COURIER_BOLD
}

class Salvar {

    SubProva[] subProvas;
    String titulo;
    boolean titulo_is_file;

    boolean save_prova;
    boolean save_gabarito;
    boolean agrupar;
    boolean arquivo_unico;
    boolean paginar;
    String texto_quest;
    String testo_gab;
    int margem_cab;
    int margem_quest;
    Font font;
    int font_size;

    String out;
    String tmp_dir;
    JProgressBar progressBar;

    public Salvar(
            SubProva[] subProvas,
            String titulo,
            boolean titulo_is_file,
            boolean save_prova,
            boolean save_gabarito,
            boolean agrupar,
            boolean arquivo_unico,
            boolean paginar,
            String texto_quest,
            String testo_gab,
            int margem_cab,
            int margem_quest,
            Font font,
            int font_size,
            String out,
            String tmp_dir,
            JProgressBar progressBar) {
        this.subProvas = subProvas;
        this.titulo = titulo;
        this.titulo_is_file = titulo_is_file;
        this.save_prova = save_prova;
        this.save_gabarito = save_gabarito;
        this.agrupar = agrupar;
        this.arquivo_unico = arquivo_unico;
        this.paginar = paginar;
        this.texto_quest = texto_quest;
        this.testo_gab = testo_gab;
        this.margem_cab = margem_cab;
        this.margem_quest = margem_quest;
        this.font = font;
        this.font_size = font_size;
        this.out = out;
        this.tmp_dir = tmp_dir;
        this.progressBar = progressBar;
    }

}

public class ExportarPresenter implements IObservable {

    ExportarView view;
    WorkDir workDir;
    BancoDeProvas bancoDeProvas;
    Prova[] provas = null;
    Prova prova = null;
    SubProva[] subProvas = null;
    DefaultListModel model = new DefaultListModel<>();
    PaginaSubProva paginaSubProva;
    int totalPages = 0;
    int totalPagesProva = 0;
    int totalPagesGabarito = 0;
    int currentPage = 0;
    int currentSubprova = 0;
    boolean salvando = false;
    String local_to_save;

    ExportarPresenter(WorkDir workDir, BancoDeProvas bancoDeProvas) {
        this.workDir = workDir;
        this.bancoDeProvas = bancoDeProvas;
        view = new ExportarView();
        setEnabled(false);
        view.setVisible(true);

        paginaSubProva = view.getPaginaSubProva();

        try {
            provas = bancoDeProvas.getProvasGeradas(view.getProgressbar());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Houve falahas ao carregar as provas.");
            Logger.getLogger(ExportarPresenter.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (provas == null || provas.length < 0) {
            JOptionPane.showMessageDialog(view, "Não há provas disponíveis, gere-as primeiro.");
            view.dispose();
            return;
        }

        for (Prova prova : provas) {
            model.addElement(prova.getNome());
        }

        view.getProva_list().setModel(model);
        view.getProva_list().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        view.getCarregar_btn().addActionListener((e) -> {
            carregar();
        });

        view.getTipo_nav_spn().addChangeListener((e) -> {
            setSubProvaHuman((int) view.getTipo_nav_spn().getValue());
        });

        view.getFirst_nav_btn().addActionListener((e) -> {
            first();
        });
        view.getPrev_nav_btn().addActionListener((e) -> {
            prev();
        });
        view.getNext_nav_btn().addActionListener((e) -> {
            next();
        });
        view.getLast_nav_btn().addActionListener((e) -> {
            last();
        });

        view.getEditar_btn().addActionListener((e) -> {
            editar();
        });

        view.getSalvar_subprova_btn().addActionListener((e) -> {
            salvarSubprova(subProvas[currentSubprova]);
        });

        view.getSalvar_btn().addActionListener((e) -> {
            salvar(subProvas);
        });

        local_to_save = workDir.getProperties().getSAVE_FILES();

        updateProva();
    }

    public ExportarView getView() {
        return view;
    }

    void setEnabled(boolean modo) {
        setEnabledSalvarSePuder(modo);
        setEnabledToolbarSePuder(modo);
        setEnabledEditSePuder(modo);
        setEnabledCarregarSePuder(modo);
    }

    void setEnabledCarregarSePuder(boolean modo) {
        modo = modo && subProvas == null;
        view.getProva_list().setEnabled(modo);
        view.getCarregar_btn().setEnabled(modo);
    }

    void setEnabledSalvarSePuder(boolean modo) {
        modo = modo && !salvando && subProvas != null && totalPages > 0;
        view.getEditar_btn().setEnabled(modo);
        view.getGerar_em_arquivo_unico_chk().setEnabled(modo);
        view.getSalvar_subprova_btn().setEnabled(modo);
        view.getSalvar_btn().setEnabled(modo);
    }

    void setEnabledToolbarSePuder(boolean modo) {
        modo = modo && subProvas != null;
        view.getFirst_nav_btn().setEnabled(modo);
        view.getPrev_nav_btn().setEnabled(modo);
        view.getNext_nav_btn().setEnabled(modo);
        view.getLast_nav_btn().setEnabled(modo);
        view.getTipo_nav_spn().setEnabled(modo);
    }

    void setEnabledEditSePuder(boolean modo) {
        modo = modo && subProvas == null;
        view.getText_questao_txt().setEnabled(modo);
        view.getText_gabarito_txt().setEnabled(modo);
        view.getMargem_cab_spn().setEnabled(modo);
        view.getMargem_questao_spn().setEnabled(modo);
        view.getFont_select().setEnabled(modo);
        view.getFont_size_spn().setEnabled(modo);
        view.getGerar_prova_chk().setEnabled(modo);
        view.getGerar_gabarito_chk().setEnabled(modo);
        view.getPaginar_chk().setEnabled(modo);
        view.getAgrupar_chk().setEnabled(modo);
        view.getCab_no_gab_chk().setEnabled(modo);
        view.getNegrito_btn().setEnabled(modo);
        for (Component component : view.getConfiguracoes_pnl()) {
            component.setEnabled(modo);
        }
    }

    private void carregar() {
        String val = view.getProva_list().getSelectedValue();
        if (val != null && !val.isEmpty()) {
            for (Prova prova : provas) {
                if (prova.getNome().equals(val)) {
                    this.prova = prova;
                }
            }
        }
        updateProva();
    }

    private void updateProva() {
        if (prova != null) {
            Prova[] sub_provas = prova.getSub_provas();
            if (sub_provas.length > 0) {
                view.getTipo_nav_spn().setModel(new SpinnerNumberModel(1, 1, sub_provas.length, 1));
                subProvas = Arrays.asList(sub_provas).stream().map((t) -> {
                    return new SubProva(t, bancoDeProvas,
                            view.getPaginar_chk().isSelected(), getFont(), (int) view.getFont_size_spn().getValue(),
                            view.getProgressbar(),
                            new String[]{view.getText_questao_txt().getText(), view.getText_gabarito_txt().getText()},
                            this,
                            view.getCab_no_gab_chk().isSelected(),
                            (int) view.getMargem_cab_spn().getValue(),
                            (int) view.getMargem_questao_spn().getValue());
                }).collect(Collectors.toList()).toArray(new SubProva[]{});
                for (SubProva subprova : subProvas) {
                    subprova.load(view.getGerar_prova_chk().isSelected(), view.getGerar_gabarito_chk().isSelected());
                }
            } else {
                JOptionPane.showMessageDialog(view, "Esta prova não contem subprovas, utilize outra.");
                prova = null;
            }
        }
        updateView();
    }

    PDType1Font getFont() {
        switch (view.getFont_select().getSelectedIndex()) {
            case 0:
                return view.getNegrito_btn().isSelected() ? PDType1Font.TIMES_BOLD : PDType1Font.TIMES_ROMAN;
            case 1:
                return view.getNegrito_btn().isSelected() ? PDType1Font.HELVETICA : PDType1Font.HELVETICA_BOLD;
            case 2:
                return view.getNegrito_btn().isSelected() ? PDType1Font.COURIER : PDType1Font.COURIER_BOLD;
        }
        return null;
    }

    void resetNav() {
        currentPage = 0;
        currentSubprova = 0;
        updateNav();
        view.getTipo_nav_spn().setValue(currentSubprova + 1);
    }

    void updateView() {
        resetNav();
        if (prova != null && subProvas != null && subProvas.length > 0) {
            totalPages = 0;
            totalPagesProva = 0;
            totalPagesGabarito = 0;
            for (SubProva subProva : subProvas) {
                subProva.setIndex_agrupado(totalPages);
                subProva.setIndex_prova(totalPagesProva);
                subProva.setIndex_gabarito(totalPagesGabarito);
                int numPagesProva = subProva.getNumPagesProva();
                int numPagesGabarito = subProva.getNumPagesGabarito();
                totalPages += (numPagesProva + numPagesGabarito);
                totalPagesProva += numPagesProva;
                totalPagesGabarito += numPagesGabarito;
            }
            for (SubProva subProva : subProvas) {
                subProva.setIndex_gabarito(totalPagesProva + subProva.getIndex_gabarito());
            }
        }
        updateNav();
    }

//// view NAVIGATOR
    void updateNav() {
        if (prova != null && subProvas != null && subProvas.length > 0) {
            boolean agrupado = view.getAgrupar_chk().isSelected();
            view.getPrev_nav_btn().setEnabled(currentPage > 0);
            view.getFirst_nav_btn().setEnabled(currentPage > 0);
            view.getNext_nav_btn().setEnabled(currentPage < totalPages - 1);
            view.getLast_nav_btn().setEnabled(currentPage < totalPages - 1);
            view.getPage_nav_lbl().setText("   " + (currentPage + 1) + " de " + totalPages + "   ");

            int old_c = currentSubprova;
            for (int i = 0; i < subProvas.length; i++) {
                if (subProvas[i].isMyPage(currentPage, agrupado, currentPage < totalPagesProva)) {
                    currentSubprova = i;
                    break;
                }
            }

            if (old_c != currentSubprova) {
                view.getTipo_nav_spn().setValue(currentSubprova + 1);
            }

            SubProva cs = subProvas[currentSubprova];
            //verificar se a pagina atual é de questao ou gabarito
            boolean qst = agrupado ? cs.inProva(currentPage) : currentPage < totalPagesProva;
            if (qst) {
                if (view.getGerar_prova_chk().isSelected()) {
                    BufferedImage renderedPage = cs.getRenderedPage(qst, currentPage, agrupado, qst);
                    paginaSubProva.setText(renderedPage == null ? "Carregando ..." : "");
                    paginaSubProva.setImage(renderedPage);
                } else {
                    paginaSubProva.setImage(null);
                    paginaSubProva.setText("Pagina de prova não será gerada.");
                }
            } else {
                if (view.getGerar_gabarito_chk().isSelected()) {
                    BufferedImage renderedPage = cs.getRenderedPage(qst, currentPage, agrupado, qst);
                    paginaSubProva.setText(renderedPage == null ? "Carregando ..." : "");
                    paginaSubProva.setImage(renderedPage);
                } else {
                    paginaSubProva.setImage(null);
                    paginaSubProva.setText("Pagina de gabarito não será gerada.");
                }
            }

            view.getFolha_lbl().setText(cs.show(currentPage, agrupado, currentPage < totalPagesProva));
        } else {
            paginaSubProva.setImage(null);
            paginaSubProva.setText("Selecione um tipo de prova ...");
            view.getPage_nav_lbl().setText("   ");
        }
        paginaSubProva.repaint();
        paginaSubProva.invalidate();
        setEnabled(true);
    }

    void first() {
        currentPage = 0;
        updateNav();
    }

    void prev() {
        currentPage = Math.max(0, currentPage - 1);
        updateNav();
    }

    void next() {
        currentPage = Math.min(currentPage + 1, totalPages - 1);
        updateNav();
    }

    void last() {
        currentPage = totalPages - 1;
        updateNav();
    }

    void setSubProvaHuman(int index) {
        if (prova != null && subProvas != null && subProvas.length > 0) {
            if (index > 0 && index <= subProvas.length) {
                currentSubprova = index - 1;
                currentPage = subProvas[index - 1].getFirstPage(
                        view.getAgrupar_chk().isSelected(),
                        currentPage < totalPagesProva);
                updateNav();
            }
        }
    }

//// view NAVIGATOR
    File[] preSave(SubProva[] subps) throws IOException {
        salvando = true;
        setEnabled(false);
        String tmpDir = bancoDeProvas.getTmpDir();
        ArrayList<File> pdfs = new ArrayList<>();

        if (view.getAgrupar_chk().isSelected()) {
            ///juntar cada tipo

            for (SubProva subp : subps) {
                File f1 = subp.getFileProva();
                File f2 = subp.getFileGabarito();
                if (f1 != null || f2 != null) {
                    pdfs.add((f1 == null || f2 == null)
                            ? (f1 == null ? f2 : f1)
                            : PDF.mergePDFs(new File[]{f1, f2},
                            tmpDir + "/" + prova.getCanonicalNome() + " " + (subp.human_ID()) + ".pdf"));
                }
            }

        } else {
            ///jutar em dois: provas e gab

            ArrayList<File> tmp = new ArrayList<>();

            for (SubProva subp : subps) {
                File f = subp.getFileProva();
                if (f != null) {
                    tmp.add(f);
                }
            }

            if (tmp.size() > 0) {
                pdfs.add(PDF.mergePDFs(tmp.toArray(new File[]{}),
                        tmpDir + "/" + prova.getCanonicalNome() + " provas.pdf"));
                tmp.clear();
            }

            for (SubProva subp : subps) {
                File f = subp.getFileGabarito();
                if (f != null) {
                    tmp.add(f);
                }
            }

            if (tmp.size() > 0) {
                pdfs.add(PDF.mergePDFs(tmp.toArray(new File[]{}),
                        tmpDir + "/" + prova.getCanonicalNome() + " gabaritos.pdf"));
                tmp.clear();
            }
        }
        if (view.getGerar_em_arquivo_unico_chk().isSelected()) {
            ///juntar os anteriores
            File single = PDF.mergePDFs(pdfs.toArray(new File[]{}),
                    tmpDir + "/" + prova.getCanonicalNome() + " provas e gabaritos.pdf");
            pdfs.clear();
            pdfs.add(single);
        }
        return pdfs.toArray(new File[]{});
    }

    void posSave(File[] files, String dir) {
        try {
            salvando = false;
            setEnabled(true);
            File path = new File(dir);
            if (!path.exists()) {
                Files.createDirectory(path.toPath());
            }
            for (File file : files) {
                Files.copy(
                        file.toPath(),
                        new File(path.getAbsolutePath() + "/" + file.getName()).toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException ex) {
            Logger.getLogger(ExportarPresenter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void salvar(SubProva[] subps) {
        try {
            File[] preSave = preSave(subps);
            String local = Janela.openFolder(view, "Diretório para salvar os arquivos", local_to_save);

            local = ((local != null) && (!local.isEmpty())) ? local : local_to_save;

            if (local.equals(local_to_save)) {
                try {
                    workDir.getProperties().setSAVE_FILES(local, view);
                } catch (Exception ex) {
                    Logger.getLogger(ExportarPresenter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            local_to_save = local;
            posSave(preSave, local);
        } catch (IOException ex) {
            Logger.getLogger(ExportarPresenter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void salvarSubprova(SubProva subProva) {
        salvar(new SubProva[]{subProva});
    }

    @Override
    public void update(int page) {
        if (page >= 0) {
            this.currentPage = page;
            this.updateNav();
        } else {
            updateView();
        }
    }

    private void editar() {
        prova = null;
        subProvas = null;
        totalPages = 0;
        totalPagesProva = 0;
        totalPagesGabarito = 0;
        currentPage = 0;
        currentSubprova = 0;
        salvando = false;
        updateView();
    }

}
