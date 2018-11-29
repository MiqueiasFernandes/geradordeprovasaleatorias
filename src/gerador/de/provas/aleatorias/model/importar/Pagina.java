/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias.model.importar;

import gerador.de.provas.aleatorias.model.pdf.Contexto;
import gerador.de.provas.aleatorias.model.pdf.PDF;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.TreeSet;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

/**
 *
 * @author conta
 */
public class Pagina extends PDFTextStripper {

    PDF pdf;
    PDPage pDPage;
    BufferedImage image;
    int index;
    int index_global;
    int index_questao;
    Contexto contexto;
    private final PDDocument document;
    private final TreeSet<Marcador> marcadores;
    ModoPagina modoPagina;
    private List<Integer> questao_excluida;

    public Pagina(PDF pdf, PDPage pDPage, BufferedImage image, int index, int index_global) throws IOException {
        this.pdf = pdf;
        this.pDPage = pDPage;
        this.image = image;
        this.index = index;
        this.index_global = index_global;
        document = new PDDocument();
        document.addPage(pDPage);
        super.setSortByPosition(true);

        this.contexto = new Contexto(this);
        this.contexto.parse();

        this.marcadores = new TreeSet();
    }

    public void setQuestao_excluida(List<Integer> questao_excluida) {
        this.questao_excluida = questao_excluida;
    }

    @Override
    protected void processTextPosition(TextPosition text) {
        contexto.setTextPosition(text);
    }

    public PDDocument getDocument() {
        return document;
    }

    public String getLog() {
        return String.join("", contexto.getLinhasAsList(true));
    }

    public int getIndex() {
        return index;
    }

    public void addMarcador(int y) {
        Marcador marcador = new Marcador(this, contexto, y);
        marcadores.add(marcador);
        marcador.update();
    }

    public void addMarcador(float y) {
        Marcador marcador = new Marcador(this, contexto, y);
        marcadores.add(marcador);
        marcador.update();
    }

    public void removeMarcador(Marcador m) {
        marcadores.remove(m);
        update_marcadores();
    }

    public void update_marcadores() {
        if (!marcadores.isEmpty()) {
            marcadores.first().update();
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getIndex_global() {
        return index_global;
    }

    public PDPage getpDPage() {
        return pDPage;
    }

    public Contexto getContexto() {
        return contexto;
    }

    public TreeSet<Marcador> getMarcadores() {
        return marcadores;
    }

    public ModoPagina getModoPagina() {
        return modoPagina;
    }

    public void setModoPagina(ModoPagina modoPagina) {
        this.modoPagina = modoPagina;
    }

    public int getIndex_questao() {
        return index_questao;
    }

    public int setIndex_questao(int index_questao) {
        this.index_questao = index_questao;
        int max = 0;
        for (Marcador marcador : marcadores) {
            max = Math.max(max, marcador.getArea().getNumero());
        }
        return max;
    }

    public Marcador getMarcadorByID(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        for (Marcador marcador : marcadores) {
            if (marcador.toString().equalsIgnoreCase(id)) {
                return marcador;
            }
        }
        return null;
    }

    public PDF getPdf() {
        return pdf;
    }

    public void removerTodosMarcadores() {
        marcadores.clear();
    }

    public boolean isQuestaoExcluida(int questao) {
        if (questao_excluida == null) {
            return false;
        }
        return questao_excluida.contains(questao);
    }

}
