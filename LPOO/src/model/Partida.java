package model;

import java.time.LocalDateTime;

public class Partida extends Entidade {

    private Clube clubeCasa;
    private Clube clubeVisitante;
    private LocalDateTime dataHora;
    private int golsCasa;
    private int golsVisitante;
    private boolean finalizada;

    public Partida() {
        super();
        this.golsCasa = 0;
        this.golsVisitante = 0;
        this.finalizada = false;
    }

    public Partida(int id, Clube clubeCasa, Clube clubeVisitante, LocalDateTime dataHora) {
        super(id, clubeCasa.getNome() + " x " + clubeVisitante.getNome());
        this.clubeCasa = clubeCasa;
        this.clubeVisitante = clubeVisitante;
        this.dataHora = dataHora;
        this.golsCasa = 0;
        this.golsVisitante = 0;
        this.finalizada = false;
    }

    public void registrarResultado(int golsCasa, int golsVisitante) {
        this.golsCasa = golsCasa;
        this.golsVisitante = golsVisitante;
        this.finalizada = true;
    }

    public boolean podeApostar() {
        if (finalizada) {
            return false;
        }
        LocalDateTime limite = dataHora.minusMinutes(20);
        return LocalDateTime.now().isBefore(limite);
    }

    public String getResultado() {
        if (golsCasa > golsVisitante) {
            return "CASA";
        } else if (golsCasa < golsVisitante) {
            return "VISITANTE";
        }
        return "EMPATE";
    }

    @Override
    public String exibirResumo() {
        String texto = clubeCasa.getNome() + " x " + clubeVisitante.getNome();
        if (finalizada) {
            texto += " | " + golsCasa + " x " + golsVisitante;
        }
        return texto;
    }

    public Clube getClubeCasa() {
        return clubeCasa;
    }

    public Clube getClubeVisitante() {
        return clubeVisitante;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public int getGolsCasa() {
        return golsCasa;
    }

    public int getGolsVisitante() {
        return golsVisitante;
    }

    public boolean isFinalizada() {
        return finalizada;
    }
}
