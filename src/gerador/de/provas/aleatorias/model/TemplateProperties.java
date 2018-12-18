/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias.model;

import gerador.de.provas.aleatorias.util.Utils;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author conta
 */
public class TemplateProperties {

    private String LOCAL_PROPERTIES = "";
    private String WORK_DIR = "gpa/";
    private String TMP_DIR = "gpa/tmp/";
    private String PROVAS_DIR = "gpa/provas";
    private String CABECALHOS_DIR = "gpa/provas/cabecalhos";
    private String QUESTOES_DIR = "gpa/questoes/";
    private String GABARITOS_DIR = "gpa/gabaritos/";
    private String ZIP_PATH = "";
    private String SAVE_FILES = "";

    public TemplateProperties(String arquivo) {
        this.LOCAL_PROPERTIES = arquivo;
    }

    public static Field[] getFields() {
        return TemplateProperties.class.getDeclaredFields();
    }

    public static String[] getKeys() {
        ArrayList<String> fields = new ArrayList<>();
        for (Field f : getFields()) {
            fields.add(f.getName());
        }
        return fields.toArray(new String[]{});
    }

    public void load(Component view) throws IllegalArgumentException, IllegalAccessException, IOException {
        if (!new File(LOCAL_PROPERTIES).exists()) {
            save(view);
        }
        Properties properties = Utils.importProperties(LOCAL_PROPERTIES);
        for (Field f : getFields()) {
            if (properties.containsKey(f.getName())) {
                String value = properties.getProperty(f.getName());
                if (value != null && !value.isEmpty()) {
                    if (f.getType() == String.class) {
                        f.set(this, value);
                    } else if (f.getType() == int.class) {
                        f.set(this, Integer.parseInt(value));
                    } else if (f.getType() == boolean.class) {
                        f.set(this, Boolean.valueOf(value));
                    } else {
                        JOptionPane.showMessageDialog(view,
                                "Erro: impossivel converter tipo " + f.getType().getName());
                    }
                }
            }
        }
    }

    public void loadFrom(String local, Component view) throws Exception {
        String old = LOCAL_PROPERTIES;
        LOCAL_PROPERTIES = local;
        load(view);
        LOCAL_PROPERTIES = old;
    }

    public void save(Component view) throws IOException, IllegalArgumentException, IllegalAccessException {
        Properties p = new Properties();
        for (Field f : getFields()) {
            Object obj = f.get(this);

            if (obj != null) {
                if (obj.getClass() == String.class) {
                    p.setProperty(f.getName(), (String) obj);
                } else if (obj.getClass() == int.class) {
                    p.setProperty(f.getName(), Integer.toString((int) obj));
                } else if (obj.getClass() == boolean.class) {
                    p.setProperty(f.getName(), Boolean.toString((boolean) obj));
                } else {
                    JOptionPane.showMessageDialog(view,
                            "Erro: impossivel salvar em tipo " + f.getType().getName());
                }
            }
        }
        Utils.exportProperties(p, LOCAL_PROPERTIES);
    }

    public void export(String local, Component view) throws Exception {
        String old = LOCAL_PROPERTIES;
        LOCAL_PROPERTIES = local;
        save(view);
        LOCAL_PROPERTIES = old;
    }

    public String getWORK_DIR() {
        return WORK_DIR;
    }

    public String getTMP_DIR() {
        return TMP_DIR;
    }

    public String getPROVAS_DIR() {
        return PROVAS_DIR;
    }

    public String getQUESTOES_DIR() {
        return QUESTOES_DIR;
    }

    public String getGABARITOS_DIR() {
        return GABARITOS_DIR;
    }

    public String getLOCAL_PROPERTIES() {
        return LOCAL_PROPERTIES;
    }

    public String getCABECALHOS_DIR() {
        return CABECALHOS_DIR;
    }

    public String getZIP_PATH() {
        return ZIP_PATH == null || ZIP_PATH.isEmpty() ? null : ZIP_PATH;
    }

    public void setZIP_PATH(String ZIP_PATH, Component view) throws Exception {
        this.ZIP_PATH = ZIP_PATH;
        save(view);
    }

    public String getSAVE_FILES() {
        return SAVE_FILES;
    }

    public void setSAVE_FILES(String SAVE_FILES, Component view) throws Exception {
        this.SAVE_FILES = SAVE_FILES;
        save(view);
    }

}
