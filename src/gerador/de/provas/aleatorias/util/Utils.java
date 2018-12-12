/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias.util;

import java.awt.Desktop;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.Properties;
import java.util.Random;

/**
 *
 * @author conta
 */
public class Utils {

    public static String getDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public static boolean inArray(String[] vetor, String str) {
        return Arrays.asList(vetor).contains(str);
    }

    public static boolean inArray(String[] vetor, String str[]) {
        return Arrays.asList(vetor).containsAll(Arrays.asList(str));
    }

    public static int getRandomNumberInRange(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    public static Properties importProperties(String file) throws FileNotFoundException, IOException {
        Properties properties = new Properties();
        FileInputStream in = new FileInputStream(file);
        properties.load(in);
        in.close();
        return properties;
    }

    public static void exportProperties(Properties properties, String file) throws FileNotFoundException, IOException {
        FileOutputStream out = new FileOutputStream(file);
        properties.store(out, "Modificado em: " + getDateTime());
        out.close();
    }

    public static void openURL(String url) throws URISyntaxException {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException e) {
                /* TODO: error handling */ }
        } else {
            /* TODO: error handling */ }
    }

    public static String toB64(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes()).replace("=", "-");
    }

    public static String fromB64(String text) {
        return new String(Base64.getDecoder().decode(text.replace("-", "=")));
    }

}
