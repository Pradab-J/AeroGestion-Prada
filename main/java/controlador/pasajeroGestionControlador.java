/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */



package controlador;

import modelo.DAO.integridadDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import modelo.DAO.pasajeroGestionDAO;
import modelo.entidad.pasajero;
import vista.JpnlGestionPasajeros;

/** CONTROLADOR de Pasajeros en el panel administrativo (CRUD). */
public class pasajeroGestionControlador
        implements ActionListener {

    private JpnlGestionPasajeros vista;
    private pasajeroGestionDAO dao;

    /** Constructor: guarda la vista y los DAO y conecta los botones con sus acciones. */
    public pasajeroGestionControlador(
            JpnlGestionPasajeros vista,
            pasajeroGestionDAO dao) {

        this.vista = vista;
        this.dao = dao;

        vista.btnGuardar.addActionListener(this);
        vista.btnEditar.addActionListener(this);
        vista.btnEliminar.addActionListener(this);
        vista.btnLimpiar.addActionListener(this);

        cargarPasajeros();

        vista.tblPasajeros.addMouseListener(
                new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(
                    java.awt.event.MouseEvent e) {

                cargarDatosTabla();
            }
        });
    }

    // =========================
    // EVENTOS
    // =========================

    /** Recibe los clics de los botones de la vista y llama a la acción que corresponde. */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vista.btnGuardar) {

            guardar();
        }

        if (e.getSource() == vista.btnEditar) {

            editar();
        }

        if (e.getSource() == vista.btnEliminar) {

            eliminar();
        }

        if (e.getSource() == vista.btnLimpiar) {

            limpiar();
        }
    }

    // =========================
    // GUARDAR
    // =========================

    /** Valida y guarda un pasajero nuevo; luego refresca la tabla. */
    private void guardar() {

        if (!validarCampos()) {
            return;
        }
        if (integridadDAO.existe("pasajeros", "documento", vista.txtDocumento.getText().trim())) {
            Validar.aviso(vista, "Ya existe un pasajero registrado con el documento "
                    + vista.txtDocumento.getText().trim() + ".");
            return;
        }

        pasajero p =
                obtenerDatosFormulario();

        if (dao.guardar(p)) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Pasajero guardado correctamente."
            );

            cargarPasajeros();
            limpiar();

        } else {

            JOptionPane.showMessageDialog(
                    vista,
                    "No se pudo guardar el pasajero."
            );
        }
    }

    // =========================
    // EDITAR
    // =========================

    /** Valida y actualiza el pasajero seleccionado. */
    private void editar() {

        if (vista.txtDocumento.getText()
                .trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Seleccione un pasajero."
            );

            return;
        }

        if (!validarCampos()) {
            return;
        }
        if (!integridadDAO.existe("pasajeros", "documento", vista.txtDocumento.getText().trim())) {
            Validar.aviso(vista, "Ese documento no corresponde a un pasajero registrado.\n"
                    + "El documento identifica al pasajero y no se puede cambiar.");
            return;
        }

        pasajero p =
                obtenerDatosFormulario();

        if (dao.actualizar(p)) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Pasajero actualizado correctamente."
            );

            cargarPasajeros();
            limpiar();

        } else {

            JOptionPane.showMessageDialog(
                    vista,
                    "No se pudo actualizar el pasajero."
            );
        }
    }

    // =========================
    // ELIMINAR
    // =========================

    /** Elimina el pasajero seleccionado tras confirmar y comprobar que no tenga tiquetes. */
    private void eliminar() {
        String docEliminar = vista.txtDocumento.getText().trim();
        if (!docEliminar.isEmpty()) {
            int tiquetes = integridadDAO.contar("tiquete", "fk_pasajero", docEliminar);
            if (tiquetes > 0) {
                Validar.aviso(vista, "No se puede eliminar este pasajero porque tiene " + tiquetes
                        + " tiquete(s) registrado(s).");
                return;
            }
        }

        String documento =
                vista.txtDocumento.getText().trim();

        if (documento.isEmpty()) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Seleccione un pasajero."
            );

            return;
        }

        int respuesta =
                JOptionPane.showConfirmDialog(
                        vista,
                        "¿Está seguro de eliminar este pasajero?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION
                );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        if (dao.eliminar(documento)) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Pasajero eliminado correctamente."
            );

            cargarPasajeros();
            limpiar();

        } else {

            JOptionPane.showMessageDialog(
                    vista,
                    "No se pudo eliminar el pasajero."
            );
        }
    }

    // =========================
    // OBTENER DATOS
    // =========================

    /** Arma un objeto pasajero con lo escrito en el formulario. */
    private pasajero obtenerDatosFormulario() {

        pasajero p =
                new pasajero();

        p.setDocumento(
                vista.txtDocumento
                        .getText()
                        .trim()
        );

        p.setTipoDocumento(
                vista.cmbTipoDocumento
                        .getSelectedItem()
                        .toString()
        );

        p.setNombre(
                vista.txtNombre
                        .getText()
                        .trim()
        );

        p.setApellido(
                vista.txtApellido
                        .getText()
                        .trim()
        );

        p.setGenero(
                vista.cmbGenero
                        .getSelectedItem()
                        .toString()
        );

        if (vista.dateFechaNacimiento.getDate()
                != null) {

            SimpleDateFormat formato =
                    new SimpleDateFormat(
                            "yyyy-MM-dd"
                    );

            p.setFechaNacimiento(
                    formato.format(
                            vista.dateFechaNacimiento
                                    .getDate()
                    )
            );
        }

        p.setNacionalidad(
                vista.txtNacionalidad
                        .getText()
                        .trim()
        );

        p.setTelefono(
                vista.txtTelefono
                        .getText()
                        .trim()
        );

        p.setCorreo(
                vista.txtCorreo
                        .getText()
                        .trim()
        );

        return p;
    }

    // =========================
    // VALIDAR
    // =========================

    /** Revisa los campos del formulario; avisa del primer error y devuelve false si hay datos inválidos. */
    private boolean validarCampos() {
        String doc = Validar.limpiar(vista.txtDocumento.getText());
        String nombre = Validar.limpiar(vista.txtNombre.getText());
        String apellido = Validar.limpiar(vista.txtApellido.getText());
        String nacionalidad = Validar.limpiar(vista.txtNacionalidad.getText());
        String telefono = Validar.limpiar(vista.txtTelefono.getText()).replace(" ", "").replace("-", "");
        String correo = Validar.limpiar(vista.txtCorreo.getText()).toLowerCase();
        vista.txtDocumento.setText(doc);
        vista.txtNombre.setText(nombre);
        vista.txtApellido.setText(apellido);
        vista.txtNacionalidad.setText(nacionalidad);
        vista.txtTelefono.setText(telefono);
        vista.txtCorreo.setText(correo);

        Object tipo = vista.cmbTipoDocumento.getSelectedItem();
        String tipoTxt = tipo == null ? "" : tipo.toString();
        java.util.Date fn = vista.dateFechaNacimiento.getDate();

        String error = Validar.primero(
                Validar.seleccion("Tipo de documento", tipo),
                Validar.documento(tipoTxt, doc),
                Validar.nombre("Nombre", nombre, 2, 50),
                Validar.nombre("Apellido", apellido, 2, 50),
                Validar.seleccion("Género", vista.cmbGenero.getSelectedItem()),
                Validar.nombre("Nacionalidad", nacionalidad, 3, 40),
                Validar.telefono("Teléfono", telefono),
                Validar.correo("Correo", correo),
                fn == null ? "Seleccione la fecha de nacimiento." : null);

        if (error == null) {
            java.time.LocalDate nacimiento = fn.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            if (nacimiento.isAfter(java.time.LocalDate.now())) {
                error = "La fecha de nacimiento no puede ser futura.";
            } else if (nacimiento.isBefore(java.time.LocalDate.now().minusYears(120))) {
                error = "La fecha de nacimiento no es válida (más de 120 años).";
            }
        }
        return !Validar.mostrar(vista, error);
    }

    // =========================
    // CARGAR TABLA
    // =========================

    /** Carga los pasajeros de la base de datos en la tabla. */
    public void cargarPasajeros() {

        ArrayList<pasajero> lista =
                dao.listar();

        DefaultTableModel modelo =
                new DefaultTableModel();

        modelo.addColumn("DOCUMENTO");
        modelo.addColumn("TIPO DOCUMENTO");
        modelo.addColumn("NOMBRE");
        modelo.addColumn("APELLIDO");
        modelo.addColumn("GÉNERO");
        modelo.addColumn("FECHA NACIMIENTO");
        modelo.addColumn("NACIONALIDAD");
        modelo.addColumn("TELÉFONO");
        modelo.addColumn("CORREO");

        for (pasajero p : lista) {

            modelo.addRow(
                    new Object[]{
                        p.getDocumento(),
                        p.getTipoDocumento(),
                        p.getNombre(),
                        p.getApellido(),
                        p.getGenero(),
                        p.getFechaNacimiento(),
                        p.getNacionalidad(),
                        p.getTelefono(),
                        p.getCorreo()
                    }
            );
        }

        vista.tblPasajeros.setModel(modelo);
    }

    // =========================
    // CARGAR DATOS DE TABLA
    // =========================

    /** Pasa al formulario los datos de la fila elegida en la tabla. */
    private void cargarDatosTabla() {

        int fila =
                vista.tblPasajeros
                        .getSelectedRow();

        if (fila == -1) {
            return;
        }

        vista.txtDocumento.setText(
                vista.tblPasajeros
                        .getValueAt(fila, 0)
                        .toString()
        );

        vista.cmbTipoDocumento.setSelectedItem(
                vista.tblPasajeros
                        .getValueAt(fila, 1)
                        .toString()
        );

        vista.txtNombre.setText(
                vista.tblPasajeros
                        .getValueAt(fila, 2)
                        .toString()
        );

        vista.txtApellido.setText(
                vista.tblPasajeros
                        .getValueAt(fila, 3)
                        .toString()
        );

        vista.cmbGenero.setSelectedItem(
                vista.tblPasajeros
                        .getValueAt(fila, 4)
                        .toString()
        );

        try {

            SimpleDateFormat formato =
                    new SimpleDateFormat(
                            "yyyy-MM-dd"
                    );

            java.util.Date fecha =
                    formato.parse(
                            vista.tblPasajeros
                                    .getValueAt(fila, 5)
                                    .toString()
                    );

            vista.dateFechaNacimiento
                    .setDate(fecha);

        } catch (Exception e) {

            vista.dateFechaNacimiento
                    .setDate(null);
        }

        vista.txtNacionalidad.setText(
                vista.tblPasajeros
                        .getValueAt(fila, 6)
                        .toString()
        );

        vista.txtTelefono.setText(
                vista.tblPasajeros
                        .getValueAt(fila, 7)
                        .toString()
        );

        vista.txtCorreo.setText(
                vista.tblPasajeros
                        .getValueAt(fila, 8)
                        .toString()
        );
    }

    // =========================
    // LIMPIAR
    // =========================

    /** Vacía el formulario y quita la selección de la tabla. */
    private void limpiar() {

        vista.txtDocumento.setText("");
        vista.txtNombre.setText("");
        vista.txtApellido.setText("");
        vista.txtNacionalidad.setText("");
        vista.txtTelefono.setText("");
        vista.txtCorreo.setText("");

        if (vista.cmbTipoDocumento
                .getItemCount() > 0) {

            vista.cmbTipoDocumento
                    .setSelectedIndex(0);
        }

        if (vista.cmbGenero
                .getItemCount() > 0) {

            vista.cmbGenero
                    .setSelectedIndex(0);
        }

        vista.dateFechaNacimiento
                .setDate(null);

        vista.tblPasajeros
                .clearSelection();
    }
}