/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias.model.exportar;

import gerador.de.provas.aleatorias.model.BancoDeProvas;
import gerador.de.provas.aleatorias.model.Prova;
import gerador.de.provas.aleatorias.model.pdf.MultiPDF;
import gerador.de.provas.aleatorias.model.pdf.PDF;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 *
 * @author conta
 */
class RenderPDF {

    String tmp_dir;
    Prova prova;
    boolean gabarito;
    String file;
    MultiPDF multiPDF;
    File cabecalho;
    BancoDeProvas bancoDeProvas;
    int pages;
    int tipo;

    public RenderPDF(
            BancoDeProvas bancoDeProvas,
            Prova prova,
            boolean gabarito,
            boolean paginar,
            PDType1Font font,
            int font_size,
            JProgressBar progressBar,
            String pattern[],
            boolean show_cabecalho,
            int margem_top,
            int margem_quest) throws Exception {
        this.bancoDeProvas = bancoDeProvas;
        this.tmp_dir = bancoDeProvas.getTmpDir();
        this.prova = prova;
        this.gabarito = gabarito;
        this.file = tmp_dir + "/" + (gabarito ? "gabarito.pdf" : "prova.pdf");
        this.multiPDF = new MultiPDF(file);

        progressBar.setValue(0);
        int pagina = 1;

        if (!gabarito || show_cabecalho) {
            if (prova.isCabecalho_is_file()) {
                this.cabecalho = bancoDeProvas.getCabecalho(prova.getCabecalho());
            } else if (PDF.createTextPDF(
                    tmp_dir + "/cabecalho.pdf",
                    prova.getCabecalho().split("\n"),
                    20,
                    font,
                    font_size,
                    true)) {
                this.cabecalho = new File(tmp_dir + "/cabecalho.pdf");
            }

            progressBar.setValue(5);

            if (!multiPDF.addSeCouber(cabecalho.getAbsolutePath(), margem_top)) {
                throw new Exception("Não cabe o cabecalho na página!");
            }
        } else {
            multiPDF.addWhiteSpace(margem_top);
        }

        progressBar.setValue(10);
        tipo = prova.getSub_prova();

        if (!PDF.createTextPDF(
                tmp_dir + "/tipo-pag-" + pagina + ".pdf",
                new String[]{"Tipo dest" + (gabarito ? "e gabarito" : "a prova") + ": " + tipo
                    + (paginar ? ("             PAGINA " + pagina) : "")},
                20,
                font,
                font_size,
                true) || !multiPDF.addSeCouber(tmp_dir + "/tipo-pag-" + pagina + ".pdf", 1)) {
            throw new Exception("Não cabe o tipo na página!");
        }

        progressBar.setValue(20);

        int total = 0;

        total = prova.getQuestoes().entrySet().stream()
                .map((entry) -> entry.getValue().length)
                .reduce(total, Integer::sum);

        int perc = 80 / total;
        int cont = 1;
        for (Map.Entry<String, Integer[]> entry : prova.getQuestoes().entrySet()) {
            for (Integer quest : entry.getValue()) {
                String text = (gabarito ? pattern[1] : pattern[0]).replace("#", "" + cont);
                String local = tmp_dir + "/titulo" + cont++ + ".pdf";
                if (!PDF.createTextPDF(
                        local,
                        new String[]{text},
                        20,
                        font,
                        font_size,
                        true)) {
                    throw new Exception("Impossivel criar texto: " + text);
                }
                File questao = gabarito
                        ? bancoDeProvas.getGabarito(entry.getKey(), quest)
                        : bancoDeProvas.getQuestao(entry.getKey(), quest);

                if (multiPDF.cabe_na_pagina(new String[]{local, questao.getAbsolutePath()}, 1 + margem_quest)) {
                    if (!multiPDF.addSeCouber(local, margem_quest) || !multiPDF.addSeCouber(questao.getAbsolutePath(), 1)) {
                        throw new Exception("não coube titulo e data " + entry.getKey() + " - " + quest);
                    }
                } else {
                    multiPDF.addPage();
                    pagina++;
                    if (paginar && (!PDF.createTextPDF(
                            tmp_dir + "/tipo-pag-" + pagina + ".pdf",
                            new String[]{"Tipo desta prova: " + prova.getSub_prova()
                                + "             PAGINA " + pagina},
                            20,
                            font,
                            font_size,
                            true) || !multiPDF.addSeCouber(tmp_dir + "/tipo-pag-" + pagina + ".pdf", margem_top))) {
                        throw new Exception("Não cabe o tipo na página " + pagina + " !"
                        );
                    }
                    if (!multiPDF.addSeCouber(local, margem_quest) || !multiPDF.addSeCouber(questao.getAbsolutePath(), 1)) {
                        throw new Exception("não coube titulo e data " + entry.getKey() + " - " + quest);
                    }
                }
            }
            progressBar.setValue(20 + (perc * (cont - 1)));
        }
        multiPDF.save(true);
        pages = pagina;
        progressBar.setValue(100);
    }

