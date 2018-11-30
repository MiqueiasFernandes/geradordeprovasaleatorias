/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias.model.importar;

import gerador.de.provas.aleatorias.model.pdf.Contexto;
import gerador.de.provas.aleatorias.model.pdf.PDF;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.TreeSet;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
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
    private final TreeSet<Marcador> marcadores;
    ModoPagina modoPagina;
    private List<Integer> questao_excluida;

    public Pagina(PDF pdf, PDPage pDPage, BufferedImage image, int index, int index_global) throws IOException {
        this.pdf = pdf;
        this.pDPage = pDPage;
        this.image = image;
        this.index = index;
        this.index_global = index_global;
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

    public String getLog() {
        return String.join("", contexto.getLinhasAsList(true));
    }

    public int getIndex() {
        return index;
    }

    public Marcador addMarcador(int y) {
        Marcador marcador = new Marcador(this, contexto, y);
        marcadores.add(marcador);
        marcador.update();
        return marcador;
    }

    public Marcador addMarcador(float y) {
        Marcador marcador = new Marcador(this, contexto, y);
        marcadores.add(marcador);
        marcador.update();
        return marcador;
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

    public void eliminarFim() {

        int botton = image.getHeight();
        for (int y = image.getHeight() - 1; y >= 0; y--) {
            for (int x = image.getWidth() - 1; x >= 0; x--) {
                if (!(image.getRGB(x, y) == Color.WHITE.getRGB())) {
                    botton = y;
                    break;
                }
            }
            if (botton != image.getHeight()) {
                break;
            }
        }

        if (botton < image.getHeight()) {

            if (botton < image.getHeight() - 50) {
                botton += 10;
            }

            Marcador marcador = new Marcador(this, contexto, botton, true);
            marcadores.add(marcador);
            marcador.update();
        }

    }

    @Override
    public String toString() {
        return pdf.getFile().getName() + " (Página " + (index + 1) + ")";
    }

    public void savePartAsImage(int[] bounds, String local) throws IOException {
        ImageIO.write(
                image.getSubimage(bounds[0], bounds[1], bounds[2], bounds[3]),
                "png",
                new File(local));
    }

    ////     x1,y1
    ///             x2,y2
    public void savePartAsPDF(float[] bounds, String local) throws IOException {

        PDRectangle mediaBox = pDPage.getMediaBox();

        PDRectangle box = new PDRectangle(bounds[0], mediaBox.getHeight() - bounds[1] - bounds[3], bounds[2], bounds[3]);

        pDPage.setMediaBox(box);

        try (PDDocument tmp = new PDDocument()) {
            tmp.addPage(pDPage);

            System.out.println("salvando " + local);
            tmp.save(local);
            tmp.close();
        }

        pDPage.setMediaBox(mediaBox);

    }

}
