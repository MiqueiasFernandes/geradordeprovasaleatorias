/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias.model.exportar;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;

/**
 *
 * @author conta
 */
public class PaginaSubProva extends JLabel {

    BufferedImage image;

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (image != null) {
            int w = (int) (getHeight() / 1.4);
            int h = getHeight();
            int pad = (getWidth() - w) / 2;
            g.drawImage(
                    image.getScaledInstance(w, h, Image.SCALE_FAST),
                    pad, 0, w, h, null);
        }
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

}
