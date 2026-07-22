package py.edu.facitec.sistema_farmacia.modelo.vistas;

import java.awt.EventQueue;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JComboBox;
import py.edu.facitec.reutilizacion.ventanas.MiVentanaGenerica;

public class VentanaCliente extends MiVentanaGenerica {

    private static final long serialVersionUID = 1L;

    private JTextField tfRuc;    // RUC/CI
    private JTextField textField_1;  // Nombre
    private JTextField textField_2;  // Apellido
    private JTextField textField_3;  // Telefono
    private JTextField textField_4;  // Direccion
    private JTextField textField_5;  // Email

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
                    VentanaCliente dialog = new VentanaCliente();
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
    public VentanaCliente() {

        JLabel lblRuc = new JLabel("RUC/CI:");
        lblRuc.setBounds(67, 49, 120, 16);
        getPanelFormulario().add(lblRuc);

        tfRuc = new JTextField();
        tfRuc.setBounds(201, 44, 130, 26);
        getPanelFormulario().add(tfRuc);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(67, 84, 120, 16);
        getPanelFormulario().add(lblNombre);

        textField_1 = new JTextField();
        textField_1.setBounds(201, 79, 250, 26);
        getPanelFormulario().add(textField_1);

        JLabel lblApellido = new JLabel("Apellido:");
        lblApellido.setBounds(67, 119, 120, 16);
        getPanelFormulario().add(lblApellido);

        textField_2 = new JTextField();
        textField_2.setBounds(201, 114, 250, 26);
        getPanelFormulario().add(textField_2);

        JLabel lblTelefono = new JLabel("Telefono:");
        lblTelefono.setBounds(67, 154, 120, 16);
        getPanelFormulario().add(lblTelefono);

        textField_3 = new JTextField();
        textField_3.setBounds(201, 149, 130, 26);
        getPanelFormulario().add(textField_3);

        JLabel lblDireccion = new JLabel("Direccion:");
        lblDireccion.setBounds(67, 189, 120, 16);
        getPanelFormulario().add(lblDireccion);

        textField_4 = new JTextField();
        textField_4.setBounds(201, 184, 250, 26);
        getPanelFormulario().add(textField_4);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(67, 224, 120, 16);
        getPanelFormulario().add(lblEmail);

        textField_5 = new JTextField();
        textField_5.setBounds(201, 219, 250, 26);
        getPanelFormulario().add(textField_5);
    }

    @Override
    protected String getTitulo() {
        return "Registro de clientes";
    }

    @Override
    protected String getTituloFormulario() {
        return "Formulario de Cliente";
    }

    public JTextField gettRuc() {
        return tfRuc;
    }

    public JTextField gettNombre() {
        return textField_1;
    }

    public JTextField gettApellido() {
        return textField_2;
    }

    public JTextField gettTelefono() {
        return textField_3;
    }

    public JTextField gettDireccion() {
        return textField_4;
    }

    public JTextField gettEmail() {
        return textField_5;
    }

	public void setUpController() {
		// TODO Auto-generated method stub
		
	}
}