    File getPDF() {
        return multiPDF.getOutFile();
    }

    String getTmp_dir() {
        return tmp_dir;
    }

    int getPages() {
        return pages;
    }

}

public class SubProva {

    Prova prova;
    BancoDeProvas bancoDeProvas;
    HashMap<String, Integer[]> questoes;
//    ArrayList<File> qs = new ArrayList<>();
//    ArrayList<File> gs = new ArrayList<>();
    File cabecalho_f;
    String cabecalho_s;
    BufferedImage[] provas = null;
    BufferedImage[] gabaritos = null;
    int index_prova;
    int index_gabarito;
    int index_agrupado;
    RenderPDF questao, gabarito;
    String[] tituloPattern;
    boolean load_q, load_g;

    boolean paginar;
    PDType1Font font;
    int font_size;
    JProgressBar progressBar;

    IObservable observable;

    boolean show_cab;
    int margem_top, margem_quest;

    public SubProva(
            Prova prova,
            BancoDeProvas bancoDeProvas,
            boolean paginar,
            PDType1Font font,
            int font_size,
            JProgressBar progressBar,
            String pattern[],
            IObservable observable,
            boolean show_cab,
            int margem_top,
            int margem_quest) {
        this.prova = prova;
        this.bancoDeProvas = bancoDeProvas;
        questoes = prova.getQuestoes();
        this.tituloPattern = pattern;
        this.observable = observable;

        this.paginar = paginar;
        this.font = font;
        this.font_size = font_size;
        this.progressBar = progressBar;
        this.show_cab = show_cab;

        load_q = load_g = false;
        this.margem_top = margem_top;
        this.margem_quest = margem_quest;

//        for (Map.Entry<String, Integer[]> entry : questoes.entrySet()) {
//            for (Integer qst : entry.getValue()) {
//                qs.add(bancoDeProvas.getQuestao(entry.getKey(), qst));
//                gs.add(bancoDeProvas.getGabarito(entry.getKey(), qst));
//            }
//        }
        if (prova.isCabecalho_is_file()) {
            cabecalho_f = bancoDeProvas.getCabecalho(prova.getCabecalho());
        } else {
            cabecalho_s = prova.getCabecalho();
        }
    }

    public BufferedImage getRenderedPage(boolean is_questao, int page, boolean agrp, boolean prov) {

        int p = page - getFirstPage(agrp, prov);

        if (p >= 0) {
            if (is_questao) {
                if (!load_g && !loadImages(!is_questao, page)) {
                    load_q = true;
                    return null;
                }
                if (provas != null && p < provas.length) {
                    return provas[p];
                }
            } else {
                if (!load_g && !loadImages(!is_questao, page)) {
                    load_g = true;
                    return null;
                }
                if (gabaritos != null && p < gabaritos.length) {
                    return gabaritos[p];
                }
            }
        }
        return null;
    }

    public int getNumPagesProva() {
        return provas == null ? 1 : provas.length;
    }

    public int getNumPagesGabarito() {
        return gabaritos == null ? 1 : gabaritos.length;
    }

