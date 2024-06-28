import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.lang.Math;

public class Prova implements Serializable {
    private String nome;
    private int pesoMax;
    private char sexo;
    private Date data;
    private List<Combate> combates;
    private List<Atleta> inscritos;
    private Atleta vencedor;
    private Estado estado;
    private Evento evento;
    private boolean inscricoesAbertas;

    public Prova(int pesoMax, char sexo, Date data, Evento evento) {
        this.pesoMax = pesoMax;
        this.nome = "<" + pesoMax;
        this.sexo = sexo;
        this.data = data;
        combates = new ArrayList<>();
        inscritos = new ArrayList<>();
        this.vencedor = null;
        this.evento = evento;
        this.inscricoesAbertas = true;
    }

    public char getSexo() {
        return sexo;
    }

    public Date getData() {
        return data;
    }

    public String getNome() {
        return nome;
    }

    public int getPesoMax() {
        return pesoMax;
    }

    public boolean isInscricoesAbertas() {
        return inscricoesAbertas;
    }

    public List<Combate> getCombates() {
        return combates;
    }

    public List<Atleta> getInscritos() {
        return inscritos;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setInscricoesAbertas(boolean inscricoesAbertas) {
        this.inscricoesAbertas = inscricoesAbertas;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
        if (estado == Estado.TERMINADO || estado == Estado.TERMINADO_DESCL){
            finalizarProva();
            evento.finalizar();
        }
    }

    public Estado getEstado() {
        return estado;
    }

    private void finalizarProva(){
        int size = inscritos.size();
        int numRounds = log2(size);
        int posicao = 1;
        List<Atleta> vencedores = new ArrayList<>();

        for (int i = numRounds; i > 0 && posicao <= 3; i--) {
            for (Combate combate : combates) {
                if (combate.getRonda() == i) {
                    Atleta vencedor = combate.getVencedor();
                    if (vencedor != null && vencedor != DadosApp.desclacificado && !vencedores.contains(vencedor)) {
                        vencedor.criar(new HistoricoAtletas(posicao, this, evento));
                        vencedores.add(vencedor);
                        Pais pais = vencedor.getPais();
                        List<HistoricoPaises> historicos = DadosApp.getInstance().getHistorico(pais);
                        boolean historicoExiste = false;
                        HistoricoPaises historicoEvento = null;
                        for (HistoricoPaises historico : historicos ){
                            if (historico.getEvento() == evento){
                                historicoEvento = historico;
                                historicoExiste = true;
                                break;
                            }
                        }

                        if (!historicoExiste){
                            historicoEvento = new HistoricoPaises(evento, 0,0,0);
                            DadosApp.getInstance().adicionar(historicoEvento, pais);
                        }

                        switch (posicao){
                            case 1:
                                historicoEvento.incrementarPrimeiro();
                                break;
                            case 2:
                                historicoEvento.incrementarSegundo();
                                break;
                            case 3:
                                historicoEvento.incrementarTerceiro();
                                break;
                        }
                        posicao++;
                    }
                }
            }
        }
    }

    public void setVencedor(Atleta vencedor) {
        this.vencedor = vencedor;
        setEstado(Estado.TERMINADO);
    }

    public int inscrever(Atleta atleta){
        if (atleta != null && !inscritos.contains(atleta)){
            inscritos.add(atleta);
            return 0;
        }
        return -1;
    }

    private void adicionar(Combate combate){
        combates.add(combate);
    }

    public void iniciar(){
        int size = inscritos.size();
        Atleta aux1 = null;
        Atleta aux2 = null;
        int countAtletas = 0;
        int numRounds = log2(size);
        int numCombatesInicial = size/2;


        for (int i = 0; i < numRounds; i++) {
            for (int j = 0; j < numCombatesInicial; j++) {
                adicionar(new Combate(combates.size() +1, this.data, false, i+1));
            }
            numCombatesInicial /= 2;
        }

        for (int i = 0; i < (size/2); i++) {
            Combate combate = combates.get(i);
            for (int j = countAtletas; j < countAtletas + 2; j+=2) {
                aux1 = inscritos.get(j);
                aux2 = inscritos.get(j+1);
            }
            countAtletas += 2;
            combate.setAtleta(aux1);
            combate.setAtleta(aux2);
        }
    }

    public void criarProxCombate(@NotNull Combate combate, Atleta atleta){
        int id = combate.getId();
        int novoCombateId = calcProxIdCombate(id);

        if (isUltimoCombate(id)){
            setVencedor(atleta); // ?????
        }else{
            Combate proxCombate = combates.get(novoCombateId-1);

            if (atleta != null){
                proxCombate.setAtleta(atleta);
                if (proxCombate.getAtleta2() != null && proxCombate.getEstado() != Estado.TERMINADO && proxCombate.getEstado() != Estado.TERMINADO_DESCL){
                    proxCombate.setEstado(Estado.INICIADO);
                }
                if (proxCombate.getEstado() == Estado.TERMINADO || proxCombate.getEstado() == Estado.TERMINADO_DESCL){
                    criarProxCombate(proxCombate, atleta);
                }
            }else {
                switch (proxCombate.ProxAtletaVencedor()){
                    case -1: // Combate terminado por desclacificação
                        criarProxCombate(proxCombate, null);
                        break;
                    case 0:
                        criarProxCombate(proxCombate, proxCombate.getVencedor());
                        break;
                    default:
                }
            }
        }
    }

    private boolean isUltimoCombate(int novoID){
        if (novoID == inscritos.size()-1){
            return true;
        }
        return false;
    }

    private int calcProxIdCombate(int idCombateTerminado) {
        int proxRondaId;
        int initialNumFights = inscritos.size()/2;
        if (idCombateTerminado % 2 != 0){
            proxRondaId = (idCombateTerminado-1) + initialNumFights- ((idCombateTerminado-1)/2);
        }else{
            proxRondaId = (idCombateTerminado-2) + initialNumFights- ((idCombateTerminado-2)/2);
        }
        return proxRondaId + 1;
    }

    private static int log2(int number) {
        return (int) (Math.log(number) / Math.log(2));
    }

}
