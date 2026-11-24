/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import vista.MapaAvion;
import vista.Tema;
import modelo.DAO.integridadDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import modelo.DAO.avionDAO;
import modelo.DAO.avion_asientosDAO;
import modelo.entidad.avion;
import modelo.entidad.avion_asientos;
import vista.JpnlConfiguracionAvion;

/**
 * CONTROLADOR de la configuración de asientos de cada avión (clases, filas y
 * distribución). Valida que el total de asientos no pase la capacidad del avión.
 */
public class avion_asientosControlador {

    private JpnlConfiguracionAvion vista;
    private avion_asientosDAO daoConfiguracion;
    private avionDAO daoAvion;

    // ID de la configuración que se está editando
    private int idSeleccionado = 0;

    /** Constructor: guarda la vista y los DAO y conecta los botones con sus acciones. */
    public avion_asientosControlador(
            JpnlConfiguracionAvion vista,
            avion_asientosDAO daoConfiguracion,
            avionDAO daoAvion) {

        this.vista = vista;
        this.daoConfiguracion = daoConfiguracion;
        this.daoAvion = daoAvion;

        // Cargar datos al iniciar
        cargarAviones();
        Tema.alMostrar(vista, this::cargarAviones);
        cargarTabla();

        vista.cmbAvion.addActionListener(e
                -> cargarConfiguracionSeleccionada()
        );

        vista.cmbClase.addActionListener(e
                -> cargarConfiguracionSeleccionada()
        );

        // BOTÓN GUARDAR
        vista.btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardar();
            }
        });

        // BOTÓN EDITAR
        vista.btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editar();
            }
        });

        // BOTÓN ELIMINAR
        vista.btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminar();
            }
        });

        // BOTÓN LIMPIAR
        vista.btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiar();
            }
        });

        // SELECCIONAR FILA DE LA TABLA
        vista.tblConfiguracion.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {
                        cargarSeleccion();
                    }

                });
    }

    /** Llena el combo de aviones disponibles. */
    // =========================================================
    // CARGAR AVIONES EN EL COMBO
    // =========================================================
    private void cargarAviones() {
        java.util.List<String> items = new java.util.ArrayList<>();
        for (avion a : daoAvion.listar()) {
            items.add(a.getModelo());
        }
        Tema.recargarCombo(vista.cmbAvion, items);
    }

    /** Valida y guarda una nueva clase de asientos para el avión. */
    // =========================================================
    // GUARDAR
    // =========================================================
    private void guardar() {
        if (!validarConfiguracion(false)) {
            return;
        }

        if (vista.cmbAvion.getSelectedItem() == null) {

            JOptionPane.showMessageDialog(vista,
                    "Seleccione un avión.");

            return;
        }

        String clase = vista.cmbClase.getSelectedItem().toString();
        String filasTexto = vista.txtFilas.getText().trim();
        String distribucion = vista.txtDistribucion.getText().trim();

        if (filasTexto.isEmpty() || distribucion.isEmpty()) {

            JOptionPane.showMessageDialog(vista,
                    "Complete todos los campos.");

            return;
        }

        int filas;

        try {

            filas = Integer.parseInt(filasTexto);

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(vista,
                    "Las filas deben ser un número.");

            return;
        }

        if (filas <= 0) {

            JOptionPane.showMessageDialog(vista,
                    "Las filas deben ser mayores que 0.");

            return;
        }

        avion_asientos a = new avion_asientos();

        a.setNombre_clase(clase);
        a.setFilas(filas);
        a.setDistribucion(distribucion);
        a.setFk_avion(
                vista.cmbAvion.getSelectedItem().toString()
        );

        if (daoConfiguracion.guardar(a)) {

            JOptionPane.showMessageDialog(vista,
                    "Configuración guardada correctamente.");

            cargarTabla();
            limpiar();

        } else {

            JOptionPane.showMessageDialog(vista,
                    "No se pudo guardar la configuración.");
        }
    }

    /** Carga todas las configuraciones en la tabla. */
    // =========================================================
    // CARGAR TABLA
    // =========================================================
    private void cargarTabla() {

        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("ID");
        modelo.addColumn("AVIÓN");
        modelo.addColumn("CLASE");
        modelo.addColumn("FILAS");
        modelo.addColumn("DISTRIBUCIÓN");

        ArrayList<avion_asientos> lista
                = daoConfiguracion.listar();

        for (avion_asientos a : lista) {

            modelo.addRow(new Object[]{
                a.getId_clase(),
                a.getFk_avion(),
                a.getNombre_clase(),
                a.getFilas(),
                a.getDistribucion()
            });
        }

        vista.tblConfiguracion.setModel(modelo);
    }

    /** Pasa al formulario los datos de la fila elegida en la tabla. */
    // =========================================================
    // SELECCIONAR UNA FILA
    // =========================================================
    private void cargarSeleccion() {

        int fila = vista.tblConfiguracion.getSelectedRow();

        if (fila == -1) {
            return;
        }

        // Guardamos el ID para editar/eliminar
        idSeleccionado
                = Integer.parseInt(
                        vista.tblConfiguracion
                                .getValueAt(fila, 0)
                                .toString()
                );

        // Avión
        String avionSeleccionado
                = vista.tblConfiguracion
                        .getValueAt(fila, 1)
                        .toString();

        vista.cmbAvion.setSelectedItem(avionSeleccionado);

        // Clase
        String clase
                = vista.tblConfiguracion
                        .getValueAt(fila, 2)
                        .toString();

        vista.cmbClase.setSelectedItem(clase);

        // Filas
        String filas
                = vista.tblConfiguracion
                        .getValueAt(fila, 3)
                        .toString();

        vista.txtFilas.setText(filas);

        // Distribución
        String distribucion
                = vista.tblConfiguracion
                        .getValueAt(fila, 4)
                        .toString();

        vista.txtDistribucion.setText(distribucion);
    }

    /** Valida y actualiza la configuración seleccionada. */
    // =========================================================
    // EDITAR
    // =========================================================
    private void editar() {
        if (!validarConfiguracion(true)) {
            return;
        }

        if (idSeleccionado == 0) {

            JOptionPane.showMessageDialog(vista,
                    "Seleccione una configuración de la tabla.");

            return;
        }

        if (vista.cmbAvion.getSelectedItem() == null) {

            JOptionPane.showMessageDialog(vista,
                    "Seleccione un avión.");

            return;
        }

        String clase
                = vista.cmbClase.getSelectedItem().toString();

        String filasTexto
                = vista.txtFilas.getText().trim();

        String distribucion
                = vista.txtDistribucion.getText().trim();

        if (filasTexto.isEmpty() || distribucion.isEmpty()) {

            JOptionPane.showMessageDialog(vista,
                    "Complete todos los campos.");

            return;
        }

        int filas;

        try {

            filas = Integer.parseInt(filasTexto);

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(vista,
                    "Las filas deben ser un número.");

            return;
        }

        if (filas <= 0) {

            JOptionPane.showMessageDialog(vista,
                    "Las filas deben ser mayores que 0.");

            return;
        }

        avion_asientos a = new avion_asientos();

        a.setId_clase(idSeleccionado);
        a.setNombre_clase(clase);
        a.setFilas(filas);
        a.setDistribucion(distribucion);
        a.setFk_avion(
                vista.cmbAvion.getSelectedItem().toString()
        );

        if (daoConfiguracion.editar(a)) {

            JOptionPane.showMessageDialog(vista,
                    "Configuración actualizada correctamente.");

            cargarTabla();
            limpiar();

        } else {

            JOptionPane.showMessageDialog(vista,
                    "No se pudo actualizar la configuración.");
        }
    }

    /** Elimina la configuración seleccionada tras confirmar. */
    // =========================================================
    // ELIMINAR
    // =========================================================
    private void eliminar() {
        if (!puedeEliminarConfiguracion()) {
            return;
        }

        if (idSeleccionado == 0) {

            JOptionPane.showMessageDialog(vista,
                    "Seleccione una configuración de la tabla.");

            return;
        }

        int confirmar = JOptionPane.showConfirmDialog(
                vista,
                "¿Está seguro de eliminar esta configuración?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmar != JOptionPane.YES_OPTION) {
            return;
        }

        if (daoConfiguracion.eliminar(idSeleccionado)) {

            JOptionPane.showMessageDialog(vista,
                    "Configuración eliminada correctamente.");

            cargarTabla();
            limpiar();

        } else {

            JOptionPane.showMessageDialog(vista,
                    "No se pudo eliminar la configuración.");
        }
    }

    /** Vacía el formulario y quita la selección. */
    // =========================================================
    // LIMPIAR
    // =========================================================
    private void limpiar() {

        idSeleccionado = 0;

        vista.txtFilas.setText("");
        vista.txtDistribucion.setText("");

        if (vista.cmbAvion.getItemCount() > 0) {
            vista.cmbAvion.setSelectedIndex(0);
        }

        if (vista.cmbClase.getItemCount() > 0) {
            vista.cmbClase.setSelectedIndex(0);
        }

        vista.tblConfiguracion.clearSelection();
    }

    /** Carga en el formulario los datos de la configuración seleccionada. */
    // =========================================================
    // CARGAR LA DISTRIBUCION
    // =========================================================
    private void cargarConfiguracionSeleccionada() {

        if (vista.cmbAvion.getSelectedItem() == null) {
            return;
        }

        String modeloAvion
                = vista.cmbAvion.getSelectedItem().toString();

        String claseSeleccionada
                = vista.cmbClase.getSelectedItem().toString();

        ArrayList<avion_asientos> lista
                = daoConfiguracion.listar();

        for (avion_asientos a : lista) {

            if (a.getFk_avion() == null) {
                continue;
            }

            if (!a.getFk_avion().equalsIgnoreCase(modeloAvion)) {
                continue;
            }

            String claseBD = a.getNombre_clase();

            if (claseBD.equalsIgnoreCase(claseSeleccionada)
                    || (claseBD.equalsIgnoreCase("Business")
                    && claseSeleccionada.equalsIgnoreCase("Bussines"))
                    || (claseBD.equalsIgnoreCase("Bussines")
                    && claseSeleccionada.equalsIgnoreCase("Business"))) {

                vista.txtFilas.setText(
                        String.valueOf(a.getFilas())
                );

                vista.txtDistribucion.setText(
                        a.getDistribucion()
                );

                return;
            }
        }

        // Si no encuentra configuración
        vista.txtFilas.setText("");
        vista.txtDistribucion.setText("");
    }

    /** Revisa los campos del formulario; muestra el primer error y devuelve false si hay datos inválidos. */
    // =========================================================
    // VALIDACIONES
    // =========================================================
    private boolean validarConfiguracion(boolean edicion) {
        if (edicion && idSeleccionado == 0) {
            Validar.aviso(vista, "Seleccione una configuración de la tabla antes de editar.");
            return false;
        }
        Object avionSel = vista.cmbAvion.getSelectedItem();
        Object claseSel = vista.cmbClase.getSelectedItem();
        String filas = Validar.limpiar(vista.txtFilas.getText());
        String dist = Validar.limpiar(vista.txtDistribucion.getText()).replace(" ", "");
        vista.txtFilas.setText(filas);
        vista.txtDistribucion.setText(dist);

        String error = Validar.primero(
                Validar.seleccion("Avión", avionSel),
                Validar.seleccion("Clase", claseSel),
                Validar.entero("Filas", filas, 1, 60),
                Validar.distribucion(dist));

        if (error == null) {
            String modelo = avionSel.toString().trim();
            int idExcluir = edicion ? idSeleccionado : 0;
            String clave = MapaAvion.claveClase(claseSel.toString());

            // una sola configuracion por clase y avion
            for (avion_asientos c : daoConfiguracion.listar()) {
                if (c.getId_clase() != idExcluir && c.getFk_avion() != null
                        && c.getFk_avion().trim().equalsIgnoreCase(modelo)
                        && MapaAvion.claveClase(c.getNombre_clase()).equals(clave)) {
                    error = "Este avión ya tiene configurada la clase «" + MapaAvion.etiquetaClase(clave)
                            + "».\nEdite esa configuración en lugar de crear otra.";
                    break;
                }
            }

            // no pasar de la capacidad del avion
            if (error == null) {
                int nuevos = Integer.parseInt(filas) * integridadDAO.asientosPorFila(dist);
                int existentes = integridadDAO.asientosConfigurados(modelo, idExcluir);
                for (avion a : daoAvion.listar()) {
                    if (a.getModelo() != null && a.getModelo().trim().equalsIgnoreCase(modelo)) {
                        Integer cap = Validar.aEntero(a.getCapacidad());
                        if (cap != null && existentes + nuevos > cap) {
                            error = "Los asientos configurados (" + (existentes + nuevos)
                                    + ") superan la capacidad del avión (" + cap + ").\n"
                                    + "Reduzca las filas o la distribución.";
                        }
                        break;
                    }
                }
            }

            // no modificar la configuracion si ya hay tiquetes vendidos con ese avion
            if (error == null && edicion && integridadDAO.tiquetesDeAvion(modelo) > 0) {
                error = "No se puede modificar esta configuración: ya hay tiquetes vendidos en vuelos de este avión.";
            }
        }
        return !Validar.mostrar(vista, error);
    }

    /** Comprueba que no existan registros que dependan de este configuración de asientos del avión antes de borrarlo; avisa y devuelve false si los hay. */
    private boolean puedeEliminarConfiguracion() {
        if (idSeleccionado == 0) {
            return true; // el metodo original ya avisa que hay que seleccionar una fila
        }
        for (avion_asientos c : daoConfiguracion.listar()) {
            if (c.getId_clase() == idSeleccionado && c.getFk_avion() != null
                    && integridadDAO.tiquetesDeAvion(c.getFk_avion().trim()) > 0) {
                Validar.aviso(vista, "No se puede eliminar esta configuración: ya hay tiquetes vendidos en vuelos del avión «"
                        + c.getFk_avion() + "».");
                return false;
            }
        }
        return true;
    }
}
