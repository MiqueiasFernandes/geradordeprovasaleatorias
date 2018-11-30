/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias.model;

import gerador.de.provas.aleatorias.GeradorDeProvasAleatorias;
import gerador.de.provas.aleatorias.util.Arquivo;
import gerador.de.provas.aleatorias.util.Janela;
import gerador.de.provas.aleatorias.util.Utils;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author conta
 */
public class WorkDir {

    TemplateProperties properties;
    File work_dir, tmp_dir, provas_dir, questoes_dir, gabaritos_dir;

    public WorkDir(TemplateProperties properties) {
        this.properties = properties;
        work_dir = new File(properties.getWORK_DIR());
        tmp_dir = new File(properties.getTMP_DIR());
        provas_dir = new File(properties.getPROVAS_DIR());
        questoes_dir = new File(properties.getQUESTOES_DIR());
        gabaritos_dir = new File(properties.getGABARITOS_DIR());
    }

    public TemplateProperties getProperties() {
        return properties;
    }

    public void inicializar() throws IOException {

        if (!work_dir.exists()) {
            Files.createDirectory(work_dir.toPath());
        }
        if (!tmp_dir.exists()) {
            Files.createDirectory(tmp_dir.toPath());
        }
        if (!provas_dir.exists()) {
            Files.createDirectory(provas_dir.toPath());
        }
        if (!questoes_dir.exists()) {
            Files.createDirectory(questoes_dir.toPath());
        }
        if (!gabaritos_dir.exists()) {
            Files.createDirectory(gabaritos_dir.toPath());
        }

    }

    public String carregarBanco(Component view) {
        alterarBanco(view);
        String zip_path = properties.getZIP_PATH();
        if (zip_path == null) {
            return "Nenhum banco de provas importado";
        }

        try {
            Arquivo.unzip(zip_path, properties.getWORK_DIR());
            properties.loadFrom(properties.getWORK_DIR() + "DATA", view);
        } catch (Exception ex) {
            Logger.getLogger(WorkDir.class.getName()).log(Level.SEVERE, null, ex);
            return "Houve um erro ao importar o banco de provas em " + zip_path;
        }

        try {
            properties.setZIP_PATH(zip_path, view);
        } catch (Exception ex) {
            Logger.getLogger(WorkDir.class.getName()).log(Level.SEVERE, null, ex);
            return "Abriu com erro: impossivel alterar arquivo de propiedades.";
        }

        return zip_path;
    }

    /**
     *
     * @param view view para abrir o selecionador de arquivo para escolher onde
     * salvar
     * @return Local onde será salvo o ZIP
     */
    public String salvarBanco(Component view) {
        String zip_path = properties.getZIP_PATH();
        zip_path = zip_path != null && zip_path.equals(".gpa") ? null : zip_path;
        if (zip_path == null) {
            zip_path = Janela.fileChooser(
                    view,
                    "Salvar Banco de provas",
                    "",
                    "Salvar",
                    new String[]{"Banco de Provas", "gpa"},
                    false)
                    .getSelectedFile()
                    .getAbsolutePath();
        }

        if (zip_path != null && !zip_path.isEmpty()) {
            zip_path += zip_path.endsWith(".gpa") ? "" : ".gpa";
            try {
                limparTemp();
            } catch (IOException ex) {
                Logger.getLogger(WorkDir.class.getName()).log(Level.SEVERE, null, ex);
                return "Banco de provas não foi salvo: impossivel limpar diretorio TMP.";
            }

            try {
                properties.setZIP_PATH(zip_path, view);
            } catch (Exception ex) {
                Logger.getLogger(WorkDir.class.getName()).log(Level.SEVERE, null, ex);
                return "Banco de provas não foi salvo: impossivel salvar arquivo de propiedades.";
            }

            try {
                properties.export(properties.getWORK_DIR() + "DATA", view);
                Arquivo.zip(properties.getWORK_DIR(), zip_path);
            } catch (Exception ex) {
                Logger.getLogger(WorkDir.class.getName()).log(Level.SEVERE, null, ex);
                return "Banco de provas não foi salvo: arquivo compactado não foi criado.";
            }

        }

        return zip_path;
    }

