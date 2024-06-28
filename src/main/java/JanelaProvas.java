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

public class JanelaProvas extends JFrame{
    private JLabel lblUser;
    private JButton btnRemover;
    private JButton btnEditar;
    private JButton btnAdicionar;
    private JPanel painelPrincipal;
    private JTable table;
    private JLabel lblEvento;
    private AbstractTableModel model = null;


    public JanelaProvas(String title, String user, Evento evento) throws HeadlessException {
        super(title);
        setContentPane(painelPrincipal);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        lblUser.setText(user);
        lblEvento.setText(evento.getNome());
        atualizarBtns(user);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        List<Prova> provas = evento.getProvas();
        if (provas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não existem provas registadas");
        }
        criarTabela(provas);

        btnAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField pesoField = new JTextField();
                JTextField dataInicioField = new JTextField();
                Character[] options = {'M', 'F'};
                JComboBox<Character> comboBox = new JComboBox<>(options);

                Object[] message = {
                        "Peso max:", pesoField,
                        "Sexo:", comboBox,
                };

                UIManager.put("OptionPane.okButtonText", "Guardar");
                int option = JOptionPane.showConfirmDialog(null, message, "Enter Details", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    int peso = Integer.parseInt(pesoField.getText());
                    char sexo = (char) comboBox.getSelectedItem();

                    // gerar data random nos dias do evento
                    long dataInicio = evento.getDataInicio().getTime();
                    long dataFim = evento.getDataFim().getTime();
                    long data = dataInicio + (long) (Math.random() * (dataFim - dataInicio));

                    Prova instance = new Prova(peso, sexo, new Date(data), evento);
                    System.out.println(instance.getData().toString());

                    evento.adicionar(instance);
                    atualizarTabela();

                } else {
                    System.out.println("Dialog canceled.");
                }
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (table.getSelectedRowCount() == 1){
                    btnEditar.setEnabled(true);
                    btnRemover.setEnabled(true);
                }
                if (e.getClickCount() == 2){
                    if (table.getSelectedRowCount() == 1){
                        int id = table.getSelectedRow();
                        Prova selected = evento.getProvas().get(id);
                        new JanelaCombates(title,user,selected);
                    }
                }
            }
        });
    }

    private void atualizarBtns(String user){
        switch (user){
            case DadosApp.ADMIN:
                btnEditar.setVisible(true);
                btnRemover.setVisible(true);
                btnAdicionar.setVisible(true);
                break;
            default:
                btnEditar.setVisible(false);
                btnRemover.setVisible(false);
                btnAdicionar.setVisible(false);
        }
    }

    private void criarTabela(List<Prova> provas){
        this.model = new AbstractTableModel() {

            private String[] columns = {"Nome","Sexo", "Data"};
            @Override
            public int getRowCount() {
                return provas.size();
            }

            @Override
            public int getColumnCount() {
                return columns.length;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Prova object = provas.get(rowIndex);
                Date data;
                SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");
                switch (columnIndex) {
                    case 0:
                        return object.getNome();
                    case 1:
                        switch (object.getSexo()){
                            case 'F':
                                return "Feminino";
                            case 'M':
                                return "Masculino";
                        }
                    case 2:
                        data = object.getData();
                        return output.format(data);
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
