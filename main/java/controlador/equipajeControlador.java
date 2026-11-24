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
import modelo.DAO.equipajeDAO;
import modelo.entidad.equipaje;
import vista.JpnlEquipaje;

/**
 * CONTROLADOR de Equipajes (administrador): CRUD de equipajes y cálculo
 * automático del precio según el peso y el tipo.
 */
public class equipajeControlador implements ActionListener {

    private JpnlEquipaje vistaEquipaje;
    private equipajeDAO daoEquipaje;

    /** Constructor: guarda la vista y los DAO y conecta los botones con sus acciones. */
    public equipajeControlador(
            JpnlEquipaje vista,
            equipajeDAO dao) {

        vistaEquipaje = vista;
        daoEquipaje = dao;

        vistaEquipaje.btnGuardar.addActionListener(this);
        vistaEquipaje.btnEditar.addActionListener(this);
        vistaEquipaje.btnEliminar.addActionListener(this);
        vistaEquipaje.btnLimpiar.addActionListener(this);

        // SELECCIONAR UNA FILA DE LA TABLA
        vistaEquipaje.tblEquipaje.addMouseListener(
                new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(
                    java.awt.event.MouseEvent e) {

                int fila
                        = vistaEquipaje.tblEquipaje
                                .getSelectedRow();

                if (fila >= 0) {

                    vistaEquipaje.txtIdEquipaje.setText(
                            vistaEquipaje.tblEquipaje
                                    .getValueAt(fila, 0)
                                    .toString()
                    );

                    vistaEquipaje.txtPeso.setText(
                            vistaEquipaje.tblEquipaje
                                    .getValueAt(fila, 1)
                                    .toString()
                    );

                    vistaEquipaje.cmbEstado.setSelectedItem(
                            vistaEquipaje.tblEquipaje
                                    .getValueAt(fila, 2)
                                    .toString()
                    );

                    vistaEquipaje.cmbTipo.setSelectedItem(
                            vistaEquipaje.tblEquipaje
                                    .getValueAt(fila, 3)
                                    .toString()
                    );
                }
            }
        }
        );

