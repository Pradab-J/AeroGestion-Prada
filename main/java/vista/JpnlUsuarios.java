package vista;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * PANEL administrativo de Usuarios (formulario y tabla). Solo diseño: la lógica
 * está en usuarioControlador.
 *
 * @author Aprendiz
 */
public class JpnlUsuarios extends JPanel {

    // Campos del formulario (públicos, igual que en los demás paneles del proyecto).
    public JTextField txtIdUsuario = new JTextField();
    public JTextField txtUsuario = new JTextField();
    public JTextField txtNombre = new JTextField();
    public JPasswordField txtContrasena = new JPasswordField();
    public JPasswordField txtConfirmar = new JPasswordField();
    public JComboBox<String> cmbRol = new JComboBox<>();
    public JComboBox<String> cmbEstado = new JComboBox<>();

    // Botones.
    public JButton btnGuardar = new JButton("Crear usuario");
    public JButton btnActualizar = new JButton("Guardar cambios");
    public JButton btnCambiarEstado = new JButton("Activar / Desactivar");
    public JButton btnLimpiar = new JButton("Limpiar");

    // Tabla.
    public JTable tblUsuarios = new JTable();
    private final JScrollPane scrollTabla = new JScrollPane(tblUsuarios);

    public JpnlUsuarios(String[] roles, String[] estados) {
        cmbRol.setModel(new DefaultComboBoxModel<>(roles));
        cmbEstado.setModel(new DefaultComboBoxModel<>(estados));
        txtIdUsuario.setEditable(false);
        tblUsuarios.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Usuario", "Nombre", "Rol", "Estado"}));
        aplicarDiseno();
    }

    /** Arma la página con la misma plantilla (cabecera, formulario y tabla) de las demás pantallas. */
    private void aplicarDiseno() {
        Tema.paginaCrud(this, new JPanel(), new JLabel(), "USUARIOS",
                new JLabel(), "Gestión de administradores, roles y estado de acceso",
                new JLabel(), txtIdUsuario, 3,
                new Object[][]{
                    {new JLabel(), "Usuario (para iniciar sesión)", txtUsuario},
                    {new JLabel(), "Nombre completo", txtNombre},
                    {new JLabel(), "Rol", cmbRol},
                    {new JLabel(), "Contraseña", txtContrasena},
                    {new JLabel(), "Confirmar contraseña", txtConfirmar},
                    {new JLabel(), "Estado", cmbEstado}},
                new JButton[]{btnGuardar, btnActualizar, btnCambiarEstado, btnLimpiar},
                new JLabel(), "Usuarios registrados", scrollTabla);
    }
}
