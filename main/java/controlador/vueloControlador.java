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

import modelo.DAO.avionDAO;
import modelo.DAO.rutaDAO;
import modelo.DAO.vueloDAO;

import modelo.entidad.avion;
import modelo.entidad.ruta;
import modelo.entidad.vuelo;

import vista.JpnlVuelo;

/**
 * CONTROLADOR de Vuelos: CRUD de vuelos con validación de fechas, horas y
 * cruces de horario del mismo avión; al crear un vuelo genera sus asientos.
 */
public class vueloControlador implements ActionListener {

    private JpnlVuelo vistaVuelo;
    private vueloDAO daoVuelo;
    private avionDAO daoAvion;
    private rutaDAO daoRuta;
    private boolean cargando = false;
    private String estadoPrevio = null;

    /** Constructor: guarda la vista y los DAO y conecta los botones con sus acciones. */
    public vueloControlador(
            JpnlVuelo vista,
            vueloDAO daoVuelo,
            avionDAO daoAvion,
            rutaDAO daoRuta) {

        this.vistaVuelo = vista;
        this.daoVuelo = daoVuelo;
        this.daoAvion = daoAvion;
        this.daoRuta = daoRuta;

        vistaVuelo.btnGuardar.addActionListener(this);
        vistaVuelo.btnEditar.addActionListener(this);
        vistaVuelo.btnEliminar.addActionListener(this);
        vistaVuelo.btnLimpiar.addActionListener(this);

        cargarAviones();
        cargarRutas();
        Tema.alMostrar(vistaVuelo, () -> {
            cargarAviones();
            cargarRutas();
        });
        listarVuelos();
        configurarLlegadaAutomatica();
        configurarEstado();

        vistaVuelo.tblVuelo.addMouseListener(
                new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {

                int fila = vistaVuelo.tblVuelo.getSelectedRow();

                if (fila >= 0) {

                    cargando = true;
                    vistaVuelo.txtIdVuelo.setText(
                            vistaVuelo.tblVuelo
                                    .getValueAt(fila, 0)
                                    .toString()
                    );

                    try {

                        SimpleDateFormat formato
                                = new SimpleDateFormat("yyyy-MM-dd");

                        vistaVuelo.jDateSalida.setDate(
                                formato.parse(
                                        vistaVuelo.tblVuelo
                                                .getValueAt(fila, 1)
                                                .toString()
                                )
                        );

                        vistaVuelo.jDateLlegada.setDate(
                                formato.parse(
                                        vistaVuelo.tblVuelo
                                                .getValueAt(fila, 2)
                                                .toString()
                                )
                        );

                    } catch (Exception ex) {
                        System.out.println(ex);
                    }

                    vistaVuelo.txtSalida.setText(
                            vistaVuelo.tblVuelo
                                    .getValueAt(fila, 3)
                                    .toString()
                    );

                    vistaVuelo.txtLlegada.setText(
                            vistaVuelo.tblVuelo
                                    .getValueAt(fila, 4)
                                    .toString()
                    );

                    vistaVuelo.cmbEstado.setSelectedItem(
                            vistaVuelo.tblVuelo
                                    .getValueAt(fila, 5)
                                    .toString()
                    );

                    String modeloAvion
                            = vistaVuelo.tblVuelo
                                    .getValueAt(fila, 6)
                                    .toString();

                    int idRuta
                            = Integer.parseInt(
                                    vistaVuelo.tblVuelo
                                            .getValueAt(fila, 7)
                                            .toString()
                            );

                    seleccionarAvion(modeloAvion);
                    seleccionarRuta(idRuta);
                    cargando = false;
                    estadoPrevio = String.valueOf(vistaVuelo.cmbEstado.getSelectedItem());
                    calcularLlegada();
                }
            }
        }
        );
    }

    /** Recibe los clics de los botones de la vista y llama a la acción que corresponde. */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vistaVuelo.btnGuardar) {

            guardarVuelo();

        } else if (e.getSource() == vistaVuelo.btnEditar) {

            editarVuelo();

        } else if (e.getSource() == vistaVuelo.btnEliminar) {

            eliminarVuelo();

        } else if (e.getSource() == vistaVuelo.btnLimpiar) {

            limpiarCampos();
        }
    }

    /** Llena el combo de aviones. */
    // =========================
    // CARGAR AVIONES
    // =========================
    private void cargarAviones() {
        java.util.List<String> items = new java.util.ArrayList<>();
        for (avion a : daoAvion.listar()) {
            items.add(a.getModelo());
        }
        Tema.recargarCombo(vistaVuelo.cmbAvion, items);
    }

    /** Llena el combo de rutas. */
    // =========================
    // CARGAR RUTAS
    // =========================
    private void cargarRutas() {
        java.util.List<String> items = new java.util.ArrayList<>();
        for (ruta r : daoRuta.listar()) {
            items.add(r.getId_ruta() + " - " + r.getOrigen() + " / " + r.getDestino());
        }
        Tema.recargarCombo(vistaVuelo.cmbRuta, items);
    }

    /** Selecciona en el combo el avión con ese modelo. */
    // =========================
    // SELECCIONAR AVION
    // =========================
    private void seleccionarAvion(String modeloAvion) {

        for (int i = 0;
                i < vistaVuelo.cmbAvion.getItemCount();
                i++) {

            String modelo
                    = vistaVuelo.cmbAvion.getItemAt(i);

            if (modelo.equals(modeloAvion)) {

                vistaVuelo.cmbAvion.setSelectedIndex(i);
                break;
            }
        }
    }

    /** Selecciona en el combo la ruta con ese id. */
    // =========================
    // SELECCIONAR RUTA
    // =========================
    private void seleccionarRuta(int idRuta) {

        for (int i = 0;
                i < vistaVuelo.cmbRuta.getItemCount();
                i++) {

            String item
                    = vistaVuelo.cmbRuta.getItemAt(i);

            int id
                    = Integer.parseInt(
                            item.split(" - ")[0]
                    );

            if (id == idRuta) {

                vistaVuelo.cmbRuta.setSelectedIndex(i);
                break;
            }
        }
    }

    /** Devuelve el modelo del avión elegido en el combo. */
    // =========================
    // OBTENER MODELO AVION
    // =========================
    private String obtenerModeloAvion() {

        return vistaVuelo.cmbAvion
                .getSelectedItem()
                .toString();
    }

    /** Devuelve el id de la ruta elegida en el combo (-1 si no hay). */
    // =========================
    // OBTENER ID RUTA
    // =========================
    private int obtenerIdRuta() {

        String item
                = vistaVuelo.cmbRuta
                        .getSelectedItem()
                        .toString();

        return Integer.parseInt(
                item.split(" - ")[0]
        );
    }

    /** Valida el formulario y guarda un nuevo registro de vuelo; luego refresca la tabla y limpia el formulario. */
    // =========================
    // GUARDAR VUELO
    // =========================
    private void guardarVuelo() {
        if (!validarVuelo(false)) {
            return;
        }

        if (vistaVuelo.jDateSalida.getDate() == null
                || vistaVuelo.jDateLlegada.getDate() == null) {

            JOptionPane.showMessageDialog(
                    null,
                    "Seleccione las fechas de salida y llegada"
            );

            return;
        }

        if (vistaVuelo.txtSalida.getText()
                .trim().isEmpty()
                || vistaVuelo.txtLlegada.getText()
                        .trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    null,
                    "Ingrese las horas de salida y llegada"
            );

            return;
        }

        if (vistaVuelo.cmbAvion.getSelectedItem() == null
                || vistaVuelo.cmbRuta.getSelectedItem() == null) {

            JOptionPane.showMessageDialog(
                    null,
                    "Seleccione el avión y la ruta"
            );

            return;
        }

        SimpleDateFormat formato
                = new SimpleDateFormat("yyyy-MM-dd");

        String fechaSalida
                = formato.format(
                        vistaVuelo.jDateSalida.getDate()
                );

        String fechaLlegada
                = formato.format(
                        vistaVuelo.jDateLlegada.getDate()
                );

        String horaSalida
                = vistaVuelo.txtSalida.getText().trim();

        String horaLlegada
                = vistaVuelo.txtLlegada.getText().trim();

        String estado
                = vistaVuelo.cmbEstado
                        .getSelectedItem()
                        .toString();

        String modeloAvion
                = obtenerModeloAvion();

        int idRuta
                = obtenerIdRuta();

        vuelo v = new vuelo();

        v.setFecha_salida(fechaSalida);
        v.setFecha_llegada(fechaLlegada);
        v.setHora_salida(horaSalida);
        v.setHora_llegada(horaLlegada);
        v.setEstado(estado);
        v.setFk_avion(modeloAvion);
        v.setFk_ruta(idRuta);

        if (daoVuelo.guardar(v)) {

            JOptionPane.showMessageDialog(
                    null,
                    "Vuelo guardado correctamente"
            );

            listarVuelos();
            limpiarCampos();

        } else {

            JOptionPane.showMessageDialog(
                    null,
                    "Error al guardar el vuelo"
            );
        }
    }

    /** Carga en la tabla los registros de vuelo que hay en la base de datos. */
    // =========================
    // LISTAR VUELOS
    // =========================
    private void listarVuelos() {

        ArrayList<vuelo> lista
                = daoVuelo.listar();

        DefaultTableModel modelo
                = new DefaultTableModel();

        modelo.addColumn("ID Vuelo");
        modelo.addColumn("Fecha Salida");
        modelo.addColumn("Fecha Llegada");
        modelo.addColumn("Hora Salida");
        modelo.addColumn("Hora Llegada");
        modelo.addColumn("Estado");
        modelo.addColumn("Avión");
        modelo.addColumn("ID Ruta");

        for (vuelo v : lista) {

            modelo.addRow(new Object[]{
                v.getId_vuelo(),
                v.getFecha_salida(),
                v.getFecha_llegada(),
                v.getHora_salida(),
                v.getHora_llegada(),
                v.getEstado(),
                v.getFk_avion(),
                v.getFk_ruta()
            });
        }

        vistaVuelo.tblVuelo.setModel(modelo);
    }

    /** Valida y actualiza el registro de vuelo seleccionado en la tabla. */
    // =========================
    // EDITAR VUELO
    // =========================
    private void editarVuelo() {
        if (!validarVuelo(true)) {
            return;
        }

        if (vistaVuelo.txtIdVuelo.getText()
                .trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    null,
                    "Seleccione un vuelo de la tabla"
            );

            return;
        }

        if (vistaVuelo.jDateSalida.getDate() == null
                || vistaVuelo.jDateLlegada.getDate() == null) {

            JOptionPane.showMessageDialog(
                    null,
                    "Seleccione las fechas de salida y llegada"
            );

            return;
        }

        if (vistaVuelo.txtSalida.getText()
                .trim().isEmpty()
                || vistaVuelo.txtLlegada.getText()
                        .trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    null,
                    "Ingrese las horas de salida y llegada"
            );

            return;
        }

        if (vistaVuelo.cmbAvion.getSelectedItem() == null
                || vistaVuelo.cmbRuta.getSelectedItem() == null) {

            JOptionPane.showMessageDialog(
                    null,
                    "Seleccione el avión y la ruta"
            );

            return;
        }

        int idVuelo
                = Integer.parseInt(
                        vistaVuelo.txtIdVuelo
                                .getText()
                                .trim()
                );

        SimpleDateFormat formato
                = new SimpleDateFormat("yyyy-MM-dd");

        String fechaSalida
                = formato.format(
                        vistaVuelo.jDateSalida.getDate()
                );

        String fechaLlegada
                = formato.format(
                        vistaVuelo.jDateLlegada.getDate()
                );

        String horaSalida
                = vistaVuelo.txtSalida
                        .getText()
                        .trim();

        String horaLlegada
                = vistaVuelo.txtLlegada
                        .getText()
                        .trim();

        String estado
                = vistaVuelo.cmbEstado
                        .getSelectedItem()
                        .toString();

        String modeloAvion
                = obtenerModeloAvion();

        int idRuta
                = obtenerIdRuta();

        vuelo v = new vuelo();

        v.setId_vuelo(idVuelo);
        v.setFecha_salida(fechaSalida);
        v.setFecha_llegada(fechaLlegada);
        v.setHora_salida(horaSalida);
        v.setHora_llegada(horaLlegada);
        v.setEstado(estado);
        v.setFk_avion(modeloAvion);
        v.setFk_ruta(idRuta);

        if (daoVuelo.editar(v)) {

            JOptionPane.showMessageDialog(
                    null,
                    "Vuelo actualizado correctamente"
            );

            listarVuelos();
            limpiarCampos();

        } else {

            JOptionPane.showMessageDialog(
                    null,
                    "Error al actualizar el vuelo"
            );
        }
    }

    /** Elimina el registro de vuelo seleccionado, tras pedir confirmación y comprobar que nada dependa de él. */
    // =========================
    // ELIMINAR VUELO
    // =========================
    private void eliminarVuelo() {
        if (!puedeEliminarVuelo()) {
            return;
        }

        if (vistaVuelo.txtIdVuelo.getText()
                .trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    null,
                    "Seleccione un vuelo de la tabla"
            );

            return;
        }

        int idVuelo
                = Integer.parseInt(
                        vistaVuelo.txtIdVuelo
                                .getText()
                                .trim()
                );

        int confirmar
                = JOptionPane.showConfirmDialog(
                        null,
                        "¿Está seguro de eliminar este vuelo?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirmar == JOptionPane.YES_OPTION) {

            if (integridadDAO.eliminarVueloConAsientos(idVuelo)) {

                JOptionPane.showMessageDialog(
                        null,
                        "Vuelo eliminado correctamente"
                );

                listarVuelos();
                limpiarCampos();

            } else {

                JOptionPane.showMessageDialog(
                        null,
                        "Error al eliminar el vuelo"
                );
            }
        }
    }

    /** Vacía el formulario y quita la selección de la tabla. */
    // =========================
    // LIMPIAR CAMPOS
    // =========================
    private void limpiarCampos() {

        cargando = true;
        vistaVuelo.txtIdVuelo.setText("");

        vistaVuelo.jDateSalida.setDate(null);
        vistaVuelo.jDateLlegada.setDate(null);

        vistaVuelo.txtSalida.setText("");
        vistaVuelo.txtLlegada.setText("");

        if (vistaVuelo.cmbEstado.getItemCount() > 0) {

            vistaVuelo.cmbEstado.setSelectedIndex(0);
        }

        if (vistaVuelo.cmbAvion.getItemCount() > 0) {

            vistaVuelo.cmbAvion.setSelectedIndex(0);
        }

        if (vistaVuelo.cmbRuta.getItemCount() > 0) {

            vistaVuelo.cmbRuta.setSelectedIndex(0);
        }

        vistaVuelo.tblVuelo.clearSelection();
        cargando = false;
        estadoPrevio = String.valueOf(vistaVuelo.cmbEstado.getSelectedItem());
        calcularLlegada();
    }

    /** Duración (en minutos) de la ruta elegida en el combo; -1 si no hay ruta. */
    private int duracionRutaSeleccionada() {
        Object sel = vistaVuelo.cmbRuta.getSelectedItem();
        if (sel == null) {
            return -1;
        }
        try {
            int idRuta = Integer.parseInt(sel.toString().split(" - ")[0].trim());
            for (ruta r : daoRuta.listar()) {
                if (r.getId_ruta() == idRuta) {
                    return r.getDuracion();
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return -1;
    }

    /**
     * Calcula fecha y hora de llegada = salida + duración de la ruta, y las
     * muestra en los campos (bloqueados). Devuelve false si faltan datos.
     */
    private boolean calcularLlegada() {
        java.util.Date fs = vistaVuelo.jDateSalida.getDate();
        java.time.LocalTime hs = Validar.aHora(vistaVuelo.txtSalida.getText());
        int minutos = duracionRutaSeleccionada();
        if (fs == null || hs == null || minutos <= 0) {
            vistaVuelo.jDateLlegada.setDate(null);
            vistaVuelo.txtLlegada.setText("");
            return false;
        }
        java.time.LocalDateTime fin = fs.toInstant().atZone(java.time.ZoneId.systemDefault())
                .toLocalDate().atTime(hs).plusMinutes(minutos);
        vistaVuelo.jDateLlegada.setDate(java.util.Date.from(
                fin.toLocalDate().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()));
        vistaVuelo.txtLlegada.setText(fin.toLocalTime().format(
                java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
        return true;
    }

    /** Bloquea la llegada y la recalcula al cambiar ruta, fecha u hora de salida. */
    private void configurarLlegadaAutomatica() {
        vistaVuelo.jDateLlegada.setEnabled(false);
        vistaVuelo.txtLlegada.setEditable(false);
        vistaVuelo.txtLlegada.setFocusable(false);

        vistaVuelo.cmbRuta.addActionListener(e -> {
            if (!cargando) {
                calcularLlegada();
            }
        });
        vistaVuelo.jDateSalida.addPropertyChangeListener("date", e -> {
            if (!cargando) {
                calcularLlegada();
            }
        });
        vistaVuelo.txtSalida.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                if (!cargando) {
                    calcularLlegada();
                }
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                if (!cargando) {
                    calcularLlegada();
                }
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
            }
        });
    }

    /** "Finalizado" solo se permite desde la fecha de salida del vuelo (hoy o después de salir). */
    private boolean finalizadoPermitido(java.time.LocalDate fechaSalida) {
        return fechaSalida != null && !fechaSalida.isAfter(java.time.LocalDate.now());
    }

    /** Impide elegir "Finalizado" antes de la fecha de salida. */
    private void configurarEstado() {
        estadoPrevio = String.valueOf(vistaVuelo.cmbEstado.getSelectedItem());
        vistaVuelo.cmbEstado.addActionListener(e -> {
            if (cargando) {
                return;
            }
            Object sel = vistaVuelo.cmbEstado.getSelectedItem();
            if (sel != null && sel.toString().equalsIgnoreCase("Finalizado")) {
                java.util.Date fs = vistaVuelo.jDateSalida.getDate();
                java.time.LocalDate d = fs == null ? null
                        : fs.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                if (!finalizadoPermitido(d)) {
                    JOptionPane.showMessageDialog(vistaVuelo, d == null
                            ? "Primero seleccione la fecha de salida."
                            : "El vuelo solo puede marcarse como Finalizado desde su fecha de salida ("
                            + d + ").");
                    cargando = true;
                    vistaVuelo.cmbEstado.setSelectedItem(estadoPrevio);
                    cargando = false;
                    return;
                }
            }
            estadoPrevio = sel == null ? null : sel.toString();
        });
    }

    /** Revisa los campos del formulario; muestra el primer error y devuelve false si hay datos inválidos. */
    // =========================================================
    // VALIDACIONES
    // =========================================================
    private boolean validarVuelo(boolean edicion) {
        Integer id = null;
        if (edicion) {
            id = Validar.aEntero(vistaVuelo.txtIdVuelo.getText());
            if (id == null) {
                Validar.aviso(vistaVuelo, "Seleccione un vuelo de la tabla antes de editar.");
                return false;
            }
        }
        String modelo = obtenerModeloAvion();
        java.util.Date fs = vistaVuelo.jDateSalida.getDate();

        String error = Validar.primero(
                modelo == null || modelo.trim().isEmpty() ? "Seleccione un avión." : null,
                vistaVuelo.cmbRuta.getSelectedItem() == null ? "Seleccione una ruta." : null,
                vistaVuelo.cmbEstado.getSelectedItem() == null ? "Seleccione un estado." : null,
                fs == null ? "Seleccione la fecha de salida." : null);

        java.time.LocalTime hs = null;
        java.time.LocalTime hl = null;
        if (error == null) {
            hs = Validar.aHora(vistaVuelo.txtSalida.getText());
            hl = Validar.aHora(vistaVuelo.txtLlegada.getText());
            if (hs == null) {
                error = "La hora de salida no es válida. Use el formato HH:mm (por ejemplo 08:30).";
            } else if (!calcularLlegada()) {
                error = "No se pudo calcular la llegada: revise la ruta y su duración.";
            }
        }
        if (error == null) {
            hl = Validar.aHora(vistaVuelo.txtLlegada.getText());
        }
        java.util.Date fl = vistaVuelo.jDateLlegada.getDate();

        if (error == null) {
            java.time.format.DateTimeFormatter horaFmt = java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss");
            java.time.format.DateTimeFormatter fechaHoraFmt = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            vistaVuelo.txtSalida.setText(hs.format(horaFmt));
            vistaVuelo.txtLlegada.setText(hl.format(horaFmt));

            java.time.LocalDate d1 = fs.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            java.time.LocalDate d2 = fl.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            java.time.LocalDateTime ini = d1.atTime(hs);
            java.time.LocalDateTime fin = d2.atTime(hl);
            String estado = vistaVuelo.cmbEstado.getSelectedItem().toString().trim();
            if (estado.equalsIgnoreCase("Finalizado") && !finalizadoPermitido(d1)) {
                error = "Solo puede marcar el vuelo como Finalizado desde su fecha de salida ("
                        + d1 + ").";
            }
            boolean cerrado = estado.equalsIgnoreCase("Cancelado") || estado.equalsIgnoreCase("Finalizado");

            if (error != null) {
                // ya hay un error (estado Finalizado no permitido)
            } else if (!fin.isAfter(ini)) {
                error = "La llegada debe ser posterior a la salida.";
            } else if (java.time.Duration.between(ini, fin).toHours() > 48) {
                error = "Un vuelo no puede durar más de 48 horas. Revise las fechas y horas.";
            } else if (!edicion && d1.isBefore(java.time.LocalDate.now())) {
                error = "La fecha de salida no puede ser anterior a hoy.";
            } else if (!cerrado && integridadDAO.vuelosSolapados(modelo.trim(), ini.format(fechaHoraFmt),
                    fin.format(fechaHoraFmt), id == null ? 0 : id) > 0) {
                error = "El avión «" + modelo + "» ya tiene otro vuelo programado en ese horario.\n"
                        + "Un avión no puede estar en dos vuelos a la vez.";
            }
        }

        if (error == null && edicion) {
            vuelo actual = daoVuelo.buscarPorId(id);
            if (actual != null) {
                boolean cambiaAvion = actual.getFk_avion() != null
                        && !actual.getFk_avion().trim().equalsIgnoreCase(modelo.trim());
                boolean cambiaRuta = actual.getFk_ruta() != obtenerIdRuta();
                if ((cambiaAvion || cambiaRuta) && integridadDAO.contar("tiquete", "fk_vuelo", id) > 0) {
                    error = "No se puede cambiar el avión ni la ruta: este vuelo ya tiene tiquetes vendidos.";
                } else if (cambiaAvion && integridadDAO.contar("asiento", "fk_vuelo", id) > 0) {
                    error = "No se puede cambiar el avión: el vuelo ya tiene asientos creados.\n"
                            + "Elimine el vuelo y créelo de nuevo con el otro avión.";
                }
            }
        }
        return !Validar.mostrar(vistaVuelo, error);
    }

    /** Comprueba que no existan registros que dependan de este vuelo antes de borrarlo; avisa y devuelve false si los hay. */
    private boolean puedeEliminarVuelo() {
        Integer id = Validar.aEntero(vistaVuelo.txtIdVuelo.getText());
        if (id == null) {
            return true; // el metodo original ya avisa que hay que seleccionar una fila
        }
        int tiquetes = integridadDAO.contar("tiquete", "fk_vuelo", id);
        if (tiquetes > 0) {
            Validar.aviso(vistaVuelo, "No se puede eliminar este vuelo porque tiene " + tiquetes
                    + " tiquete(s) vendido(s).\nElimine o cancele primero esas reservas.");
            return false;
        }
        return true;
    }
}
