/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias.model;

/**
 *
 * @author conta
 */
public class BancoDeProvas {

    private String local;
    private boolean alterado = false;

    public BancoDeProvas(String local) {
        this.local = local;
    }

    public void salvar() {
        this.alterado = false;
    }

    public String getLocal() {
        return local;
    }

    public boolean isAlterado() {
        return alterado;
    }

}
