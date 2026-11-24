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
import modelo.DAO.aeropuertoDAO;
import modelo.entidad.aeropuerto;
import vista.JpnlAeropuerto;

/**
 * CONTROLADOR de Aeropuertos: conecta JpnlAeropuerto con aeropuertoDAO
 * (guardar, listar, editar y eliminar con sus validaciones).
 */
public class aeropuertoControlador implements ActionListener {

    private JpnlAeropuerto vistaAeropuerto;
    private aeropuertoDAO daoAeropuerto;

    /** Constructor: guarda la vista y los DAO y conecta los botones con sus acciones. */
    public aeropuertoControlador(
            JpnlAeropuerto vista,
            aeropuertoDAO dao) {

        vistaAeropuerto = vista;
        daoAeropuerto = dao;

        vistaAeropuerto.btnGuardar.addActionListener(this);
        vistaAeropuerto.btnEditar.addActionListener(this);
        vistaAeropuerto.btnEliminar.addActionListener(this);
        vistaAeropuerto.btnLimpiar.addActionListener(this);

        // =========================
        // SELECCIONAR FILA
        // =========================
        vistaAeropuerto.tblAeropuerto.addMouseListener(
                new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {

                int fila
                        = vistaAeropuerto.tblAeropuerto.getSelectedRow();

                if (fila >= 0) {

                    vistaAeropuerto.txtIdAeropuerto.setText(
                            vistaAeropuerto.tblAeropuerto
                                    .getValueAt(fila, 0)
                                    .toString()
                    );

                    vistaAeropuerto.txtNombre.setText(
                            vistaAeropuerto.tblAeropuerto
                                    .getValueAt(fila, 1)
                                    .toString()
                    );

                    vistaAeropuerto.txtCodigoIata.setText(
                            vistaAeropuerto.tblAeropuerto
                                    .getValueAt(fila, 2)
                                    .toString()
                    );

                    vistaAeropuerto.txtCiudad.setText(
                            vistaAeropuerto.tblAeropuerto
                                    .getValueAt(fila, 3)
                                    .toString()
                    );

                    vistaAeropuerto.txtPais.setText(
                            vistaAeropuerto.tblAeropuerto
                                    .getValueAt(fila, 4)
                                    .toString()
                    );
                }
            }
        }
        );