        listarEquipajes();
    }

    /** Recibe los clics de los botones de la vista y llama a la acción que corresponde. */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource()
                == vistaEquipaje.btnGuardar) {

            guardarEquipaje();
        }

        if (e.getSource()
                == vistaEquipaje.btnEditar) {

            editarEquipaje();
        }

        if (e.getSource()
                == vistaEquipaje.btnEliminar) {

            eliminarEquipaje();
        }

        if (e.getSource()
                == vistaEquipaje.btnLimpiar) {

            limpiarCampos();
        }
    }

    /** Calcula el precio según el tipo (mano o bodega) y el peso del equipaje. */
    // =========================================================
    // OBTENER PRECIO AUTOMÁTICO
    // =========================================================
    private double obtenerPrecio(
            double peso,
            String tipo) {

        if (tipo.equalsIgnoreCase(
                "Equipaje de mano")) {

            return 0;
        }

        if (tipo.equalsIgnoreCase(
                "Equipaje de bodega")) {

            if (peso <= 10) {
                return 30000;
            }

            if (peso <= 20) {
                return 50000;
            }

            if (peso <= 30) {
                return 70000;
            }
        }

        return 0;
    }

    /** Valida el formulario y guarda un nuevo registro de equipaje; luego refresca la tabla y limpia el formulario. */
    // =========================================================
    // GUARDAR
    // =========================================================
    public void guardarEquipaje() {
        if (!validarEquipaje(false)) {
            return;
        }

        try {

            double peso
                    = Double.parseDouble(
                            vistaEquipaje.txtPeso
                                    .getText()
                                    .trim()
                    );

            String estado
                    = vistaEquipaje.cmbEstado
                            .getSelectedItem()
                            .toString();

            String tipo
                    = vistaEquipaje.cmbTipo
                            .getSelectedItem()
                            .toString();

            // PRECIO AUTOMÁTICO
            double precio
                    = obtenerPrecio(
                            peso,
                            tipo
                    );

            equipaje e
                    = new equipaje();

            e.setPeso(peso);
            e.setEstado(estado);
            e.setTipo(tipo);
            e.setPrecio(precio);

            if (daoEquipaje.guardar(e)) {

                JOptionPane.showMessageDialog(
                        null,
                        "Equipaje guardado correctamente\n"
                        + "Precio: $"
                        + String.format(
                                "%,.0f",
                                precio
                        )
                );

                listarEquipajes();
                limpiarCampos();

            } else {

                JOptionPane.showMessageDialog(
                        null,
                        "Error al guardar el equipaje"
                );
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    null,
                    "El peso debe ser un número"
            );
        }
    }

    /** Carga en la tabla los registros de equipaje que hay en la base de datos. */
    // =========================================================
    // LISTAR
    // =========================================================
    public void listarEquipajes() {

        ArrayList<equipaje> lista
                = daoEquipaje.listar();

        DefaultTableModel modelo
                = new DefaultTableModel();

        modelo.addColumn("ID Equipaje");
        modelo.addColumn("Peso");
        modelo.addColumn("Estado");
        modelo.addColumn("Tipo");
        modelo.addColumn("Precio");

        for (equipaje e : lista) {

            Object datos[] = {
                e.getId_equipaje(),
                e.getPeso(),
                e.getEstado(),
                e.getTipo(),
                "$"
                + String.format(
                "%,.0f",
                e.getPrecio()
                )
            };

            modelo.addRow(datos);
        }

        vistaEquipaje.tblEquipaje
                .setModel(modelo);
    }

    /** Valida y actualiza el registro de equipaje seleccionado en la tabla. */
    // =========================================================
    // EDITAR
    // =========================================================
    public void editarEquipaje() {
        if (!validarEquipaje(true)) {
            return;
        }

        try {

            int id
                    = Integer.parseInt(
                            vistaEquipaje.txtIdEquipaje
                                    .getText()
                                    .trim()
                    );

            double peso
                    = Double.parseDouble(
                            vistaEquipaje.txtPeso
                                    .getText()
                                    .trim()
                    );

            String estado
                    = vistaEquipaje.cmbEstado
                            .getSelectedItem()
                            .toString();

            String tipo
                    = vistaEquipaje.cmbTipo
                            .getSelectedItem()
                            .toString();

            // VOLVER A CALCULAR EL PRECIO
            double precio
                    = obtenerPrecio(
                            peso,
                            tipo
                    );

            equipaje e
                    = new equipaje();

            e.setId_equipaje(id);
            e.setPeso(peso);
            e.setEstado(estado);
            e.setTipo(tipo);
            e.setPrecio(precio);

            if (daoEquipaje.editar(e)) {

                JOptionPane.showMessageDialog(
                        null,
                        "Equipaje actualizado correctamente\n"
                        + "Precio: $"
                        + String.format(
                                "%,.0f",
                                precio
                        )
                );

                listarEquipajes();
                limpiarCampos();

            } else {

                JOptionPane.showMessageDialog(
                        null,
                        "Error al actualizar el equipaje"
                );
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    null,
                    "Verifique el ID y el peso"
            );
        }
    }

    /** Elimina el registro de equipaje seleccionado, tras pedir confirmación y comprobar que nada dependa de él. */
    // =========================================================
    // ELIMINAR
    // =========================================================
    public void eliminarEquipaje() {
        if (!puedeEliminarEquipaje()) {
            return;
        }

        try {

            int id
                    = Integer.parseInt(
                            vistaEquipaje.txtIdEquipaje
                                    .getText()
                                    .trim()
                    );

            int respuesta
                    = JOptionPane.showConfirmDialog(
                            null,
                            "¿Está seguro de eliminar "
                            + "este equipaje?",
                            "Confirmar eliminación",
                            JOptionPane.YES_NO_OPTION
                    );

            if (respuesta
                    == JOptionPane.YES_OPTION) {

                if (daoEquipaje.eliminar(id)) {

                    JOptionPane.showMessageDialog(
                            null,
                            "Equipaje eliminado correctamente"
                    );

                    listarEquipajes();
                    limpiarCampos();

                } else {

                    JOptionPane.showMessageDialog(
                            null,
                            "Error al eliminar el equipaje"
                    );
                }
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    null,
                    "Seleccione un equipaje para eliminar"
            );
        }
    }

    /** Vacía el formulario y quita la selección de la tabla. */
    // =========================================================
    // LIMPIAR
    // =========================================================
    public void limpiarCampos() {

        vistaEquipaje.txtIdEquipaje
                .setText("");

        vistaEquipaje.txtPeso
                .setText("");

        if (vistaEquipaje.cmbEstado
                .getItemCount() > 0) {

            vistaEquipaje.cmbEstado
                    .setSelectedIndex(0);
        }

        if (vistaEquipaje.cmbTipo
                .getItemCount() > 0) {

            vistaEquipaje.cmbTipo
                    .setSelectedIndex(0);
        }
    }

    /** Revisa los campos del formulario; muestra el primer error y devuelve false si hay datos inválidos. */
    // =========================================================
    // VALIDACIONES
    // =========================================================
    private boolean validarEquipaje(boolean edicion) {
        Integer id = null;
        if (edicion) {
            id = Validar.aEntero(vistaEquipaje.txtIdEquipaje.getText());
            if (id == null) {
                Validar.aviso(vistaEquipaje, "Seleccione un equipaje de la tabla antes de editar.");
                return false;
            }
        }
        String peso = Validar.limpiar(vistaEquipaje.txtPeso.getText()).replace(',', '.');
        vistaEquipaje.txtPeso.setText(peso);
        String error = Validar.primero(
                Validar.seleccion("Tipo", vistaEquipaje.cmbTipo.getSelectedItem()),
                Validar.seleccion("Estado", vistaEquipaje.cmbEstado.getSelectedItem()),
                Validar.decimalPositivo("Peso (kg)", peso, 30));
        return !Validar.mostrar(vistaEquipaje, error);
    }

    /** Comprueba que no existan registros que dependan de este equipaje antes de borrarlo; avisa y devuelve false si los hay. */
    private boolean puedeEliminarEquipaje() {
        Integer id = Validar.aEntero(vistaEquipaje.txtIdEquipaje.getText());
        if (id == null) {
            return true; // el metodo original ya avisa que hay que seleccionar una fila
        }
        int tiquetes = integridadDAO.contar("tiquete", "fk_equipaje", id);
        if (tiquetes > 0) {
            Validar.aviso(vistaEquipaje, "No se puede eliminar este equipaje porque está asociado a " + tiquetes
                    + " tiquete(s).");
            return false;
        }
        return true;
    }
}
