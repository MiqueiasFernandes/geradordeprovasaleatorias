/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias.model.pdf;

import gerador.de.provas.aleatorias.util.Singleton;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.multipdf.LayerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;

/**
 *
 * @author conta
 */
class PDFFrame {

    File file;
    PDDocument pdf;
    float height;
    AffineTransform affineTransform;
    PDFormXObject formXObject;
    int id;

    public PDFFrame(File file, float height, float y, PDDocument pdf, PDFormXObject formXObject) {
        this.file = file;
        this.height = height;
        this.pdf = pdf;
        this.affineTransform = AffineTransform.getTranslateInstance(0.0, y);
        this.formXObject = formXObject;
        this.id = Singleton.getInstance().getNextFrameID();
    }

    public void close() throws IOException {
        pdf.close();
    }

    @Override
    public String toString() {
        return file.getName() + " " + id;
    }

}

class PDFPage {

    static final float PAGE_HEIGHT = PDRectangle.A4.getHeight();

    PDPage outPdfPage;
    ArrayList<PDFFrame> frames;
    LayerUtility layerUtility;
    PDDocument outPdf;
    int padding = 0;

    public PDFPage(PDDocument outPdf) {
        frames = new ArrayList<>();
        this.outPdf = outPdf;
        layerUtility = new LayerUtility(outPdf);

        PDRectangle outPdfFrame = new PDRectangle(PDRectangle.A4.getWidth(), PAGE_HEIGHT);

        // Create output page with calculated frame and add it to the document
        COSDictionary dict = new COSDictionary();
        dict.setItem(COSName.TYPE, COSName.PAGE);
        dict.setItem(COSName.MEDIA_BOX, outPdfFrame);
        dict.setItem(COSName.CROP_BOX, outPdfFrame);
        dict.setItem(COSName.ART_BOX, outPdfFrame);
        outPdfPage = new PDPage(dict);
        outPdf.addPage(outPdfPage);
    }

    public float height_ocupado() {
        float f = padding;
        f = frames.stream().map((frame) -> frame.height)
                .reduce(f, (accumulator, _item) -> accumulator + _item);
        return f;
    }

    public boolean has_space() {
        return PAGE_HEIGHT - height_ocupado() > 1;
    }

    boolean cabe_na_pagina(String arquivo, int pad) throws IOException {
        if (!has_space()) {
            return false;
        }
        File file = new File(arquivo);
        float y = 0;
        try (PDDocument pdf = PDDocument.load(file)) {
            PDRectangle frame = pdf.getPage(0).getCropBox();
            y = PAGE_HEIGHT - (height_ocupado() + frame.getHeight() + pad);
        }
        return y > 1;
    }

    boolean cabe_na_pagina(String[] arquivo, int pad) throws IOException {
        if (!has_space()) {
            return false;
        }
        float y = PAGE_HEIGHT - (height_ocupado() + pad);
        for (String arq : arquivo) {
            File file = new File(arq);
            try (PDDocument pdf = PDDocument.load(file)) {
                PDRectangle frame = pdf.getPage(0).getCropBox();
                y -= frame.getHeight();
                if (1 >= y) {
                    return false;
                }
            }
        }
        return true;
    }

    public void addWhiteSpace(int space) {
        this.padding += space;
    }

    public boolean addSeCouber(String arquivo, int top) throws IOException {
        if (!has_space()) {
            return false;
        }
        File file = new File(arquivo);
        PDDocument pdf = PDDocument.load(file);
        PDRectangle frame = pdf.getPage(0).getCropBox();
        float y = PAGE_HEIGHT - (height_ocupado() + frame.getHeight() + top);
        if (y > 1) {
            PDFFrame pdfFrame = new PDFFrame(file, frame.getHeight(), y, pdf, layerUtility.importPageAsForm(pdf, 0));
            layerUtility.appendFormAsLayer(outPdfPage,
                    pdfFrame.formXObject,
                    pdfFrame.affineTransform,
                    pdfFrame.toString());
            frames.add(pdfFrame);
            padding += top;
        } else {
            pdf.close();
        }
        return y > 1;
    }

