import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JanelaPrincipal extends JFrame {
    private JButton btnEventos;
    private JButton btnRankings;
    private JButton btnAtletas;
    private JButton btnFicheiros;
    private JButton btnMudarUser;
    private JButton btnLogInOut;
    private JPanel painelPrincipal;
    private JLabel lblUser;

    private static DadosApp dados;
    private String user = DadosApp.GUEST;

    public JanelaPrincipal(String title) throws HeadlessException {
        super(title);
        setContentPane(painelPrincipal);
        lblUser.setText(user);
        btnMudarUser.setVisible(false);
        btnLogInOut.setText("Login");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                DadosApp.getInstance().guardarDados();  //////// COMENTAR PARA EFEITOS DE TESTE COM OS DADOS DO MAIN
                setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        });

        btnEventos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new JanelaEventos(title,user);
            }
        });
        btnAtletas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new JanelaAtletas(title,user);
            }
        });

        btnRankings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new JanelaRankings(title,user);
            }
        });

        btnFicheiros.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new JanelaFicheiros(title);
            }
        });

        btnLogInOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(lblUser.getText().equals(DadosApp.GUEST)){   //Se não está autenticado
                    mudarUser();
                    btnMudarUser.setVisible(true);
                    btnLogInOut.setText("Logout");
                }else{     // Se está autenticado, permite fazer logout
                    btnMudarUser.setVisible(false);
                    user = DadosApp.GUEST;
                    lblUser.setText(DadosApp.GUEST);
                    btnLogInOut.setText("Login");
                }
            }
        });

        btnMudarUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mudarUser();
            }
        });
    }

    private void mudarUser(){
        String optToChoose[] = {"Juri", "Admin"};
        String option = (String) JOptionPane.showInputDialog(null, "Login","Choose job",
                JOptionPane.QUESTION_MESSAGE,null, optToChoose,optToChoose[0]);
        switch (option){
            case "Juri":
                user = DadosApp.JURI;
                break;
            case "Guest":
                user = DadosApp.JURI;
                break;
            case "Admin":
                user = DadosApp.ADMIN;
                break;
        }
        lblUser.setText(option);
    }

    public static void main(String[] args) {

        // PARA TESTE

        // COMENTAR NO CONSTRUTOR DADOSAPP() A CHAMADA DA FUNÇÃO LERDADOS() E DESCOMENTAR AS INICIALIZAÇÕES
        // COMENTAR A LINHA 35 DESTE FICHEIRO
        // DESCOMENTAR AS LINHAS A BAIXO

        /*Atleta at1 = new Atleta("Joana", Pais.PORTUGAL, 'F', Modalidade.JUDO, 54,new Date("10/12/1999"), 912020201);
        Atleta at2 = new Atleta("Maria", Pais.PORTUGAL, 'F', Modalidade.JUDO, 50,new Date("2/12/2000"), 912020202);
        Atleta at3 = new Atleta("Rita", Pais.PORTUGAL, 'F', Modalidade.JUDO, 52,new Date("8/12/1998"), 912020203);
        Atleta at4 = new Atleta("Catarina", Pais.PORTUGAL, 'F', Modalidade.JUDO, 55,new Date("12/12/1997"), 912020204);

        Atleta at5 = new Atleta("Carolina", Pais.PORTUGAL, 'F', Modalidade.JUDO, 60,new Date("21/12/1999"), 912020205);
        Atleta at6 = new Atleta("Liliana", Pais.PORTUGAL, 'F', Modalidade.JUDO, 55,new Date("6/12/2000"), 912020206);
        Atleta at7 = new Atleta("Carla", Pais.PORTUGAL, 'F', Modalidade.JUDO, 48,new Date("3/12/1998"), 912020207);
        Atleta at8 = new Atleta("Helena", Pais.PORTUGAL, 'F', Modalidade.JUDO, 57,new Date("5/12/1997"), 912020208);

        Atleta at9 = new Atleta("Veronica", Pais.PORTUGAL, 'F', Modalidade.JUDO, 54,new Date("10/12/1999"), 912020201);
        Atleta at10 = new Atleta("Filipa", Pais.PORTUGAL, 'F', Modalidade.JUDO, 50,new Date("2/12/2000"), 912020202);
        Atleta at11 = new Atleta("Matilde", Pais.PORTUGAL, 'F', Modalidade.JUDO, 52,new Date("8/12/1998"), 912020203);
        Atleta at12 = new Atleta("Carina", Pais.PORTUGAL, 'F', Modalidade.JUDO, 55,new Date("12/12/1997"), 912020204);

        Atleta at13 = new Atleta("Fernanda", Pais.PORTUGAL, 'F', Modalidade.JUDO, 60,new Date("21/12/1999"), 912020205);
        Atleta at14 = new Atleta("Olga", Pais.PORTUGAL, 'F', Modalidade.JUDO, 55,new Date("6/12/2000"), 912020206);
        Atleta at15 = new Atleta("Lara", Pais.PORTUGAL, 'F', Modalidade.JUDO, 48,new Date("3/12/1998"), 912020207);
        Atleta at16 = new Atleta("PaulaJudo", Pais.PORTUGAL, 'F', Modalidade.JUDO, 57,new Date("5/12/1997"), 912020208);
        Atleta at17 = new Atleta("PaulaKarate", Pais.PORTUGAL, 'F', Modalidade.KARATE, 57,new Date("5/12/1997"), 912020208);


        Atleta atm = new Atleta("Miguel", Pais.PORTUGAL, 'M', Modalidade.JUDO, 65,new Date("23/10/1999"), 912020210);

        Evento ev1 = new Evento("Campeonato Nacional Judo", new Date("2023/10/12"), new Date("2023/10/16"),"Lisboa", Pais.PORTUGAL, Modalidade.JUDO);
        Evento ev2 = new Evento("Campeonato Regional", new Date("2023/07/12"), new Date("2023/07/15"),"Lisboa", Pais.PORTUGAL, Modalidade.JUDO);
        DadosApp.getInstance().adicionar(ev1);
        DadosApp.getInstance().adicionar(ev2);
        DadosApp.getInstance().adicionar(at1);
        DadosApp.getInstance().adicionar(at2);
        DadosApp.getInstance().adicionar(at3);
        DadosApp.getInstance().adicionar(at4);
        DadosApp.getInstance().adicionar(at5);
        DadosApp.getInstance().adicionar(at6);
        DadosApp.getInstance().adicionar(at7);
        DadosApp.getInstance().adicionar(at8);
        DadosApp.getInstance().adicionar(at9);
        DadosApp.getInstance().adicionar(at10);
        DadosApp.getInstance().adicionar(at12);
        DadosApp.getInstance().adicionar(at13);
        DadosApp.getInstance().adicionar(at14);
        DadosApp.getInstance().adicionar(at15);
        DadosApp.getInstance().adicionar(at16);
        DadosApp.getInstance().adicionar(at17);

        DadosApp.getInstance().adicionar(atm);
        Prova p1 = new Prova(65, 'F', new Date("1/1/2001"), ev1);
        Prova p2 = new Prova(55, 'F', new Date("1/1/2001"), ev1);
        p1.inscrever(at1);
        p1.inscrever(at2);
        p1.inscrever(at3);
        p1.inscrever(at4);
        p1.inscrever(at5);
        p1.inscrever(at6);
        p1.inscrever(at7);
        p1.inscrever(at8);
        p1.inscrever(at9);
        p1.inscrever(at10);
        p1.inscrever(at11);
        p1.inscrever(at12);
        p1.inscrever(at13);
        p1.inscrever(at14);
        p1.inscrever(at15);
        p1.inscrever(at16);

        p2.inscrever(at1);
        p2.inscrever(at2);
        p2.inscrever(at3);
        p2.inscrever(at4);
        p2.inscrever(at5);
        p2.inscrever(at6);
        p2.inscrever(at7);
        p2.inscrever(at8);
        ev1.adicionar(p1);
        ev1.adicionar(p2);
        ev1.iniciar();

        Prova p3 = new Prova(50, 'F', new Date("01/01/2000"), ev2);
        ev2.adicionar(p3);
        p3.inscrever(at2);
        p3.inscrever(at7);
        p3.inscrever(at15);
        p3.inscrever(at10);
        ev2.iniciar();*/


        new JanelaPrincipal("JudoJoka");
    }
}
