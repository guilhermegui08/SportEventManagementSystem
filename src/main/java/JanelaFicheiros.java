import javax.swing.*;

public class JanelaFicheiros extends JFrame{
    private JPanel painelPrincipal;
    private JButton importarAtletasButton;
    private JButton importarDadosDeEventosButton;
    private JButton exportarAtletasButton;
    private JButton exportarDadosDeEventosButton;

    public JanelaFicheiros(String title) {
        super(title);
        setContentPane(painelPrincipal);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
