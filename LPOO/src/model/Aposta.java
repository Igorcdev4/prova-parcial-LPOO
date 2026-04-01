package model;

public class Aposta extends Entidade implements Pontuavel {

    private Participante participante;
    private Partida partida;
    private int golsCasaAposta;
    private int golsVisitanteAposta;

    public Aposta() {
        super();
    }

    public Aposta(int id, Participante participante, Partida partida, int golsCasaAposta, int golsVisitanteAposta) {
        super(id, "Aposta de " + participante.getNome());
        this.participante = participante;
        this.partida = partida;
        this.golsCasaAposta = golsCasaAposta;
        this.golsVisitanteAposta = golsVisitanteAposta;
    }

    public String getResultadoAposta() {
        if (golsCasaAposta > golsVisitanteAposta) {
            return "CASA";
        } else if (golsCasaAposta < golsVisitanteAposta) {
            return "VISITANTE";
        }
        return "EMPATE";
    }

    @Override
    public int calcularPontos() {
        if (!partida.isFinalizada()) {
            return 0;
        }
        boolean acertouPlacar = golsCasaAposta == partida.getGolsCasa()
                && golsVisitanteAposta == partida.getGolsVisitante();
        if (acertouPlacar) {
            return 10;
        }
        boolean acertouResultado = getResultadoAposta().equals(partida.getResultado());
        if (acertouResultado) {
            return 5;
        }
        return 0;
    }

    @Override
    public String exibirPontuacao() {
        return participante.getNome() + " | "
                + partida.getClubeCasa().getSigla() + " " + golsCasaAposta
                + " x " + golsVisitanteAposta + " " + partida.getClubeVisitante().getSigla()
                + " => " + calcularPontos() + " pts";
    }

    @Override
    public String exibirResumo() {
        return exibirPontuacao();
    }

    public Participante getParticipante() {
        return participante;
    }

    public Partida getPartida() {
        return partida;
    }

    public int getGolsCasaAposta() {
        return golsCasaAposta;
    }

    public int getGolsVisitanteAposta() {
        return golsVisitanteAposta;
    }
}

