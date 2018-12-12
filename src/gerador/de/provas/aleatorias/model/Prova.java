/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador.de.provas.aleatorias.model;

import gerador.de.provas.aleatorias.util.Utils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 *
 * @author conta
 */
class QuestaoDaProva {

    String nome_da_prova;
    Integer[] questoes;

    public QuestaoDaProva(String nome_da_prova, Integer[] questoes) {
        this.nome_da_prova = nome_da_prova;
        this.questoes = questoes;
    }

}

public class Prova {

    private final File file;
    private int sub_prova;
    private String nome;
    private boolean cabecalho_is_file;
    private String cabecalho;
    private int quantidade_de_sub_provas = 1;

    ArrayList<QuestaoDaProva> questoes = new ArrayList<>();
    Prova[] sub_provas = null;

    public Prova(File prova) throws Exception {
        this.file = prova;
        if (file.exists()) {
            Scanner sc = new Scanner(file);
            nome = sc.nextLine();
            cabecalho_is_file = sc.nextBoolean();
            sc.nextLine();
            cabecalho = sc.nextLine();
            quantidade_de_sub_provas = sc.nextInt();
            sub_provas = new Prova[quantidade_de_sub_provas];
            sc.nextLine();
            int ln = 4;
            int idprova = 0;
            try {
                while (sc.hasNextLine()) {
                    String linha = sc.nextLine();
                    ln++;
                    if (linha.startsWith("#")) {
                        if (questoes.size() > 0) {
                            sub_provas[idprova -1] = new Prova(this, idprova);
                            questoes.clear();
                        }
                        if ("#".equals(linha)) {
                            if (idprova != quantidade_de_sub_provas) {
                                throw new Exception("Arquivo de prova invalido");
                            }
                            break;
                        }
                        idprova = Integer.parseInt(linha.substring(1));
                    } else {
                        String nbrs = sc.nextLine().trim();
                        ln++;
                        Integer[] qst = (nbrs != null && !nbrs.isEmpty() && nbrs.split(" ").length > 0)
                                ? Arrays.asList(nbrs.split(" ")).stream().map((t) -> {
                                    return Integer.parseInt(t);
                                }).collect(Collectors.toList()).toArray(new Integer[]{}) : new Integer[]{};
                        questoes.add(new QuestaoDaProva(linha,
                                qst));
                    }
                }
            } catch (Exception e) {
                throw new Exception("Arquivo de prova com erro de formatação " + prova.getAbsolutePath() + " na linha " + ln);
            }
        }
    }

    private Prova(Prova mae, int sub_prova) {
        this.file = mae.file;
        this.sub_prova = sub_prova;
        this.questoes.addAll(mae.questoes);
        this.nome = mae.nome;
        this.cabecalho_is_file = mae.cabecalho_is_file;
        this.cabecalho = mae.cabecalho;
        this.quantidade_de_sub_provas = mae.quantidade_de_sub_provas;
    }

    public Prova(File prova, int sub_prova) throws Exception {
        this.file = prova;
        this.sub_prova = sub_prova;
        if (sub_prova > 0) {
            Scanner sc = new Scanner(file);
            nome = sc.nextLine();
            cabecalho_is_file = sc.nextBoolean();
            sc.nextLine();
            cabecalho = sc.nextLine();
            quantidade_de_sub_provas = sc.nextInt();
            sc.nextLine();
            int ln = 4;
            int idprova = 0;
            try {
                while (sc.hasNextLine()) {
                    String linha = sc.nextLine();
                    ln++;

                    if ("#".equals(linha)) {
                        if (idprova != quantidade_de_sub_provas) {
                            throw new Exception("Arquivo de prova invalido");
                        }
                        break;
                    }

                    if (linha.startsWith("#")) {
                        idprova = Integer.parseInt(linha.substring(1));
                    } else if (idprova == sub_prova) {
                        String nbrs = sc.nextLine().trim();
                        ln++;
                        Integer[] qst = (nbrs != null && !nbrs.isEmpty() && nbrs.split(" ").length > 0)
                                ? Arrays.asList(nbrs.split(" ")).stream().map((t) -> {
                                    return Integer.parseInt(t);
                                }).collect(Collectors.toList()).toArray(new Integer[]{}) : new Integer[]{};
                        questoes.add(new QuestaoDaProva(linha,
                                qst));
                    }
                }
            } catch (Exception e) {
                throw new Exception("Arquivo de prova com erro de formatação " + prova.getAbsolutePath() + " na linha " + ln);
            }
        }
    }

    public void salvar() throws IOException {
        boolean novo = !file.exists();
        FileWriter writer = new FileWriter(file, !novo);
        if (novo) {
            writer.write(nome + "\n");
            writer.write(cabecalho_is_file + "\n");
            writer.write(cabecalho + "\n");
            writer.write(quantidade_de_sub_provas + "\n");
            writer.write("#");
        }
        writer.write(Math.abs(sub_prova) + "\n");
        for (QuestaoDaProva questao : questoes) {
            writer.write(questao.nome_da_prova + "\n");
            writer.write(Arrays.toString(questao.questoes).replace("[", " ").replace("]", " ").replace(",", "") + "\n");
        }
        writer.write("#");
        writer.close();
    }

    public int getSub_prova() {
        return Math.abs(sub_prova);
    }

    public Prova[] getSub_provas() {
        return sub_provas;
    }

    public boolean isCabecalho_is_file() {
        return cabecalho_is_file;
    }

    public void setCabecalhoAsFile(boolean set) {
        cabecalho_is_file = set;
    }

    public void setCabecalho(String cabecalho) {
        this.cabecalho = cabecalho_is_file ? cabecalho : Utils.toB64(cabecalho);
    }

    public String getCabecalho() {
        return cabecalho_is_file ? cabecalho : Utils.fromB64(cabecalho);
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidade_de_sub_provas() {
        return quantidade_de_sub_provas;
    }

    public void setQuantidade_de_sub_provas(int quantidade_de_sub_provas) {
        this.quantidade_de_sub_provas = quantidade_de_sub_provas;
    }

    public void addQuestoes(String nome_da_prova, Integer[] qst) {
        questoes.add(new QuestaoDaProva(nome_da_prova, qst));
    }

    public int total() {
        int t = 0;
        for (QuestaoDaProva questoe : questoes) {
            t += questoe.questoes.length;
        }
        return t;
    }

    @Override
    public String toString() {
        return file.getName() + "(" + getSub_prova() + ") => " + Arrays.toString(questoes.stream().map((t) -> {
            return t.nome_da_prova + "{" + Arrays.toString(t.questoes).replace("[", "").replace("]", "") + "} (" + t.questoes.length + ")"; //To change body of generated lambdas, choose Tools | Templates.
        }).collect(Collectors.toList()).toArray(new String[]{})) + " : " + total();
    }

    public File getFile() {
        return file;
    }

}
