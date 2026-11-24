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

import modelo.entidad.asiento;
import modelo.entidad.equipaje;
import modelo.entidad.pasajero;
import modelo.entidad.tiquete;
import modelo.entidad.vuelo;

import vista.Inicio;
import vista.JpnlConfirmarReserva;
import vista.JpnlTiqueteVuelo;
import modelo.DAO.aeropuertoDAO;
import modelo.DAO.rutaDAO;
import modelo.entidad.aeropuerto;
import modelo.entidad.ruta;

/**
 * CONTROLADOR de la pantalla final de la reserva: muestra el resumen, valida
 * que el pasajero no tenga ya reserva en el vuelo, ocupa el asiento, guarda el
 * tiquete y muestra el tiquete de vuelo.
 */
public class confirmarReservaControlador implements ActionListener {

    private JpnlConfirmarReserva vista;
    private Inicio inicio;

    private tiqueteDAO daoTiquete;
    private asientoDAO daoAsiento;
    private equipajeDAO daoEquipaje;
    private pasajeroGestionDAO daoPasajero;
    private vueloDAO daoVuelo;

    private int idVuelo;
    private String documentoPasajero;
    private int idAsiento;
    private int idEquipaje;

    /** Constructor: guarda la vista y los DAO y conecta los botones con sus acciones. */
    public confirmarReservaControlador(
            JpnlConfirmarReserva vista,
            Inicio inicio,
            tiqueteDAO daoTiquete,
            asientoDAO daoAsiento,
            equipajeDAO daoEquipaje,
            pasajeroGestionDAO daoPasajero,
            vueloDAO daoVuelo,
            int idVuelo,
            String documentoPasajero,
            int idAsiento,
            int idEquipaje) {

        this.vista = vista;
        this.inicio = inicio;
        this.daoTiquete = daoTiquete;
        this.daoAsiento = daoAsiento;
        this.daoEquipaje = daoEquipaje;
        this.daoPasajero = daoPasajero;
        this.daoVuelo = daoVuelo;

        this.idVuelo = idVuelo;
        this.documentoPasajero = documentoPasajero;
        this.idAsiento = idAsiento;
        this.idEquipaje = idEquipaje;

        vista.btnConfirmar.addActionListener(this);

        cargarInformacion();
    }

