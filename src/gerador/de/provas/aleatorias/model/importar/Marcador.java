/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias.model.importar;

import gerador.de.provas.aleatorias.model.pdf.Contexto;
import gerador.de.provas.aleatorias.util.Singleton;

/**
 *
 * @author conta
 */
public class Marcador implements Comparable<Marcador> {

    private final Pagina pagina;
    private final Contexto contexto;
    private final float y_pdf;
    private final int y_image;
    private final Area area;
    private final int id;

    public Marcador(Pagina pagina, Contexto contexto, int y_view) {
        this.id = Singleton.getInstance().getNextMarcadorID();
        this.pagina = pagina;
        this.contexto = contexto;
        this.y_image = contexto.yView2Yimage(y_view);
        this.y_pdf = contexto.yimage2YPDF(y_image);
        this.area = new Area(this, pagina);
    }

    public Marcador(Pagina pagina, Contexto contexto, float y_pdf) {
        this.id = Singleton.getInstance().getNextMarcadorID();
        this.pagina = pagina;
        this.contexto = contexto;
        this.y_pdf = y_pdf;
        this.y_image = contexto.yPDF2Yimage(y_pdf);
        this.area = new Area(this, pagina);
    }

    public int getY_view() {
        return contexto.yimage2Yview(y_image);
    }

    public float getY_pdf() {
        return y_pdf;
    }

    public int getY_image() {
        return y_image;
    }

    public Area getArea() {
        return area;
    }

    public boolean update() {
        return area.update();
    }

    @Override
    public String toString() {
        return "M" + id;
    }

    @Override
    public int compareTo(Marcador o) {
        return y_image - o.y_image;
    }

}
