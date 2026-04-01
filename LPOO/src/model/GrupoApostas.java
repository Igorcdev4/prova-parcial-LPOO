package model;

import java.util.ArrayList;
import java.util.List;

public class GrupoApostas extends Entidade {

    private Campeonato campeonato;
    private List<Participante> participantes;
    private static final int MAX_PARTICIPANTES = 5;

    public GrupoApostas() {
        super();
        this.participantes = new ArrayList<>();
    }

    public GrupoApostas(int id, String nome, Campeonato campeonato) {
        super(id, nome);
        this.campeonato = campeonato;
        this.participantes = new ArrayList<>();
    }

    public boolean adicionarParticipante(Participante participante) {
        if (participantes.size() >= MAX_PARTICIPANTES) {
            return false;
        }
        for (Participante p : participantes) {
            if (p.getId() == participante.getId()) {
                return false;
            }
        }
        participantes.add(participante);
        return true;
    }

    public List<Participante> getClassificacao() {
        List<Participante> classificacao = new ArrayList<>(participantes);
        classificacao.sort((p1, p2) -> p2.calcularPontos() - p1.calcularPontos());
        return classificacao;
    }

    @Override
    public String exibirResumo() {
        return getNome() + " - " + participantes.size() + " participantes";
    }

    public Campeonato getCampeonato() {
        return campeonato;
    }

    public void setCampeonato(Campeonato campeonato) {
        this.campeonato = campeonato;
    }

    public List<Participante> getParticipantes() {
        return participantes;
    }
}