        listarAeropuertos();
    }

    /** Recibe los clics de los botones de la vista y llama a la acción que corresponde. */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vistaAeropuerto.btnGuardar) {
            guardarAeropuerto();
        }

        if (e.getSource() == vistaAeropuerto.btnEditar) {
            editarAeropuerto();
        }

        if (e.getSource() == vistaAeropuerto.btnEliminar) {
            eliminarAeropuerto();
        }

        if (e.getSource() == vistaAeropuerto.btnLimpiar) {
            limpiarCampos();
        }
    }

    /** Valida el formulario y guarda un nuevo registro de aeropuerto; luego refresca la tabla y limpia el formulario. */
    // =========================
    // GUARDAR
    // =========================
    public void guardarAeropuerto() {
        if (!validarAeropuerto(false)) {
            return;
        }

        String nombre
                = vistaAeropuerto.txtNombre.getText();

        String codigo = vistaAeropuerto.txtCodigoIata.getText().trim().toUpperCase();

       
        String ciudad
                = vistaAeropuerto.txtCiudad.getText();

        String pais
                = vistaAeropuerto.txtPais.getText();

        if (codigo.length() != 3) {
            JOptionPane.showMessageDialog(null,
                    "El código IATA debe tener exactamente 3 caracteres");
            return;
        }
        
        if (nombre.isEmpty()
                || codigo.isEmpty()
                || ciudad.isEmpty()
                || pais.isEmpty()) {

            JOptionPane.showMessageDialog(
                    null,
                    "Complete todos los campos"
            );

            return;
        }

        aeropuerto a = new aeropuerto();

        a.setNombre(nombre);
        a.setCodigo_iata(codigo);
        a.setCiudad(ciudad);
        a.setPais(pais);

        if (daoAeropuerto.guardar(a)) {

            JOptionPane.showMessageDialog(
                    null,
                    "Aeropuerto guardado correctamente"
            );

            listarAeropuertos();
            limpiarCampos();

        } else {

            JOptionPane.showMessageDialog(
                    null,
                    "Error al guardar el aeropuerto"
            );
        }
    }

    /** Carga en la tabla los registros de aeropuerto que hay en la base de datos. */
    // =========================
    // LISTAR
    // =========================
    public void listarAeropuertos() {

        ArrayList<aeropuerto> lista
                = daoAeropuerto.listar();

        DefaultTableModel modelo
                = new DefaultTableModel();

        modelo.addColumn("ID Aeropuerto");
        modelo.addColumn("Nombre");
        modelo.addColumn("Código IATA");
        modelo.addColumn("Ciudad");
        modelo.addColumn("País");

        for (aeropuerto a : lista) {

            Object datos[] = {
                a.getId_aeropuerto(),
                a.getNombre(),
                a.getCodigo_iata(),
                a.getCiudad(),
                a.getPais()
            };

            modelo.addRow(datos);
        }

        vistaAeropuerto.tblAeropuerto.setModel(modelo);
    }

    /** Valida y actualiza el registro de aeropuerto seleccionado en la tabla. */
    // =========================
    // EDITAR
    // =========================
    public void editarAeropuerto() {
        if (!validarAeropuerto(true)) {
            return;
        }

        try {

            int id = Integer.parseInt(
                    vistaAeropuerto.txtIdAeropuerto.getText()
            );

            String nombre
                    = vistaAeropuerto.txtNombre.getText();

            String codigo
                    = vistaAeropuerto.txtCodigoIata.getText();

            String ciudad
                    = vistaAeropuerto.txtCiudad.getText();

            String pais
                    = vistaAeropuerto.txtPais.getText();

            if (nombre.isEmpty()
                    || codigo.isEmpty()
                    || ciudad.isEmpty()
                    || pais.isEmpty()) {

                JOptionPane.showMessageDialog(
                        null,
                        "Complete todos los campos"
                );

                return;
            }

            aeropuerto a = new aeropuerto();

            a.setId_aeropuerto(id);
            a.setNombre(nombre);
            a.setCodigo_iata(codigo);
            a.setCiudad(ciudad);
            a.setPais(pais);

            if (daoAeropuerto.editar(a)) {

                JOptionPane.showMessageDialog(
                        null,
                        "Aeropuerto actualizado correctamente"
                );

                listarAeropuertos();
                limpiarCampos();

            } else {

                JOptionPane.showMessageDialog(
                        null,
                        "Error al actualizar el aeropuerto"
                );
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    null,
                    "Seleccione un aeropuerto de la tabla"
            );
        }
    }

    /** Elimina el registro de aeropuerto seleccionado, tras pedir confirmación y comprobar que nada dependa de él. */
    // =========================
    // ELIMINAR
    // =========================
    public void eliminarAeropuerto() {
        if (!puedeEliminarAeropuerto()) {
            return;
        }

        try {

            int id = Integer.parseInt(
                    vistaAeropuerto.txtIdAeropuerto.getText()
            );

            int respuesta
                    = JOptionPane.showConfirmDialog(
                            null,
                            "¿Está seguro de eliminar este aeropuerto?",
                            "Confirmar eliminación",
                            JOptionPane.YES_NO_OPTION
                    );

            if (respuesta == JOptionPane.YES_OPTION) {

                if (daoAeropuerto.eliminar(id)) {

                    JOptionPane.showMessageDialog(
                            null,
                            "Aeropuerto eliminado correctamente"
                    );

                    listarAeropuertos();
                    limpiarCampos();

                } else {

                    JOptionPane.showMessageDialog(
                            null,
                            "Error al eliminar el aeropuerto"
                    );
                }
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    null,
                    "Seleccione un aeropuerto de la tabla"
            );
        }
    }

    /** Vacía el formulario y quita la selección de la tabla. */
    // =========================
    // LIMPIAR
    // =========================
    public void limpiarCampos() {

        vistaAeropuerto.txtIdAeropuerto.setText("");
        vistaAeropuerto.txtNombre.setText("");
        vistaAeropuerto.txtCodigoIata.setText("");
        vistaAeropuerto.txtCiudad.setText("");
        vistaAeropuerto.txtPais.setText("");

        vistaAeropuerto.tblAeropuerto.clearSelection();
    }

    /** Devuelve la ciudad del aeropuerto con ese id (null si no existe). */
    // =========================================================
    // VALIDACIONES
    // =========================================================
    private String ciudadActual(int id) {
        for (aeropuerto a : daoAeropuerto.listar()) {
            if (a.getId_aeropuerto() == id) {
                return a.getCiudad();
            }
        }
        return null;
    }

    /** Revisa los campos del formulario; muestra el primer error y devuelve false si hay datos inválidos. */
    private boolean validarAeropuerto(boolean edicion) {
        Integer id = null;
        if (edicion) {
            id = Validar.aEntero(vistaAeropuerto.txtIdAeropuerto.getText());
            if (id == null) {
                Validar.aviso(vistaAeropuerto, "Seleccione un aeropuerto de la tabla antes de editar.");
                return false;
            }
        }
        String nombre = Validar.limpiar(vistaAeropuerto.txtNombre.getText());
        String iata = Validar.limpiar(vistaAeropuerto.txtCodigoIata.getText()).toUpperCase();
        String ciudad = Validar.limpiar(vistaAeropuerto.txtCiudad.getText());
        String pais = Validar.limpiar(vistaAeropuerto.txtPais.getText());
        vistaAeropuerto.txtNombre.setText(nombre);
        vistaAeropuerto.txtCodigoIata.setText(iata);
        vistaAeropuerto.txtCiudad.setText(ciudad);
        vistaAeropuerto.txtPais.setText(pais);

        String error = Validar.primero(
                Validar.texto("Nombre", nombre, 3, 80),
                Validar.iata(iata),
                Validar.nombre("Ciudad", ciudad, 2, 60),
                Validar.nombre("País", pais, 2, 60),
                integridadDAO.existeOtro("aeropuerto", "codigo_iata", iata, "id_aeropuerto", id)
                ? "Ya existe un aeropuerto con el código IATA «" + iata + "»." : null,
                integridadDAO.existeOtro("aeropuerto", "nombre", nombre, "id_aeropuerto", id)
                ? "Ya existe un aeropuerto con el nombre «" + nombre + "»." : null);

        if (error == null && edicion) {
            String anterior = ciudadActual(id);
            if (anterior != null && !Validar.clave(anterior).equals(Validar.clave(ciudad))
                    && integridadDAO.rutasPorCiudad(anterior) > 0) {
                error = "No se puede cambiar la ciudad: hay rutas que salen o llegan a «" + anterior
                        + "».\nEdite primero esas rutas.";
            }
        }
        return !Validar.mostrar(vistaAeropuerto, error);
    }

    /** Comprueba que no existan registros que dependan de este aeropuerto antes de borrarlo; avisa y devuelve false si los hay. */
    private boolean puedeEliminarAeropuerto() {
        Integer id = Validar.aEntero(vistaAeropuerto.txtIdAeropuerto.getText());
        if (id == null) {
            return true; // el metodo original ya avisa que hay que seleccionar una fila
        }
        int rutas = integridadDAO.contar("ruta", "fk_aeropuerto", id);
        String ciudad = ciudadActual(id);
        int porCiudad = ciudad == null ? 0 : integridadDAO.rutasPorCiudad(ciudad);
        if (rutas > 0 || porCiudad > 0) {
            Validar.aviso(vistaAeropuerto, "No se puede eliminar este aeropuerto porque hay rutas que lo usan.\n"
                    + "Elimine o cambie primero esas rutas.");
            return false;
        }
        return true;
    }
}
