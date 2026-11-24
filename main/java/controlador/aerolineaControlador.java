/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */



package controlador;

import modelo.DAO.integridadDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.DAO.aerolineaDAO;
import modelo.entidad.aerolineas;
import vista.JpnlAerolineas;

/**
 * CONTROLADOR de Aerolíneas: conecta el panel JpnlAerolineas con aerolineaDAO.
 * Atiende los botones, valida los datos y refresca la tabla.
 */
public class aerolineaControlador implements ActionListener {

    private JpnlAerolineas vistaAerolinea;
    private aerolineaDAO daoAerolinea;

    /** Constructor: guarda la vista y los DAO y conecta los botones con sus acciones. */
    // =========================
    // CONSTRUCTOR
    // =========================
    public aerolineaControlador(
            JpnlAerolineas vista,
            aerolineaDAO dao) {

        vistaAerolinea = vista;
        daoAerolinea = dao;

        // BOTONES
        vistaAerolinea.btnGuardar.addActionListener(this);
        vistaAerolinea.btnEditar.addActionListener(this);
        vistaAerolinea.btnEliminar.addActionListener(this);
        vistaAerolinea.btnLimpiar.addActionListener(this);

        // =========================
        // SELECCIONAR FILA DE TABLA
        // =========================
        vistaAerolinea.tblAerolineas.addMouseListener(
            new java.awt.event.MouseAdapter() {

                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {

                    int fila =
                            vistaAerolinea.tblAerolineas.getSelectedRow();

                    if (fila >= 0) {

                        vistaAerolinea.txtIdAerolinea.setText(
                                vistaAerolinea.tblAerolineas
                                        .getValueAt(fila, 0)
                                        .toString()
                        );

                        vistaAerolinea.txtNombre.setText(
                                vistaAerolinea.tblAerolineas
                                        .getValueAt(fila, 1)
                                        .toString()
                        );

                        vistaAerolinea.txtCorreo.setText(
                                vistaAerolinea.tblAerolineas
                                        .getValueAt(fila, 2)
                                        .toString()
                        );

                        vistaAerolinea.txtContacto.setText(
                                vistaAerolinea.tblAerolineas
                                        .getValueAt(fila, 3)
                                        .toString()
                        );
                    }
                }
            }
        );

        // CARGAR DATOS AL ABRIR
        listarAerolineas();
    }

