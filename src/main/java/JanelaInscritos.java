import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JanelaInscritos extends JFrame{
    private JLabel lblCombate;
    private JLabel lblUser;
    private JButton btnRemoverAtleta;
    private JButton btnInscreverAtleta;
    private JPanel painelPrincipal;
    private JTable table;
    private AbstractTableModel model = null;

    public JanelaInscritos(String title, Prova prova, String user) throws HeadlessException {
        super(title);
        lblCombate.setText(prova.getNome());
        setContentPane(painelPrincipal);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        lblUser.setText(user);
        atualizarBtns(user, prova);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        List<Atleta> inscritos = prova.getInscritos();
        if (inscritos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não existem atletas inscritos.");
        }
        criarTabela(inscritos);

        btnInscreverAtleta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (prova.getEvento().getEstado() == Estado.INICIADO){
                    JOptionPane.showMessageDialog(null, "Inscrições fechadas. Evento iniciado.");
                }
                else {

                    List<Atleta> atletas = DadosApp.getInstance().getAtletas(prova.getPesoMax(), prova.getSexo(), prova.getEvento().getModalidade());
                    List<Atleta> atletasNãoInscritos = new ArrayList<>();

                    //verificar se atleta ja esta inscrito

                    for (Atleta atleta : atletas) {
                        if (!inscritos.contains(atleta)) {
                            atletasNãoInscritos.add(atleta);
                        }
                    }

                    Object[] options = atletasNãoInscritos.toArray();

                    JComboBox<Object> atletasPermitidos = new JComboBox<>(options);

                    Object[] message = {
                            "Atleta:", atletasPermitidos,
                    };

                    int option = JOptionPane.showConfirmDialog(null, message, "Enter Details", JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        if (!atletasNãoInscritos.isEmpty()) {
                            Atleta atleta = (Atleta) atletasPermitidos.getSelectedItem();
                            switch (prova.inscrever(atleta)) {
                                case 0:
                                    JOptionPane.showMessageDialog(null, "Atleta inscrito com sucesso");
                                    break;
                                case -1:
                                    JOptionPane.showMessageDialog(null, "Atleta já inscrito");
                            }
                            atualizarTabela();
                        }
                    } else {
                        System.out.println("Dialog canceled.");
                    }
                }
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (table.getSelectedRowCount() == 1){
                    btnRemoverAtleta.setEnabled(true);
                }
            }
        });
    }

    private void atualizarBtns(String user, Prova prova){
        switch (user){
            case DadosApp.ADMIN:
                btnInscreverAtleta.setVisible(true);
                btnRemoverAtleta.setVisible(true);
                btnRemoverAtleta.setEnabled(false);
                if (!prova.isInscricoesAbertas()){
                    btnInscreverAtleta.setEnabled(false);
                }else {
                    btnInscreverAtleta.setEnabled(true);
                }

                break;
            default:
                btnInscreverAtleta.setVisible(false);
                btnRemoverAtleta.setVisible(false);
        }
    }

    private void criarTabela(List<Atleta> atletas) {
        this.model = new AbstractTableModel() {

            private String[] columns = {"Nome", "Peso", "Pais", "Data Nascimento"};

            @Override
            public int getRowCount() {
                return atletas.size();
            }

            @Override
            public int getColumnCount() {
                return columns.length;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Atleta object = atletas.get(rowIndex);
                Date data;
                SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");
                switch (columnIndex) {
                    case 0:
                        return object.getNome();
                    case 1:
                        return object.getPeso();
                    case 2:
                        return object.getPais().getNomeCompleto();
                    case 3:
                        data = object.getDataNascimento();
                        return output.format(data);
                    case 7:
                        switch (object.getSexo()) {
                            case 'F':
                                return "Feminino";
                            case 'M':
                                return "Masculino";
                        }
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
