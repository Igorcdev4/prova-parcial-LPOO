package model;

import java.util.ArrayList;
import java.util.List;

public class Participante extends Entidade implements Pontuavel {

    private String email;
    private List<Aposta> apostas;

    public Participante() {
        super();
        this.email = "";
        this.apostas = new ArrayList<>();
    }

    public Participante(int id, String nome, String email) {
        super(id, nome);
        this.email = email;
        this.apostas = new ArrayList<>();
    }

    public void adicionarAposta(Aposta aposta) {
        apostas.add(aposta);
    }

    @Override
    public int calcularPontos() {
        int total = 0;
        for (Aposta a : apostas) {
            total += a.calcularPontos();
        }
        return total;
    }

    @Override
    public String exibirPontuacao() {
        return getNome() + " - Total: " + calcularPontos() + " pts";
    }

    @Override
    public String exibirResumo() {
        return getNome() + " (" + email + ")";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Aposta> getApostas() {
        return apostas;
    }
}
