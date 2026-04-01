package model;

import java.util.ArrayList;
import java.util.List;

public class Campeonato extends Entidade {

    private List<Clube> clubes;
    private List<Partida> partidas;
    private static final int MAX_CLUBES = 8;

    public Campeonato() {
        super();
        this.clubes = new ArrayList<>();
        this.partidas = new ArrayList<>();
    }

    public Campeonato(int id, String nome) {
        super(id, nome);
        this.clubes = new ArrayList<>();
        this.partidas = new ArrayList<>();
    }

    public boolean adicionarClube(Clube clube) {
        if (clubes.size() >= MAX_CLUBES) {
            return false;
        }
        for (Clube c : clubes) {
            if (c.getId() == clube.getId()) {
                return false;
            }
        }
        clubes.add(clube);
        return true;
    }

    public void adicionarPartida(Partida partida) {
        partidas.add(partida);
    }

    @Override
    public String exibirResumo() {
        return getNome() + " - " + clubes.size() + " clubes, " + partidas.size() + " partidas";
    }

    public List<Clube> getClubes() {
        return clubes;
    }

    public List<Partida> getPartidas() {
        return partidas;
    }
}
