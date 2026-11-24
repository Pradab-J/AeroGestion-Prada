package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.DAO.integridadDAO;
import modelo.DAO.usuarioDAO;
import modelo.entidad.usuario;
import vista.JpnlUsuarios;
import vista.Tema;

/**
 * CONTROLADOR de la gestión de usuarios del sistema: crear un administrador,
 * asignarle un rol, editar sus datos y cambiar su estado (Activo / Inactivo).
 */
public class usuarioControlador implements ActionListener {

    /** Roles disponibles. Para agregar otro rol basta con añadirlo aquí. */
    public static final String[] ROLES = {"Administrador", "Operador"};
    /** Estados disponibles. Solo un usuario "Activo" puede iniciar sesión. */
    public static final String[] ESTADOS = {"Activo", "Inactivo"};

    private static final String ROL_ADMIN = "Administrador";
    private static final String ESTADO_ACTIVO = "Activo";
    private static final String ESTADO_INACTIVO = "Inactivo";

    private final JpnlUsuarios vista;
    private final usuarioDAO dao;

    /** Constructor: guarda la vista y el DAO y conecta los botones con sus acciones. */
    public usuarioControlador(JpnlUsuarios vista, usuarioDAO dao) {
        this.vista = vista;
        this.dao = dao;

        vista.btnGuardar.addActionListener(this);
        vista.btnActualizar.addActionListener(this);
        vista.btnCambiarEstado.addActionListener(this);
        vista.btnLimpiar.addActionListener(this);

        // Al hacer clic en una fila, sus datos pasan al formulario.
        vista.tblUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                cargarFilaSeleccionada();
            }
        });

        listarUsuarios();
        // Recarga la tabla cada vez que se abre la pantalla.
        Tema.alMostrar(vista, this::listarUsuarios);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnGuardar) {
            crearUsuario();
        } else if (e.getSource() == vista.btnActualizar) {
            actualizarUsuario();
        } else if (e.getSource() == vista.btnCambiarEstado) {
            cambiarEstado();
        } else if (e.getSource() == vista.btnLimpiar) {
            limpiarCampos();
        }
    }

    // =========================================================
    // CREAR
    // =========================================================
    /** Valida el formulario y registra un usuario nuevo. */
    private void crearUsuario() {
        if (!vista.txtIdUsuario.getText().trim().isEmpty()) {
            Validar.aviso(vista, "Hay un usuario seleccionado. Pulse «Limpiar» para crear uno nuevo.");
            return;
        }
        String login = Validar.limpiar(vista.txtUsuario.getText());
        String nombre = Validar.limpiar(vista.txtNombre.getText());
        String clave = new String(vista.txtContrasena.getPassword());
        String confirmar = new String(vista.txtConfirmar.getPassword());

        String error = Validar.primero(
                validarLogin(login),
                Validar.nombre("Nombre completo", nombre, 3, 60),
                Validar.seleccion("Rol", vista.cmbRol.getSelectedItem()),
                Validar.seleccion("Estado", vista.cmbEstado.getSelectedItem()),
                validarClave(clave, confirmar, true),
                integridadDAO.existe("usuario", "usuario", login)
                ? "Ya existe un usuario con el nombre «" + login + "»." : null);
        if (Validar.mostrar(vista, error)) {
            return;
        }

        usuario u = new usuario();
        u.setUsuario(login);
        u.setNombre(nombre);
        u.setContraseña(clave);
        u.setRol(vista.cmbRol.getSelectedItem().toString());
        u.setEstado(vista.cmbEstado.getSelectedItem().toString());

        if (dao.crear(u)) {
            JOptionPane.showMessageDialog(vista, "Usuario creado correctamente.");
            limpiarCampos();
            listarUsuarios();
        } else {
            JOptionPane.showMessageDialog(vista, "No se pudo crear el usuario.");
        }
    }

    // =========================================================
    // ACTUALIZAR (nombre, rol, estado y, si se escribe, contraseña)
    // =========================================================
    private void actualizarUsuario() {
        Integer id = Validar.aEntero(vista.txtIdUsuario.getText());
        if (id == null) {
            Validar.aviso(vista, "Seleccione un usuario de la tabla antes de guardar cambios.");
            return;
        }
        String nombre = Validar.limpiar(vista.txtNombre.getText());
        String rol = vista.cmbRol.getSelectedItem() == null ? null : vista.cmbRol.getSelectedItem().toString();
        String estado = vista.cmbEstado.getSelectedItem() == null ? null : vista.cmbEstado.getSelectedItem().toString();
        String clave = new String(vista.txtContrasena.getPassword());
        String confirmar = new String(vista.txtConfirmar.getPassword());

        String error = Validar.primero(
                Validar.nombre("Nombre completo", nombre, 3, 60),
                Validar.seleccion("Rol", rol),
                Validar.seleccion("Estado", estado),
                clave.isEmpty() && confirmar.isEmpty() ? null : validarClave(clave, confirmar, false),
                dejariaSinAdministrador(id, rol, estado)
                ? "No puede quitar el rol ni desactivar al único administrador activo.\n"
                + "Cree o active primero otro administrador." : null);
        if (Validar.mostrar(vista, error)) {
            return;
        }

        usuario u = new usuario();
        u.setId_usuario(id);
        u.setNombre(nombre);
        u.setRol(rol);
        u.setEstado(estado);
        u.setContraseña(clave); // vacía = no se cambia

        if (dao.actualizar(u)) {
            JOptionPane.showMessageDialog(vista, "Usuario actualizado correctamente.");
            limpiarCampos();
            listarUsuarios();
        } else {
            JOptionPane.showMessageDialog(vista, "No se pudo actualizar el usuario.");
        }
    }

    // =========================================================
    // CAMBIAR ESTADO (Activo <-> Inactivo)
    // =========================================================
    private void cambiarEstado() {
        Integer id = Validar.aEntero(vista.txtIdUsuario.getText());
        int fila = vista.tblUsuarios.getSelectedRow();
        if (id == null || fila < 0) {
            Validar.aviso(vista, "Seleccione un usuario de la tabla para cambiar su estado.");
            return;
        }
        String rolActual = String.valueOf(vista.tblUsuarios.getValueAt(fila, 3));
        String estadoActual = String.valueOf(vista.tblUsuarios.getValueAt(fila, 4));
        String login = String.valueOf(vista.tblUsuarios.getValueAt(fila, 1));
        String nuevo = ESTADO_ACTIVO.equalsIgnoreCase(estadoActual) ? ESTADO_INACTIVO : ESTADO_ACTIVO;

        if (dejariaSinAdministrador(id, rolActual, nuevo)) {
            Validar.aviso(vista, "No puede desactivar al único administrador activo.\n"
                    + "Cree o active primero otro administrador.");
            return;
        }
        int ok = JOptionPane.showConfirmDialog(vista,
                "¿Cambiar el estado de «" + login + "» a " + nuevo + "?",
                "Confirmar cambio de estado", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) {
            return;
        }
        if (dao.cambiarEstado(id, nuevo)) {
            JOptionPane.showMessageDialog(vista, "Estado actualizado a " + nuevo + ".");
            limpiarCampos();
            listarUsuarios();
        } else {
            JOptionPane.showMessageDialog(vista, "No se pudo cambiar el estado.");
        }
    }

    // =========================================================
    // VALIDACIONES
    // =========================================================
    /** Usuario de 4 a 30 caracteres: letras, números, punto, guion y guion bajo. */
    private String validarLogin(String login) {
        String e = Validar.texto("Usuario", login, 4, 30);
        if (e != null) {
            return e;
        }
        if (!login.matches("^[A-Za-z0-9._-]+$")) {
            return "«Usuario» solo puede tener letras, números, punto, guion y guion bajo (sin espacios).";
        }
        return null;
    }

    /** Contraseña de 6 a 50 caracteres, sin espacios, y que coincida con la confirmación. */
    private String validarClave(String clave, String confirmar, boolean obligatoria) {
        if (clave.isEmpty()) {
            return obligatoria ? "El campo «Contraseña» es obligatorio." : null;
        }
        if (clave.length() < 6) {
            return "La contraseña debe tener al menos 6 caracteres.";
        }
        if (clave.length() > 50) {
            return "La contraseña no puede superar los 50 caracteres.";
        }
        if (clave.contains(" ")) {
            return "La contraseña no puede tener espacios.";
        }
        if (!clave.equals(confirmar)) {
            return "La contraseña y su confirmación no coinciden.";
        }
        return null;
    }

    /**
     * True si el cambio dejaría al sistema sin ningún administrador activo
     * (nadie podría volver a iniciar sesión).
     */
    private boolean dejariaSinAdministrador(int id, String rolNuevo, String estadoNuevo) {
        boolean seguiraSiendoAdminActivo = ROL_ADMIN.equals(rolNuevo) && ESTADO_ACTIVO.equals(estadoNuevo);
        if (seguiraSiendoAdminActivo) {
            return false;
        }
        return dao.contarAdministradoresActivos(id) == 0;
    }

    // =========================================================
    // TABLA Y FORMULARIO
    // =========================================================
    /** Carga en la tabla todos los usuarios registrados. */
    public void listarUsuarios() {
        ArrayList<usuario> lista = dao.listar();
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        modelo.addColumn("ID");
        modelo.addColumn("Usuario");
        modelo.addColumn("Nombre");
        modelo.addColumn("Rol");
        modelo.addColumn("Estado");
        for (usuario u : lista) {
            modelo.addRow(new Object[]{
                u.getId_usuario(), u.getUsuario(), u.getNombre(), u.getRol(), u.getEstado()});
        }
        vista.tblUsuarios.setModel(modelo);
    }

    /** Pasa los datos de la fila elegida al formulario (la contraseña nunca se muestra). */
    private void cargarFilaSeleccionada() {
        int fila = vista.tblUsuarios.getSelectedRow();
        if (fila < 0) {
            return;
        }
        vista.txtIdUsuario.setText(String.valueOf(vista.tblUsuarios.getValueAt(fila, 0)));
        vista.txtUsuario.setText(String.valueOf(vista.tblUsuarios.getValueAt(fila, 1)));
        vista.txtUsuario.setEditable(false); // el nombre de usuario identifica la cuenta
        vista.txtNombre.setText(String.valueOf(vista.tblUsuarios.getValueAt(fila, 2)));
        vista.cmbRol.setSelectedItem(String.valueOf(vista.tblUsuarios.getValueAt(fila, 3)));
        vista.cmbEstado.setSelectedItem(String.valueOf(vista.tblUsuarios.getValueAt(fila, 4)));
        vista.txtContrasena.setText("");
        vista.txtConfirmar.setText("");
    }

    /** Deja el formulario vacío, listo para crear un usuario nuevo. */
    private void limpiarCampos() {
        vista.txtIdUsuario.setText("");
        vista.txtUsuario.setText("");
        vista.txtUsuario.setEditable(true);
        vista.txtNombre.setText("");
        vista.txtContrasena.setText("");
        vista.txtConfirmar.setText("");
        vista.cmbRol.setSelectedIndex(0);
        vista.cmbEstado.setSelectedIndex(0);
        vista.tblUsuarios.clearSelection();
    }
}