    public int getNumPags() {
        return getNumPagesProva() + getNumPagesGabarito();
    }

    public Prova getProva() {
        return prova;
    }

    public boolean loadImages(boolean gabarito, int page) {
        if (gabarito && gabaritos == null || !gabarito && provas == null) {
            new Thread(() -> {
                try {
                    render(gabarito);
                    observable.update(page);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(progressBar, "Falhou ao renderizar prova Q " + toString());
                    Logger.getLogger(SubProva.class.getName()).log(Level.SEVERE, null, ex);
                }
            }).start();
            return false;
        }
        return true;
    }

    public int getIndex_prova() {
        return index_prova;
    }

    public void setIndex_prova(int index_prova) {
        this.index_prova = index_prova;
    }

    public int getIndex_gabarito() {
        return index_gabarito;
    }

    public void setIndex_gabarito(int index_gabarito) {
        this.index_gabarito = index_gabarito;
    }

    public int getIndex_agrupado() {
        return index_agrupado;
    }

    public void setIndex_agrupado(int index_agrupado) {
        this.index_agrupado = index_agrupado;
    }

    public boolean isMyPage(int page, boolean agrupado, boolean prova) {
        if (agrupado) {
            return page >= index_agrupado && page < (index_agrupado + getNumPags());
        }
        if (prova) {
            return page >= index_prova && page < (index_prova + getNumPagesProva());
        }
        return page >= index_gabarito && page < (index_gabarito + getNumPagesGabarito());
    }

    public int getFirstPage(boolean agrupado, boolean prova) {
        if (agrupado) {
            return index_agrupado + (prova ? 0 : getNumPagesProva());
        }
        if (prova) {
            return index_prova;
        }
        return index_gabarito;
    }

    public boolean inProva(int currentPage) {
        return (currentPage - index_agrupado) < getNumPagesProva();
    }

    public String show(int page, boolean agrupado, boolean prova) {
        return toString() + " [page " + (1 + page - (agrupado ? index_agrupado : prova ? index_prova : index_gabarito))
                + " de " + (agrupado ? getNumPags() : prova ? getNumPagesProva() : getNumPagesGabarito()) + "] "
                + (agrupado ? (inProva(page) ? "PROVA" : "GABARITO") : (prova ? "PROVA" : "GABARITO"));
    }

    @Override
    public String toString() {
        return "SupProva { " + prova.getSub_prova() + " }";
    }

    private void render(
            boolean gabarito
    ) throws Exception {
        RenderPDF renderPDF = new RenderPDF(
                bancoDeProvas,
                prova,
                gabarito,
                paginar, font, font_size, progressBar,
                tituloPattern,
                show_cab,
                margem_top,
                margem_quest);
        renderPDF(renderPDF.getPDF(), gabarito, progressBar);
        if (gabarito) {
            this.gabarito = renderPDF;
        } else {
            this.questao = renderPDF;
        }
    }

    void renderPDF(File file, boolean gabarito, JProgressBar progress) throws IOException {
        try (PDDocument document = PDDocument.load(file)) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            int nPaginas = document.getNumberOfPages();

            ArrayList<BufferedImage> imgs = new ArrayList<>();
            progress.setValue(0);
            for (int page = 0; page < nPaginas; ++page) {
                progress.setValue(page * 100 / nPaginas);
                imgs.add(pdfRenderer.renderImageWithDPI(page, PDF.DPI, ImageType.RGB));
            }

            if (gabarito) {
                gabaritos = imgs.toArray(new BufferedImage[]{});
            } else {
                provas = imgs.toArray(new BufferedImage[]{});
            }
        }
        progress.setValue(100);

    }

    public void load(boolean provas, boolean gabaritos) {
        if (provas) {
            loadImages(false, -1);
        }
        if (gabaritos) {
            loadImages(true, -1);
        }
    }

    public File getFileProva() {
        return questao != null ? questao.getPDF() : null;
    }

    public File getFileGabarito() {
        return gabarito != null ? gabarito.getPDF() : null;
    }

    public String human_ID() {
        return "" + (prova.getSub_prova());
    }

}
