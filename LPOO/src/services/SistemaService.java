package Services;

import model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SistemaService {

    private List<Clube> clubes;
    private List<Campeonato> campeonatos;
    private List<Participante> participantes;
    private List<GrupoApostas> grupos;
    private List<Aposta> apostas;
    private int contadorId;
    private static final int MAX_GRUPOS = 5;

    public SistemaService() {
        this.clubes = new ArrayList<>();
        this.campeonatos = new ArrayList<>();
        this.participantes = new ArrayList<>();
        this.grupos = new ArrayList<>();
        this.apostas = new ArrayList<>();
        this.contadorId = 1;
    }

    private int proximoId() {
        return contadorId++;
    }

    public Clube cadastrarClube(String nome, String sigla) {
        Clube clube = new Clube(proximoId(), nome, sigla);
        clubes.add(clube);
        return clube;
    }

    public Campeonato cadastrarCampeonato(String nome) {
        Campeonato campeonato = new Campeonato(proximoId(), nome);
        campeonatos.add(campeonato);
        return campeonato;
    }

    public boolean adicionarClubeAoCampeonato(Campeonato campeonato, Clube clube) {
        return campeonato.adicionarClube(clube);
    }

    public Partida cadastrarPartida(Campeonato campeonato, Clube casa, Clube visitante, LocalDateTime dataHora) {
        Partida partida = new Partida(proximoId(), casa, visitante, dataHora);
        campeonato.adicionarPartida(partida);
        return partida;
    }

    public Participante cadastrarParticipante(String nome, String email) {
        Participante participante = new Participante(proximoId(), nome, email);
        participantes.add(participante);
        return participante;
    }

    public GrupoApostas criarGrupo(String nome, Campeonato campeonato) {
        if (grupos.size() >= MAX_GRUPOS) {
            return null;
        }
        GrupoApostas grupo = new GrupoApostas(proximoId(), nome, campeonato);
        grupos.add(grupo);
        return grupo;
    }

    public boolean adicionarParticipanteAoGrupo(GrupoApostas grupo, Participante participante) {
        return grupo.adicionarParticipante(participante);
    }

    public Aposta registrarAposta(Participante participante, Partida partida, int golsCasa, int golsVisitante) {
        if (!partida.podeApostar()) {
            return null;
        }
        for (Aposta a : apostas) {
            if (a.getParticipante().getId() == participante.getId()
                    && a.getPartida().getId() == partida.getId()) {
                return null;
            }
        }
        Aposta aposta = new Aposta(proximoId(), participante, partida, golsCasa, golsVisitante);
        apostas.add(aposta);
        participante.adicionarAposta(aposta);
        return aposta;
    }

    public void registrarResultado(Partida partida, int golsCasa, int golsVisitante) {
        partida.registrarResultado(golsCasa, golsVisitante);
    }

    public List<Clube> getClubes() {
        return clubes;
    }

    public List<Campeonato> getCampeonatos() {
        return campeonatos;
    }

    public List<Participante> getParticipantes() {
        return participantes;
    }

    public List<GrupoApostas> getGrupos() {
        return grupos;
    }

    public List<Aposta> getApostas() {
        return apostas;
    }
}