    /** Recibe los clics de los botones de la vista y llama a la acción que corresponde. */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vista.btnConfirmar) {
            confirmarReserva();
        }
    }

    // =========================================================
    // CARGAR INFORMACIÓN
    // =========================================================

    /** Muestra el resumen (vuelo, pasajero, asiento, equipaje y precios) antes de confirmar. */
    private void cargarInformacion() {

        // =====================================================
        // VUELO
        // =====================================================

        vuelo v = daoVuelo.buscarPorId(idVuelo);

        if (v != null) {

            vista.lblVuelo.setText(
                    "Vuelo: " + v.getId_vuelo()
            );

            vista.lblAvion.setText(
                    "Avión: " + v.getFk_avion()
            );
        }

        // =====================================================
        // PASAJERO
        // =====================================================

        String nombrePasajero = documentoPasajero;

        for (pasajero p : daoPasajero.listar()) {

            if (p.getDocumento().equals(documentoPasajero)) {

                nombrePasajero =
                        p.getDocumento()
                        + " - "
                        + p.getNombre()
                        + " "
                        + p.getApellido();

                break;
            }
        }

        vista.lblPasajero.setText(
                "Pasajero: " + nombrePasajero
        );

        // =====================================================
        // ASIENTO
        // =====================================================

        double precioAsiento = 0;

        for (asiento a : daoAsiento.listar()) {

            if (a.getId_asiento() == idAsiento) {

                precioAsiento = a.getPrecio();

                vista.lblAsiento.setText(
                        "Asiento: "
                        + a.getNum_asiento()
                        + " - "
                        + a.getClase()
                );

                break;
            }
        }

        // Mostrar precio del asiento

        vista.lblPrecioAsiento.setText(
                "Precio asiento: $"
                + formatearPrecio(precioAsiento)
        );

        // =====================================================
        // EQUIPAJE
        // =====================================================

        double precioEquipaje = 0;

        for (equipaje e : daoEquipaje.listar()) {

            if (e.getId_equipaje() == idEquipaje) {

                precioEquipaje = e.getPrecio();

                vista.lblEquipaje.setText(
                        "Equipaje: " + e.getTipo()
                );

                vista.lblPeso.setText(
                        "Peso: " + e.getPeso() + " kg"
                );

                break;
            }
        }

        // Mostrar precio del equipaje

        vista.lblPrecioEquipaje.setText(
                "Precio equipaje: $"
                + formatearPrecio(precioEquipaje)
        );

        // =====================================================
        // TOTAL
        // =====================================================

        double total = precioAsiento + precioEquipaje;

        vista.lblTotal.setText(
                "TOTAL: $" + formatearPrecio(total)
        );
    }

    // =========================================================
    // CONFIRMAR RESERVA
    // =========================================================

    /**
     * Crea el tiquete: valida método de pago y que el pasajero no tenga ya una reserva
     * en el vuelo, ocupa el asiento de forma atómica, guarda y muestra el tiquete.
     */
    private void confirmarReserva() {

        if (vista.cmbMetodoPago.getSelectedItem() == null) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Seleccione un método de pago."
            );

            return;
        }

        String metodoPago =
                vista.cmbMetodoPago
                        .getSelectedItem()
                        .toString();

        // =====================================================
        // OBTENER PRECIOS
        // =====================================================

        double precioAsiento = obtenerPrecioAsiento();

        double precioEquipaje = obtenerPrecioEquipaje();

        double total =
                precioAsiento
                + precioEquipaje;

        // =====================================================
        // CREAR TIQUETE
        // =====================================================

        tiquete t = new tiquete();

        java.text.SimpleDateFormat formato =
                new java.text.SimpleDateFormat(
                        "yyyy-MM-dd"
                );

        String fecha =
                formato.format(
                        new java.util.Date()
                );

        t.setFecha_emision(fecha);

        // Número/referencia de reserva

        String reserva =
                "RES-" + System.currentTimeMillis();

        t.setReserva(reserva);

        t.setMetodo_pago(metodoPago);

        t.setEquipaje(idEquipaje);

        t.setPasajero(documentoPasajero);

        t.setAsiento(idAsiento);

        t.setVuelo(idVuelo);

        // =====================================================
        // GUARDAR PRECIOS
        // =====================================================

        t.setPrecio_asiento(precioAsiento);

        t.setPrecio_equipaje(precioEquipaje);

        t.setTotal(total);

        // =====================================================
        // GUARDAR TIQUETE
        // =====================================================

        // Un pasajero solo puede tener una reserva vigente por vuelo
        if (modelo.DAO.integridadDAO.pasajeroYaReservoVuelo(documentoPasajero, idVuelo)) {
            JOptionPane.showMessageDialog(
                    vista,
                    "El pasajero con documento " + documentoPasajero
                    + " ya tiene una reserva en este vuelo.\n"
                    + "Solo se permite una reserva por pasajero en cada vuelo.",
                    "Reserva duplicada",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        vista.btnConfirmar.setEnabled(false); // evita reservar dos veces con doble clic

        // El asiento se ocupa de forma atomica: si otra persona lo tomo antes, no se vende dos veces
        if (!daoAsiento.reservarSiDisponible(idAsiento)) {
            JOptionPane.showMessageDialog(
                    vista,
                    "Lo sentimos, ese asiento acaba de ser reservado por otro pasajero.\n"
                    + "Por favor elija otro asiento.",
                    "Asiento no disponible",
                    JOptionPane.WARNING_MESSAGE
            );
            inicio.volver(); // vuelve a Equipaje
            inicio.volver(); // vuelve a Asiento
            return;
        }

        if (daoTiquete.guardar(t)) {

            // En vez del cuadro de mensaje, se muestra el tiquete de vuelo
            JpnlTiqueteVuelo panelTiquete = new JpnlTiqueteVuelo(
                    construirDatosTiquete(reserva, metodoPago, fecha,
                            precioAsiento, precioEquipaje, total));
            panelTiquete.btnInicio.addActionListener(ev -> inicio.mostrarInicio());
            inicio.mostrarFinal(panelTiquete);
        } else {
            daoAsiento.liberar(idAsiento); // el asiento no queda bloqueado si fallo el guardado
            vista.btnConfirmar.setEnabled(true);
            JOptionPane.showMessageDialog(
                    vista,
                    "No se pudo realizar la reserva."
            );
        }
    }

    /** Reúne todos los datos que se dibujan en el tiquete de vuelo. */
    // =========================================================
    // DATOS PARA EL TIQUETE DE VUELO
    // =========================================================
    private JpnlTiqueteVuelo.Datos construirDatosTiquete(String reserva, String metodoPago,
            String fecha, double precioAsiento, double precioEquipaje, double total) {
        JpnlTiqueteVuelo.Datos d = new JpnlTiqueteVuelo.Datos();
        d.reserva = reserva;
        d.metodoPago = metodoPago;
        d.fechaEmision = fecha;
        d.precioAsiento = precioAsiento;
        d.precioEquipaje = precioEquipaje;
        d.total = total;

        // Pasajero
        d.documento = documentoPasajero;
        d.pasajero = documentoPasajero;
        for (pasajero p : daoPasajero.listar()) {
            if (p.getDocumento().equals(documentoPasajero)) {
                d.pasajero = p.getNombre() + " " + p.getApellido();
                break;
            }
        }

        // Asiento
        for (asiento a : daoAsiento.listar()) {
            if (a.getId_asiento() == idAsiento) {
                d.asiento = a.getNum_asiento();
                d.clase = a.getClase();
                break;
            }
        }

        // Equipaje
        for (equipaje e : daoEquipaje.listar()) {
            if (e.getId_equipaje() == idEquipaje) {
                d.equipaje = e.getTipo();
                d.peso = e.getPeso() + " kg";
                break;
            }
        }

        // Vuelo y ruta
        vuelo v = daoVuelo.buscarPorId(idVuelo);
        if (v != null) {
            d.vuelo = "N° " + v.getId_vuelo();
            d.avion = v.getFk_avion();
            d.fechaSalida = v.getFecha_salida();
            d.horaSalida = v.getHora_salida();
            d.fechaLlegada = v.getFecha_llegada();
            d.horaLlegada = v.getHora_llegada();
            try {
                ruta r = null;
                for (ruta x : new rutaDAO().listar()) {
                    if (x.getId_ruta() == v.getFk_ruta()) {
                        r = x;
                        break;
                    }
                }
                if (r != null) {
                    java.util.ArrayList<aeropuerto> aeropuertos = new aeropuertoDAO().listar();
                    String[] o = datosAeropuerto(r.getOrigen(), aeropuertos);
                    String[] de = datosAeropuerto(r.getDestino(), aeropuertos);
                    d.origenCiudad = o[0];
                    d.origenCodigo = o[1];
                    d.origenAeropuerto = o[2];
                    d.destinoCiudad = de[0];
                    d.destinoCodigo = de[1];
                    d.destinoAeropuerto = de[2];
                }
            } catch (Exception ex) {
                // si no se puede leer la ruta, el tiquete se muestra igual con lo que haya
            }
        }
        return d;
    }

    /** La ruta guarda "Ciudad - Nombre del aeropuerto". Devuelve {ciudad, codigo IATA, aeropuerto}. */
    private String[] datosAeropuerto(String textoRuta, java.util.ArrayList<aeropuerto> aeropuertos) {
        String texto = textoRuta == null ? "" : textoRuta.trim();
        for (aeropuerto a : aeropuertos) {
            String completo = (a.getCiudad() + " - " + a.getNombre()).trim();
            if (completo.equalsIgnoreCase(texto)) {
                String codigo = a.getCodigo_iata() == null || a.getCodigo_iata().trim().isEmpty()
                        ? JpnlTiqueteVuelo.codigoDesdeCiudad(a.getCiudad())
                        : a.getCodigo_iata().trim().toUpperCase();
                return new String[]{a.getCiudad(), codigo, a.getNombre()};
            }
        }
        // la ruta guarda solo la ciudad: se busca el aeropuerto de esa ciudad
        for (aeropuerto a : aeropuertos) {
            if (a.getCiudad() != null && a.getCiudad().trim().equalsIgnoreCase(texto)) {
                String codigo = a.getCodigo_iata() == null || a.getCodigo_iata().trim().isEmpty()
                        ? JpnlTiqueteVuelo.codigoDesdeCiudad(a.getCiudad())
                        : a.getCodigo_iata().trim().toUpperCase();
                return new String[]{a.getCiudad(), codigo, a.getNombre()};
            }
        }
        // no se encontro el aeropuerto: se separa el texto como se pueda
        String ciudad = texto, nombre = "";
        int i = texto.indexOf(" - ");
        if (i > 0) {
            ciudad = texto.substring(0, i);
            nombre = texto.substring(i + 3);
        }
        return new String[]{ciudad, JpnlTiqueteVuelo.codigoDesdeCiudad(ciudad), nombre};
    }

    // =========================================================
    // OBTENER PRECIO ASIENTO
    // =========================================================

    /** Devuelve el precio del asiento elegido. */
    private double obtenerPrecioAsiento() {

        for (asiento a : daoAsiento.listar()) {

            if (a.getId_asiento() == idAsiento) {

                return a.getPrecio();
            }
        }

        return 0;
    }

    // =========================================================
    // OBTENER PRECIO EQUIPAJE
    // =========================================================

    /** Devuelve el precio del equipaje elegido. */
    private double obtenerPrecioEquipaje() {

        for (equipaje e : daoEquipaje.listar()) {

            if (e.getId_equipaje() == idEquipaje) {

                return e.getPrecio();
            }
        }

        return 0;
    }

    // =========================================================
    // CAMBIAR ASIENTO A OCUPADO
    // =========================================================

    /** Cambia el estado del asiento elegido (por ejemplo a Ocupado). */
    private void cambiarEstadoAsiento() {

        for (asiento a : daoAsiento.listar()) {

            if (a.getId_asiento() == idAsiento) {

                a.setEstado("Ocupado");

                daoAsiento.editar(a);

                break;
            }
        }
    }

    // =========================================================
    // FORMATEAR PRECIO
    // =========================================================

    /** Da formato de dinero a un número (separador de miles). */
    private String formatearPrecio(double precio) {

        return String.format(
                "%,.0f",
                precio
        );
    }
}