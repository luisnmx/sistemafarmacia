package py.edu.facitec.sistema_farmacia.modelo.vistas;

import java.awt.EventQueue;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JComboBox;
import py.edu.facitec.reutilizacion.ventanas.MiVentanaGenerica;

public class VentanaCategoria extends MiVentanaGenerica {

    private static final long serialVersionUID = 1L;

    private JTextField textField;    // Descripcion de la categoria
    private JComboBox<String> cbxEstado;  // Estado (Activo/Inactivo)

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
                    VentanaCategoria dialog = new VentanaCategoria();
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
    public VentanaCategoria() {

        JLabel lblDescripcion = new JLabel("Descripcion:");
        lblDescripcion.setBounds(67, 49, 120, 16);
        getPanelFormulario().add(lblDescripcion);

        textField = new JTextField();
        textField.setBounds(201, 44, 250, 26);
        getPanelFormulario().add(textField);

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setBounds(67, 84, 120, 16);
        getPanelFormulario().add(lblEstado);

        cbxEstado = new JComboBox<String>();
        cbxEstado.addItem("Activo");
        cbxEstado.addItem("Inactivo");
        cbxEstado.setBounds(201, 79, 170, 26);
        getPanelFormulario().add(cbxEstado);
    }

    public JTextField gettDescripcion() {
        return textField;
    }

    public JComboBox<String> getcbxEstado() {
        return cbxEstado;
    }

    @Override
    protected String getTitulo() {
        return "Registro de categorias";
    }

    @Override
    protected String getTituloFormulario() {
        return "Formulario de Categoria";
    }

	public void setUpController() {
		// TODO Auto-generated method stub
		
	}
}
