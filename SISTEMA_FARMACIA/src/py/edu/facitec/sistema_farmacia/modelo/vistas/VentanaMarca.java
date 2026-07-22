package py.edu.facitec.sistema_farmacia.modelo.vistas;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import py.edu.facitec.reutilizacion.ventanas.MiVentanaGenerica;
import py.edu.facitec.sistema_farmacia.modelo.controladores.VentanaMarcaController;

public class VentanaMarca extends MiVentanaGenerica {

    private JTextField tDescripcion;
    private JComboBox<String> cbxEstado;

    public VentanaMarca() {
        super();
        
        // 1. Instanciar los componentes del formulario
        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setBounds(20, 20, 100, 25);
        getPanelFormulario().add(lblDescripcion);

        tDescripcion = new JTextField();
        tDescripcion.setBounds(120, 20, 250, 25);
        getPanelFormulario().add(tDescripcion);

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setBounds(20, 60, 100, 25);
        getPanelFormulario().add(lblEstado);

        cbxEstado = new JComboBox<>();
        cbxEstado.setBounds(120, 60, 150, 25);
        cbxEstado.addItem("Activo");
        cbxEstado.addItem("Inactivo");
        getPanelFormulario().add(cbxEstado);

        // 2. Conectar el controlador AL FINAL, cuando los componentes ya existen
        setUpController();
    }

    @Override
    public String getTitulo() {
        return "Gestión de Marcas";
    }

    @Override
    public String getTituloFormulario() {
        return "Datos de la Marca";
    }

    @Override
    public void setUpController() {
        new VentanaMarcaController(this);
    }

    // Getters para que el controlador acceda a los componentes
    public JTextField gettDescripcion() {
        return tDescripcion;
    }

    public JComboBox<String> getcbxEstado() {
        return cbxEstado;
    }
}