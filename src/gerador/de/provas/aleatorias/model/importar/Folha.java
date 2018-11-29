/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias.model.importar;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Rectangle2D;
import javax.swing.JLabel;

/**
 *
 * @author conta
 */
public class Folha extends JLabel {

    Pagina pagina;

    int y = -1;

    public Folha() {
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                y = e.getY();
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (y >= 0 && e.getClickCount() > 1) {
                    pagina.addMarcador(y);
                }
                repaint();
            }

        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updatePage();
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (pagina != null) {
            int w = (int) (getHeight() / 1.4);
            int h = getHeight();
            g.drawImage(
                    pagina.getImage()
                            .getScaledInstance(w, h, Image.SCALE_FAST),
                    (getWidth() - w) / 2, 0, w, h, null);

            for (Marcador marcador : pagina.getMarcadores()) {
                Area area = marcador.getArea();
                int y_view = marcador.getY_view();
                int y1 = area.getY();
                int y2 = area.getH(getHeight());
                g.setColor(Color.BLACK);

                for (int i = 0; i < getWidth(); i += 15) {
                    g.drawLine(i, y_view, i + 10, y_view);
                }

                Color c = area.isOculto() ? Color.orange
                        : (area.isExcluido() ? Color.RED
                        : (area.isQuestao() ? Color.BLUE : Color.GREEN));
                setTransparency10(c, 50, g);
                g.fillRect(10, y1, getWidth() - 20, y2);

                g.setColor(Color.BLACK);
                drawStringWithBack(g, Color.BLACK, Color.WHITE, marcador.toString(), getWidth() - 50, y_view + 20);
                drawStringWithBack(g, Color.BLACK, Color.WHITE, area.toString(), 10, y_view + 20);
            }

            g.setColor(Color.red);
            g.drawLine(0, y, getWidth(), y);
            g.drawString(Integer.toString(y), getWidth() - 25, y - 10);
        }
    }

    private void setTransparency10(Color c, int alpha, Graphics g) {
        g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), (int) 2.5 * alpha));
    }

    public void setPageView(Pagina current) {
        pagina = current;
        updatePage();
    }

    private void updatePage() {
        if (pagina != null) {
            pagina.getContexto().setView_heiht(getHeight());
        }
    }

    private void drawStringWithBack(Graphics g, Color f, Color b, String text, int x, int y) {
        FontMetrics fm = g.getFontMetrics();
        Rectangle2D rect = fm.getStringBounds(text, g);
        g.setColor(b);
        g.fillRect(x - 3,
                y - fm.getAscent(),
                (int) rect.getWidth() + 3,
                (int) rect.getHeight());

        g.setColor(f);
        g.drawString(text, x, y);
    }

}
