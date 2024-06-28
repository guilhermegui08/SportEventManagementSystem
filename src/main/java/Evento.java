import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Evento implements Serializable {
    private String nome;
    private Date dataInicio;
    private Date dataFim;
    private String local;
    private Pais pais;
    private Estado estado;
    private List<Prova> provas;
    private Modalidade modalidade;

    public Evento(String nome, Date dataInicio, Date dataFim, String local, Pais pais, Modalidade modalidade) {
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.local = local;
        this.pais = pais;
        this.provas = new ArrayList<>();
        this.estado = Estado.POR_INICIAR;
        this.modalidade = modalidade;
    }

    public String getNome() {
        return nome;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public String getLocal() {
        return local;
    }

    public Pais getPais() {
        return pais;
    }

    public List<Prova> getProvas() {
        return provas;
    }

    public Estado getEstado() {
        return estado;
    }

    public Modalidade getModalidade() {
        return modalidade;
    }

    public void adicionar(Prova prova){
        if (prova != null){
            provas.add(prova);
        }
    }

    public int iniciar(){
        int size;

        for (Prova prova : provas) {
            size = prova.getInscritos().size();

            if (size < 4){
                return -1;
            }
            int result = (int)(Math.log(size) / Math.log(2));
            if (size != Math.pow(2,result)){
                return -2;
            }
        }

        for (Prova prova : provas) {
            prova.iniciar();
            prova.setInscricoesAbertas(false);
        }

        this.estado = Estado.INICIADO;

        return 0;
    }

    public void finalizar() {
        Estado estadoProva;
        for (Prova prova : provas){
            estadoProva = prova.getEstado();
            if (estadoProva!= Estado.TERMINADO && estadoProva != Estado.TERMINADO_DESCL ){
                return;
            }
        }
        this.estado = Estado.TERMINADO;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