    public void closePage() throws IOException {
        for (PDFFrame frame : frames) {
            frame.close();
        }
    }

}

public class MultiPDF {

    File outFile;
    PDDocument outPdf;
    ArrayList<PDFPage> pages = new ArrayList<>();

    public MultiPDF(String outFile) {
        this.outFile = new File(outFile);
        outPdf = new PDDocument();
        addPage();
    }

    public void addPage() {
        pages.add(new PDFPage(outPdf));
    }

    public boolean cabe_na_pagina(String[] arquivos, int pad) throws IOException {
        return pages.get(pages.size() - 1).cabe_na_pagina(arquivos, pad);
    }

    public boolean cabe_na_pagina(String arquivo, int pad) throws IOException {
        return pages.get(pages.size() - 1).cabe_na_pagina(arquivo, pad);
    }

    public boolean addSeCouber(String arquivo, int top) throws IOException {
        return pages.get(pages.size() - 1).addSeCouber(arquivo, top);
    }

    public void save(boolean close) throws IOException {
        outPdf.save(outFile);
        if (close) {
            close();
        }
    }

    public void close() throws IOException {
        outPdf.close();
        for (PDFPage page : pages) {
            page.closePage();
        }
    }

    public static void generateMultiPDF(String FILE1_PATH, String FILE2_PATH, String OUTFILE_PATH) {
        File pdf1File = new File(FILE1_PATH);
        File pdf2File = new File(FILE2_PATH);
        File outPdfFile = new File(OUTFILE_PATH);
        PDDocument pdf1 = null;
        PDDocument pdf2 = null;
        PDDocument outPdf = null;
        try {
            pdf1 = PDDocument.load(pdf1File);
            pdf2 = PDDocument.load(pdf2File);
            outPdf = new PDDocument();

            float y = PDRectangle.A4.getHeight();

            // Create output PDF frame
            PDRectangle pdf1Frame = pdf1.getPage(0).getCropBox();
            PDRectangle pdf2Frame = pdf2.getPage(0).getCropBox();
            PDRectangle outPdfFrame = new PDRectangle(PDRectangle.A4.getWidth(), y);

            // Create output page with calculated frame and add it to the document
            COSDictionary dict = new COSDictionary();
            dict.setItem(COSName.TYPE, COSName.PAGE);
            dict.setItem(COSName.MEDIA_BOX, outPdfFrame);
            dict.setItem(COSName.CROP_BOX, outPdfFrame);
            dict.setItem(COSName.ART_BOX, outPdfFrame);
            PDPage outPdfPage = new PDPage(dict);
            outPdf.addPage(outPdfPage);

            // Source PDF pages has to be imported as form XObjects to be able to insert them at a specific point in the output page
            LayerUtility layerUtility = new LayerUtility(outPdf);
            PDFormXObject formPdf1 = layerUtility.importPageAsForm(pdf1, 0);
            PDFormXObject formPdf2 = layerUtility.importPageAsForm(pdf2, 0);

            // Add form objects to output page
            y -= pdf1Frame.getHeight();
            AffineTransform af1 = AffineTransform.getTranslateInstance(0.0, y);

            y -= pdf2Frame.getHeight();
            AffineTransform af2 = AffineTransform.getTranslateInstance(0.0, y);
            layerUtility.appendFormAsLayer(outPdfPage, formPdf1, af1, "camada1");
            layerUtility.appendFormAsLayer(outPdfPage, formPdf2, af2, "cabmada2");

            outPdf.save(outPdfFile);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pdf1 != null) {
                    pdf1.close();
                }
                if (pdf2 != null) {
                    pdf2.close();
                }
                if (outPdf != null) {
                    outPdf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.exit(0);
    }

    public void addWhiteSpace(int space) {
        pages.get(pages.size() - 1).addWhiteSpace(space);
    }

    public File getOutFile() {
        return outFile;
    }

}
