/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */



package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

import modelo.DAO.asientoDAO;
import modelo.DAO.equipajeDAO;
import modelo.DAO.pasajeroGestionDAO;
import modelo.DAO.tiqueteDAO;
import modelo.DAO.vueloDAO;

import modelo.entidad.equipaje;

import vista.Inicio;
import vista.JpnlConfirmarReserva;
import vista.JpnlSeleccionarEquipaje;

/**
 * CONTROLADOR de la selección de equipaje (paso 4 de la reserva): calcula peso
 * y precio, guarda el equipaje y avanza a la confirmación.
 */
public class seleccionarEquipajeControlador
        implements ActionListener {

    private JpnlSeleccionarEquipaje vista;
    private equipajeDAO daoEquipaje;
    private Inicio inicio;

    // DATOS DE LOS PASOS ANTERIORES
    private int idVuelo;
    private String documentoPasajero;
    private int idAsiento;

    // DAOS PARA CONFIRMACIÓN
    private tiqueteDAO daoTiquete;
    private asientoDAO daoAsiento;
    private pasajeroGestionDAO daoPasajero;
    private vueloDAO daoVuelo;

    // ID DEL EQUIPAJE CREADO
    private int idEquipajeSeleccionado;

    /** Constructor: guarda la vista y los DAO y conecta los botones con sus acciones. */
    public seleccionarEquipajeControlador(
            JpnlSeleccionarEquipaje vista,
            equipajeDAO daoEquipaje,
            Inicio inicio,
            int idVuelo,
            String documentoPasajero,
            int idAsiento,
            tiqueteDAO daoTiquete,
            asientoDAO daoAsiento,
            pasajeroGestionDAO daoPasajero,
            vueloDAO daoVuelo) {

        this.vista = vista;
        this.daoEquipaje = daoEquipaje;
        this.inicio = inicio;

        this.idVuelo = idVuelo;
        this.documentoPasajero = documentoPasajero;
        this.idAsiento = idAsiento;

        this.daoTiquete = daoTiquete;
        this.daoAsiento = daoAsiento;
        this.daoPasajero = daoPasajero;
        this.daoVuelo = daoVuelo;

        // BOTÓN CONTINUAR
        vista.btnContinuar.addActionListener(this);

        // CUANDO CAMBIE EL TIPO DE EQUIPAJE
        vista.cmbTipoEquipaje.addActionListener(e -> {
            actualizarDatosEquipaje();
        });

        // CARGAR DATOS INICIALES
        actualizarDatosEquipaje();
    }

    /** Recibe los clics de los botones de la vista y llama a la acción que corresponde. */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vista.btnContinuar) {

            continuar();
        }
    }

    /** Al cambiar el tipo de equipaje actualiza el peso y el precio mostrados. */
    // =========================================================
    // ACTUALIZAR PESO Y PRECIO AUTOMÁTICAMENTE
    // =========================================================
    private void actualizarDatosEquipaje() {

        if (vista.cmbTipoEquipaje.getSelectedItem() == null) {
            return;
        }

        String tipo = vista.cmbTipoEquipaje
                .getSelectedItem()
                .toString();

        double peso = 0;
        double precio = 0;

        switch (tipo) {

            case "Sin equipaje":

                peso = 0;
                precio = 0;

                break;

            case "Equipaje de mano":

                peso = 0;
                precio = 0;

                break;

            case "Equipaje de bodega 10 kg":

                peso = 10;
                precio = 30000;

                break;

            case "Equipaje de bodega 20 kg":

                peso = 20;
                precio = 50000;

                break;

            case "Equipaje de bodega 30 kg":

                peso = 30;
                precio = 70000;

                break;

            default:

                peso = 0;
                precio = 0;

                break;
        }

        // MOSTRAR PESO
        vista.txtPeso.setText(
                String.valueOf((int) peso) + " kg"
        );

        // MOSTRAR PRECIO
        vista.txtPrecio.setText(
                "$"
                + String.format(
                        "%,.0f",
                        precio
                )
        );
    }

    /** Guarda el equipaje elegido y avanza a la confirmación de la reserva. */
    // =========================================================
    // CONTINUAR
    // =========================================================
    private void continuar() {

        if (vista.cmbTipoEquipaje.getSelectedItem() == null) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Seleccione un tipo de equipaje."
            );

            return;
        }

        String tipo = vista.cmbTipoEquipaje
                .getSelectedItem()
                .toString();

        // =========================================
        // OBTENER PESO Y PRECIO AUTOMÁTICAMENTE
        // =========================================
        double peso = obtenerPeso(tipo);
        double precio = obtenerPrecio(tipo);

        try {

            // =========================================
            // CREAR EQUIPAJE
            // =========================================
            equipaje nuevoEquipaje
                    = new equipaje();

            nuevoEquipaje.setPeso(peso);

            nuevoEquipaje.setEstado(
                    "Registrado"
            );

            nuevoEquipaje.setTipo(
                    tipo
            );

            // PRECIO AUTOMÁTICO
            nuevoEquipaje.setPrecio(
                    precio
            );

            // =========================================
            // GUARDAR Y OBTENER ID
            // =========================================
            idEquipajeSeleccionado
                    = daoEquipaje.guardarYObtenerId(
                            nuevoEquipaje
                    );

            if (idEquipajeSeleccionado == -1) {

                JOptionPane.showMessageDialog(
                        vista,
                        "No se pudo guardar el equipaje."
                );

                return;
            }

            // =========================================
            // CREAR PANEL DE CONFIRMACIÓN
            // =========================================
            JpnlConfirmarReserva panelConfirmacion
                    = new JpnlConfirmarReserva();

            confirmarReservaControlador controladorConfirmacion
                    = new confirmarReservaControlador(
                            panelConfirmacion,
                            inicio,
                            daoTiquete,
                            daoAsiento,
                            daoEquipaje,
                            daoPasajero,
                            daoVuelo,
                            idVuelo,
                            documentoPasajero,
                            idAsiento,
                            idEquipajeSeleccionado
                    );

            // =========================================
            // MOSTRAR CONFIRMACIÓN
            // =========================================
            inicio.mostrarPanel(
                    panelConfirmacion
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    vista,
                    "No se pudo registrar el equipaje."
            );

            System.out.println(
                    "Error al registrar equipaje: "
                    + ex
            );
        }
    }

    /** Devuelve el peso (kg) que corresponde al tipo de equipaje. */
    // =========================================================
    // OBTENER PESO
    // =========================================================
    private double obtenerPeso(String tipo) {

        switch (tipo) {

            case "Equipaje de bodega 10 kg":
                return 10;

            case "Equipaje de bodega 20 kg":
                return 20;

            case "Equipaje de bodega 30 kg":
                return 30;

            case "Equipaje de mano":
                return 0;

            case "Sin equipaje":
                return 0;

            default:
                return 0;
        }
    }

    /** Devuelve el precio que corresponde al tipo de equipaje. */
    // =========================================================
    // OBTENER PRECIO
    // =========================================================
    private double obtenerPrecio(String tipo) {

        switch (tipo) {

            case "Equipaje de bodega 10 kg":
                return 30000;

            case "Equipaje de bodega 20 kg":
                return 50000;

            case "Equipaje de bodega 30 kg":
                return 70000;

            case "Equipaje de mano":
                return 0;

            case "Sin equipaje":
                return 0;

            default:
                return 0;
        }
    }

    /** Devuelve el id del equipaje guardado para esta reserva. */
    // =========================================================
    // OBTENER ID DEL EQUIPAJE
    // =========================================================
    public int getIdEquipajeSeleccionado() {

        return idEquipajeSeleccionado;
    }
}