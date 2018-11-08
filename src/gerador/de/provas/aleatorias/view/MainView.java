/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias.view;

import javax.swing.JLabel;
import javax.swing.JMenuItem;

/**
 *
 * @author conta
 */
public class MainView extends javax.swing.JFrame {

    /**
     * Creates new form MainView
     */
    public MainView() {
        initComponents();
    }

    public JMenuItem getExportar_provas_menu() {
        return exportar_provas_menu;
    }

    public JMenuItem getGerar_provas_menu() {
        return gerar_provas_menu;
    }

    public JMenuItem getImportar_banco_de_dados_menu() {
        return importar_banco_de_dados_menu;
    }

    public JMenuItem getImportar_provas_menu() {
        return importar_provas_menu;
    }

    public JLabel getLocal_prova_label() {
        return local_prova_label;
    }

    public JLabel getProvas_geradas_label() {
        return provas_geradas_label;
    }

    public JLabel getProvas_label() {
        return provas_label;
    }

    public JLabel getQuestoes_label() {
        return questoes_label;
    }

    public JMenuItem getSalvar_banco_de_dados_menu() {
        return salvar_banco_de_dados_menu;
    }

    public JMenuItem getSalvar_como_banco_de_dados_menu() {
        return salvar_como_banco_de_dados_menu;
    }

    public JMenuItem getNovo_banco_de_provas_menu() {
        return novo_banco_de_provas_menu;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        local_prova_label = new javax.swing.JLabel();
        questoes_label = new javax.swing.JLabel();
        provas_label = new javax.swing.JLabel();
        provas_geradas_label = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        novo_banco_de_provas_menu = new javax.swing.JMenuItem();
        importar_banco_de_dados_menu = new javax.swing.JMenuItem();
        salvar_banco_de_dados_menu = new javax.swing.JMenuItem();
        salvar_como_banco_de_dados_menu = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        importar_provas_menu = new javax.swing.JMenuItem();
        gerar_provas_menu = new javax.swing.JMenuItem();
        exportar_provas_menu = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setIcon(new javax.swing.ImageIcon("C:\\Users\\conta\\Downloads\\Elegant_circle-icons-78.png")); // NOI18N
        jLabel1.setText("jLabel1");

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        local_prova_label.setText("...");
        jToolBar1.add(local_prova_label);
        local_prova_label.getAccessibleContext().setAccessibleName("banco_de_prova_label");

        questoes_label.setText("0 Questões");

        provas_label.setText("0 Provas");

        provas_geradas_label.setText("0 Provas Geradas");

        jMenu1.setText("Banco de Prova");

        novo_banco_de_provas_menu.setText("Novo");
        jMenu1.add(novo_banco_de_provas_menu);

        importar_banco_de_dados_menu.setText("Importar");
        jMenu1.add(importar_banco_de_dados_menu);

        salvar_banco_de_dados_menu.setText("Salvar");
        jMenu1.add(salvar_banco_de_dados_menu);

        salvar_como_banco_de_dados_menu.setText("Salvar como");
        jMenu1.add(salvar_como_banco_de_dados_menu);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Prova");

        importar_provas_menu.setText("Importar provas");
        jMenu2.add(importar_provas_menu);

        gerar_provas_menu.setText("Gerar provas");
        jMenu2.add(gerar_provas_menu);

        exportar_provas_menu.setText("Exportar Provas");
        jMenu2.add(exportar_provas_menu);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Sobre");
        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(questoes_label)
                    .addComponent(provas_label)
                    .addComponent(provas_geradas_label))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(questoes_label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(provas_label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(provas_geradas_label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel1.getAccessibleContext().setAccessibleName("imagem_prova");
        questoes_label.getAccessibleContext().setAccessibleName("questoes_label");

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem exportar_provas_menu;
    private javax.swing.JMenuItem gerar_provas_menu;
    private javax.swing.JMenuItem importar_banco_de_dados_menu;
    private javax.swing.JMenuItem importar_provas_menu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel local_prova_label;
    private javax.swing.JMenuItem novo_banco_de_provas_menu;
    private javax.swing.JLabel provas_geradas_label;
    private javax.swing.JLabel provas_label;
    private javax.swing.JLabel questoes_label;
    private javax.swing.JMenuItem salvar_banco_de_dados_menu;
    private javax.swing.JMenuItem salvar_como_banco_de_dados_menu;
    // End of variables declaration//GEN-END:variables
}
