/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias.model.pdf;

import gerador.de.provas.aleatorias.model.importar.ModoPagina;
import gerador.de.provas.aleatorias.model.importar.Pagina;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 *
 * @author conta
 */
public class PDF {

    public static final int DPI = 200;
    private final File file;
    private final String arquivo;
    private final PDDocument document;
    private final List<Pagina> pages;
    private final PDFRenderer pdfRenderer;
    private final int nPaginas;
    private final int id;
    private final int page_index_global_start;

    public PDF(String arquivo, JProgressBar progress, int id, int start) throws IOException {
        this.file = new File(arquivo);
        this.arquivo = arquivo;
        this.id = id;
        this.page_index_global_start = start;
        document = PDDocument.load(file);
        nPaginas = document.getNumberOfPages();
        pages = new ArrayList<>();

        pdfRenderer = new PDFRenderer(document);

        progress.setMinimum(0);
        progress.setMaximum(100);
        for (int page = 0; page < nPaginas; ++page) {
            progress.setValue(page * 100 / nPaginas);
            pages.add(new Pagina(
                    this,
                    document.getPage(page),
                    pdfRenderer.renderImageWithDPI(page, DPI, ImageType.RGB), page, start + page));
        }

        progress.setValue(100);
    }

    public File getFile() {
        return file;
    }

    public String getArquivo() {
        return arquivo;
    }

    public PDDocument getDocument() {
        return document;
    }

    public List<Pagina> getPages() {
        return pages;
    }

    public PDFRenderer getPdfRenderer() {
        return pdfRenderer;
    }

    public void setModoPagina(ModoPagina modoPagina) {
        pages.forEach((t) -> {
            t.setModoPagina(modoPagina);
        });
    }

    public void close() {
        try {
            document.close();
        } catch (IOException ex) {
            Logger.getLogger(PDF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
