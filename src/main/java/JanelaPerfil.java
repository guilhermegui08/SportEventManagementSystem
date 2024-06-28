import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JanelaPerfil extends JFrame{
    private JPanel painelPrincipal;
    private JLabel lblAtleta;
    private JLabel lblUser;
    private JButton btnFiltrar;
    private JList listDadosAtleta;
    private JTable table;
    private AbstractTableModel model = null;

    public JanelaPerfil(String title, String user, Atleta atleta) throws HeadlessException {
        super(title);
        lblAtleta.setText(atleta.getNome());
        setContentPane(painelPrincipal);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        lblUser.setText(user);
        atualizarBtns(user);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        List<HistoricoAtletas> historicoAtletas = atleta.getHistorico();
        if (historicoAtletas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não existem dados registadas");
        }
        criarTabela(historicoAtletas);

        criarListaDados(atleta);

        btnFiltrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Evento> eventos = new ArrayList<>();
                for (HistoricoAtletas item: historicoAtletas) {
                    eventos.add(item.getEvento());
                }

                Object[] options = eventos.toArray();

                //verificar se atleta ja esta inscrito TODO

                JComboBox<Object> eventosAssociados = new JComboBox<>(options);

                Object[] message = {
                        "Evento:", eventosAssociados,
                };

                int option = JOptionPane.showConfirmDialog(null, message, "Enter Details", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {

                    Evento evento = (Evento) eventosAssociados.getSelectedItem();
                    List<HistoricoAtletas> historicoAtletasFiltrado = new ArrayList<>();
                    for (HistoricoAtletas item: historicoAtletas) {
                        if (item.getEvento() == evento){
                            historicoAtletasFiltrado.add(item);
                        }
                    }
                    criarTabela(historicoAtletasFiltrado);
                } else {
                    System.out.println("Dialog canceled.");
                }
            }
        });
    }

    private void criarListaDados(Atleta atleta) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addElement("Nome: " + atleta.getNome());
        switch (atleta.getSexo()){
            case 'F':
                listModel.addElement("Sexo: Feminino");
                break;
            case 'M':
                listModel.addElement("Sexo: Masculino");
                break;
        }
        listModel.addElement("Modalidade: " + atleta.getModalidade());
        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");
        Date data = atleta.getDataNascimento();
        listModel.addElement("Data de nascimento: " + output.format(data));
        listModel.addElement("País: " + atleta.getPais().getNomeCompleto());
        listModel.addElement("Peso: " + atleta.getPeso() + "kg");
        listModel.addElement("Contacto: " + atleta.getContacto());

        listDadosAtleta.setModel(listModel);
    }

    private void atualizarBtns(String user){
        btnFiltrar.setVisible(true);
        btnFiltrar.setEnabled(true);
    }

    private void criarTabela(List<HistoricoAtletas> historicoAtletas){
        this.model = new AbstractTableModel() {

            private String[] columns = {"Posição","Prova", "Evento", "Data"};
            @Override
            public int getRowCount() {
                return historicoAtletas.size();
            }

            @Override
            public int getColumnCount() {
                return columns.length;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                HistoricoAtletas object = historicoAtletas.get(rowIndex);
                Date data;
                SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");
                switch (columnIndex) {
                    case 0:
                        return object.getPosicao()+"º lugar";
                    case 1:
                        return object.getProva().getNome();
                    case 2:
                        return object.getEvento().getNome();
                    case 3:
                        data = object.getProva().getData();
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