    private void alterarBanco(Component view) {
        if (getNumQuestoes() > 0 && Janela.showDialogisYes(view, "Salvar banco de provas?",
                "Deseja salvar o banco de provas atual ?\n"
                + "Se não salvar vai perder todas provas criadas ou importadas")) {
            salvarBanco(view);
        }
    }

    public WorkDir criarBanco(Component view) throws Exception {
        alterarBanco(view);
        File properties_file = new File(properties.getLOCAL_PROPERTIES());
        if (properties_file.exists()) {
            try {
                Files.delete(properties_file.toPath());
            } catch (IOException ex) {
                Logger.getLogger(WorkDir.class.getName()).log(Level.SEVERE, null, ex);
                throw new Exception("Banco de provas não foi criado: impossivel deletar arquivo de propriedades.");
            }
        }
        if (work_dir.exists()) {
            try {
                Arquivo.deleteDirectoryRecursive(work_dir.toPath());
            } catch (IOException ex) {
                Logger.getLogger(WorkDir.class.getName()).log(Level.SEVERE, null, ex);
                throw new Exception("Banco de provas não foi criado: impossivel deletar diretorio de trabalho.");
            }
        }
        return GeradorDeProvasAleatorias.inicializar();
    }

    public void limparTemp() throws IOException {
        Arquivo.deleteDirectoryRecursive(tmp_dir.toPath());
        Files.createDirectory(tmp_dir.toPath());
    }

    /**
     *
     * @return nome das pastas que é os tipos de provas
     */
    public File[] getQuestoesFolders() {
        return questoes_dir.listFiles(); /// retorna
    }

    /**
     *
     * @return nome das pastas que é os tipos de provas
     */
    public File[] getGabaritosFolders() {
        return gabaritos_dir.listFiles();/// retorna pastas com nome dos tipos de provas
    }

    /**
     *
     * @param type tipo de prova
     * @return nome das pastas que é os tipos de provas
     */
    public File[] getQuestoesofType(String type) {
        File[] listFiles = getQuestoesFolders();

        if (listFiles != null) {
            for (File dir : listFiles) {
                if (dir.getName().equals(type) && dir.isDirectory()) {
                    return dir.listFiles();
                }
            }
        }

        return null;
    }

    /**
     *
     * @param type tipo de prova
     * @return nome das pastas que é os tipos de provas
     */
    public File[] getGabaritosofType(String type) {

        File[] listFiles = getGabaritosFolders();

        if (listFiles != null) {
            for (File dir : listFiles) {
                if (dir.getName().equals(type) && dir.isDirectory()) {
                    return dir.listFiles();
                }
            }
        }

        return null;
    }

    public File[] getProvas() {
        return provas_dir.listFiles();
    }

    public int getNumQuestoes() {

        File[] folders = getQuestoesFolders();
        if (folders == null) {
            return 0;
        }

        int cont = 0;
        for (File tipo : folders) {
            File[] questoesofType = getQuestoesofType(tipo.getName());
            cont = questoesofType == null ? 0 : questoesofType.length;
        }

        return cont;
    }

    public int getNumGabaritos() {

        File[] folders = getGabaritosFolders();
        if (folders == null) {
            return 0;
        }

        int cont = 0;
        for (File tipo : folders) {
            File[] questoesofType = getGabaritosofType(tipo.getName());
            cont = questoesofType == null ? 0 : questoesofType.length;
        }

        return cont;
    }

    public int getNumProvas() {
        File[] listFiles = getProvas();
        return listFiles == null ? 0 : listFiles.length;
    }

    public String[] getTiposdeProvas() {
        File[] folders = getQuestoesFolders();
        if (folders == null) {
            return null;
        }
        return Arrays.asList(folders).stream().map((t) -> {
            return t.getName();
        }).collect(Collectors.toList()).toArray(new String[]{});
    }

    public boolean hasTipodeProva(String tipo) {
        return Utils.inArray(getTiposdeProvas(), tipo);
    }

}
