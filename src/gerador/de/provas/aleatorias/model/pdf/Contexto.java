/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias.model.pdf;

import gerador.de.provas.aleatorias.model.importar.Pagina;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.TextPosition;

/**
 *
 * @author conta
 */
class PointInLine {

    float x;
    float y;
    int page;

    public PointInLine(float x, float y, int page) {
        this.x = x;
        this.y = y;
        this.page = page;
    }

    public boolean inLine(PointInLine p) {
        return p.y == y;
    }

    public static PointInLine first(PointInLine[] ps) {
        PointInLine min = ps[0];

        for (PointInLine pointInLine : ps) {
            if (pointInLine.x < min.x) {
                min = pointInLine;
            }
        }

        return min;

    }

    public List<WordPosition> inLine(List<WordPosition> p) {

        p.removeIf((t) -> {
            return !inLine(t.line);
        });
        p.sort((o1, o2) -> {
            return (int) ((o1.line.x - o2.line.x) * 100); //To change body of generated lambdas, choose Tools | Templates.
        });
        ArrayList<WordPosition> arr = new ArrayList<>();
        String tmp = "";
        float x_ = this.x;

        String regex = "\\h|\\s|\\v";

        for (WordPosition w : p) {
            tmp += w.word.replaceAll(regex, "");
            if (tmp.isEmpty()) {
                x_ = w.line.x;
            } else if (w.word.matches(regex) && !tmp.replaceAll(regex, "").isEmpty()) {
                arr.add(new WordPosition(tmp.replaceAll(regex, ""), w.page, new PointInLine(x_, y, page), w.height, w.width));
                tmp = "";
                x_ = this.x;
            }
        }
        arr.add(new WordPosition("\n", p.get(0).page, this, -1, 1));
        return arr;
    }

}

class WordPosition {

    String word;
    int page;
    PointInLine line;
    float height;
    float width;

    public WordPosition(String word, int page, PointInLine line, float height, float width) {
        this.word = word;
        this.page = page;
        this.line = line;
        this.height = height;
        this.width = width;
    }

    public float startY() {
        return height < 0 ? line.y : line.y - height;
    }

    @Override
    public String toString() {
        return word;
    }

}

public class Contexto {

    private final Pagina pagina;
    private final ArrayList<WordPosition> palavras = new ArrayList();
    private final HashSet<Float> y_linhas = new HashSet();
    private Float[] y_linhas_sorted;
    private final SortedMap<Float, String> texto = new TreeMap<>();
    private final SortedMap<Float, List<WordPosition>> posicoes = new TreeMap<>();
    private final SortedMap<Float, Float> minimo = new TreeMap<>();
    private final float pdf_height;
    private final float pdf_height_ratio;
    private final float image_height;
    private int view_heiht = -1;
    private float view_heiht_ratio = -1;

    public Contexto(Pagina pagina) {
        this.pagina = pagina;
        image_height = pagina.getImage().getHeight();
        pdf_height = pagina.getpDPage().getMediaBox().getHeight();
        pdf_height_ratio = (image_height + 0.0001f) / pdf_height; ///quantas vezes a imagem é maior que o pdf
    }

    public void parse() throws IOException {
        PDDocument document = new PDDocument();
        document.addPage(pagina.getpDPage());
        pagina.getText(document);

        y_linhas_sorted = y_linhas.toArray(new Float[]{});
        Arrays.sort(y_linhas_sorted);
        for (Float line : y_linhas_sorted) {
            List<WordPosition> inLine = getLine(line);
            texto.put(inLine.get(0).startY(), String.join(" ", inLine.stream().map((t) -> {
                return t.word;
            }).collect(Collectors.toList())));
            posicoes.put(inLine.get(0).startY(), inLine);
            float x = -1;
            for (WordPosition palavra : palavras) {
                if (palavra.line.y == line && palavra.line.x < x || x < 0) {
                    x = palavra.line.x;
                }
            }
            minimo.put(inLine.get(0).startY(), x);
            palavras.removeAll(inLine);
        }
    }

    private List<WordPosition> getLine(Float line) {
        return new PointInLine(0, line, pagina.getIndex()).inLine(new ArrayList(palavras));
    }

    public void setTextPosition(TextPosition text) {
        String u = text.getUnicode();
        if (u.matches("\\p{L}|\\p{Print}")) {
            palavras.add(new WordPosition(
                    u,
                    pagina.getIndex(),
                    new PointInLine(text.getX(), text.getY(), pagina.getIndex()),
                    text.getFontSize(),
                    text.getWidth()));
            y_linhas.add(text.getY());
        }
    }

    public List<String> getLinhasAsList(boolean log) {
        return log ? texto.entrySet().stream().map((t) -> {
            return getNumZero(yimage2Yview(yPDF2Yimage(t.getKey()))) + " => " + t.getValue();
        }).collect(Collectors.toList()) : texto.values().stream().collect(Collectors.toList());
    }

    String getNumZero(int n) {
        if (n < 10) {
            return "000" + n;
        } else if (n < 100) {
            return "00" + n;
        } else if (n < 1000) {
            return "0" + n;
        }
        return "" + n;
    }

    public int yView2Yimage(int y) {
        return (int) (y * view_heiht_ratio);
    }

    public int yPDF2Yimage(float y) {
        return (int) (y * pdf_height_ratio);
    }

    public int yimage2Yview(int y) {
        return (int) (y / view_heiht_ratio);
    }

    public float yimage2YPDF(int y) {
        return y / pdf_height_ratio;
    }

    public void setView_heiht(int h) {
        view_heiht = h;
        this.view_heiht_ratio = (image_height + 0.0001f) / view_heiht; ///quantas vezes a imagem é maior que a view
    }

    public SortedMap<Float, String> getTexto() {
        return texto;
    }

    //// x   y   width height
    public Float[] getRect(Float y, String regex) {
        try {
            Float[] ret = new Float[4];
            List<WordPosition> line = posicoes.get(y);
            ret[3] = line.get(0).height;
            ret[1] = line.get(0).line.y - ret[3];
            ret[3] = line.get(0).height * 1.3f;
            String text = String.join(" ", line.stream().map((t) -> {
                return t.word;
            }).collect(Collectors.toList()));

            Pattern word = Pattern.compile(regex);
            Matcher match = word.matcher(text);
            int end = -1;
            while (match.find()) {
                end = text.substring(0, match.end()).split(" ").length;
            }
            if (end < 0) {
                return null;
            }
            end = Math.min(line.size(), end);
            ret[0] = ret[2] = minimo.get(y);
            for (int i = 0; i < end; i++) {
                ret[2] += line.get(i).width;
            }
            return ret;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean tapar(Float linha, String regex) {

        Float[] local = getRect(linha, regex);
        if (local != null) {
            pagina.addAreaATapar(local);
            return true;
        }
        return false;
    }

}
