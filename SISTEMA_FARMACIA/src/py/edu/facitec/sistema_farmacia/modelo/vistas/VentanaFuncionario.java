package py.edu.facitec.sistema_farmacia.modelo.vistas;

import java.awt.EventQueue;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JComboBox;
import py.edu.facitec.reutilizacion.ventanas.MiVentanaGenerica;

public class VentanaFuncionario extends MiVentanaGenerica {

    private static final long serialVersionUID = 1L;

    private JTextField textField;    // Nombre
    private JTextField textField_1;  // Apellido
    private JTextField textField_2;  // Documento
    private JTextField textField_3;  // Email
    private JTextField textField_4;  // Telefono
    private JTextField textField_5;  // Cargo
    private JComboBox<String> cbxEstado; // Estado (Activo/Inactivo)

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
                    VentanaFuncionario dialog = new VentanaFuncionario();
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
    public VentanaFuncionario() {

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(67, 49, 120, 16);
        getPanelFormulario().add(lblNombre);

        textField = new JTextField();
        textField.setBounds(201, 44, 250, 26);
        getPanelFormulario().add(textField);

        JLabel lblApellido = new JLabel("Apellido:");
        lblApellido.setBounds(67, 84, 120, 16);
        getPanelFormulario().add(lblApellido);

        textField_1 = new JTextField();
        textField_1.setBounds(201, 79, 250, 26);
        getPanelFormulario().add(textField_1);

        JLabel lblDocumento = new JLabel("Documento:");
        lblDocumento.setBounds(67, 119, 120, 16);
        getPanelFormulario().add(lblDocumento);

        textField_2 = new JTextField();
        textField_2.setBounds(201, 114, 130, 26);
        getPanelFormulario().add(textField_2);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(67, 154, 120, 16);
        getPanelFormulario().add(lblEmail);

        textField_3 = new JTextField();
        textField_3.setBounds(201, 149, 250, 26);
        getPanelFormulario().add(textField_3);

        JLabel lblTelefono = new JLabel("Telefono:");
        lblTelefono.setBounds(67, 189, 120, 16);
        getPanelFormulario().add(lblTelefono);

        textField_4 = new JTextField();
        textField_4.setBounds(201, 184, 130, 26);
        getPanelFormulario().add(textField_4);

        JLabel lblCargo = new JLabel("Cargo:");
        lblCargo.setBounds(67, 224, 120, 16);
        getPanelFormulario().add(lblCargo);

        textField_5 = new JTextField();
        textField_5.setBounds(201, 219, 130, 26);
        getPanelFormulario().add(textField_5);

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setBounds(67, 259, 120, 16);
        getPanelFormulario().add(lblEstado);

        cbxEstado = new JComboBox<String>();
        cbxEstado.addItem("Activo");
        cbxEstado.addItem("Inactivo");
        cbxEstado.setBounds(201, 254, 170, 26);
        getPanelFormulario().add(cbxEstado);
    }

    public JTextField gettNombre() {
        return textField;
    }

    public JTextField gettApellido() {
        return textField_1;
    }

    public JTextField gettDocumento() {
        return textField_2;
    }

    public JTextField gettEmail() {
        return textField_3;
    }

    public JTextField gettTelefono() {
        return textField_4;
    }

    public JTextField gettCargo() {
        return textField_5;
    }

    public JComboBox<String> getcbxEstado() {
        return cbxEstado;
    }

    @Override
    protected String getTitulo() {
        return "Registro de funcionarios";
    }

    @Override
    protected String getTituloFormulario() {
        return "Formulario de Funcionario";
    }

	public void setUpController() {
		// TODO Auto-generated method stub
		
	}
}