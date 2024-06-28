import java.io.Serializable;
import java.util.Date;

public class Combate implements Serializable {
    private int id;
    private Atleta atleta1;
    private Atleta atleta2;
    private Date hora;
    private Estado estado;
    private Atleta vencedor;
    private boolean proxAtletaVencedor = false;
    private int ronda;

    public Combate(int id, Atleta atleta1, Atleta atleta2, Date hora, int ronda) {
        this.id = id;
        this.atleta1 = atleta1;
        this.atleta2 = atleta2;
        this.hora = hora;
        this.estado = Estado.POR_INICIAR;
        this.vencedor = null;
        this.proxAtletaVencedor = false;
        this.ronda = ronda;
    }

    public Combate(int id, Date hora, boolean vencedor, int ronda) {
        this.id = id;
        this.atleta1 = null;
        this.atleta2 = null;
        this.hora = hora;
        this.estado = Estado.POR_INICIAR;
        this.vencedor = null;
        this.proxAtletaVencedor = false;
        this.ronda = ronda;
        /*
        if (vencedor){
            proxAtletaVencedor = true;
        }*/
    }

    /*public Combate(int id, Atleta atleta1,Date hora, boolean vencedor) {
        this.id = id;
        this.atleta1 = atleta1;
        this.atleta2 = null;
        this.hora = hora;
        if (vencedor){
            this.estado = Estado.TERMINADO;
            this.vencedor = atleta1;
        }else{
            this.estado = Estado.POR_INICIAR;
            this.vencedor = null;
        }
        this.proxAtletaVencedor = false;
    } */

    public int getId() {
        return id;
    }

    public Atleta getAtleta1() {
        return atleta1;
    }

    public Atleta getAtleta2() {
        return atleta2;
    }

    public Date getHora() {
        return hora;
    }

    public Estado getEstado() {
        return estado;
    }

    public Atleta getVencedor() {
        if (vencedor == null)
            return null;
        return vencedor;
    }

    public int getRonda() {
        return ronda;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public int ProxAtletaVencedor() {
        if (atleta1 != null && atleta1.getNome() != "_"){
            setVencedor(atleta1, true);
            setAtleta(DadosApp.desclacificado);
        }else {
            setAtleta(DadosApp.desclacificado);
            if (this.proxAtletaVencedor){
                setEstado(Estado.TERMINADO_DESCL);
                return -1;
            }
            this.proxAtletaVencedor = true;
            return 1;
        }
        return 0;
    }

    public void setAtleta(Atleta atleta) {
        if (atleta1 == null){
            atleta1 = atleta;
        }else if(atleta2 == null){
            atleta2 = atleta;
        }
        if (this.proxAtletaVencedor){
            setVencedor(atleta, true);
        }
    }

    public void setVencedor(Atleta vencedor, boolean desclassificacao) {
        this.vencedor = vencedor;
        if (desclassificacao){
            this.estado = Estado.TERMINADO_DESCL;
        }else {
            this.estado = Estado.TERMINADO;
        }
    }
}
