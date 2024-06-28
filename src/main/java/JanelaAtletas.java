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

public class JanelaAtletas extends JFrame{
    private JLabel lblUser;
    private JButton btnAdicionar;
    private JButton btnRemover;
    private JButton btnEditar;
    private JPanel painelPrincipal;
    private JTable table;
    private AbstractTableModel model = null;

    public JanelaAtletas(String title, String user) throws HeadlessException {
        super(title);
        setContentPane(painelPrincipal);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        lblUser.setText(user);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        atualizarBtns(user);

        List<Atleta> atletas = DadosApp.getInstance().getAtletas();
        if (atletas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não existem atletas registados.");
        }
        criarTabela(atletas);

        btnAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getDadosUtilizador(false);
            }
        });

        btnRemover.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRowCount() > 0) {
                    Atleta selected = getAtletaSelecionado();
                    switch (DadosApp.getInstance().remover(selected)) {
                        case 0:
                            JOptionPane.showMessageDialog(null, "Atleta removido com sucesso.");
                            break;
                        case -1:
                            JOptionPane.showMessageDialog(null, "Não é possivel remover o atleta. Está inscrito num evento a decorrer.");
                    }
                }
            }
        });

        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRowCount() > 0){
                    getDadosUtilizador(true);
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
                        Atleta selected = getAtletaSelecionado();
                        new JanelaPerfil(title, user, selected);
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
                btnRemover.setEnabled(false);
                btnEditar.setEnabled(false);
                break;
            default:
                btnEditar.setVisible(false);
                btnRemover.setVisible(false);
                btnAdicionar.setVisible(false);
        }
    }

    private void criarTabela(List<Atleta> atletas) {
        this.model = new AbstractTableModel() {

            private String[] columns = {"Nome", "Peso", "Sexo","Pais", "Data Nascimento"};

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
                        switch (object.getSexo()) {
                            case 'F':
                                return "Feminino";
                            case 'M':
                                return "Masculino";
                        }
                    case 3:
                        return object.getPais().getNomeCompleto();
                    case 4:
                        data = object.getDataNascimento();
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

    private void getDadosUtilizador(boolean editar){
        JTextField nomeField = new JTextField();
        JTextField pesoField = new JTextField();
        Object[] optionsPais = Pais.values();
        JComboBox<Object> paises = new JComboBox<>(optionsPais);
        Object[] optionsMod = Modalidade.values();
        JComboBox<Object> modalidades = new JComboBox<>(optionsMod);
        JTextField contactoField = new JTextField();
        JTextField dataNascimentoField = new JTextField();
        Character[] optionsSex = {'M', 'F'};
        JComboBox<Character> sexoField = new JComboBox<>(optionsSex);

        SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
        Atleta selected = null;

        if (editar){
            selected = getAtletaSelecionado();
            nomeField.setText(selected.getNome());
            pesoField.setText(String.valueOf(selected.getPeso()));
            paises.setSelectedItem(selected.getPais());
            modalidades.setSelectedItem(selected.getModalidade());
            contactoField.setText(String.valueOf(selected.getContacto()));
            dataNascimentoField.setText(spf.format(selected.getDataNascimento()));
            sexoField.setSelectedItem(selected.getSexo());
        }

        Object[] message = {

                "Nome:", nomeField,
                "Sexo:", sexoField,
                "Modalidade:", modalidades,
                "Peso:", pesoField,
                "País:", paises,
                "Telefone:", contactoField,
                "Data Nascim.: '__/__/____'", dataNascimentoField,

        };

        boolean dadosValidos = true;
        Date dataNascim = null;

        do {
            int option = JOptionPane.showConfirmDialog(null, message, "Enter Details", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {

                String nome = nomeField.getText();
                Pais pais = (Pais) paises.getSelectedItem();
                Modalidade modalidade = (Modalidade) modalidades.getSelectedItem();
                try {
                    spf.parse(dataNascimentoField.getText());
                    dataNascim = new Date(dataNascimentoField.getText());
                } catch (Exception ex) {
                    dadosValidos = false;
                }
                long contacto = Long.parseLong(contactoField.getText());
                if (contacto > 999999999 && contacto <= 0) {
                    dadosValidos = false;
                }
                int peso = Integer.parseInt(pesoField.getText());
                if (peso <= 0) {
                    dadosValidos = false;
                }
                char sexo = (char) sexoField.getSelectedItem();

                if (dadosValidos) {
                    if (editar){
                        selected.setNome(nome);
                        selected.setPeso(peso);
                        selected.setContacto(contacto);
                        selected.setPais(pais);
                        selected.setSexo(sexo);
                        selected.setDataNascimento(dataNascim);
                        JOptionPane.showMessageDialog(null, "Atleta aterado com sucesso");
                    }else {
                        Atleta instance = new Atleta(nome, pais, sexo, modalidade, peso, dataNascim, contacto);
                        DadosApp.getInstance().adicionar(instance);
                        JOptionPane.showMessageDialog(null, "Atleta registado com sucesso");
                    }
                    atualizarTabela();
                } else {
                    JOptionPane.showMessageDialog(null, "Dados inválidos");
                }
            } else {
                System.out.println("Dialog canceled.");
                break;
            }
        }while (dadosValidos == false);
    }

    private Atleta getAtletaSelecionado(){
        int id = table.getSelectedRow();
        return DadosApp.getInstance().getAtletas().get(id);
    }
}
