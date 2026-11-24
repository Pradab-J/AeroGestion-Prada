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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JOptionPane;
import modelo.DAO.asientoDAO;
import modelo.DAO.avionDAO;
import modelo.DAO.avion_asientosDAO;
import modelo.DAO.vueloDAO;
import modelo.entidad.asiento;
import modelo.entidad.avion;
import modelo.entidad.avion_asientos;
import modelo.entidad.vuelo;
import vista.JpnlAsientos;
import vista.MapaAvion;
import modelo.DAO.tiqueteDAO;
import modelo.DAO.pasajeroGestionDAO;
import modelo.entidad.tiquete;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * CONTROLADOR del panel administrativo de Asientos: dibuja el mapa del avión de
 * un vuelo, permite cambiar el estado de cada asiento, consultar quién lo ocupa y
 * guardar los cambios.
 */
public class asientoControlador {

    private JpnlAsientos vista;
    private asientoDAO daoAsiento;
    private avionDAO daoAvion;
    private avion_asientosDAO daoConfiguracion;
    private vueloDAO daoVuelo;
    private boolean cargando = false;
    private tiqueteDAO daoTiquete = new tiqueteDAO();
    private pasajeroGestionDAO daoPasajero = new pasajeroGestionDAO();

    /**
     * Todos los asientos del vuelo mostrado, por "numero_CLASE".
     */
    private final Map<String, asiento> mapaAsientos = new HashMap<>();
    /**
     * Claves de los asientos nuevos o modificados que faltan por guardar.
     */
    private final Set<String> pendientes = new HashSet<>();
    /**
     * Botones dibujados, por la misma clave.
     */
    private final Map<String, MapaAvion.Silla> sillas = new HashMap<>();

