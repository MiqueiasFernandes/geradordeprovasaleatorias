/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias.model.importar;

import java.util.Iterator;

/**
 *
 * @author conta
 */
public class Area {

    private final Marcador marcador;
    private final Pagina pagina;
    private boolean oculto, questao, gabarito;

    public Area(Marcador marcador, Pagina pagina) {
        this.marcador = marcador;
        this.pagina = pagina;
    }

    /*
    -                           ESPAÇO VAZIO
    ----------------------------MARCADOR 1
    -    TITULO DOCUMENT
    -
    -
     */
    public boolean update() {

        boolean atual_q = questao, atual_g = gabarito;

        if (oculto) {
            gabarito = questao = false;
        } else {
            switch (pagina.getModoPagina()) {
                case GABARITO:
                    gabarito = true;
                    questao = false;
                    break;
                case QUESTAO:
                    questao = true;
                    gabarito = false;
                    break;
                case MISTO: {

                    Marcador anterior = pagina.getMarcadores().lower(marcador);

                    while (anterior != null && anterior.getArea().isOculto()) {
                        anterior = pagina.getMarcadores().lower(anterior);
                    }

                    if (anterior == null) {
                        gabarito = false;
                        questao = true;
                    } else {
                        questao = anterior.getArea().isGabarito();
                        gabarito = !questao;
                    }
                }
                break;
            }
        }

        boolean alterou = (atual_q != questao) || (atual_g != gabarito);

        if (alterou) {
            Marcador next = pagina.getMarcadores().higher(marcador);

            while (next != null && next != marcador) {
                next.getArea().update();
                Marcador t = next;
                next = pagina.getMarcadores().higher(marcador);
                if (next == t) {
                    break;
                }
            }
        }
        return alterou;
    }

    public void ocultar() {
        oculto = true;
        update();
    }

    public void mostrar() {
        this.oculto = false;
        update();
    }

    public boolean isOculto() {
        return oculto;
    }

    public boolean isQuestao() {
        return questao;
    }

    public boolean isGabarito() {
        return gabarito;
    }

    public boolean isVisivel() {
        return !oculto;
    }

    public int getNumero() {

        Iterator<Marcador> it = pagina.getMarcadores().iterator();

        int cont = 0;

        while (it.hasNext()) {

            Marcador next = it.next();

            if (next.getArea().isVisivel()) {
                switch (pagina.getModoPagina()) {
                    case QUESTAO:
                    case GABARITO:
                        cont++;
                        break;
                    case MISTO:
                        if (questao && next.getArea().questao) {
                            cont++;
                        }
                        if (gabarito && next.getArea().gabarito) {
                            cont++;
                        }
                        break;
                }
            }

            if (next == marcador) {
                return pagina.getIndex_questao() + cont;
            }
        }

        return 0;
    }

    public int getY() {
        return marcador.getY_view();
    }

    public int getH(int view_height) {
        Marcador higher = pagina.getMarcadores().higher(marcador);
        return higher != null ? higher.getY_view() - getY() : view_height - getY();
    }

    public boolean isExcluido() {
        return pagina.isQuestaoExcluida(getNumero());
    }

    @Override
    public String toString() {
        return oculto ? "Parte excluida"
                : (questao ? ("Questão " + getNumero())
                        : ("Gabarito " + getNumero())) + (isExcluido() ? " SERÁ EXCLUIDA!" : "");
    }

}
