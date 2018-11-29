/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias.util;

/**
 *
 * @author conta
 */
public class Singleton {

    private int marcador_count = 0;

    public int getNextMarcadorID() {
        return ++marcador_count;
    }

    private Singleton() {
    }

    public static Singleton getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {

        private static final Singleton INSTANCE = new Singleton();
    }
}