    /** Constructor: guarda la vista y los DAO y conecta los botones con sus acciones. */
    public asientoControlador(
            JpnlAsientos vista,
            asientoDAO daoAsiento,
            avionDAO daoAvion,
            avion_asientosDAO daoConfiguracion,
            vueloDAO daoVuelo) {
        this.vista = vista;
        this.daoAsiento = daoAsiento;
        this.daoAvion = daoAvion;
        this.daoConfiguracion = daoConfiguracion;
        this.daoVuelo = daoVuelo;

        cargando = true;
        cargarAviones();
        cargando = false;

        vista.cmbAvion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!cargando) {
                    cargarVuelosDelAvion();
                }
            }
        });
        vista.cmbVuelo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!cargando) {
                    cargarCabina();
                }
            }
        });
        vista.btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarCambios();
            }
        });
        vista.btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiar();
            }
        });

        // Antes el primer avion de la lista no cargaba sus vuelos hasta cambiar de avion y volver.
        cargarVuelosDelAvion();
        Tema.alMostrar(vista, this::refrescarListas);
    }

    /** Devuelve el precio que corresponde a una clase de asiento. */
    // =========================================================
    // PRECIO AUTOMATICO DEL ASIENTO
    // =========================================================
    private double obtenerPrecioAsiento(String clase) {
        String k = MapaAvion.claveClase(clase);
        if (k.equals("BUSINESS")) {
            return 150000;
        }
        if (k.equals("ECONOMICA")) {
            return 80000;
        }
        return 0;
    }

    /** Llena el combo de aviones con los registrados en la base de datos. */
    // =========================================================
    // CARGAR TODOS LOS AVIONES
    // =========================================================
    private void cargarAviones() {
        vista.cmbAvion.removeAllItems();
        ArrayList<avion> lista = daoAvion.listar();
        for (avion a : lista) {
            vista.cmbAvion.addItem(a.getModelo());
        }
    }

    /** Llena el combo con los vuelos del avión elegido. */
    // =========================================================
    // CARGAR VUELOS DEL AVION
    // =========================================================
    private void cargarVuelosDelAvion() {
        cargando = true;
        vista.cmbVuelo.removeAllItems();
        mapaAsientos.clear();
        pendientes.clear();
        sillas.clear();

        if (vista.cmbAvion.getSelectedItem() == null) {
            cargando = false;
            MapaAvion.mensaje(vista.pnlCabina, "No hay aviones registrados.");
            return;
        }
        String modeloAvion = vista.cmbAvion.getSelectedItem().toString().trim();
        ArrayList<vuelo> lista = daoVuelo.listar();
        java.util.Map<Integer, String> rutas = VueloTexto.rutas();
        for (vuelo v : lista) {
            if (v.getFk_avion() != null && v.getFk_avion().trim().equalsIgnoreCase(modeloAvion)) {
                // Se muestra "id - Origen a Destino - fecha hora" para reconocer el vuelo fácilmente
                vista.cmbVuelo.addItem(VueloTexto.texto(v, rutas));
            }
        }
        cargando = false;

        if (vista.cmbVuelo.getItemCount() > 0) {
            vista.cmbVuelo.setSelectedIndex(0);
            cargarCabina();
        } else {
            MapaAvion.mensaje(vista.pnlCabina, "Este avión todavía no tiene vuelos registrados.");
        }
    }

    /** Dibuja el mapa de asientos del vuelo seleccionado, con el estado de cada silla. */
    // =========================================================
    // CARGAR CABINA
    // =========================================================
    private void cargarCabina() {
        mapaAsientos.clear();
        pendientes.clear();
        sillas.clear();

        if (vista.cmbVuelo.getSelectedItem() == null) {
            MapaAvion.mensaje(vista.pnlCabina, "Seleccione un vuelo para ver sus asientos.");
            return;
        }
        int idVuelo;
        try {
            idVuelo = VueloTexto.id(vista.cmbVuelo.getSelectedItem());
        } catch (Exception e) {
            return;
        }
        vuelo v = daoVuelo.buscarPorId(idVuelo);
        if (v == null) {
            JOptionPane.showMessageDialog(vista, "No se encontró el vuelo.");
            return;
        }
        String modeloAvion = v.getFk_avion();

        // Asientos que ya existen en la base de datos
        for (asiento a : daoAsiento.listarPorVuelo(idVuelo)) {
            if (a.getNum_asiento() != null) {
                mapaAsientos.put(MapaAvion.clave(a.getNum_asiento(), a.getClase()), a);
            }
        }

        // Clases configuradas para el avion (acepta Business/Bussines, Economica/Económica)
        List<MapaAvion.Seccion> secciones = new ArrayList<>();
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
        secciones = MapaAvion.ordenar(secciones);

        // Los asientos que faltan se crean en memoria como "Disponible" para que
        // el boton Guardar los cree TODOS (antes solo se guardaban los que se tocaban
        // y el pasajero veia el resto como inexistentes).
        for (MapaAvion.Seccion s : secciones) {
            for (String numero : MapaAvion.numeros(s)) {
                String clave = MapaAvion.clave(numero, s.clave);
                if (!mapaAsientos.containsKey(clave)) {
                    asiento nuevo = new asiento();
                    nuevo.setNum_asiento(numero);
                    nuevo.setClase(MapaAvion.clasePorBD(s.clave));
                    nuevo.setEstado("Disponible");
                    nuevo.setFk_vuelo(idVuelo);
                    nuevo.setPrecio(obtenerPrecioAsiento(s.clave));
                    mapaAsientos.put(clave, nuevo);
                    pendientes.add(clave);
                }
            }
        }

        MapaAvion.Origen origen = (clase, numero) -> {
            asiento a = mapaAsientos.get(MapaAvion.clave(numero, clase));
            return a == null ? MapaAvion.DISPONIBLE : MapaAvion.estado(a.getEstado(), MapaAvion.DISPONIBLE);
        };
        javax.swing.JPanel avion = MapaAvion.construir(secciones, origen, false, silla -> aplicarEstado(silla), sillas);
        MapaAvion.instalar(vista.pnlCabina, avion, true);
    }

    /**
     * Clic en un asiento (administrador): si está ocupado muestra quién lo tiene;
     * si no, le aplica el estado elegido en el combo y lo deja pendiente de guardar.
     */
    // =========================================================
    // CLIC EN UN ASIENTO: aplica el estado elegido en el combo
    // =========================================================
    // CLIC EN UN ASIENTO DESDE EL ADMINISTRADOR
    private void aplicarEstado(MapaAvion.Silla silla) {

        String clave = MapaAvion.clave(
                silla.getNumero(),
                silla.getClaveClase()
        );

        asiento a = mapaAsientos.get(clave);

        if (a == null) {
            return;
        }

        // Si ya está ocupado, consultar al pasajero.
        if (MapaAvion.estado(a.getEstado(), MapaAvion.DISPONIBLE)
                .equals(MapaAvion.OCUPADO)) {

            consultarPasajero(a);
            return;
        }

        // Si no está ocupado, se conserva el cambio de estado actual.
        if (vista.cmbEstado.getSelectedItem() == null) {
            return;
        }

        String nuevoEstado = vista.cmbEstado.getSelectedItem().toString();

        a.setEstado(nuevoEstado);
        a.setPrecio(obtenerPrecioAsiento(a.getClase()));

        pendientes.add(clave);

        silla.setEstado(
                MapaAvion.estado(nuevoEstado, MapaAvion.DISPONIBLE)
        );
    }

    /** Muestra los datos del pasajero que ocupa un asiento (según su tiquete). */
    // CONSULTAR LOS DATOS DEL PASAJERO ASOCIADO
    private void consultarPasajero(asiento a) {

        tiquete t = daoTiquete.buscarPorAsientoYVuelo(
                a.getId_asiento(),
                a.getFk_vuelo()
        );

        if (t == null) {
            JOptionPane.showMessageDialog(
                    vista,
                    "Este asiento está ocupado, pero no se encontró "
                    + "un tiquete asociado al vuelo.",
                    "Información del asiento",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        modelo.entidad.pasajero p
                = daoPasajero.buscarPorDocumento(t.getPasajero());

        if (p == null) {
            JOptionPane.showMessageDialog(
                    vista,
                    "No se encontró el pasajero asociado al tiquete #"
                    + t.getId_tiquete(),
                    "Información del asiento",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Color azulOscuro = new Color(23, 42, 58);
        Color azul = new Color(29, 140, 248);
        Color fondo = new Color(244, 247, 250);
        Color gris = new Color(90, 103, 115);
        Color blanco = Color.WHITE;

        JPanel principal = new JPanel(new BorderLayout(0, 14));
        principal.setBackground(fondo);
        principal.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        principal.setPreferredSize(new Dimension(470, 520));

        // CABECERA
        JPanel cabecera = new JPanel();
        cabecera.setLayout(new BoxLayout(cabecera, BoxLayout.Y_AXIS));
        cabecera.setBackground(azulOscuro);
        cabecera.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        JLabel titulo = new JLabel("AEROGESTION PRADA");
        titulo.setForeground(blanco);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 19));
        titulo.setAlignmentX(JLabel.LEFT_ALIGNMENT);

        JLabel subtitulo = new JLabel("INFORMACIÓN DEL PASAJERO");
        subtitulo.setForeground(new Color(190, 210, 225));
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitulo.setAlignmentX(JLabel.LEFT_ALIGNMENT);

        cabecera.add(titulo);
        cabecera.add(Box.createVerticalStrut(6));
        cabecera.add(subtitulo);

        // CONTENIDO
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setOpaque(false);
        contenido.setBorder(BorderFactory.createEmptyBorder(0, 18, 12, 18));

        // TARJETA DEL ASIENTO
        JPanel tarjetaAsiento = new JPanel(new BorderLayout(12, 0));
        tarjetaAsiento.setBackground(azulOscuro);
        tarjetaAsiento.setBorder(
                BorderFactory.createEmptyBorder(15, 18, 15, 18)
        );
        tarjetaAsiento.setMaximumSize(new Dimension(Integer.MAX_VALUE, 85));

        JPanel infoAsiento = new JPanel();
        infoAsiento.setOpaque(false);
        infoAsiento.setLayout(new BoxLayout(infoAsiento, BoxLayout.Y_AXIS));

        JLabel asientoTitulo = new JLabel("ASIENTO " + a.getNum_asiento());
        asientoTitulo.setForeground(blanco);
        asientoTitulo.setFont(new Font("Segoe UI", Font.BOLD, 21));

        JLabel clase = new JLabel("Clase: " + a.getClase());
        clase.setForeground(new Color(210, 225, 235));
        clase.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        infoAsiento.add(asientoTitulo);
        infoAsiento.add(Box.createVerticalStrut(5));
        infoAsiento.add(clase);

        JLabel estado = new JLabel("OCUPADO");
        estado.setForeground(blanco);
        estado.setBackground(new Color(30, 145, 95));
        estado.setOpaque(true);
        estado.setFont(new Font("Segoe UI", Font.BOLD, 11));
        estado.setBorder(BorderFactory.createEmptyBorder(6, 9, 6, 9));

        tarjetaAsiento.add(infoAsiento, BorderLayout.CENTER);
        tarjetaAsiento.add(estado, BorderLayout.EAST);

        contenido.add(tarjetaAsiento);
        contenido.add(Box.createVerticalStrut(12));

        // DATOS DEL PASAJERO
        JPanel tarjetaPasajero = crearTarjeta("DATOS DEL PASAJERO", azulOscuro);

        agregarDato(tarjetaPasajero, "Nombre completo",
                p.getNombre() + " " + p.getApellido(), gris);

        agregarDato(tarjetaPasajero, "Tipo de documento",
                p.getTipoDocumento(), gris);

        agregarDato(tarjetaPasajero, "Número de documento",
                p.getDocumento(), gris);

        agregarDato(tarjetaPasajero, "Teléfono",
                p.getTelefono(), gris);

        agregarDato(tarjetaPasajero, "Correo electrónico",
                p.getCorreo(), gris);

        contenido.add(tarjetaPasajero);
        contenido.add(Box.createVerticalStrut(12));

        // DATOS DEL TIQUETE
        JPanel tarjetaTiquete = crearTarjeta("DATOS DEL TIQUETE", azulOscuro);

        agregarDato(tarjetaTiquete, "Número de tiquete",
                String.valueOf(t.getId_tiquete()), gris);

        agregarDato(tarjetaTiquete, "Vuelo",
                String.valueOf(t.getVuelo()), gris);

        agregarDato(tarjetaTiquete, "Fecha de emisión",
                t.getFecha_emision(), gris);

        agregarDato(tarjetaTiquete, "Método de pago",
                t.getMetodo_pago(), gris);

        // TOTAL
        JPanel total = new JPanel(new BorderLayout());
        total.setBackground(blanco);
        total.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 228, 235)),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)
        ));
        total.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel totalTexto = new JLabel("TOTAL DEL TIQUETE");
        totalTexto.setFont(new Font("Segoe UI", Font.BOLD, 12));
        totalTexto.setForeground(gris);

        JLabel totalValor = new JLabel(
                "$" + String.format("%,.0f", t.getTotal())
        );
        totalValor.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalValor.setForeground(azul);
        totalValor.setHorizontalAlignment(SwingConstants.RIGHT);

        total.add(totalTexto, BorderLayout.WEST);
        total.add(totalValor, BorderLayout.EAST);

        contenido.add(tarjetaTiquete);
        contenido.add(Box.createVerticalStrut(12));
        contenido.add(total);

        // PIE
        JPanel cuerpo = new JPanel(new BorderLayout());
        cuerpo.setBackground(fondo);
        cuerpo.add(contenido, BorderLayout.NORTH);

        principal.add(cabecera, BorderLayout.NORTH);
        principal.add(cuerpo, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(
                vista,
                principal,
                "Detalle del asiento",
                JOptionPane.PLAIN_MESSAGE
        );
    }

    /** Crea un panel tipo tarjeta con título para mostrar datos. */
