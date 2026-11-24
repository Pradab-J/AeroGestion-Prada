/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package controlador;

import vista.Tema;
import modelo.DAO.integridadDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.DAO.asientoDAO;
import modelo.DAO.equipajeDAO;
import modelo.DAO.pasajeroGestionDAO;
import modelo.DAO.tiqueteDAO;
import modelo.DAO.vueloDAO;
import modelo.entidad.asiento;
import modelo.entidad.equipaje;
import modelo.entidad.pasajero;
import modelo.entidad.tiquete;
import modelo.entidad.vuelo;
import vista.JpnlTiquete;

/**
 * CONTROLADOR de Tiquetes (administrador): CRUD de tiquetes manteniendo el
 * asiento sincronizado (ocupado al crear, libre al eliminar) y evitando
 * reservas duplicadas de un pasajero en el mismo vuelo.
 */
public class tiqueteControlador implements ActionListener {

    private JpnlTiquete vista;
    private tiqueteDAO daoTiquete;
    private equipajeDAO daoEquipaje;
    private pasajeroGestionDAO daoPasajero;
    private asientoDAO daoAsiento;
    private vueloDAO daoVuelo;

    /** Constructor: guarda la vista y los DAO y conecta los botones con sus acciones. */
    public tiqueteControlador(
            JpnlTiquete vista,
            tiqueteDAO daoTiquete,
            equipajeDAO daoEquipaje,
            pasajeroGestionDAO daoPasajero,
            asientoDAO daoAsiento,
            vueloDAO daoVuelo) {

        this.vista = vista;
        this.daoTiquete = daoTiquete;
        this.daoEquipaje = daoEquipaje;
        this.daoPasajero = daoPasajero;
        this.daoAsiento = daoAsiento;
        this.daoVuelo = daoVuelo;

        vista.btnGuardar.addActionListener(this);
        vista.btnEditar.addActionListener(this);
        vista.btnEliminar.addActionListener(this);
        vista.btnLimpiar.addActionListener(this);

        vista.tblTiquete.getSelectionModel().addListSelectionListener(e -> {
            seleccionarTiquete();
        });

        cargarEquipajes();
        cargarPasajeros();
        cargarAsientos();
        cargarVuelos();
        Tema.alMostrar(vista, () -> {
            cargarEquipajes();
            cargarPasajeros();
            cargarAsientos();
            cargarVuelos();
        });
        listarTiquetes();
    }

