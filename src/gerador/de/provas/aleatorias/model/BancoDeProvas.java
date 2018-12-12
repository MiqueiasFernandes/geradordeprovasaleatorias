/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias.model;

import java.awt.Component;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public boolean hasRegex(String tipo) {
        return workDir.getProperties().hasRegex(tipo);
    }

    public String[] getRegexOf(String tipo) {
        return workDir.getProperties().getRegexOfTipo(tipo);
    }

    public String[] getTiposDeProvas() {
        return workDir.getTiposdeProvas();
    }

    public int getNumQuestaoOfTipo(String tipo) {
        File[] questoesofType = workDir.getQuestoesofType(tipo);
        return questoesofType == null ? 0 : questoesofType.length;
    }

    public File[] listarCabecalhos() {
        File cabs = new File(workDir.getProperties().getCABECALHOS_DIR());
        if (cabs.exists() && cabs.isDirectory()) {
            return cabs.listFiles();
        }
        return null;
    }

    public Prova getProvaByName(String name) {
        File[] provasGeradas = workDir.getProvasGeradas();
        if (provasGeradas == null) {
            return null;
        }
        for (File prova : provasGeradas) {
            if (prova.getName().equals(name)) {
                try {
                    return new Prova(prova);
                } catch (Exception ex) {
                    Logger.getLogger(BancoDeProvas.class.getName()).log(Level.SEVERE, null, ex);
                    return null;
                }
            }
        }
        return null;
    }

    public Prova getSubProvaByName(String name, int id) {
        Prova prova = getProvaByName(name);
        try {
            return id < 1 || prova == null || prova.getQuantidade_de_sub_provas() < id
                    ? null
                    : new Prova(prova.getFile(), id);
        } catch (Exception ex) {
            Logger.getLogger(BancoDeProvas.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String toString() {
        return zip.getName().replace(".gpa", "") + " em " + zip.getAbsolutePath().replace(zip.getName(), "");
    }

}
