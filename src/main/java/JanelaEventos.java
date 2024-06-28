import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class JanelaEventos extends JFrame{
    private JPanel painelPrincipal;
    private JLabel lblUser;
    private JButton btnAdicionar;
    private JButton btnIniciarEvento;
    private JButton btnEditar;
    private JButton btnRemover;
    private JTable table;
    //private JList list;
    private AbstractTableModel model = null;

    public JanelaEventos(String title, String user) throws HeadlessException {
        super(title);
        setContentPane(painelPrincipal);
        lblUser.setText(user);
        atualizarBtns(user);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        //atualizarList(DadosApp.getInstance().getEventos());

        List<Evento> eventos = DadosApp.getInstance().getEventos();
        if (eventos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não existem eventos registados");
        }
        criarTabela(eventos);

        btnAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField nameField = new JTextField();
                JTextField dataInicioField = new JTextField();
                JTextField dataFimField = new JTextField();
                JTextField localField = new JTextField();
                Object[] optionsPais = Pais.values();
                JComboBox<Object> paises = new JComboBox<>(optionsPais);

                Object[] options = {Modalidade.JUDO, Modalidade.JUIJITSU, Modalidade.KARATE};

                JComboBox<Object> modalidades = new JComboBox<>(options);


                Object[] message = {
                        "Name:", nameField,
                        "Data Inicio:", dataInicioField,
                        "Data Fim:", dataFimField,
                        "Modalidade: ", modalidades,
                        "Local:", localField,
                        "País:", paises,
                };

                UIManager.put("OptionPane.okButtonText", "Guardar");
                int option = JOptionPane.showConfirmDialog(null, message, "Enter Details", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String name = nameField.getText();
                    Date dataInicio = new Date(dataInicioField.getText());
                    Date dataFim = new Date(dataFimField.getText());
                    Modalidade modalidade = (Modalidade) modalidades.getSelectedItem();
                    String local = localField.getText();
                    Pais pais = (Pais) paises.getSelectedItem();

                    Evento instance = new Evento(name, dataInicio, dataFim, local, pais, modalidade);

                    DadosApp.getInstance().adicionar(instance);
                    atualizarTabela();

                } else {
                    System.out.println("Dialog canceled.");
                }
            }
        });

        btnIniciarEvento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Evento selected = DadosApp.getInstance().getEventos().get(table.getSelectedRow());
                String message = null;
                if (selected.getEstado() == Estado.POR_INICIAR) {
                    int code = selected.iniciar();
                    switch (code) {
                        case -1:
                            message = "Num. de inscritos por prova tem de ser de 4 ou mais atletas.";
                            break;
                        case -2:
                            message = "Num. de inscritos por prova tem de ser potencia de 2";
                            break;
                        default:
                            message = "Evento '" + selected.getNome() + "' iniciado.";
                            atualizarTabela();
                    }
                    if (message != null)
                        JOptionPane.showMessageDialog(null, message);
                }else{
                    JOptionPane.showMessageDialog(null, "Evento já iniciado");
                }
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (table.getSelectedRowCount() == 1){
                    int id = table.getSelectedRow();
                    Evento selected = DadosApp.getInstance().getEventos().get(id);
                    btnEditar.setEnabled(true);
                    btnRemover.setEnabled(true);
                    btnIniciarEvento.setEnabled(true);
                    if (selected.getEstado() != Estado.POR_INICIAR){
                        btnIniciarEvento.setEnabled(false);
                    }
                    if (e.getClickCount() == 2){
                        if (table.getSelectedRowCount() == 1){
                            new JanelaProvas(title, user, selected);
                        }
                    }
                }

            }
        });
    }

    private void atualizarBtns(String user){
        switch (user){
            case DadosApp.ADMIN:
                btnEditar.setEnabled(false);
                btnRemover.setEnabled(false);
                btnIniciarEvento.setEnabled(false);
                break;
            default:
                btnEditar.setVisible(false);
                btnRemover.setVisible(false);
                btnAdicionar.setVisible(false);
                btnIniciarEvento.setVisible(false);
        }
    }

    private void criarTabela(List<Evento> eventos){
        this.model = new AbstractTableModel() {

            private String[] columns = {"Nome","Data Inicio", "Data Fim", "Local", "Pais", "Estado"};

            @Override
            public int getRowCount() {
                return eventos.size();
            }

            @Override
            public int getColumnCount() {
                return columns.length;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Evento object = eventos.get(rowIndex);
                Date data = null;
                SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");
                switch (columnIndex) {
                    case 0:
                        return object.getNome();
                    case 1:
                        data = object.getDataInicio();
                        return output.format(data);
                    case 2:
                        data = object.getDataFim();
                        return output.format(data);
                    case 3:
                        return object.getLocal();
                    case 4:
                        return object.getPais().getNomeCompleto();
                    case 5:
                        return object.getEstado();
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


    /*
    private void atualizarList(List<Evento> eventos){
        if (eventos != null){
            for (Evento evento: eventos) {
                modeloLista.addElement(evento);
            }
        }
        else {
            modeloLista.addElement("Não existem eventos");
        }
        list.setModel(modeloLista);
    }
    */
}
