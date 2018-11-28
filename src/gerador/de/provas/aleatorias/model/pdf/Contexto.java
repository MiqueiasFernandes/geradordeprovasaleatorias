/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias.model.pdf;

import gerador.de.provas.aleatorias.model.importar.Pagina;

/**
 *
 * @author conta
 */
public class Contexto {

    Pagina pagina;

    public Contexto(Pagina pagina) {
        this.pagina = pagina;
    }

    public int yView2Yimage(int y) {
        return 0;
    }

    public int yPDF2Yimage(float y) {
        return 0;
    }

    public int yimage2Yview(int y) {
        return 0;
    }

    public float yimage2YPDF(int y) {
        return 0;
    }

}
