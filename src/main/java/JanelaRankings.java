import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.channels.Pipe;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JanelaRankings extends JFrame{
    private JLabel lblUser;
    private JPanel painelPrincipal;
    private JComboBox select;
    private JTable table;
    private DefaultTableModel model;


    public JanelaRankings(String title, String user) throws HeadlessException {
        super(title);
        setContentPane(painelPrincipal);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        lblUser.setText(user);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        List<Pais> paisesComHistorico = DadosApp.getInstance().getPaisesComHist();
        if (paisesComHistorico.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não existem dados registadas");
        }else {
            select.addItem("Global");

            select.setSelectedItem("Global");
            for (Evento evento : DadosApp.getInstance().getEventos()) {
                if (evento.getEstado() == Estado.TERMINADO) {
                    select.addItem(evento);
                }
            }

            criarTabela(paisesComHistorico, null);
        }

        select.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object selectedItem = select.getSelectedItem();
                if (!selectedItem.equals("Global")){
                    Evento eventoSelec = (Evento) selectedItem;
                    criarTabela(paisesComHistorico, eventoSelec);
                }else {
                    criarTabela(paisesComHistorico, null);
                }
            }
        });
    }

    private void criarTabela(List<Pais> paisesComHistorico, Evento evento){

        //table.setEnabled(true);
        model = new DefaultTableModel();

        model.addColumn("Pais");
        model.addColumn("1ºlugar");
        model.addColumn("2ºlugar");
        model.addColumn("3ºlugar");
        if (evento != null){
            model.addColumn("Evento");
        }

        // Iterate over each country and its historico list
        if (evento == null) {
            for (Pais pais : paisesComHistorico) {
                int primeiros = 0, segundos = 0, terceiros = 0;
                for (HistoricoPaises historico : DadosApp.getInstance().getHistorico(pais)) {

                    primeiros += historico.getPrimeiro();
                    segundos += historico.getSegundo();
                    terceiros += historico.getTerceiro();
                }
                if (primeiros == 0 && segundos == 0 && terceiros == 0){
                    continue;
                }
                Object[] row = {
                        pais.getNomeCompleto(),
                        primeiros,
                        segundos,
                        terceiros,
                        //historico.getEvento().toString()
                };
                model.addRow(row);
            }
        }
        if (evento != null){
            for (Pais pais : paisesComHistorico) {
                for (HistoricoPaises historico : DadosApp.getInstance().getHistorico(pais)) {
                    if (historico.getEvento() == evento){
                        Object[] row = {
                                pais.getNomeCompleto(),
                                historico.getPrimeiro(),
                                historico.getSegundo(),
                                historico.getTerceiro(),
                                historico.getEvento().toString()
                        };
                        model.addRow(row);
                    }
                }

            }
        }

        table.setModel(model);
        table.setEnabled(false);


        /*this.model = new AbstractTableModel() {

            private String[] columns = {"País" ,"1ºlugar","2ºlugar", "3ºlugar", "Evento"};
            @Override
            public int getRowCount() {
                return paisesComHistorico.size();
            }

            @Override
            public int getColumnCount() {
                return columns.length;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Pais object = paisesComHistorico.get(rowIndex);
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
        table.setModel(model);*/
    }
}
