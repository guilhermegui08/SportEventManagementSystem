import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.List;

public class JanelaCombates extends JFrame{
    private JPanel painelPrincipal;
    private JButton btnPesar;
    private JButton btnRegistarResultado;
    private JButton btnInscritos;
    private JLabel lblUser;
    private JLabel lblCombate;
    private JTable table;
    private AbstractTableModel model = null;

    public JanelaCombates(String title, String user, Prova prova) throws HeadlessException {
        super(title);
        lblCombate.setText(prova.getNome());
        setContentPane(painelPrincipal);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        lblUser.setText(user);
        atualizarBtns(user);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        List<Combate> combates = prova.getCombates();
        if (combates.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não existem combates registados. Inscrições abertas.");
        }
        criarTabela(combates);

        btnInscritos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new JanelaInscritos(title, prova, user);
            }
        });

        btnPesar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = table.getSelectedRow();
                Combate combate = prova.getCombates().get(id);
                Atleta atleta1 = combate.getAtleta1();
                Atleta atleta2 = combate.getAtleta2();

                JTextField peso1Field = new JTextField();
                JTextField peso2Field = new JTextField();

                Object[] message = {
                        atleta1.getNome() + ":", peso1Field,
                        atleta2.getNome() + ":", peso2Field,
                };

                int option = JOptionPane.showConfirmDialog(null, message, "Enter Details", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    int peso1 = Integer.parseInt(peso1Field.getText());
                    int peso2 = Integer.parseInt(peso2Field.getText());

                    atleta1.setPeso(peso1);
                    atleta2.setPeso(peso2);

                    boolean atleta1_OK = false;
                    boolean atleta2_OK = false;

                    int pesoMax = prova.getPesoMax();
                    if (peso1 <= pesoMax){
                        atleta1_OK = true;
                    }
                    if (peso2 <= pesoMax){
                        atleta2_OK = true;
                    }

                    if (atleta1_OK && atleta2_OK){
                        combate.setEstado(Estado.INICIADO);
                    }
                    else {
                        Atleta atletaOK = null;
                        if (atleta1_OK) {
                            combate.setVencedor(atleta1, true);
                            atletaOK = atleta1;
                        }

                        if (atleta2_OK){
                            combate.setVencedor(atleta2,true);
                            atletaOK = atleta2;
                        }

                        if (atletaOK != null){
                            prova.criarProxCombate(combate, atletaOK);
                        }else {
                            combate.setVencedor(DadosApp.desclacificado, true);
                            prova.criarProxCombate(combate, null);
                        }

                    }
                    atualizarTabela();
                } else {
                    System.out.println("Dialog canceled.");
                }
            }
        });
        btnRegistarResultado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = table.getSelectedRow();
                Combate combate = prova.getCombates().get(id);

                if (combate.getEstado() == Estado.INICIADO) {
                    Object[] options = {combate.getAtleta1(), combate.getAtleta2()};

                    JComboBox<Object> atletas = new JComboBox<>(options);

                    Object[] message = {
                            "Vencedor:", atletas
                    };

                    int option = JOptionPane.showConfirmDialog(null, message, "Enter Details", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {

                        Atleta atleta = (Atleta) atletas.getSelectedItem();
                        combate.setVencedor(atleta, false);
                        prova.criarProxCombate(combate, atleta);
                        atualizarTabela();

                    } else {
                        System.out.println("Dialog canceled.");
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Combate por iniciar");
                }
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (table.getSelectedRowCount() == 1){
                    int id = table.getSelectedRow();
                    switch (combates.get(id).getEstado()){
                        case INICIADO:
                            btnPesar.setEnabled(false);
                            btnRegistarResultado.setEnabled(true);
                            break;
                        case POR_INICIAR:
                            btnRegistarResultado.setEnabled(false);
                            btnPesar.setEnabled(true);
                            break;
                        default:
                            btnPesar.setEnabled(false);
                            btnRegistarResultado.setEnabled(false);
                    }
                }
            }
        });
    }

    private void atualizarBtns(String user){
        switch (user){
            case DadosApp.JURI:
                btnInscritos.setVisible(true);
                btnPesar.setVisible(true);
                btnRegistarResultado.setVisible(true);
                btnPesar.setEnabled(false);
                btnRegistarResultado.setEnabled(false);
                break;
            default:
                btnInscritos.setVisible(true);
                btnPesar.setVisible(false);
                btnRegistarResultado.setVisible(false);
        }
    }

    private void criarTabela(List<Combate> combates){
        this.model = new AbstractTableModel() {

            private String[] columns = {"Id","Atleta 1", "Atleta 2", "Hora", "Estado", "Ronda", "Vencedor"};
            @Override
            public int getRowCount() {
                return combates.size();
            }

            @Override
            public int getColumnCount() {
                return columns.length;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Combate object = combates.get(rowIndex);
                switch (columnIndex) {
                    case 0:
                        return "Combate " + object.getId();
                    case 1:
                        if (object.getAtleta1() != null && object.getAtleta1().getNome() == "_"){
                            return "Desclacificado";
                        }
                        return object.getAtleta1() != null ? object.getAtleta1() : "Por apurar";
                    case 2:
                        if (object.getAtleta2() != null && object.getAtleta2().getNome() == "_"){
                            return "Desclacificado";
                        }
                        return object.getAtleta2() != null ? object.getAtleta2() : "Por apurar";
                    case 3:
                        return object.getHora(); //////
                    case 4:
                        return object.getEstado();
                    case 5:
                        return object.getRonda();
                    case 6:
                        if (object.getVencedor() != null && object.getVencedor().getNome() == "_"){
                            return "Desclacificado";
                        }
                        return object.getVencedor() != null ? object.getVencedor() : "Por apurar";
                    default:
                        return null;
                }
            }

            @Override
            public String getColumnName(int column) {
                return columns[column];
            }
        };
        table.setModel(model);
    }

    public void atualizarTabela(){
        model.fireTableDataChanged();
    }
}
