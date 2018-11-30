/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias.util;

import gerador.de.provas.aleatorias.model.pdf.PDF;
import static gerador.de.provas.aleatorias.util.Janela.PATH_SEP;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author conta
 */
public class Arquivo {

    public static void deleteDirectoryRecursive(String path) throws IOException {
        deleteDirectoryRecursive(new File(path).toPath());
    }

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

    public static void zip(String folder, String toZip) throws IOException {
        FileOutputStream fos = new FileOutputStream(toZip);
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        File dir = new File(folder);
        for (File f : dir.listFiles()) {
            zipFile(f, f.getName(), zipOut);
        }
        zipOut.close();
        fos.close();
    }

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    public static void unzip(String fileZip, String path) throws IOException {
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            String fileName = zipEntry.getName();
            if (!fileName.endsWith("/")) {
                System.out.println("importando " + fileName + " em " + path + fileName);
                File newFile = new File(path + fileName);
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }

}