// CREA UNA TARJETA PARA AGRUPAR INFORMACIÓN
    private JPanel crearTarjeta(String titulo, Color azulOscuro) {

        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 228, 235)),
                BorderFactory.createEmptyBorder(10, 13, 10, 13)
        ));
        tarjeta.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        JLabel encabezado = new JLabel(titulo);
        encabezado.setForeground(azulOscuro);
        encabezado.setFont(new Font("Segoe UI", Font.BOLD, 12));
        encabezado.setBorder(
                BorderFactory.createEmptyBorder(0, 0, 8, 0)
        );
        encabezado.setAlignmentX(JLabel.LEFT_ALIGNMENT);

        tarjeta.add(encabezado);

        return tarjeta;
    }

    /** Agrega una línea "nombre: valor" a una tarjeta de datos. */
// AGREGA CADA CAMPO COMO UNA FILA
    private void agregarDato(
            JPanel tarjeta,
            String nombre,
            String valor,
            Color gris) {

        JPanel fila = new JPanel(new BorderLayout(12, 0));
        fila.setOpaque(false);
        fila.setBorder(
                BorderFactory.createEmptyBorder(4, 0, 4, 0)
        );
        fila.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));

        JLabel etiqueta = new JLabel(nombre);
        etiqueta.setForeground(gris);
        etiqueta.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel dato = new JLabel(
                valor == null || valor.trim().isEmpty()
                ? "No registrado" : valor
        );
        dato.setForeground(new Color(35, 45, 55));
        dato.setFont(new Font("Segoe UI", Font.BOLD, 12));
        dato.setHorizontalAlignment(SwingConstants.RIGHT);

        fila.add(etiqueta, BorderLayout.WEST);
        fila.add(dato, BorderLayout.CENTER);

        tarjeta.add(fila);
    }

    /** Guarda en la base de datos los cambios de estado hechos en el mapa. */
    // =========================================================
    // GUARDAR CAMBIOS
    // =========================================================
    private void guardarCambios() {
        if (vista.cmbVuelo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(vista, "Seleccione un vuelo.");
            return;
        }
        if (pendientes.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "No hay cambios para guardar.");
            return;
        }
        int guardados = 0;
        int actualizados = 0;
        int errores = 0;
        for (String clave : new ArrayList<>(pendientes)) {
            asiento a = mapaAsientos.get(clave);
            if (a == null) {
                continue;
            }
            a.setPrecio(obtenerPrecioAsiento(a.getClase()));
            asiento existente = daoAsiento.buscarPorVueloYNumero(
                    a.getFk_vuelo(), a.getNum_asiento(), a.getClase());
            if (existente == null) {
                if (daoAsiento.guardar(a)) {
                    guardados++;
                } else {
                    errores++;
                }
            } else {
                existente.setPrecio(a.getPrecio());
                existente.setEstado(a.getEstado());
                if (daoAsiento.editar(existente)) {
                    actualizados++;
                } else {
                    errores++;
                }
            }
        }
        JOptionPane.showMessageDialog(vista,
                "Asientos creados: " + guardados
                + "\nEstados actualizados: " + actualizados
                + (errores > 0 ? "\nCon error: " + errores : ""));
        cargarCabina();
    }

    /** Quita la selección y descarta los cambios pendientes del mapa. */
    // =========================================================
    // LIMPIAR: descarta los cambios sin guardar y recarga el vuelo
    // =========================================================
    private void limpiar() {
        if (vista.cmbEstado.getItemCount() > 0) {
            vista.cmbEstado.setSelectedIndex(0);
        }
        cargarCabina();
    }

    // =========================================================
    // AL VOLVER A LA PANTALLA: aviones y vuelos nuevos aparecen sin reabrir el programa
    // =========================================================
    private boolean primeraVezMostrado = true;

    /** Recarga aviones y vuelos para reflejar lo registrado en otras pantallas. */
    private void refrescarListas() {
        if (primeraVezMostrado) {
            primeraVezMostrado = false; // la primera vez ya se cargo en el constructor
            return;
        }
        java.util.List<String> aviones = new java.util.ArrayList<>();
        for (avion a : daoAvion.listar()) {
            aviones.add(a.getModelo());
        }
        if (Tema.recargarCombo(vista.cmbAvion, aviones)) {
            cargarVuelosDelAvion(); // cambio la lista de aviones: se recarga todo
            return;
        }
        if (vista.cmbAvion.getSelectedItem() == null) {
            return;
        }
        String modelo = vista.cmbAvion.getSelectedItem().toString().trim();
        java.util.List<String> vuelos = new java.util.ArrayList<>();
        java.util.Map<Integer, String> rutas = VueloTexto.rutas();
        for (vuelo v : daoVuelo.listar()) {
            if (v.getFk_avion() != null && v.getFk_avion().trim().equalsIgnoreCase(modelo)) {
                vuelos.add(VueloTexto.texto(v, rutas));
            }
        }
        Object antes = vista.cmbVuelo.getSelectedItem();
        if (Tema.recargarCombo(vista.cmbVuelo, vuelos)) {
            Object ahora = vista.cmbVuelo.getSelectedItem();
            if (ahora == null) {
                MapaAvion.mensaje(vista.pnlCabina, "Este avión todavía no tiene vuelos registrados.");
            } else if (!ahora.equals(antes)) {
                cargarCabina();
            }
        }
    }
}
