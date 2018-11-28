/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias.model.importar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
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
                    pagina.addMarcador(y, getHeight());
                }
                repaint();
            }

        });

        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {

            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (pagina != null) {
            int l  = (int)(getHeight() / 1.4);
            g.drawImage(pagina.getImage(), (getWidth()-l) / 2, 0, l, getHeight(), null);

//            Marcador m = pagina.getMarcadores();
//            while (m != null) {
//                Tipo t = m.tipo;
//                g.setColor(t.isQuestion ? Color.blue : Color.red);
//                g.drawLine(0, m.y, getWidth(), m.y);
//
//                g.drawString(m.toString(), getWidth() / 2, m.y + 10);
//
//                m = m.next;
//
//            }
            g.setColor(Color.red);
            g.drawLine(0, y, getWidth(), y);
        }
    }

    public void setPageView(Pagina current) {
        pagina = current;
    }

}