    /** Recibe los clics de los botones de la vista y llama a la acción que corresponde. */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vista.btnGuardar) {
            guardarTiquete();
        }

        if (e.getSource() == vista.btnEditar) {
            editarTiquete();
        }

        if (e.getSource() == vista.btnEliminar) {
            eliminarTiquete();
        }

        if (e.getSource() == vista.btnLimpiar) {
            limpiar();
        }
    }

    // =========================================================
    // CARGAR EQUIPAJES
    // =========================================================

    /** Llena el combo de equipajes. */
    private void cargarEquipajes() {
        java.util.List<String> items = new java.util.ArrayList<>();
        for (equipaje e : daoEquipaje.listar()) {
            items.add(e.getId_equipaje() + " - " + e.getTipo() + " - " + e.getPeso() + " kg");
        }
        Tema.recargarCombo(vista.cmbEquipaje, items);
    }

    // =========================================================
    // CARGAR PASAJEROS
    // =========================================================

    /** Llena el combo de pasajeros. */
    private void cargarPasajeros() {
        java.util.List<String> items = new java.util.ArrayList<>();
        for (pasajero p : daoPasajero.listar()) {
            items.add(p.getDocumento() + " - " + p.getNombre() + " " + p.getApellido());
        }
        Tema.recargarCombo(vista.cmbPasajero, items);
    }

    // =========================================================
    // CARGAR ASIENTOS
    // =========================================================

    /** Llena el combo de asientos disponibles. */
    private void cargarAsientos() {
        // El texto incluye el vuelo y el estado para no confundirse (el identificador sigue siendo el primer numero)
        java.util.List<String> items = new java.util.ArrayList<>();
        // Se muestra el vuelo con su ruta y fecha (no solo el número) para distinguir los asientos
        java.util.Map<Integer, String> vuelos = VueloTexto.detalles();
        for (asiento a : daoAsiento.listar()) {
            items.add(a.getId_asiento() + " - " + a.getNum_asiento() + " - " + a.getClase()
                    + " - Vuelo " + a.getFk_vuelo() + " (" + vuelos.getOrDefault(a.getFk_vuelo(), "sin datos")
                    + ") · " + a.getEstado());
        }
        Tema.recargarCombo(vista.cmbAsiento, items);
    }

    // =========================================================
    // CARGAR VUELOS
    // =========================================================

    /** Llena el combo de vuelos. */
    private void cargarVuelos() {
        java.util.List<String> items = new java.util.ArrayList<>();
        java.util.Map<Integer, String> rutas = VueloTexto.rutas();
        for (vuelo v : daoVuelo.listar()) {
            items.add(VueloTexto.texto(v, rutas) + " - " + v.getFk_avion());
        }
        Tema.recargarCombo(vista.cmbVuelo, items);
    }

    // =========================================================
    // OBTENER PRECIO DEL ASIENTO
    // =========================================================

    /** Devuelve el precio del asiento con ese id. */
    private double obtenerPrecioAsiento(int idAsiento) {

        ArrayList<asiento> lista = daoAsiento.listar();

        for (asiento a : lista) {

            if (a.getId_asiento() == idAsiento) {

                return a.getPrecio();
            }
        }

        return 0;
    }

    // =========================================================
    // OBTENER PRECIO DEL EQUIPAJE
    // =========================================================

    /** Devuelve el precio del equipaje con ese id. */
    private double obtenerPrecioEquipaje(int idEquipaje) {

        ArrayList<equipaje> lista = daoEquipaje.listar();

        for (equipaje e : lista) {

            if (e.getId_equipaje() == idEquipaje) {

                return e.getPrecio();
            }
        }

        return 0;
    }

    // =========================================================
    // GUARDAR TIQUETE
    // =========================================================

    /** Valida el formulario y guarda un nuevo registro de tiquete; luego refresca la tabla y limpia el formulario. */
    private void guardarTiquete() {
        if (!validarTiquete(false)) {
            return;
        }

        try {

            if (vista.jDateEmision.getDate() == null) {

                JOptionPane.showMessageDialog(
                        vista,
                        "Seleccione la fecha de emisión."
                );

                return;
            }

            if (vista.cmbEquipaje.getSelectedItem() == null) {

                JOptionPane.showMessageDialog(
                        vista,
                        "Seleccione un equipaje."
                );

                return;
            }

            if (vista.cmbPasajero.getSelectedItem() == null) {

                JOptionPane.showMessageDialog(
                        vista,
                        "Seleccione un pasajero."
                );

                return;
            }

            if (vista.cmbAsiento.getSelectedItem() == null) {

                JOptionPane.showMessageDialog(
                        vista,
                        "Seleccione un asiento."
                );

                return;
            }

            if (vista.cmbVuelo.getSelectedItem() == null) {

                JOptionPane.showMessageDialog(
                        vista,
                        "Seleccione un vuelo."
                );

                return;
            }

            tiquete t = new tiquete();

            // =================================================
            // FECHA
            // =================================================

            SimpleDateFormat formato
                    = new SimpleDateFormat("yyyy-MM-dd");

            t.setFecha_emision(
                    formato.format(
                            vista.jDateEmision.getDate()
                    )
            );

            // =================================================
            // RESERVA
            // =================================================

            t.setReserva(
                    vista.txtReserva.getText().trim()
            );

            // =================================================
            // METODO DE PAGO
            // =================================================

            t.setMetodo_pago(
                    vista.cmbMetodoPago
                            .getSelectedItem()
                            .toString()
            );

            // =================================================
            // EQUIPAJE
            // =================================================

            String equipajeTexto
                    = vista.cmbEquipaje
                            .getSelectedItem()
                            .toString();

            int idEquipaje
                    = Integer.parseInt(
                            equipajeTexto
                                    .split(" - ")[0]
                    );

            t.setEquipaje(idEquipaje);

            // =================================================
            // PASAJERO
            // =================================================

            String pasajeroTexto
                    = vista.cmbPasajero
                            .getSelectedItem()
                            .toString();

            String documento
                    = pasajeroTexto
                            .split(" - ")[0];

            t.setPasajero(documento);

            // =================================================
            // ASIENTO
            // =================================================

            String asientoTexto
                    = vista.cmbAsiento
                            .getSelectedItem()
                            .toString();

            int idAsiento
                    = Integer.parseInt(
                            asientoTexto
                                    .split(" - ")[0]
                    );

            t.setAsiento(idAsiento);

            // =================================================
            // VUELO
            // =================================================

            String vueloTexto
                    = vista.cmbVuelo
                            .getSelectedItem()
                            .toString();

            int idVuelo
                    = Integer.parseInt(
                            vueloTexto
                                    .split(" - ")[0]
                    );

            t.setVuelo(idVuelo);

            // =================================================
            // CALCULAR PRECIOS AUTOMATICAMENTE
            // =================================================

            double precioAsiento
                    = obtenerPrecioAsiento(idAsiento);

            double precioEquipaje
                    = obtenerPrecioEquipaje(idEquipaje);

            double total
                    = precioAsiento + precioEquipaje;

            t.setPrecio_asiento(precioAsiento);
            t.setPrecio_equipaje(precioEquipaje);
            t.setTotal(total);

            // =================================================
            // GUARDAR
            // =================================================

            if (guardarOcupandoAsiento(t)) {

                JOptionPane.showMessageDialog(
                        vista,
                        "Tiquete guardado correctamente.\n\n"
                        + "Precio asiento: $"
                        + String.format(
                                "%,.0f",
                                precioAsiento
                        )
                        + "\n"
                        + "Precio equipaje: $"
                        + String.format(
                                "%,.0f",
                                precioEquipaje
                        )
                        + "\n"
                        + "TOTAL: $"
                        + String.format(
                                "%,.0f",
                                total
                        )
                );

                listarTiquetes();
                limpiar();

            } else {

                JOptionPane.showMessageDialog(
                        vista,
                        "No se pudo guardar el tiquete."
                );
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Error al guardar el tiquete: "
                    + ex.getMessage()
            );
        }
    }

    // =========================================================
    // LISTAR TIQUETES
    // =========================================================

    /** Carga todos los tiquetes en la tabla. */
    private void listarTiquetes() {

        ArrayList<tiquete> lista
                = daoTiquete.listar();

        DefaultTableModel modelo
                = new DefaultTableModel();

        modelo.addColumn("ID");
        modelo.addColumn("FECHA EMISIÓN");
        modelo.addColumn("RESERVA");
        modelo.addColumn("MÉTODO PAGO");
        modelo.addColumn("EQUIPAJE");
        modelo.addColumn("PASAJERO");
        modelo.addColumn("ASIENTO");
        modelo.addColumn("VUELO");
        modelo.addColumn("PRECIO ASIENTO");
        modelo.addColumn("PRECIO EQUIPAJE");
        modelo.addColumn("TOTAL");

        for (tiquete t : lista) {

            Object[] datos = {

                t.getId_tiquete(),
                t.getFecha_emision(),
                t.getReserva(),
                t.getMetodo_pago(),
                t.getEquipaje(),
                t.getPasajero(),
                t.getAsiento(),
                t.getVuelo(),
                "$" + String.format(
                        "%,.0f",
                        t.getPrecio_asiento()
                ),
                "$" + String.format(
                        "%,.0f",
                        t.getPrecio_equipaje()
                ),
                "$" + String.format(
                        "%,.0f",
                        t.getTotal()
                )
            };

            modelo.addRow(datos);
        }

        vista.tblTiquete.setModel(modelo);
    }

    // =========================================================
    // SELECCIONAR REGISTRO DE LA TABLA
    // =========================================================

    /** Pasa al formulario los datos del tiquete elegido en la tabla. */
    private void seleccionarTiquete() {

        int fila
                = vista.tblTiquete.getSelectedRow();

        if (fila == -1) {
            return;
        }

        vista.txtIdTiquete.setText(
                vista.tblTiquete
                        .getValueAt(fila, 0)
                        .toString()
        );

        vista.txtReserva.setText(
                vista.tblTiquete
                        .getValueAt(fila, 2)
                        .toString()
        );

        String metodoPago
                = vista.tblTiquete
                        .getValueAt(fila, 3)
                        .toString();

        vista.cmbMetodoPago.setSelectedItem(
                metodoPago
        );

        int idEquipaje
                = Integer.parseInt(
                        vista.tblTiquete
                                .getValueAt(fila, 4)
                                .toString()
                );

        seleccionarEquipaje(idEquipaje);

        String documento
                = vista.tblTiquete
                        .getValueAt(fila, 5)
                        .toString();

        seleccionarPasajero(documento);

        int idAsiento
                = Integer.parseInt(
                        vista.tblTiquete
                                .getValueAt(fila, 6)
                                .toString()
                );

        seleccionarAsiento(idAsiento);

        int idVuelo
                = Integer.parseInt(
                        vista.tblTiquete
                                .getValueAt(fila, 7)
                                .toString()
                );

        seleccionarVuelo(idVuelo);

        try {

            String fecha
                    = vista.tblTiquete
                            .getValueAt(fila, 1)
                            .toString();

            java.util.Date fechaDate
                    = new SimpleDateFormat(
                            "yyyy-MM-dd"
                    ).parse(fecha);

            vista.jDateEmision.setDate(fechaDate);

        } catch (Exception ex) {

            System.out.println(
                    "Error con la fecha: "
                    + ex
            );
        }
    }

    // =========================================================
    // SELECCIONAR EQUIPAJE
    // =========================================================

    /** Selecciona en el combo el equipaje con ese id. */
    private void seleccionarEquipaje(int id) {

        for (int i = 0;
                i < vista.cmbEquipaje.getItemCount();
                i++) {

            String texto
                    = vista.cmbEquipaje
                            .getItemAt(i);

            int idCombo
                    = Integer.parseInt(
                            texto.split(" - ")[0]
                    );

            if (idCombo == id) {

                vista.cmbEquipaje
                        .setSelectedIndex(i);

                break;
            }
        }
    }

    // =========================================================
    // SELECCIONAR PASAJERO
    // =========================================================

    /** Selecciona en el combo el pasajero con ese documento. */
    private void seleccionarPasajero(String documento) {

        for (int i = 0;
                i < vista.cmbPasajero.getItemCount();
                i++) {

            String texto
                    = vista.cmbPasajero
                            .getItemAt(i);

            String documentoCombo
                    = texto.split(" - ")[0];

            if (documentoCombo.equals(documento)) {

                vista.cmbPasajero
                        .setSelectedIndex(i);

                break;
            }
        }
    }

    // =========================================================
    // SELECCIONAR ASIENTO
    // =========================================================

    /** Selecciona en el combo el asiento con ese id. */
    private void seleccionarAsiento(int id) {

        for (int i = 0;
                i < vista.cmbAsiento.getItemCount();
                i++) {

            String texto
                    = vista.cmbAsiento
                            .getItemAt(i);

            int idCombo
                    = Integer.parseInt(
                            texto.split(" - ")[0]
                    );

            if (idCombo == id) {

                vista.cmbAsiento
                        .setSelectedIndex(i);

                break;
            }
        }
    }

    // =========================================================
    // SELECCIONAR VUELO
    // =========================================================

    /** Selecciona en el combo el vuelo con ese id. */
    private void seleccionarVuelo(int id) {

        for (int i = 0;
                i < vista.cmbVuelo.getItemCount();
                i++) {

            String texto
                    = vista.cmbVuelo
                            .getItemAt(i);

            int idCombo
                    = Integer.parseInt(
                            texto.split(" - ")[0]
                    );

            if (idCombo == id) {

                vista.cmbVuelo
                        .setSelectedIndex(i);

                break;
            }
        }
    }

    // =========================================================
    // EDITAR
    // =========================================================

    /** Valida y actualiza el registro de tiquete seleccionado en la tabla. */
    private void editarTiquete() {
        if (!validarTiquete(true)) {
            return;
        }

        try {

            if (vista.txtIdTiquete
                    .getText()
                    .trim()
                    .isEmpty()) {

                JOptionPane.showMessageDialog(
                        vista,
                        "Seleccione un tiquete de la tabla."
                );

                return;
            }

            tiquete t = new tiquete();

            int idTiquete
                    = Integer.parseInt(
                            vista.txtIdTiquete
                                    .getText()
                                    .trim()
                    );

            t.setId_tiquete(idTiquete);

            if (vista.jDateEmision.getDate() == null) {

                JOptionPane.showMessageDialog(
                        vista,
                        "Seleccione la fecha de emisión."
                );

                return;
            }

            SimpleDateFormat formato
                    = new SimpleDateFormat("yyyy-MM-dd");

            t.setFecha_emision(
                    formato.format(
                            vista.jDateEmision.getDate()
                    )
            );

            t.setReserva(
                    vista.txtReserva
                            .getText()
                            .trim()
            );

            t.setMetodo_pago(
                    vista.cmbMetodoPago
                            .getSelectedItem()
                            .toString()
            );

            // EQUIPAJE

            String equipajeTexto
                    = vista.cmbEquipaje
                            .getSelectedItem()
                            .toString();

            int idEquipaje
                    = Integer.parseInt(
                            equipajeTexto
                                    .split(" - ")[0]
                    );

            t.setEquipaje(idEquipaje);

            // PASAJERO

            String pasajeroTexto
                    = vista.cmbPasajero
                            .getSelectedItem()
                            .toString();

            t.setPasajero(
                    pasajeroTexto
                            .split(" - ")[0]
            );

            // ASIENTO

            String asientoTexto
                    = vista.cmbAsiento
                            .getSelectedItem()
                            .toString();

            int idAsiento
                    = Integer.parseInt(
                            asientoTexto
                                    .split(" - ")[0]
                    );

            t.setAsiento(idAsiento);

            // VUELO

            String vueloTexto
                    = vista.cmbVuelo
                            .getSelectedItem()
                            .toString();

            int idVuelo
                    = Integer.parseInt(
                            vueloTexto
                                    .split(" - ")[0]
                    );

            t.setVuelo(idVuelo);

            // =================================================
            // RECALCULAR PRECIOS
            // =================================================

            double precioAsiento
                    = obtenerPrecioAsiento(idAsiento);

            double precioEquipaje
                    = obtenerPrecioEquipaje(idEquipaje);

            double total
                    = precioAsiento + precioEquipaje;

            t.setPrecio_asiento(precioAsiento);
            t.setPrecio_equipaje(precioEquipaje);
            t.setTotal(total);

            // =================================================
            // EDITAR
            // =================================================

            if (editarSincronizandoAsiento(t)) {

                JOptionPane.showMessageDialog(
                        vista,
                        "Tiquete actualizado correctamente.\n\n"
                        + "Precio asiento: $"
                        + String.format(
                                "%,.0f",
                                precioAsiento
                        )
                        + "\n"
                        + "Precio equipaje: $"
                        + String.format(
                                "%,.0f",
                                precioEquipaje
                        )
                        + "\n"
                        + "TOTAL: $"
                        + String.format(
                                "%,.0f",
                                total
                        )
                );

                listarTiquetes();
                limpiar();

            } else {

                JOptionPane.showMessageDialog(
                        vista,
                        "No se pudo actualizar el tiquete."
                );
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Error al editar: "
                    + ex.getMessage()
            );
        }
    }

    // =========================================================
    // ELIMINAR
    // =========================================================

    /** Elimina el registro de tiquete seleccionado, tras pedir confirmación y comprobar que nada dependa de él. */
    private void eliminarTiquete() {

        try {

            if (vista.txtIdTiquete
                    .getText()
                    .trim()
                    .isEmpty()) {

                JOptionPane.showMessageDialog(
                        vista,
                        "Seleccione un tiquete de la tabla."
                );

                return;
            }

            int id
                    = Integer.parseInt(
                            vista.txtIdTiquete
                                    .getText()
                                    .trim()
                    );

            int respuesta
                    = JOptionPane.showConfirmDialog(
                            vista,
                            "¿Está seguro de eliminar este tiquete?",
                            "Confirmar eliminación",
                            JOptionPane.YES_NO_OPTION
                    );

            if (respuesta
                    == JOptionPane.YES_OPTION) {

                if (eliminarLiberandoAsiento(id)) {

                    JOptionPane.showMessageDialog(
                            vista,
                            "Tiquete eliminado correctamente."
                    );

                    listarTiquetes();
                    limpiar();

                } else {

                    JOptionPane.showMessageDialog(
                            vista,
                            "No se pudo eliminar el tiquete."
                    );
                }
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Error al eliminar: "
                    + ex.getMessage()
            );
        }
    }

    // =========================================================
    // LIMPIAR
    // =========================================================

    /** Vacía el formulario y quita la selección de la tabla. */
    private void limpiar() {

        vista.txtIdTiquete.setText("");
        vista.txtReserva.setText("");
        vista.jDateEmision.setDate(null);

        if (vista.cmbMetodoPago.getItemCount() > 0) {
            vista.cmbMetodoPago.setSelectedIndex(0);
        }

        if (vista.cmbEquipaje.getItemCount() > 0) {
            vista.cmbEquipaje.setSelectedIndex(0);
        }

        if (vista.cmbPasajero.getItemCount() > 0) {
            vista.cmbPasajero.setSelectedIndex(0);
        }

        if (vista.cmbAsiento.getItemCount() > 0) {
            vista.cmbAsiento.setSelectedIndex(0);
        }

        if (vista.cmbVuelo.getItemCount() > 0) {
            vista.cmbVuelo.setSelectedIndex(0);
        }

        vista.tblTiquete.clearSelection();
    }

    /** Los combos muestran "id - descripción"; esto extrae el id (número antes del guion). */
    // =========================================================
    // ASIENTO SIEMPRE SINCRONIZADO CON EL TIQUETE
    // =========================================================
    private int idPrefijo(Object item) {
        try {
            return Integer.parseInt(item.toString().split(" - ")[0].trim());
        } catch (Exception e) {
            return -1;
        }
    }

    /** Busca un asiento por su id (null si no existe). */
    private asiento buscarAsientoPorId(int id) {
        for (asiento a : daoAsiento.listar()) {
            if (a.getId_asiento() == id) {
                return a;
            }
        }
        return null;
    }

    /** Busca un tiquete por su id (null si no existe). */
    private tiquete buscarTiquetePorId(int id) {
        for (tiquete t : daoTiquete.listar()) {
            if (t.getId_tiquete() == id) {
                return t;
            }
        }
        return null;
    }

    /** Valida el formulario y guarda un nuevo registro de tiquete; luego refresca la tabla y limpia el formulario. */
    private boolean guardarOcupandoAsiento(tiquete t) {
        // Un pasajero solo puede tener una reserva vigente por vuelo
        if (modelo.DAO.integridadDAO.pasajeroYaReservoVuelo(t.getPasajero(), t.getVuelo())) {
            Validar.aviso(vista, "Ese pasajero ya tiene una reserva en este vuelo.");
            return false;
        }
        // El asiento se ocupa primero y de forma atomica: si otro lo tomo, no se vende dos veces
        if (!daoAsiento.reservarSiDisponible(t.getAsiento())) {
            Validar.aviso(vista, "El asiento ya no está disponible. Elija otro.");
            cargarAsientos();
            return false;
        }
        if (daoTiquete.guardar(t)) {
            cargarAsientos();
            return true;
        }
        daoAsiento.liberar(t.getAsiento()); // si no se pudo guardar, el asiento no queda bloqueado
        cargarAsientos();
        return false;
    }

    /** Edita el tiquete y ajusta los asientos (libera el anterior y ocupa el nuevo) de forma coherente. */
    private boolean editarSincronizandoAsiento(tiquete t) {
        tiquete anterior = buscarTiquetePorId(t.getId_tiquete());
        int viejo = anterior == null ? t.getAsiento() : anterior.getAsiento();
        boolean cambia = viejo != t.getAsiento();
        if (cambia && !daoAsiento.reservarSiDisponible(t.getAsiento())) {
            Validar.aviso(vista, "El asiento elegido ya no está disponible.");
            cargarAsientos();
            return false;
        }
        if (daoTiquete.editar(t)) {
            if (cambia) {
                daoAsiento.liberar(viejo);
            }
            cargarAsientos();
            return true;
        }
        if (cambia) {
            daoAsiento.liberar(t.getAsiento());
        }
        cargarAsientos();
        return false;
    }

    /** Elimina el tiquete y deja libre su asiento. */
    private boolean eliminarLiberandoAsiento(int idTiquete) {
        boolean ok = integridadDAO.eliminarTiqueteLiberandoAsiento(idTiquete);
        if (ok) {
            cargarAsientos();
        }
        return ok;
    }

    /** Revisa los campos del formulario; muestra el primer error y devuelve false si hay datos inválidos. */
    // =========================================================
    // VALIDACIONES
    // =========================================================
    private boolean validarTiquete(boolean edicion) {
        Integer idTiquete = null;
        if (edicion) {
            idTiquete = Validar.aEntero(vista.txtIdTiquete.getText());
            if (idTiquete == null) {
                Validar.aviso(vista, "Seleccione un tiquete de la tabla antes de editar.");
                return false;
            }
        }
        Object sPasajero = vista.cmbPasajero.getSelectedItem();
        Object sVuelo = vista.cmbVuelo.getSelectedItem();
        Object sAsiento = vista.cmbAsiento.getSelectedItem();
        Object sEquipaje = vista.cmbEquipaje.getSelectedItem();
        Object sPago = vista.cmbMetodoPago.getSelectedItem();

        String error = Validar.primero(
                Validar.seleccion("Pasajero", sPasajero),
                Validar.seleccion("Vuelo", sVuelo),
                Validar.seleccion("Asiento", sAsiento),
                Validar.seleccion("Equipaje", sEquipaje),
                Validar.seleccion("Método de pago", sPago),
                vista.jDateEmision.getDate() == null ? "Seleccione la fecha de emisión." : null);

        if (error == null) {
            int idVuelo = idPrefijo(sVuelo);
            int idAsiento = idPrefijo(sAsiento);
            java.time.LocalDate emision = vista.jDateEmision.getDate().toInstant()
                    .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            asiento a = idAsiento < 0 ? null : buscarAsientoPorId(idAsiento);
            vuelo v = idVuelo < 0 ? null : daoVuelo.buscarPorId(idVuelo);

            if (idVuelo < 0 || idAsiento < 0 || a == null || v == null) {
                error = "No se pudo leer el vuelo o el asiento seleccionado. Recargue la pantalla e intente de nuevo.";
            } else if (emision.isAfter(java.time.LocalDate.now())) {
                error = "La fecha de emisión no puede ser futura.";
            } else if (a.getFk_vuelo() != idVuelo) {
                error = "El asiento " + a.getNum_asiento() + " pertenece al vuelo " + a.getFk_vuelo()
                        + ", no al vuelo " + idVuelo + ".\nElija un asiento del vuelo seleccionado.";
            } else if (v.getEstado() != null && (v.getEstado().equalsIgnoreCase("Cancelado")
                    || v.getEstado().equalsIgnoreCase("Finalizado"))) {
                error = "No se pueden registrar tiquetes en un vuelo " + v.getEstado().toLowerCase() + ".";
            } else if (integridadDAO.tiquetesActivosDeAsiento(idAsiento, idTiquete == null ? 0 : idTiquete) > 0) {
                error = "Ese asiento ya tiene una reserva activa.";
            } else {
                boolean libre = "Disponible".equalsIgnoreCase(a.getEstado());
                if (!edicion && !libre) {
                    error = "El asiento " + a.getNum_asiento() + " no está disponible (estado: " + a.getEstado() + ").";
                } else if (edicion) {
                    tiquete actual = buscarTiquetePorId(idTiquete);
                    if (actual != null && actual.getAsiento() != idAsiento && !libre) {
                        error = "El asiento " + a.getNum_asiento() + " no está disponible (estado: " + a.getEstado() + ").";
                    }
                }
            }
        }

        if (error == null) {
            String reserva = Validar.limpiar(vista.txtReserva.getText());
            if (reserva.isEmpty() && !edicion) {
                reserva = "RES-" + System.currentTimeMillis(); // se genera sola si se deja vacia
            }
            vista.txtReserva.setText(reserva);
            error = Validar.primero(
                    Validar.codigoReserva(reserva),
                    integridadDAO.existeOtro("tiquete", "reserva", reserva, "id_tiquete", idTiquete)
                    ? "Ya existe una reserva con el código «" + reserva + "»." : null);
        }
        return !Validar.mostrar(vista, error);
    }
}
