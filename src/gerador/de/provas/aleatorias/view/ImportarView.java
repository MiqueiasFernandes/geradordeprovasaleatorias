/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias.view;

import gerador.de.provas.aleatorias.model.importar.Folha;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author conta
 */
public class ImportarView extends javax.swing.JFrame {

    /**
     * Creates new form ImportarView
     */
    public ImportarView() {
        initComponents();
        setTitle("Importar provas");
        buttonGroup1.add(gabarito_apos_cada_questao);
        buttonGroup1.add(gabarito_no_outro_arquivo);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jToolBar1 = new javax.swing.JToolBar();
        first_page_nav_button = new javax.swing.JButton();
        previous_page_nav_button = new javax.swing.JButton();
        atual_page_spinner = new javax.swing.JSpinner();
        total_page_nav_label = new javax.swing.JLabel();
        next_page_nav_button = new javax.swing.JButton();
        last_page_nav_button = new javax.swing.JButton();
        num_questoes_importadas_label = new javax.swing.JLabel();
        marcadores_btn = new javax.swing.JButton();
        carregar_mais_arquivos = new javax.swing.JButton();
        importar_botao = new javax.swing.JButton();
        progressbar = new javax.swing.JProgressBar();
        folha = folha = new Folha();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        gabarito_apos_cada_questao = new javax.swing.JRadioButton();
        gabarito_no_outro_arquivo = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        modelo_de_inicio_de_questao = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        modelo_de_inicio_de_gabarito = new javax.swing.JTextField();
        aplicar_a_todos_desse_arquivo_btn = new javax.swing.JButton();
        aplicar_a_esta_pagina_btn = new javax.swing.JButton();
        excluir_parte_branca = new javax.swing.JCheckBox();
        como_fazer_isso = new javax.swing.JLabel();
        aplicar_a_todos_btn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        remover_marcador = new javax.swing.JTextField();
        adicionar_marcador = new javax.swing.JTextField();
        eliminar_questao = new javax.swing.JTextField();
        ocultar_regiao_do_marcador_text = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        aplicar_btn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        nome_da_prova = new javax.swing.JTextField();
        pdf_info_lbl = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        log_area = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        first_page_nav_button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gerador/de/provas/aleatorias/view/imgs/first.png"))); // NOI18N
        first_page_nav_button.setToolTipText("ir a primeira página");
        first_page_nav_button.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        first_page_nav_button.setFocusable(false);
        first_page_nav_button.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        first_page_nav_button.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(first_page_nav_button);

        previous_page_nav_button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gerador/de/provas/aleatorias/view/imgs/prev.png"))); // NOI18N
        previous_page_nav_button.setToolTipText("voltar página");
        previous_page_nav_button.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        previous_page_nav_button.setFocusable(false);
        previous_page_nav_button.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        previous_page_nav_button.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(previous_page_nav_button);

        atual_page_spinner.setMaximumSize(new java.awt.Dimension(50, 20));
        jToolBar1.add(atual_page_spinner);

        total_page_nav_label.setText("- 0");
        jToolBar1.add(total_page_nav_label);

        next_page_nav_button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gerador/de/provas/aleatorias/view/imgs/next.png"))); // NOI18N
        next_page_nav_button.setToolTipText("proxima página");
        next_page_nav_button.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        next_page_nav_button.setFocusable(false);
        next_page_nav_button.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        next_page_nav_button.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(next_page_nav_button);

        last_page_nav_button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gerador/de/provas/aleatorias/view/imgs/last.png"))); // NOI18N
        last_page_nav_button.setToolTipText("ir a ultima página");
        last_page_nav_button.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        last_page_nav_button.setFocusable(false);
        last_page_nav_button.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        last_page_nav_button.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(last_page_nav_button);

        num_questoes_importadas_label.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        num_questoes_importadas_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        num_questoes_importadas_label.setText("0 questões para importar");
        num_questoes_importadas_label.setMaximumSize(new java.awt.Dimension(1000, 14));
        num_questoes_importadas_label.setMinimumSize(new java.awt.Dimension(100, 14));
        jToolBar1.add(num_questoes_importadas_label);
        num_questoes_importadas_label.getAccessibleContext().setAccessibleName("");
        num_questoes_importadas_label.getAccessibleContext().setAccessibleDescription("");

        marcadores_btn.setText(" Marcadores");
        marcadores_btn.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        marcadores_btn.setFocusable(false);
        marcadores_btn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        marcadores_btn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(marcadores_btn);

        carregar_mais_arquivos.setText("Carregar mais arquivos");
        carregar_mais_arquivos.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        carregar_mais_arquivos.setFocusable(false);
        carregar_mais_arquivos.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        carregar_mais_arquivos.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(carregar_mais_arquivos);

        importar_botao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        importar_botao.setText("IMPORTAR");
        importar_botao.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        importar_botao.setFocusable(false);
        importar_botao.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        importar_botao.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(importar_botao);

        folha.setBackground(new java.awt.Color(204, 204, 204));
        folha.setMinimumSize(new java.awt.Dimension(300, 0));
        folha.setOpaque(true);

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Separar Questões"));

        gabarito_apos_cada_questao.setSelected(true);
        gabarito_apos_cada_questao.setText("após cada questão");

        gabarito_no_outro_arquivo.setText("no outro arquivo");
        gabarito_no_outro_arquivo.setToolTipText("");

        jLabel2.setText("Gabarito:");

        jLabel3.setText("Modelo de início de questão");

        modelo_de_inicio_de_questao.setText("1)");

        jLabel4.setText("Modelo de início de gabarito");

        modelo_de_inicio_de_gabarito.setText("1)");

        aplicar_a_todos_desse_arquivo_btn.setText("Aplicar a todas páginas desse arquivo");

        aplicar_a_esta_pagina_btn.setText("Aplicar a esta página");

        excluir_parte_branca.setSelected(true);
        excluir_parte_branca.setText("Excluir partes em branco");

        como_fazer_isso.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        como_fazer_isso.setForeground(new java.awt.Color(0, 0, 204));
        como_fazer_isso.setText("como fazer isso?");

        aplicar_a_todos_btn.setText("Aplicar a todos arquivos");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 2, Short.MAX_VALUE)
                        .addComponent(aplicar_a_esta_pagina_btn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(aplicar_a_todos_desse_arquivo_btn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(aplicar_a_todos_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(como_fazer_isso)
                                .addGap(44, 44, 44))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(modelo_de_inicio_de_questao)
                                    .addComponent(jLabel4))
                                .addGap(6, 6, 6))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(modelo_de_inicio_de_gabarito)
                                .addGap(151, 151, 151)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(gabarito_no_outro_arquivo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(gabarito_apos_cada_questao, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(excluir_parte_branca, javax.swing.GroupLayout.Alignment.TRAILING)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(como_fazer_isso))
                .addGap(4, 4, 4)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gabarito_apos_cada_questao)
                    .addComponent(modelo_de_inicio_de_questao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gabarito_no_outro_arquivo)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(modelo_de_inicio_de_gabarito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(excluir_parte_branca))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(aplicar_a_todos_desse_arquivo_btn)
                    .addComponent(aplicar_a_esta_pagina_btn)
                    .addComponent(aplicar_a_todos_btn)))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Aplicar Nessa Página"));

        jLabel5.setText("Remover Marcador:");

        jLabel6.setText("Adicionar MArcador:");

        jLabel7.setText("Eliminar questão com gabarito:");

        remover_marcador.setText("nomes: M1,M2,M3");
        remover_marcador.setToolTipText("nomes: M1,M2,M3");

        adicionar_marcador.setText("posições y: 356,1030,50");
        adicionar_marcador.setToolTipText("posições y: 356,1030,50");

        eliminar_questao.setText("numeros: 1,2,3,4");
        eliminar_questao.setToolTipText("numeros: 1,2,3,4");

        ocultar_regiao_do_marcador_text.setText("nomes: M1,M3,M5");
        ocultar_regiao_do_marcador_text.setToolTipText("nomes: M1,M3,M5");

        jLabel8.setText("Ocultar região do Marcador:");

        aplicar_btn.setText("Aplicar");
        aplicar_btn.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(adicionar_marcador)
                            .addComponent(remover_marcador)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ocultar_regiao_do_marcador_text)
                            .addComponent(eliminar_questao))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(aplicar_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(remover_marcador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(adicionar_marcador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(eliminar_questao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ocultar_regiao_do_marcador_text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(aplicar_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jLabel5.getAccessibleContext().setAccessibleName("");

        jLabel1.setText("Nome desse tipo de prova:");

        nome_da_prova.setText("provas do primeiro conteudo");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(nome_da_prova))
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(nome_da_prova, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pdf_info_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pdf_info_lbl.setText("...");

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        log_area.setEditable(false);
        log_area.setColumns(20);
        log_area.setFont(new java.awt.Font("Courier New", 0, 13)); // NOI18N
        log_area.setRows(5);
        log_area.setAutoscrolls(false);
        jScrollPane1.setViewportView(log_area);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(progressbar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(folha, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(pdf_info_lbl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(folha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pdf_info_lbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressbar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public JTextField getAdicionar_marcador() {
        return adicionar_marcador;
    }

    public JButton getAplicar_a_esta_pagina_btn() {
        return aplicar_a_esta_pagina_btn;
    }

    public JButton getAplicar_a_todos_desse_arquivo_btn() {
        return aplicar_a_todos_desse_arquivo_btn;
    }

    public JButton getAplicar_a_todos_btn() {
        return aplicar_a_todos_btn;
    }

    public JSpinner getAtual_page_spinner() {
        return atual_page_spinner;
    }

    public ButtonGroup getButtonGroup() {
        return buttonGroup1;
    }

    public JTextField getEliminar_questao() {
        return eliminar_questao;
    }

    public JCheckBox getExcluir_parte_branca() {
        return excluir_parte_branca;
    }

    public JButton getFirst_page_nav_button() {
        return first_page_nav_button;
    }

    public Folha getFolha() {
        return (Folha) folha;
    }

    public JRadioButton getGabarito_apos_cada_questao() {
        return gabarito_apos_cada_questao;
    }

    public JRadioButton getGabarito_no_outro_arquivo() {
        return gabarito_no_outro_arquivo;
    }

    public JButton getImportar_botao() {
        return importar_botao;
    }

    public JLabel getjLabel4() {
        return jLabel4;
    }

    public JButton getLast_page_nav_button() {
        return last_page_nav_button;
    }

    public JTextArea getLog_area() {
        return log_area;
    }

    public JTextField getModelo_de_inicio_de_gabarito() {
        return modelo_de_inicio_de_gabarito;
    }

    public JTextField getModelo_de_inicio_de_questao() {
        return modelo_de_inicio_de_questao;
    }

    public JButton getNext_page_nav_button() {
        return next_page_nav_button;
    }

    public JTextField getNome_da_prova() {
        return nome_da_prova;
    }

    public JLabel getNum_questoes_importadas_label() {
        return num_questoes_importadas_label;
    }

    public JButton getPrevious_page_nav_button() {
        return previous_page_nav_button;
    }

    public JProgressBar getProgressbar() {
        return progressbar;
    }

    public JTextField getRemover_marcador() {
        return remover_marcador;
    }

    public JLabel getTotal_page_nav_label() {
        return total_page_nav_label;
    }

    public JButton getCarregar_mais_arquivos() {
        return carregar_mais_arquivos;
    }

    public JButton getAplicar_btn() {
        return aplicar_btn;
    }

    public JTextField getOcultar_regiao_do_marcador_text() {
        return ocultar_regiao_do_marcador_text;
    }

    public JLabel getPdf_info_lbl() {
        return pdf_info_lbl;
    }

    public JButton getMarcadores_btn() {
        return marcadores_btn;
    }

    public JLabel getComo_fazer_isso() {
        return como_fazer_isso;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField adicionar_marcador;
    private javax.swing.JButton aplicar_a_esta_pagina_btn;
    private javax.swing.JButton aplicar_a_todos_btn;
    private javax.swing.JButton aplicar_a_todos_desse_arquivo_btn;
    private javax.swing.JButton aplicar_btn;
    private javax.swing.JSpinner atual_page_spinner;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton carregar_mais_arquivos;
    private javax.swing.JLabel como_fazer_isso;
    private javax.swing.JTextField eliminar_questao;
    private javax.swing.JCheckBox excluir_parte_branca;
    private javax.swing.JButton first_page_nav_button;
    private javax.swing.JLabel folha;
    private javax.swing.JRadioButton gabarito_apos_cada_questao;
    private javax.swing.JRadioButton gabarito_no_outro_arquivo;
    private javax.swing.JButton importar_botao;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton last_page_nav_button;
    private javax.swing.JTextArea log_area;
    private javax.swing.JButton marcadores_btn;
    private javax.swing.JTextField modelo_de_inicio_de_gabarito;
    private javax.swing.JTextField modelo_de_inicio_de_questao;
    private javax.swing.JButton next_page_nav_button;
    private javax.swing.JTextField nome_da_prova;
    private javax.swing.JLabel num_questoes_importadas_label;
    private javax.swing.JTextField ocultar_regiao_do_marcador_text;
    private javax.swing.JLabel pdf_info_lbl;
    private javax.swing.JButton previous_page_nav_button;
    private javax.swing.JProgressBar progressbar;
    private javax.swing.JTextField remover_marcador;
    private javax.swing.JLabel total_page_nav_label;
    // End of variables declaration//GEN-END:variables
}
