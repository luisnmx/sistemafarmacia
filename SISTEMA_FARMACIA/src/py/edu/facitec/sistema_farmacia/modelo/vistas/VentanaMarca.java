package py.edu.facitec.sistema_farmacia.modelo.vistas;

import java.awt.EventQueue;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JComboBox;
import py.edu.facitec.reutilizacion.ventanas.MiVentanaGenerica;

public class VentanaMarca extends MiVentanaGenerica {

    private static final long serialVersionUID = 1L;

    private JTextField textField;    // Descripcion

    /**
     * Launch the application.
     */
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    VentanaMarca dialog = new VentanaMarca();
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    dialog.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the dialog.
     */
    public VentanaMarca() {

        JLabel lblDescripcion = new JLabel("Descripcion:");
        lblDescripcion.setBounds(67, 49, 120, 16);
        getPanelFormulario().add(lblDescripcion);

        textField = new JTextField();
        textField.setBounds(201, 44, 250, 26);
        getPanelFormulario().add(textField);

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setBounds(67, 84, 120, 16);
        getPanelFormulario().add(lblEstado);

        JComboBox<String> cbxEstado = new JComboBox<String>();
        cbxEstado.setBounds(201, 79, 170, 26);
        getPanelFormulario().add(cbxEstado);
    }

    @Override
    protected String getTitulo() {
        return "Registro de marcas";
    }

    @Override
    protected String getTituloFormulario() {
        return "Formulario de Marca";
    }

	public void setUpController() {
		// TODO Auto-generated method stub
		
	}
}