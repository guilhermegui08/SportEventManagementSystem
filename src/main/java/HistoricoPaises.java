import java.io.Serializable;

public class HistoricoPaises implements Serializable {
    private Evento evento;
    private int primeiro;
    private int segundo;
    private int terceiro;

    public HistoricoPaises(Evento evento, int primeiro, int segundo, int terceiro) {
        this.evento = evento;
        this.primeiro = primeiro;
        this.segundo = segundo;
        this.terceiro = terceiro;
    }

    public Evento getEvento() {
        return evento;
    }

    public int getPrimeiro() {
        return primeiro;
    }

    public int getSegundo() {
        return segundo;
    }

    public int getTerceiro() {
        return terceiro;
    }

    public void incrementarPrimeiro(){
        primeiro++;
    }
    public void incrementarSegundo(){
        segundo++;
    }
    public void incrementarTerceiro(){
        terceiro++;
    }
}
