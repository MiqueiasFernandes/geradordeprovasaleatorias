/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias;

import gerador.de.provas.aleatorias.model.TemplateProperties;
import gerador.de.provas.aleatorias.model.WorkDir;
import gerador.de.provas.aleatorias.presenter.MainPresenter;
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

        WorkDir workDir = inicializar();

        if (workDir == null) {
            throw new RuntimeException("Diretório de trabalho não pode ser criado, \n"
                    + "verfique espaço em disco e permissões para criar arquivos e pastas e tente novamente.");
        } else {
            new MainPresenter(workDir);
        }

    }

    public static WorkDir inicializar() {
        TemplateProperties properies = new TemplateProperties(LOCAL_PROPERTIES_FILE);
        WorkDir workDir = null;
        try {
            properies.load(null);
            workDir = new WorkDir(properies);
            workDir.clearWorkDir();
            workDir.inicializar();
        } catch (Exception ex) {
            Logger.getLogger(GeradorDeProvasAleatorias.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Falhou ao importar arquivo de propriedades ou inicializar diretorio de trabalho.");
        }
        return workDir;
    }

}
