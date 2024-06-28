import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Atleta implements Serializable {
    private String nome;
    private Pais pais;
    private char sexo;
    private Modalidade modalidade;
    private int peso;
    private Date dataNascimento;
    private long contacto;
    private List<HistoricoAtletas> historicoAtletas;

    public Atleta(String nome, Pais pais, char sexo, Modalidade modalidade, int peso, Date dataNascimento, long contacto) {
        this.nome = nome;
        this.pais = pais;
        this.sexo = sexo;
        this.modalidade = modalidade;
        this.peso = peso;
        this.dataNascimento = dataNascimento;
        this.contacto = contacto;
        this.historicoAtletas = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public Pais getPais() {
        return pais;
    }

    public char getSexo() {
        return sexo;
    }

    public Modalidade getModalidade() {
        return modalidade;
    }

    public int getPeso() {
        return peso;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public long getContacto() {
        return contacto;
    }


    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }

    public void setModalidade(Modalidade modalidade) {
        this.modalidade = modalidade;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setContacto(long contacto) {
        this.contacto = contacto;
    }

    public void criar(HistoricoAtletas historicoAtletas){
        this.historicoAtletas.add(historicoAtletas);
    }

    public List<HistoricoAtletas> getHistorico() {
        return historicoAtletas;
    }

    @Override
    public String toString() {
        return nome +" " + peso + "kg - " + pais ;
    }
}
