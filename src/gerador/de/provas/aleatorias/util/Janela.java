/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias.util;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author conta
 */
public class Janela {

    public static final String PATH_SEP = "/";

    public static String openFolder(Component view, String title, String current) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(current));
        chooser.setDialogTitle(title);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile().getAbsolutePath();
        } else {
            return null;
        }
    }

    public static JFileChooser fileChooser(Component view,
            String titulo,
            String diretorio,
            String botao,
            String[] filtro,
            boolean multifile) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(multifile);
        fileChooser.setDialogTitle(titulo);
        if (diretorio != null && !diretorio.isEmpty()) {
            fileChooser.setCurrentDirectory(new File(diretorio));
        }
        fileChooser.setApproveButtonText(botao == null || botao.isEmpty() ? "Abrir" : botao);
        if (filtro != null && filtro.length > 1) {
            fileChooser.setFileFilter(new FileNameExtensionFilter(
                    filtro[0], Arrays.copyOfRange(filtro, 1, filtro.length)));
        }

        if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            return fileChooser;
        }
        return null;
    }

    public static boolean showDialog(Component view, String titulo, String texto) {
        return JOptionPane.YES_OPTION
                == JOptionPane.showConfirmDialog(view,
                        texto,
                        titulo,
                        YES_NO_OPTION);
    }

    public static boolean restartApplication(Component view) {
        try {
            final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
            final File currentJar = new File(gerador.de.provas.aleatorias.GeradorDeProvasAleatorias.class.getProtectionDomain().getCodeSource().getLocation().toURI());

            /* is it a jar file? */
            if (!currentJar.getName().endsWith(".jar")) {
                return false;
            }

            /* Build command: java -jar application.jar */
            final ArrayList<String> command = new ArrayList<String>();
            command.add(javaBin);
            command.add("-jar");
            command.add(currentJar.getPath());

            final ProcessBuilder builder = new ProcessBuilder(command);
            builder.start();
            System.exit(0);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Impossivel recarregar o programa!");
        }
        return false;
    }

}
