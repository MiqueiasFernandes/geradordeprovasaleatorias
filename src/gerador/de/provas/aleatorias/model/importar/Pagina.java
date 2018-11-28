/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias.model.importar;

import gerador.de.provas.aleatorias.model.pdf.Contexto;
import gerador.de.provas.aleatorias.model.pdf.PDF;
import java.awt.Image;
import java.awt.image.BufferedImage;
import org.apache.pdfbox.pdmodel.PDPage;

/**
 *
 * @author conta
 */
public class Pagina {

    PDF pdf;
    PDPage pDPage;
    BufferedImage image;
    int index;
    int index_global;
    Contexto contexto;

    public Pagina(PDF pdf, PDPage pDPage, BufferedImage image, int index, int index_global) {
        this.pdf = pdf;
        this.pDPage = pDPage;
        this.image = image;
        this.index = index;
        this.index_global = index_global;
        this.contexto = new Contexto(this);
    }

    public String getLog() {
        return null;
    }

    public int getIndex() {
        return index;
    }

    void addMarcador(int y, int height) {

    }

    Image getImage() {
        return image;
    }

    public int getIndex_global() {
        return index_global;
    }

}