    /** Recibe los clics de los botones de la vista y llama a la acción que corresponde. */
    // =========================
    // ACTION PERFORMED
    // =========================
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vistaAerolinea.btnGuardar) {
            guardarAerolinea();
        }

        if (e.getSource() == vistaAerolinea.btnEditar) {
            editarAerolinea();
        }

        if (e.getSource() == vistaAerolinea.btnEliminar) {
            eliminarAerolinea();
        }

        if (e.getSource() == vistaAerolinea.btnLimpiar) {
            limpiarCampos();
        }
    }

    /** Valida el formulario y guarda un nuevo registro de aerolínea; luego refresca la tabla y limpia el formulario. */
    // =========================
    // GUARDAR
    // =========================
    public void guardarAerolinea() {
        if (!validarAerolinea(false)) {
            return;
        }

        String nombre = vistaAerolinea.txtNombre.getText();
        String correo = vistaAerolinea.txtCorreo.getText();
        String contacto = vistaAerolinea.txtContacto.getText();

        // VALIDAR CAMPOS VACÍOS
        if (nombre.isEmpty()
                || correo.isEmpty()
                || contacto.isEmpty()) {

            JOptionPane.showMessageDialog(
                    null,
                    "Complete todos los campos"
            );

            return;
        }

        aerolineas a = new aerolineas();

        a.setNombre(nombre);
        a.setCorreo(correo);
        a.setContacto(contacto);

        if (daoAerolinea.guardar(a)) {

            JOptionPane.showMessageDialog(
                    null,
                    "Aerolínea guardada correctamente"
            );

            listarAerolineas();
            limpiarCampos();

        } else {

            JOptionPane.showMessageDialog(
                    null,
                    "Error al guardar la aerolínea"
            );
        }
    }

    /** Carga en la tabla los registros de aerolínea que hay en la base de datos. */
    // =========================
    // LISTAR
    // =========================
    public void listarAerolineas() {

        ArrayList<aerolineas> lista =
                daoAerolinea.listar();

        DefaultTableModel modelo =
                new DefaultTableModel();

        modelo.addColumn("ID Aerolínea");
        modelo.addColumn("Nombre");
        modelo.addColumn("Correo");
        modelo.addColumn("Contacto");

        for (aerolineas a : lista) {

            Object datos[] = {

                a.getId_aerolinea(),
                a.getNombre(),
                a.getCorreo(),
                a.getContacto()
            };

            modelo.addRow(datos);
        }

        vistaAerolinea.tblAerolineas.setModel(modelo);
    }

    /** Valida y actualiza el registro de aerolínea seleccionado en la tabla. */
    // =========================
    // EDITAR
    // =========================
    public void editarAerolinea() {
        if (!validarAerolinea(true)) {
            return;
        }

        try {

            int id = Integer.parseInt(
                    vistaAerolinea.txtIdAerolinea.getText()
            );

            String nombre =
                    vistaAerolinea.txtNombre.getText();

            String correo =
                    vistaAerolinea.txtCorreo.getText();

            String contacto =
                    vistaAerolinea.txtContacto.getText();

            // VALIDAR CAMPOS VACÍOS
            if (nombre.isEmpty()
                    || correo.isEmpty()
                    || contacto.isEmpty()) {

                JOptionPane.showMessageDialog(
                        null,
                        "Complete todos los campos"
                );

                return;
            }

            aerolineas a = new aerolineas();

            a.setId_aerolinea(id);
            a.setNombre(nombre);
            a.setCorreo(correo);
            a.setContacto(contacto);

            if (daoAerolinea.editar(a)) {

                JOptionPane.showMessageDialog(
                        null,
                        "Aerolínea actualizada correctamente"
                );

                listarAerolineas();
                limpiarCampos();

            } else {

                JOptionPane.showMessageDialog(
                        null,
                        "Error al actualizar la aerolínea"
                );
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    null,
                    "Seleccione una aerolínea de la tabla"
            );
        }
    }

    /** Elimina el registro de aerolínea seleccionado, tras pedir confirmación y comprobar que nada dependa de él. */
    // =========================
    // ELIMINAR
    // =========================
    public void eliminarAerolinea() {
        if (!puedeEliminarAerolinea()) {
            return;
        }

        try {

            int id = Integer.parseInt(
                    vistaAerolinea.txtIdAerolinea.getText()
            );

            int respuesta =
                    JOptionPane.showConfirmDialog(
                            null,
                            "¿Está seguro de eliminar esta aerolínea?",
                            "Confirmar eliminación",
                            JOptionPane.YES_NO_OPTION
                    );

            if (respuesta == JOptionPane.YES_OPTION) {

                if (daoAerolinea.eliminar(id)) {

                    JOptionPane.showMessageDialog(
                            null,
                            "Aerolínea eliminada correctamente"
                    );

                    listarAerolineas();
                    limpiarCampos();

                } else {

                    JOptionPane.showMessageDialog(
                            null,
                            "Error al eliminar la aerolínea"
                    );
                }
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    null,
                    "Seleccione una aerolínea de la tabla"
            );
        }
    }

    /** Vacía el formulario y quita la selección de la tabla. */
    // =========================
    // LIMPIAR
    // =========================
    public void limpiarCampos() {

        vistaAerolinea.txtIdAerolinea.setText("");
        vistaAerolinea.txtNombre.setText("");
        vistaAerolinea.txtCorreo.setText("");
        vistaAerolinea.txtContacto.setText("");

        vistaAerolinea.tblAerolineas.clearSelection();
    }

    /** Revisa los campos del formulario; muestra el primer error y devuelve false si hay datos inválidos. */
    // =========================================================
    // VALIDACIONES
    // =========================================================
    private boolean validarAerolinea(boolean edicion) {
        Integer id = null;
        if (edicion) {
            id = Validar.aEntero(vistaAerolinea.txtIdAerolinea.getText());
            if (id == null) {
                Validar.aviso(vistaAerolinea, "Seleccione una aerolínea de la tabla antes de editar.");
                return false;
            }
        }
        String nombre = Validar.limpiar(vistaAerolinea.txtNombre.getText());
        String correo = Validar.limpiar(vistaAerolinea.txtCorreo.getText());
        String contacto = Validar.limpiar(vistaAerolinea.txtContacto.getText());
        vistaAerolinea.txtNombre.setText(nombre);
        vistaAerolinea.txtCorreo.setText(correo);
        vistaAerolinea.txtContacto.setText(contacto);

        String error = Validar.primero(
                Validar.texto("Nombre", nombre, 2, 60),
                Validar.correo("Correo", correo),
                Validar.texto("Contacto", contacto, 3, 50),
                integridadDAO.existeOtro("aerolineas", "nombre_aerolinea", nombre, "id_aerolinea", id)
                ? "Ya existe una aerolínea con el nombre «" + nombre + "»." : null);
        return !Validar.mostrar(vistaAerolinea, error);
    }

    /** Comprueba que no existan registros que dependan de este aerolínea antes de borrarlo; avisa y devuelve false si los hay. */
    private boolean puedeEliminarAerolinea() {
        Integer id = Validar.aEntero(vistaAerolinea.txtIdAerolinea.getText());
        if (id == null) {
            return true; // el metodo original ya avisa que hay que seleccionar una fila
        }
        int aviones = integridadDAO.contar("avion", "fk_aerolinea", id);
        if (aviones > 0) {
            Validar.aviso(vistaAerolinea, "No se puede eliminar esta aerolínea porque tiene " + aviones
                    + " avión(es) registrado(s).\nElimine o cambie primero esos aviones.");
            return false;
        }
        return true;
    }
}
