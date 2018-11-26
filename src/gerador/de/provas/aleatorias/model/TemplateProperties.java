/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias.model;

import gerador.de.provas.aleatorias.util.Utils;
import java.awt.Component;
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
    private String QUESTOES_DATA = "gpa/questoes_data";
    private String PROVAS_DATA = "gpa/provas_data";
    private String QUESTOES_DIR = "gpa/questoes/";
    private String GABARITOS_DIR = "gpa/gabaritos/";
    private String ZIP_PATH = "";

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

}
