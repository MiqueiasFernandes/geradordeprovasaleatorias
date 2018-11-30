/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias.model;

import java.awt.Component;
import java.io.File;

/**
 *
 * @author conta
 */
public class BancoDeProvas {

    WorkDir workDir;
    Component view;
    File zip;

    public BancoDeProvas(WorkDir workDir, Component view) {
        this.workDir = workDir;
        this.view = view;
        this.zip = new File(workDir.getProperties().getZIP_PATH());
    }

    public String salvar() {
        return workDir.salvarBanco(view);
    }

    public int getNumQuestoes() {
        return workDir.getNumQuestoes();
    }

    public int getNumProvasGeradas() {
        return workDir.getNumProvasGeradas();
    }

    public int getNumTiposdeProvas() {
        return workDir.getNumTiposDeProvas();
    }

    public int getNumGabaritos() {
        return workDir.getNumGabaritos();
    }

    @Override
    public String toString() {
        return zip.getName().replace(".gpa", "") + " em " + zip.getAbsolutePath().replace(zip.getName(), "");
    }

}
