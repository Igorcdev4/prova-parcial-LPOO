package model;

public class Clube extends Entidade {

    private String sigla;

    public Clube() {
        super();
        this.sigla = "";
    }

    public Clube(int id, String nome, String sigla) {
        super(id, nome);
        this.sigla = sigla;
    }

    @Override
    public String exibirResumo() {
        return getNome() + " (" + sigla + ")";
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }
}
