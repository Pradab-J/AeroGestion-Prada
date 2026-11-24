/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import vista.Tema;
import modelo.DAO.integridadDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.DAO.aerolineaDAO;
import modelo.DAO.avionDAO;
import modelo.entidad.aerolineas;
import modelo.entidad.avion;
import vista.JpnlAvion;

/** CONTROLADOR de Aviones: conecta JpnlAvion con avionDAO y aerolineaDAO. */
public class avionControlador implements ActionListener {

    private JpnlAvion vistaAvion;
    private avionDAO daoAvion;
    private aerolineaDAO daoAerolinea;

    /** Constructor: guarda la vista y los DAO y conecta los botones con sus acciones. */
    public avionControlador(
            JpnlAvion vista,
            avionDAO dao,
            aerolineaDAO daoAerolinea) {

        vistaAvion = vista;
        daoAvion = dao;
        this.daoAerolinea = daoAerolinea;

        vistaAvion.btnGuardar.addActionListener(this);
        vistaAvion.btnEditar.addActionListener(this);
        vistaAvion.btnEliminar.addActionListener(this);
        vistaAvion.btnLimpiar.addActionListener(this);

// Detectar cuando se selecciona una fila de la tabla
        vistaAvion.tblAvion.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {

                        int fila = vistaAvion.tblAvion.getSelectedRow();

                        if (fila != -1) {

                            vistaAvion.txtModelo.setText(
                                    vistaAvion.tblAvion.getValueAt(fila, 0).toString()
                            );

                            vistaAvion.txtAño.setText(
                                    vistaAvion.tblAvion.getValueAt(fila, 1).toString()
                            );

                            vistaAvion.txtCapacidad.setText(
                                    vistaAvion.tblAvion.getValueAt(fila, 2).toString()
                            );

                            vistaAvion.txtEstado.setSelectedItem(
                                    vistaAvion.tblAvion.getValueAt(fila, 3).toString()
                            );

                            String idAerolinea
                                    = vistaAvion.tblAvion.getValueAt(fila, 4).toString();

                            for (int i = 0; i < vistaAvion.cmbAerolinea.getItemCount(); i++) {

                                String item = vistaAvion.cmbAerolinea.getItemAt(i);

                                if (item.startsWith(idAerolinea + " - ")) {
                                    vistaAvion.cmbAerolinea.setSelectedIndex(i);
                                    break;
                                }
                            }
                        }
                    }
                });

        cargarAerolineas();
        Tema.alMostrar(vistaAvion, this::cargarAerolineas);
        listarAviones();

    }

    /** Recibe los clics de los botones de la vista y llama a la acción que corresponde. */
    // =========================
    // EVENTOS
    // =========================
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vistaAvion.btnGuardar) {
            guardarAvion();
        } else if (e.getSource() == vistaAvion.btnEditar) {
            editarAvion();
        } else if (e.getSource() == vistaAvion.btnEliminar) {
            eliminarAvion();
        } else if (e.getSource() == vistaAvion.btnLimpiar) {
            limpiar();
        }
    }

    /** Llena el combo de aerolíneas con las registradas. */
    // =========================
    // CARGAR AEROLÍNEAS
    // =========================
    private void cargarAerolineas() {
        java.util.List<String> items = new ArrayList<>();
        for (aerolineas a : daoAerolinea.listar()) {
            items.add(a.getId_aerolinea() + " - " + a.getNombre());
        }
        Tema.recargarCombo(vistaAvion.cmbAerolinea, items);
    }

    /** Valida el formulario y guarda un nuevo registro de avión; luego refresca la tabla y limpia el formulario. */
    // =========================
    // GUARDAR AVIÓN
    // =========================
    public void guardarAvion() {
        if (!validarAvion(false)) {
            return;
        }

        if (vistaAvion.txtModelo.getText().trim().isEmpty()
                || vistaAvion.txtAño.getText().trim().isEmpty()
                || vistaAvion.txtCapacidad.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    vistaAvion,
                    "Complete todos los campos."
            );

            return;
        }

        if (vistaAvion.cmbAerolinea.getSelectedItem() == null) {

            JOptionPane.showMessageDialog(
                    vistaAvion,
                    "Seleccione una aerolínea."
            );

            return;
        }

        avion a = new avion();

        a.setModelo(
                vistaAvion.txtModelo.getText().trim()
        );

        a.setAño(
                vistaAvion.txtAño.getText().trim()
        );

        a.setCapacidad(
                vistaAvion.txtCapacidad.getText().trim()
        );

        a.setEstado(
                vistaAvion.txtEstado.getSelectedItem().toString()
        );

        // Obtener ID de la aerolínea
        String aerolineaSeleccionada
                = vistaAvion.cmbAerolinea
                        .getSelectedItem()
                        .toString();

        String idTexto
                = aerolineaSeleccionada
                        .split(" - ")[0];

        int idAerolinea;

        try {

            idAerolinea = Integer.parseInt(idTexto);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    vistaAvion,
                    "No se pudo obtener la aerolínea."
            );

            return;
        }

        a.setFk_aerolinea(idAerolinea);

        // GUARDAR
        if (daoAvion.guardar(a)) {

            JOptionPane.showMessageDialog(
                    vistaAvion,
                    "Avión guardado correctamente."
            );

            listarAviones();
            limpiar();

        } else {

            JOptionPane.showMessageDialog(
                    vistaAvion,
                    "Error al guardar el avión."
            );
        }
    }

    /** Carga en la tabla los registros de avión que hay en la base de datos. */
    // =========================
    // LISTAR AVIONES
    // =========================
    public void listarAviones() {

        ArrayList<avion> lista
                = daoAvion.listar();

        DefaultTableModel modelo
                = new DefaultTableModel();

        modelo.addColumn("Modelo");
        modelo.addColumn("Año");
        modelo.addColumn("Capacidad");
        modelo.addColumn("Estado");
        modelo.addColumn("ID Aerolínea");

        for (avion a : lista) {

            Object datos[] = {
                a.getModelo(),
                a.getAño(),
                a.getCapacidad(),
                a.getEstado(),
                a.getFk_aerolinea()
            };

            modelo.addRow(datos);
        }

        vistaAvion.tblAvion.setModel(modelo);
    }

    /** Vacía el formulario y quita la selección de la tabla. */
    // =========================
    // LIMPIAR
    // =========================
    public void limpiar() {
        vistaAvion.tblAvion.clearSelection();
        vistaAvion.txtModelo.setText("");
        vistaAvion.txtAño.setText("");
        vistaAvion.txtCapacidad.setText("");
        if (vistaAvion.txtEstado.getItemCount() > 0) {
            vistaAvion.txtEstado.setSelectedIndex(0);
        }
        if (vistaAvion.cmbAerolinea.getItemCount() > 0) {
            vistaAvion.cmbAerolinea.setSelectedIndex(0);
        }
    }

    /** Devuelve el modelo del avión seleccionado en la tabla o el formulario. */
    // =========================================================
    // EDITAR / ELIMINAR (antes los botones no hacian nada)
    // =========================================================
    private String modeloSeleccionado() {
        int fila = vistaAvion.tblAvion.getSelectedRow();
        return fila < 0 ? null : vistaAvion.tblAvion.getValueAt(fila, 0).toString();
    }

    /** Devuelve el id de la aerolínea elegida en el combo (-1 si no hay). */
    private int idAerolineaSeleccionada() {
        try {
            return Integer.parseInt(vistaAvion.cmbAerolinea.getSelectedItem().toString().split(" - ")[0].trim());
        } catch (Exception e) {
            return -1;
        }
    }

    /** Valida y actualiza el registro de avión seleccionado en la tabla. */
    public void editarAvion() {
        if (!validarAvion(true)) {
            return;
        }
        int idAerolinea = idAerolineaSeleccionada();
        if (idAerolinea < 0) {
            Validar.aviso(vistaAvion, "No se pudo obtener la aerolínea.");
            return;
        }
        avion a = new avion();
        a.setModelo(modeloSeleccionado());
        a.setAño(vistaAvion.txtAño.getText().trim());
        a.setCapacidad(vistaAvion.txtCapacidad.getText().trim());
        a.setEstado(vistaAvion.txtEstado.getSelectedItem().toString());
        a.setFk_aerolinea(idAerolinea);
        if (daoAvion.editar(a)) {
            JOptionPane.showMessageDialog(vistaAvion, "Avión actualizado correctamente.");
            listarAviones();
            limpiar();
        } else {
            JOptionPane.showMessageDialog(vistaAvion, "Error al actualizar el avión.");
        }
    }

    /** Elimina el registro de avión seleccionado, tras pedir confirmación y comprobar que nada dependa de él. */
    public void eliminarAvion() {
        String modelo = modeloSeleccionado();
        if (modelo == null) {
            Validar.aviso(vistaAvion, "Seleccione un avión de la tabla.");
            return;
        }
        int vuelos = integridadDAO.contar("vuelo", "fk_avion", modelo);
        int configs = integridadDAO.contar("avion_asientos", "fk_avion", modelo);
        if (vuelos > 0) {
            Validar.aviso(vistaAvion, "No se puede eliminar el avión «" + modelo + "» porque tiene " + vuelos
                    + " vuelo(s) registrado(s).\nElimine primero esos vuelos.");
            return;
        }
        if (configs > 0) {
            Validar.aviso(vistaAvion, "No se puede eliminar el avión «" + modelo + "» porque tiene " + configs
                    + " clase(s) de asientos configurada(s).\nElimínelas primero en «Configuración de aviones».");
            return;
        }
        int ok = JOptionPane.showConfirmDialog(vistaAvion, "¿Está seguro de eliminar el avión «" + modelo + "»?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) {
            return;
        }
        if (daoAvion.eliminar(modelo)) {
            JOptionPane.showMessageDialog(vistaAvion, "Avión eliminado correctamente.");
            listarAviones();
            limpiar();
        } else {
            JOptionPane.showMessageDialog(vistaAvion, "Error al eliminar el avión.");
        }
    }

    /** Revisa los campos del formulario; muestra el primer error y devuelve false si hay datos inválidos. */
    // =========================================================
    // VALIDACIONES
    // =========================================================
    private boolean validarAvion(boolean edicion) {
        String modelo = Validar.limpiar(vistaAvion.txtModelo.getText());
        String anio = Validar.limpiar(vistaAvion.txtAño.getText());
        String capacidad = Validar.limpiar(vistaAvion.txtCapacidad.getText());
        vistaAvion.txtModelo.setText(modelo);
        vistaAvion.txtAño.setText(anio);
        vistaAvion.txtCapacidad.setText(capacidad);

        int anioMax = java.time.Year.now().getValue() + 1;
        String error = Validar.primero(
                Validar.texto("Modelo", modelo, 2, 50),
                Validar.entero("Año", anio, 1950, anioMax),
                Validar.entero("Capacidad", capacidad, 1, 1000),
                Validar.seleccion("Aerolínea", vistaAvion.cmbAerolinea.getSelectedItem()),
                Validar.seleccion("Estado", vistaAvion.txtEstado.getSelectedItem()));

        if (error == null && !edicion && integridadDAO.existe("avion", "modelo", modelo)) {
            error = "Ya existe un avión con el modelo «" + modelo + "».";
        }
        if (error == null && edicion) {
            String sel = modeloSeleccionado();
            if (sel == null) {
                error = "Seleccione un avión de la tabla antes de editar.";
            } else if (!Validar.clave(sel).equals(Validar.clave(modelo))) {
                error = "El modelo identifica al avión y no se puede cambiar.\n"
                        + "Si es otro modelo, registre un avión nuevo.";
            } else {
                int configurados = integridadDAO.asientosConfigurados(sel, 0);
                int cap = Integer.parseInt(capacidad);
                if (configurados > cap) {
                    error = "La capacidad (" + cap + ") es menor que los asientos ya configurados ("
                            + configurados + ").\nAjuste primero la configuración de asientos.";
                }
            }
        }
        return !Validar.mostrar(vistaAvion, error);
    }
}
