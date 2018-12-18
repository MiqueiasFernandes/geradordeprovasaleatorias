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
import org.apache.fontbox.util.BoundingBox;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
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

        for (int page = 0; page < nPaginas; ++page) {
            if (progress != null) {
                progress.setValue(page * 100 / nPaginas);
            }
            pages.add(new Pagina(
                    this,
                    document.getPage(page),
                    pdfRenderer.renderImageWithDPI(page, DPI, ImageType.RGB), page, start + page));
        }
        if (progress != null) {
            progress.setValue(100);
        }
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

    public static boolean createTextPDF(
            String arquivo,
            String[] text,
            int pad,
            PDType1Font font,
            int size,
            boolean clear) throws IOException {
        File file = new File(arquivo);
        try (PDDocument pdf = new PDDocument()) {

            PDRectangle frame = new PDRectangle(PDRectangle.A4.getWidth(), PDRectangle.A4.getHeight());

            // Create output page with calculated frame and add it to the document
            COSDictionary dict = new COSDictionary();
            dict.setItem(COSName.TYPE, COSName.PAGE);
            dict.setItem(COSName.MEDIA_BOX, frame);
            dict.setItem(COSName.CROP_BOX, frame);
            dict.setItem(COSName.ART_BOX, frame);

            PDPage page = new PDPage(dict);
            float y;

            //Setting the font to the Content stream
            try (PDPageContentStream contentStream = new PDPageContentStream(pdf, page)) {
                //Setting the font to the Content stream
                contentStream.setFont(font, size);

                //Adding text in the form of string
                y = frame.getHeight() - size;
                for (String line : text) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(pad, (y -= size));
                    contentStream.showText(line);
                    contentStream.endText();
                }
            }

            if (clear && y > 0) {
                page.setMediaBox(new PDRectangle(
                        new BoundingBox(0, PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth(), y - (size / 3))
                ));
            }
            pdf.addPage(page);
            pdf.save(arquivo);
        }

        return file.exists();
    }

    public static File mergePDFs(File[] pdfs, String out) {
        try {
            PDFMergerUtility PDFmerger = new PDFMergerUtility();
            PDFmerger.setDestinationFileName(out);

            ArrayList<PDDocument> docs = new ArrayList<>();

            for (File pdf : pdfs) {
                docs.add(PDDocument.load(pdf));
                PDFmerger.addSource(pdf);
            }

            PDFmerger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());

            //Closing the documents
            for (PDDocument doc : docs) {
                doc.close();
            }

            return new File(out);
        } catch (IOException ex) {
            Logger.getLogger(PDF.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
