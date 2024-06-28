import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DadosApp implements Serializable{
    private static DadosApp instance = null;
    public static final String ADMIN = "Admin";
    public static final String JURI = "Juri";
    public static final String GUEST = "Guest";
    public static final Atleta desclacificado = new Atleta("_", null, ' ', null,0, null, 0);

    private List<Evento> eventos;

    private List<Atleta> atletas;
    private HashMap<Pais, List<HistoricoPaises>> historicoPaises;

    public DadosApp() {
        // PARA UTILIZAR OS TESTES DO MAIN, DESCOMENTAR AS LINHAS A BAIXO E COMENTAR A LINHA "lerDados()"
        //
        // eventos = new ArrayList<>();
        // atletas = new ArrayList<>();
        // historicoPaises = new HashMap<>();
        //

        lerDados();
    }

    public static synchronized DadosApp getInstance(){
        if (instance == null){
            instance = new DadosApp();
        }
        return instance;
    }

    public List<Atleta> getAtletas() {
        return atletas;
    }
    public List<Atleta> getAtletas(int pesoMax, char sexo, Modalidade modalidade) {
        List<Atleta> aux = new ArrayList<>();
        for (Atleta atleta: this.atletas) {
            if (atleta.getPeso() <= pesoMax && atleta.getSexo() == sexo && atleta.getModalidade() == modalidade){
                aux.add(atleta);
            }
        }
        return aux;
    }

    public List<HistoricoPaises> getHistorico(Pais pais){
        if (!historicoPaises.containsKey(pais)){
            historicoPaises.put(pais, new ArrayList<>());
        }
        return historicoPaises.get(pais);
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public int adicionar(Evento evento){
        if (evento != null && !eventos.contains(evento)) {
            eventos.add(evento);
            return 0;
        }
        return -1;
    }

    public void adicionar(HistoricoPaises historicoPais, Pais pais){
        if (historicoPaises.containsKey(pais)) {
            historicoPaises.get(pais).add(historicoPais);

        }
    }

    public int adicionar(Atleta atleta){
        if (atleta != null && !atletas.contains(atleta)) {
            atletas.add(atleta);
            return 0;
        }
        return -1;
    }

    public int remover(Atleta atleta){
        for (Evento evento: eventos) {
            if (evento.getEstado() != Estado.TERMINADO) {
                for (Prova prova : evento.getProvas()) {
                    if (prova.getInscritos().contains(atleta)) {
                        return -1;
                    }
                }
            }
        }
        atletas.remove(atleta);
        return 0;
    }

    public void guardarDados() {
        ObjectOutputStream oos = null;
        try {
            File f = new File(System.getProperty("user.home") + File.separator + "judojoka.savefile");
            oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(eventos);
            oos.writeObject(atletas);
            oos.writeObject(historicoPaises);
        } catch (IOException ex) {
            Logger.getLogger(DadosApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void lerDados() {
        ObjectInputStream ois = null;
        File f = new File(System.getProperty("user.home") + File.separator + "judojoka.savefile");
        if (f.canRead()) {
            try {
                ois = new ObjectInputStream(new FileInputStream(f));
                eventos = (List<Evento>) ois.readObject();
                atletas = (List<Atleta>) ois.readObject();
                historicoPaises = (HashMap<Pais, List<HistoricoPaises>>) ois.readObject();
                ois.close();
            } catch (IOException ex) {
                Logger.getLogger(DadosApp.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DadosApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else {
            eventos = new ArrayList<>();
            atletas = new ArrayList<>();
            historicoPaises = new HashMap<>();
        }
    }

    public List<Pais> getPaisesComHist() {
        List<Pais> paisesComHist = new ArrayList<>();
        Set<Pais> keys = historicoPaises.keySet();
        for (Pais pais : keys){
            paisesComHist.add(pais);
        }
        return paisesComHist;
    }
}
