/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias.util;

import static gerador.de.provas.aleatorias.util.Janela.PATH_SEP;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author conta
 */
public class Arquivo {

    public static void deleteDirectoryRecursive(Path path) throws IOException {
        Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    public static File[] copyFilesFromFolder(String origem, String destino) throws IOException {
        System.out.println("copianddo " + origem + "/* para " + destino + "/*");
        ArrayList<File> dirs = new ArrayList();
        for (File file : new File(origem).listFiles()) {
            if (file.isFile()) {
                Files.copy(file.toPath(),
                        new File(destino + PATH_SEP + file.getName()).toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
            } else {
                dirs.add(file);
            }
        }
        return dirs.toArray(new File[]{});
    }

    public static void copyFilesRecursive(String origem, String destino) throws IOException {
        for (File dir : copyFilesFromFolder(origem, destino)) {
            File f = new File(destino + PATH_SEP + dir.getName());
            System.out.println("copiando recursivo: " + f.getAbsolutePath());
            Files.createDirectory(f.toPath());
            copyFilesRecursive(dir.getAbsolutePath(), f.getAbsolutePath());
        }
    }
}
