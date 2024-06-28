import java.io.Serializable;

public class HistoricoAtletas implements Serializable {

    private int posicao;
    private Prova prova;
    private Evento evento;

    public HistoricoAtletas(int posicao, Prova prova, Evento evento) {
        this.posicao = posicao;
        this.prova = prova;
        this.evento = evento;
    }

    public int getPosicao() {
        return posicao;
    }

    public Prova getProva() {
        return prova;
    }

    public Evento getEvento() {
        return evento;
    }
}
