/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias;

import gerador.de.provas.aleatorias.model.TemplateProperties;
import gerador.de.provas.aleatorias.presenter.MainPresenter;
import gerador.de.provas.aleatorias.util.Utils;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author conta
 */
public class GeradorDeProvasAleatorias {

    /**
     * @param args the command line arguments
     */
    private static final String LOCAL_PROPERTIES_FILE = ".gerador_de_provas_aleatorias";

    public static void main(String[] args) {
        TemplateProperties properies = new TemplateProperties(LOCAL_PROPERTIES_FILE);

        File file_properties = new File(LOCAL_PROPERTIES_FILE);

        try {
            if (file_properties.exists()) {
                properies.load(null);
            } else {
                properies.save(null);
            }
        } catch (Exception ex) {
            Logger.getLogger(GeradorDeProvasAleatorias.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Falhou ao importar arquivo de propriedades");
        }

        new MainPresenter(properies);

    }

}
