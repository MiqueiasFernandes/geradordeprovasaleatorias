/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias.model.importar;

/**
 *
 * @author conta
 */
public class Questao implements Comparable<Questao> {

    int num;
    Marcador questao;
    Marcador gabarito;
    boolean remover = false;
    boolean sem_gabarito = false;

    public Questao(int num) {
        this.num = num;
    }

    public Marcador getQuestao() {
        return questao;
    }

    public void setQuestao(Marcador questao) {
        if (questao.getArea().isQuestao() && questao.getArea().getNumero() == num) {
            this.questao = questao;
            this.remover = questao.getArea().isExcluido();
        }
    }

    public Marcador getGabarito() {
        return gabarito;
    }

    public void setGabarito(Marcador gabarito) {
        if (gabarito.getArea().isGabarito() && gabarito.getArea().getNumero() == num) {
            this.gabarito = gabarito;
        }
    }

    public void setSemGabarito() {
        sem_gabarito = true;
    }

    public boolean ok() {
        return (sem_gabarito || (gabarito != null)) && questao != null;
    }

    @Override
    public int compareTo(Questao o) {
        return num - o.num;
    }

    public int getNum() {
        return num;
    }

    public void excluir() {
        this.remover = true;
    }

    //  [ X1, Y1,  X2,  Y2   ]
    public float[] getRectPDF(boolean qst, int view_h) {
        float[] fs = new float[4];
        Marcador m = qst ? questao : gabarito;
        if (m == null) {
            return null;
        }

        fs[0] = 0;
        fs[1] = m.getY_pdf();
        fs[2] = m.getPagina().getpDPage().getMediaBox().getWidth();
        fs[3] = m.getContexto().yimage2YPDF(m.getContexto().yView2Yimage(m.getArea().getH(view_h)));

        return fs;
    }

    public int[] getRectImage(boolean qst, int view_h) {
        int[] fs = new int[4];

        Marcador m = qst ? questao : gabarito;
        if (m == null) {
            return null;
        }

        fs[0] = 0;
        fs[1] = m.getY_image();
        fs[2] = m.getPagina().getImage().getWidth();
        fs[3] = m.getContexto().yView2Yimage(m.getArea().getH(view_h));

        return fs;
    }

    public Pagina getPagina(boolean qst) {
        return qst ? (questao == null ? null : questao.getPagina()) : (gabarito == null ? null : gabarito.getPagina());
    }

}
