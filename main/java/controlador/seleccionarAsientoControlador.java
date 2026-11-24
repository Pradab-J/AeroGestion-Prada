/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import vista.Tema;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import modelo.DAO.asientoDAO;
import modelo.DAO.avion_asientosDAO;
import modelo.DAO.equipajeDAO;
import modelo.DAO.pasajeroGestionDAO;
import modelo.DAO.tiqueteDAO;
import modelo.DAO.vueloDAO;
import modelo.entidad.asiento;
import modelo.entidad.avion_asientos;
import modelo.entidad.vuelo;
import vista.Inicio;
import vista.JpnlSeleccionarAsiento;
import vista.MapaAvion;
import vista.JpnlSeleccionarEquipaje;

/**
 * CONTROLADOR de la selección de asiento (paso 3 de la reserva): dibuja el mapa
 * del avión, deja elegir un asiento disponible y avanza a escoger equipaje.
 */
public class seleccionarAsientoControlador
        implements ActionListener {

    private JpnlSeleccionarAsiento vista;
    private asientoDAO daoAsiento;
    private vueloDAO daoVuelo;
    private avion_asientosDAO daoConfiguracion;
    private int idVuelo;
    private asiento asientoSeleccionado;
    private Inicio inicio;
    private equipajeDAO daoEquipaje;
    private String documentoPasajero;
    private tiqueteDAO daoTiquete;
    private pasajeroGestionDAO daoPasajero;

    /** Constructor: guarda la vista y los DAO y conecta los botones con sus acciones. */
    public seleccionarAsientoControlador(
            JpnlSeleccionarAsiento vista,
            asientoDAO daoAsiento,
            vueloDAO daoVuelo,
            avion_asientosDAO daoConfiguracion,
            int idVuelo,
            Inicio inicio,
            equipajeDAO daoEquipaje,
            String documentoPasajero,
            tiqueteDAO daoTiquete,
            pasajeroGestionDAO daoPasajero) {
        this.vista = vista;
        this.daoAsiento = daoAsiento;
        this.daoVuelo = daoVuelo;
        this.daoConfiguracion = daoConfiguracion;
        this.idVuelo = idVuelo;
        this.inicio = inicio;
        this.daoEquipaje = daoEquipaje;
        this.documentoPasajero = documentoPasajero;
        this.daoTiquete = daoTiquete;
        this.daoPasajero = daoPasajero;
        vista.btnContinuar.addActionListener(
                e -> continuar()
        );
        cargarVuelo();
        Tema.alMostrar(vista, this::refrescarCabina);
    }

    /** Lee los datos del vuelo elegido y los muestra en el encabezado. */
    // ==============================
    // CARGAR VUELO
    // ==============================
    private void cargarVuelo() {
        vuelo v = daoVuelo.buscarPorId(idVuelo);
        if (v == null) {
            JOptionPane.showMessageDialog(
                    vista,
                    "No se encontró el vuelo seleccionado."
            );
            return;
        }
        vista.lblVuelo.setText(
                "Vuelo: " + v.getId_vuelo()
        );
        vista.lblAvion.setText(
                "Avión: " + v.getFk_avion()
        );
        /*
         * El avión se utiliza únicamente para
         * buscar la configuración de la cabina.
         *
         * Los asientos se buscarán por el ID
         * del vuelo.
         */
        cargarCabina(v.getFk_avion());
    }

    // ==============================
    // CARGAR CABINA
    // ==============================
    /** Asientos del vuelo, por "numero_CLASE" (se consultan UNA sola vez). */
    private final java.util.Map<String, asiento> asientosDelVuelo = new java.util.HashMap<>();
    private MapaAvion.Silla sillaSeleccionada;
    private final java.util.Map<String, MapaAvion.Silla> sillas = new java.util.HashMap<>();
    private String modeloActual;
    private boolean primeraVez = true;

    /** Dibuja el mapa del avión con los asientos del vuelo (solo los disponibles se pueden elegir). */
    private void cargarCabina(String modeloAvion) {
        modeloActual = modeloAvion;
        sillas.clear();
        sillaSeleccionada = null;
        asientoSeleccionado = null;
        asientosDelVuelo.clear();

        // 1) Todos los asientos del vuelo en una sola consulta.
        //    La clave incluye la CLASE: Business y Economica repiten "1A", "1B"...
        for (asiento a : daoAsiento.listarPorVuelo(idVuelo)) {
            if (a.getNum_asiento() != null) {
                asientosDelVuelo.put(MapaAvion.clave(a.getNum_asiento(), a.getClase()), a);
            }
        }

        // 2) Clases configuradas para este avion (acepta Business/Bussines, Economica/Económica)
        java.util.List<MapaAvion.Seccion> secciones = new ArrayList<>();
        if (modeloAvion != null) {
            for (avion_asientos config : daoConfiguracion.listar()) {
                if (config.getFk_avion() == null || config.getNombre_clase() == null
                        || config.getDistribucion() == null) {
                    continue;
                }
                if (!config.getFk_avion().trim().equalsIgnoreCase(modeloAvion.trim())) {
                    continue;
                }
                secciones.add(new MapaAvion.Seccion(
                        config.getNombre_clase(), config.getFilas(), config.getDistribucion()));
            }
        }

        if (asientosDelVuelo.isEmpty()) {
            MapaAvion.mensaje(vista.pnlCabina,
                    "Este vuelo todavía no tiene asientos habilitados. Pídale al administrador que los guarde.");
            return;
        }

        // 3) Estado visual de cada asiento
        MapaAvion.Origen origen = (clase, numero) -> {
            asiento a = asientosDelVuelo.get(MapaAvion.clave(numero, clase));
            if (a == null) {
                return null; // no existe para este vuelo
            }
            String est = MapaAvion.estado(a.getEstado(), MapaAvion.MANTENIMIENTO);
            if (est.equals(MapaAvion.DISPONIBLE) || est.equals(MapaAvion.OCUPADO)) {
                return est;
            }
            return MapaAvion.MANTENIMIENTO; // dañado o en mantenimiento = "no disponible"
        };
        MapaAvion.Clic clic = silla -> {
            asiento a = asientosDelVuelo.get(MapaAvion.clave(silla.getNumero(), silla.getClaveClase()));
            if (a != null) {
                seleccionarAsiento(a, silla);
            }
        };
        javax.swing.JPanel avion = MapaAvion.construir(secciones, origen, true, clic, sillas);
        MapaAvion.instalar(vista.pnlCabina, avion, false);
    }

    /** Marca el asiento elegido (y desmarca el anterior) y muestra su número, clase y precio. */
    // ==============================
    // SELECCIONAR ASIENTO
    // ==============================
    private void seleccionarAsiento(asiento a, MapaAvion.Silla silla) {
        if (sillaSeleccionada != null && sillaSeleccionada != silla) {
            sillaSeleccionada.setEstado(MapaAvion.DISPONIBLE);
        }
        sillaSeleccionada = silla;
        silla.setEstado(MapaAvion.SELECCIONADO);
        asientoSeleccionado = a;

        String precio = String.format("%,.0f", a.getPrecio());
        vista.lblAsientoSeleccionado.setText(
                "Asiento seleccionado: " + a.getNum_asiento() + " - " + a.getClase() + " - $" + precio);
    }

    /** Comprueba que haya un asiento elegido y avanza a la selección de equipaje. */
    // ==============================
    // CONTINUAR
    // ==============================
    private void continuar() {
        if (asientoSeleccionado == null) {
            JOptionPane.showMessageDialog(
                    vista,
                    "Seleccione un asiento antes de continuar."
            );
            return;
        }
        JpnlSeleccionarEquipaje panelEquipaje
                = new JpnlSeleccionarEquipaje();
        seleccionarEquipajeControlador controladorEquipaje
                = new seleccionarEquipajeControlador(
                        panelEquipaje,
                        daoEquipaje,
                        inicio,
                        idVuelo,
                        documentoPasajero,
                        asientoSeleccionado.getId_asiento(),
                        daoTiquete,
                        daoAsiento,
                        daoPasajero,
                        daoVuelo
                );
        inicio.mostrarPanel(
                panelEquipaje
        );
    }

    /** Recibe los clics de los botones de la vista y llama a la acción que corresponde. */
    // ==============================
    // ACTION LISTENER
    // ==============================
    @Override
    public void actionPerformed(
            ActionEvent e) {
        if (e.getSource()
                == vista.btnContinuar) {
            continuar();
        }
    }

    /**
     * Al volver a esta pantalla se vuelve a leer el estado real de los asientos
     * (otro pasajero pudo haber reservado alguno) y se conserva la eleccion si sigue libre.
     */
    private void refrescarCabina() {
        if (primeraVez) {
            primeraVez = false; // la primera vez ya se dibujo en el constructor
            return;
        }
        asiento previo = asientoSeleccionado;
        cargarCabina(modeloActual);
        vista.lblAsientoSeleccionado.setText("Asiento seleccionado: ninguno");
        if (previo != null) {
            String clave = MapaAvion.clave(previo.getNum_asiento(), previo.getClase());
            asiento nuevo = asientosDelVuelo.get(clave);
            MapaAvion.Silla silla = sillas.get(clave);
            if (nuevo != null && silla != null && silla.isEnabled()) {
                seleccionarAsiento(nuevo, silla);
            }
        }
    }
}
