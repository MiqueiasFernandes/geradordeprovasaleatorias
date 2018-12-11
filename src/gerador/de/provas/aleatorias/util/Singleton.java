/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias.util;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Random;

/**
 *
 * @author conta
 */
public class Singleton {

    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String lower = upper.toLowerCase(Locale.ROOT);
    public static final String digits = "0123456789";
    public static final String alphanum = upper + lower + digits;
    private final Random random;
    private final char[] symbols;
    private final char[] buf;

    /**
     * Generate a random string.
     *
     * @return palavra aleatória
     */
    public String nextString() {
        for (int idx = 0; idx < buf.length; ++idx) {
            buf[idx] = symbols[random.nextInt(symbols.length)];
        }
        return new String(buf);
    }

    private int marcador_count = 0;
    private int prova_count = 0;

    public int getNextMarcadorID() {
        return ++marcador_count;
    }

    public int getNextProvaID() {
        return ++prova_count;
    }

    public int getNextRandomInt(int limit) {
        return random.nextInt(limit);
    }

    private Singleton() {

        this.random = new SecureRandom();
        this.symbols = alphanum.toCharArray();
        this.buf = new char[21];

    }

    public static Singleton getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {

        private static final Singleton INSTANCE = new Singleton();
    }
}
