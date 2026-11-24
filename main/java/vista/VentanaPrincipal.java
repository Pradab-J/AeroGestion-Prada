package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import vista.JpnlReservas;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 * VENTANA principal del administrador: menú lateral con las secciones
 * (aerolíneas, aviones, rutas, vuelos, asientos, tiquetes, reservas, etc.) y la
 * zona donde se muestra el panel elegido.
 * 
 * @author Aprendiz
 */
public class VentanaPrincipal extends javax.swing.JFrame {

    private JpnlPasajeros panelPasajeros;
    private JpnlEquipaje panelEquipaje;
    private JpnlAerolineas panelAerolineas;
    private JpnlAeropuerto panelAeropuerto;
    private JpnlRuta panelRuta;
    private JpnlVuelo panelVuelo;
    private JpnlAsientos panelAsientos;
    private JpnlConfiguracionAvion panelConfiguracion;
    private JpnlAvion panelAvion;
    private JpnlGestionPasajeros panelGestionPasajeros;
    private JpnlTiquete panelTiquete;
    private JpnlReservas panelReservas;
    private javax.swing.JButton botonActivo = null;
    // Gestión de usuarios (se arma por código; no forma parte del formulario de NetBeans).
    private JpnlUsuarios panelUsuarios;
    private final javax.swing.JButton btnUsuarios = new javax.swing.JButton();

    /** Indica qué panel se muestra al elegir «Usuarios» en el menú lateral. */
    public void setPanelUsuarios(JpnlUsuarios panelUsuarios) {
        this.panelUsuarios = panelUsuarios;
    }

    /**
     * Creates new form VentanaPrincipal
     */
    public VentanaPrincipal(
            JpnlPasajeros panelPasajeros,
            JpnlEquipaje panelEquipaje,
            JpnlAerolineas panelAerolineas,
            JpnlAeropuerto panelAeropuerto,
            JpnlRuta panelRuta,
            JpnlVuelo panelVuelo,
            JpnlAsientos panelAsientos,
            JpnlConfiguracionAvion panelConfiguracion,
            JpnlAvion panelAvion,
            JpnlGestionPasajeros panelGestionPasajeros,
            JpnlTiquete panelTiquete,
            JpnlReservas panelReservas) {

        initComponents();
        setLocationRelativeTo(null);

        estilizarBotonMenu(btnVuelos);
        estilizarBotonMenu(btnTiquetes);
        estilizarBotonMenu(btnAerolineas);
        estilizarBotonMenu(btnTiquetes);
        estilizarBotonMenu(btnAerolineas);
        estilizarBotonMenu(btnAeropuertos);
        estilizarBotonMenu(btnEquipajes);
        estilizarBotonMenu(btnPasajeros);
        estilizarBotonMenu(btnAsientos);
        //estilizarBotonMenu(btnCiudades);
        estilizarBotonMenu(btnReservas);
        estilizarBotonMenu(btnCerrarSesion);
        estilizarBotonMenu(btnConfiguracion);
        estilizarBotonMenu(btnAviones);
        estilizarBotonMenu(btnRutas);

        this.panelPasajeros = panelPasajeros;
        this.panelEquipaje = panelEquipaje;
        this.panelAerolineas = panelAerolineas;
        this.panelAeropuerto = panelAeropuerto;
        this.panelRuta = panelRuta;
        this.panelVuelo = panelVuelo;
        this.panelAsientos = panelAsientos;
        this.panelConfiguracion = panelConfiguracion;
        this.panelAvion = panelAvion;
        this.panelGestionPasajeros = panelGestionPasajeros;
        this.panelTiquete = panelTiquete;
        this.panelReservas = panelReservas;

        aplicarDiseno();
    }

    // =====================================================================
    // DISENO MODERNO (no toca initComponents: se aplica despues de crearlo)
    // =====================================================================
    private javax.swing.JLabel lblSeccion;

    /** Aplica el estilo visual (colores, fuentes, tamaños y posiciones) a los componentes del panel. */
    private void aplicarDiseno() {
        setTitle("AeroGestión Prada - Administrador");
        java.awt.Container raiz = getContentPane();
        raiz.removeAll();
        raiz.setLayout(new BorderLayout());
        raiz.setBackground(Tema.FONDO);
        jPanel2.setVisible(false);

        // ---------------- BARRA LATERAL ----------------
        javax.swing.JPanel lateral = new javax.swing.JPanel(new BorderLayout());
        lateral.setBackground(Tema.NAVY);
        lateral.setPreferredSize(new java.awt.Dimension(262, 100));

        // Marca
        javax.swing.JPanel marca = new javax.swing.JPanel();
        marca.setOpaque(false);
        marca.setLayout(new javax.swing.BoxLayout(marca, javax.swing.BoxLayout.X_AXIS));
        marca.setBorder(
                javax.swing.BorderFactory.createEmptyBorder(14, 22, 10, 16)
        );
        javax.swing.JLabel icono = new javax.swing.JLabel(Tema.iconoAvion(34, Tema.AZUL));
        jLabel1.setText("AeroGestión Prada");
        jLabel1.setFont(Tema.fuente(java.awt.Font.BOLD, 17));
        jLabel1.setForeground(Color.WHITE);
        jLabel1.setPreferredSize(null);
        btnPanelAdministrativo.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btnPanelAdministrativo.setText("Panel administrativo");
        btnPanelAdministrativo.setFont(Tema.fuente(java.awt.Font.PLAIN, 12));
        btnPanelAdministrativo.setForeground(new Color(140, 165, 188));
        btnPanelAdministrativo.setOpaque(false);
        btnPanelAdministrativo.setContentAreaFilled(false);
        btnPanelAdministrativo.setBorderPainted(false);
        btnPanelAdministrativo.setFocusPainted(false);
        btnPanelAdministrativo.setRolloverEnabled(false);
        btnPanelAdministrativo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnPanelAdministrativo.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        btnPanelAdministrativo.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnPanelAdministrativo.setCursor(java.awt.Cursor.getDefaultCursor());
        javax.swing.JPanel textosMarca = new javax.swing.JPanel();
        textosMarca.setOpaque(false);
        textosMarca.setLayout(new javax.swing.BoxLayout(textosMarca, javax.swing.BoxLayout.Y_AXIS));
        jLabel1.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        btnPanelAdministrativo.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        textosMarca.add(jLabel1);
        textosMarca.add(btnPanelAdministrativo);
        marca.add(icono);
        marca.add(javax.swing.Box.createHorizontalStrut(12));
        marca.add(textosMarca);

        // Menu
        javax.swing.JPanel menu = new javax.swing.JPanel();
        menu.setBackground(Tema.NAVY);
        menu.setLayout(new javax.swing.BoxLayout(menu, javax.swing.BoxLayout.Y_AXIS));
        menu.setBorder(
                javax.swing.BorderFactory.createEmptyBorder(0, 6, 4, 6)
        );

        seccionMenu(menu, jLabel4, "OPERACIÓN");
        itemMenu(menu, btnVuelos, "Vuelos");
        itemMenu(menu, btnTiquetes, "Tiquetes");
        itemMenu(menu, btnReservas, "Reservas");
        itemMenu(menu, btnPasajeros, "Pasajeros");
        menu.add(javax.swing.Box.createVerticalStrut(10));
        seccionMenu(menu, new javax.swing.JLabel(), "CATÁLOGO");
        itemMenu(menu, btnAerolineas, "Aerolíneas");
        itemMenu(menu, btnAeropuertos, "Aeropuertos");
        itemMenu(menu, btnRutas, "Rutas");
        itemMenu(menu, btnAviones, "Aviones");
        itemMenu(menu, btnConfiguracion, "Configuración de aviones");
        itemMenu(menu, btnAsientos, "Asientos");
        itemMenu(menu, btnEquipajes, "Equipajes");
        menu.add(javax.swing.Box.createVerticalStrut(10));
        seccionMenu(menu, new javax.swing.JLabel(), "ADMINISTRACIÓN");
        itemMenu(menu, btnUsuarios, "Usuarios");
        btnUsuarios.addActionListener(ev -> {
            seleccionarBoton(btnUsuarios);
            if (panelUsuarios != null) {
                mostrarPanel(panelUsuarios);
            }
        });

        javax.swing.JScrollPane scrollMenu = new javax.swing.JScrollPane(menu,
                javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollMenu.setBorder(null);
        scrollMenu.getViewport().setBackground(Tema.NAVY);
        scrollMenu.getVerticalScrollBar().setUnitIncrement(16);

        // Pie: cerrar sesion
        javax.swing.JPanel pie = new javax.swing.JPanel(new BorderLayout());
        pie.setBackground(Tema.NAVY);
        pie.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(40, 66, 88)),
                javax.swing.BorderFactory.createEmptyBorder(10, 6, 12, 6)));
        btnCerrarSesion.setText("Cerrar sesión");
        Tema.botonMenu(btnCerrarSesion);
        btnCerrarSesion.setPreferredSize(new java.awt.Dimension(240, 42));
        pie.add(btnCerrarSesion, BorderLayout.CENTER);

        lateral.add(marca, BorderLayout.NORTH);
        lateral.add(scrollMenu, BorderLayout.CENTER);
        lateral.add(pie, BorderLayout.SOUTH);

        // ---------------- BARRA SUPERIOR ----------------
        javax.swing.JPanel barra = new javax.swing.JPanel(new BorderLayout());
        barra.setBackground(Color.WHITE);
        barra.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, Tema.BORDE),
                javax.swing.BorderFactory.createEmptyBorder(14, 28, 14, 28)));

        lblSeccion = new javax.swing.JLabel("Inicio");
        lblSeccion.setFont(Tema.fuente(java.awt.Font.BOLD, 22));
        lblSeccion.setForeground(Tema.NAVY);
        jLabel3.setText("Sistema de gestión de vuelos");
        jLabel3.setFont(Tema.fuente(java.awt.Font.PLAIN, 13));
        jLabel3.setForeground(Tema.TEXTO_SUAVE);
        lblSeccion.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        jLabel3.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        javax.swing.JPanel titulos = new javax.swing.JPanel();
        titulos.setOpaque(false);
        titulos.setLayout(new javax.swing.BoxLayout(titulos, javax.swing.BoxLayout.Y_AXIS));
        titulos.add(lblSeccion);
        titulos.add(jLabel3);

        javax.swing.JLabel avatar = new javax.swing.JLabel("A", javax.swing.SwingConstants.CENTER) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                        java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Tema.AZUL);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        avatar.setFont(Tema.fuente(java.awt.Font.BOLD, 15));
        avatar.setForeground(Color.WHITE);
        avatar.setPreferredSize(new java.awt.Dimension(38, 38));
        javax.swing.JLabel rol = new javax.swing.JLabel("Administrador");
        rol.setFont(Tema.fuente(java.awt.Font.BOLD, 14));
        rol.setForeground(Tema.TEXTO);
        javax.swing.JPanel usuario = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 0));
        usuario.setOpaque(false);
        usuario.add(rol);
        usuario.add(avatar);

        barra.add(titulos, BorderLayout.WEST);
        barra.add(usuario, BorderLayout.EAST);

        // ---------------- CONTENIDO ----------------
        pnlContenido.setBackground(Tema.FONDO);
        pnlContenido.setBorder(javax.swing.BorderFactory.createEmptyBorder(22, 28, 24, 28));
        mostrarBienvenida();

        javax.swing.JPanel derecha = new javax.swing.JPanel(new BorderLayout());
        derecha.setBackground(Tema.FONDO);
        derecha.add(barra, BorderLayout.NORTH);
        derecha.add(pnlContenido, BorderLayout.CENTER);

        raiz.add(lateral, BorderLayout.WEST);
        raiz.add(derecha, BorderLayout.CENTER);

        // ---------------- TAMANO DE VENTANA ----------------
        int ancho = 1320, alto = 800;
        try {
            java.awt.Dimension pantalla = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            ancho = Math.min(ancho, pantalla.width - 40);
            alto = Math.min(alto, pantalla.height - 60);
        } catch (java.awt.HeadlessException ex) {
            // sin pantalla: se usan los valores por defecto
        }
        setMinimumSize(new java.awt.Dimension(1000, 620));
        setSize(ancho, alto);
        setLocationRelativeTo(null);
    }

    /** Agrega un título de sección al menú lateral. */
    private void seccionMenu(javax.swing.JPanel menu, javax.swing.JLabel etiqueta, String texto) {
        etiqueta.setText(texto);
        etiqueta.setFont(Tema.fuente(java.awt.Font.BOLD, 11));
        etiqueta.setForeground(new Color(120, 148, 172));
        etiqueta.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 18, 6, 0));
        etiqueta.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        etiqueta.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 30));
        menu.add(etiqueta);
    }

    /** Agrega un botón al menú lateral. */
    private void itemMenu(javax.swing.JPanel menu,
            javax.swing.JButton boton, String texto) {

        boton.setText(texto);
        Tema.botonMenu(boton);

        boton.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        // Botones más compactos
        boton.setPreferredSize(new java.awt.Dimension(240, 34));
        boton.setMaximumSize(
                new java.awt.Dimension(Integer.MAX_VALUE, 34)
        );

        menu.add(boton);
    }

    /** Muestra la pantalla de bienvenida cuando no hay ningún panel elegido. */
    private void mostrarBienvenida() {
        pnlContenido.removeAll();
        pnlContenido.setLayout(new java.awt.GridBagLayout());

        javax.swing.JPanel tarjeta = new javax.swing.JPanel();
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setLayout(new javax.swing.BoxLayout(tarjeta, javax.swing.BoxLayout.Y_AXIS));
        tarjeta.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(Tema.BORDE),
                javax.swing.BorderFactory.createEmptyBorder(40, 56, 40, 56)));

        javax.swing.JLabel ic = new javax.swing.JLabel(Tema.iconoAvion(64, Tema.AZUL));
        ic.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        javax.swing.JLabel titulo = new javax.swing.JLabel("Bienvenido al panel administrativo");
        titulo.setFont(Tema.fuente(java.awt.Font.BOLD, 24));
        titulo.setForeground(Tema.NAVY);
        titulo.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        javax.swing.JLabel texto = new javax.swing.JLabel(
                "<html><div style='width:380px;text-align:center'>Seleccione una opción del menú "
                + "lateral para gestionar vuelos, tiquetes, rutas, aviones, pasajeros y más.</div></html>");
        texto.setFont(Tema.fuente(java.awt.Font.PLAIN, 15));
        texto.setForeground(Tema.TEXTO_SUAVE);
        texto.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        tarjeta.add(ic);
        tarjeta.add(javax.swing.Box.createVerticalStrut(18));
        tarjeta.add(titulo);
        tarjeta.add(javax.swing.Box.createVerticalStrut(10));
        tarjeta.add(texto);
        pnlContenido.add(tarjeta);
        pnlContenido.revalidate();
        pnlContenido.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")

    /*private void seleccionarBoton(javax.swing.JButton botonSeleccionado) {

        Color normal = new Color(23, 42, 58);
        Color activo = new Color(29, 140, 248);

        btnInicio.setBackground(normal);
        btnAviones.setBackground(normal);
        btnVuelos.setBackground(normal);
        btnRutas.setBackground(normal);
        btnAerolineas.setBackground(normal);
        btnAeropuertos.setBackground(normal);
        btnPasajeros.setBackground(normal);
        btnEquipaje.setBackground(normal);
        btnTiquetes.setBackground(normal);

        botonSeleccionado.setBackground(activo);
    }*/
    private void seleccionarBoton(javax.swing.JButton boton) {
        if (botonActivo != null) {
            botonActivo.putClientProperty("activo", false);
            botonActivo.repaint();
        }
        botonActivo = boton;
        botonActivo.putClientProperty("activo", true);
        botonActivo.repaint();
        if (lblSeccion != null) {
            lblSeccion.setText(boton.getText());
        }
    }

    // =====================================================================
    // CERRAR SESION
    // =====================================================================
    private Runnable alCerrarSesion;

    /** Quien arma la aplicacion (inicioControlador) indica que hacer al cerrar sesion. */
    public void setAlCerrarSesion(Runnable accion) {
        this.alCerrarSesion = accion;
    }

    /** Deja el panel como recien abierto: bienvenida y ninguna opcion marcada. */
    public void reiniciar() {
        if (botonActivo != null) {
            botonActivo.putClientProperty("activo", false);
            botonActivo.repaint();
            botonActivo = null;
        }
        if (lblSeccion != null) {
            lblSeccion.setText("Inicio");
        }
        mostrarBienvenida();
    }

    /** Muestra en el centro de la ventana el panel de la sección elegida. */
    public void mostrarPanel(JPanel panel) {
        pnlContenido.removeAll();
        pnlContenido.setLayout(new BorderLayout());

        // El panel ocupa todo el espacio; solo aparece scroll si la ventana es muy pequeña
        pnlContenido.add(Tema.scrollAjustable(panel), BorderLayout.CENTER);

        pnlContenido.revalidate();
        pnlContenido.repaint();
    }

    /** Aplica el estilo visual a un botón del menú. */
    private void estilizarBotonMenu(javax.swing.JButton boton) {
        Tema.botonMenu(boton);
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        pnlMenu = new javax.swing.JPanel();
        pnlContenido = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        pnlMen = new javax.swing.JPanel();
        btnVuelos = new javax.swing.JButton();
        btnTiquetes = new javax.swing.JButton();
        btnAerolineas = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        btnPanelAdministrativo = new javax.swing.JButton();
        btnRutas = new javax.swing.JButton();
        btnAviones = new javax.swing.JButton();
        btnAeropuertos = new javax.swing.JButton();
        btnEquipajes = new javax.swing.JButton();
        btnPasajeros = new javax.swing.JButton();
        btnCerrarSesion = new javax.swing.JButton();
        btnAsientos = new javax.swing.JButton();
        btnConfiguracion = new javax.swing.JButton();
        btnReservas = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        jMenu2.setText("File");
        jMenuBar1.add(jMenu2);

        jMenu3.setText("Edit");
        jMenuBar1.add(jMenu3);

        pnlMenu.setBackground(new java.awt.Color(204, 204, 204));
        pnlMenu.setPreferredSize(new java.awt.Dimension(900, 200));

        javax.swing.GroupLayout pnlMenuLayout = new javax.swing.GroupLayout(pnlMenu);
        pnlMenu.setLayout(pnlMenuLayout);
        pnlMenuLayout.setHorizontalGroup(
            pnlMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 162, Short.MAX_VALUE)
        );
        pnlMenuLayout.setVerticalGroup(
            pnlMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlContenido.setBackground(new java.awt.Color(244, 247, 250));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlContenidoLayout = new javax.swing.GroupLayout(pnlContenido);
        pnlContenido.setLayout(pnlContenidoLayout);
        pnlContenidoLayout.setHorizontalGroup(
            pnlContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlContenidoLayout.createSequentialGroup()
                .addContainerGap(753, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlContenidoLayout.setVerticalGroup(
            pnlContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlContenidoLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(360, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Bernard MT Condensed", 0, 36)); // NOI18N
        jLabel1.setText("AEROGESTION  PRADA");

        pnlMen.setBackground(new java.awt.Color(23, 42, 58));

        btnVuelos.setBackground(new java.awt.Color(23, 42, 58));
        btnVuelos.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnVuelos.setForeground(new java.awt.Color(255, 255, 255));
        btnVuelos.setText("VUELOS");
        btnVuelos.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnVuelos.setFocusPainted(false);
        btnVuelos.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnVuelos.setOpaque(true);
        btnVuelos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVuelosActionPerformed(evt);
            }
        });

        btnTiquetes.setBackground(new java.awt.Color(23, 42, 58));
        btnTiquetes.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnTiquetes.setForeground(new java.awt.Color(255, 255, 255));
        btnTiquetes.setText("TIQUETES");
        btnTiquetes.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnTiquetes.setFocusPainted(false);
        btnTiquetes.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnTiquetes.setOpaque(true);
        btnTiquetes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTiquetesActionPerformed(evt);
            }
        });

        btnAerolineas.setBackground(new java.awt.Color(23, 42, 58));
        btnAerolineas.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnAerolineas.setForeground(new java.awt.Color(255, 255, 255));
        btnAerolineas.setText("AEROLINEAS");
        btnAerolineas.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnAerolineas.setFocusPainted(false);
        btnAerolineas.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAerolineas.setOpaque(true);
        btnAerolineas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAerolineasActionPerformed(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(0, 0, 255));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("GESTIONAR");
        jLabel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        btnPanelAdministrativo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnPanelAdministrativo.setText("PANEL ADMINSTRATIVO");
        btnPanelAdministrativo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPanelAdministrativoActionPerformed(evt);
            }
        });

        btnRutas.setBackground(new java.awt.Color(23, 42, 58));
        btnRutas.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnRutas.setForeground(new java.awt.Color(255, 255, 255));
        btnRutas.setText("RUTAS");
        btnRutas.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnRutas.setFocusPainted(false);
        btnRutas.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnRutas.setOpaque(true);
        btnRutas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRutasActionPerformed(evt);
            }
        });

        btnAviones.setBackground(new java.awt.Color(23, 42, 58));
        btnAviones.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnAviones.setForeground(new java.awt.Color(255, 255, 255));
        btnAviones.setText("AVIONES");
        btnAviones.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnAviones.setFocusPainted(false);
        btnAviones.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAviones.setOpaque(true);
        btnAviones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAvionesActionPerformed(evt);
            }
        });

        btnAeropuertos.setBackground(new java.awt.Color(23, 42, 58));
        btnAeropuertos.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnAeropuertos.setForeground(new java.awt.Color(255, 255, 255));
        btnAeropuertos.setText("AEROPUERTOS");
        btnAeropuertos.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnAeropuertos.setFocusPainted(false);
        btnAeropuertos.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAeropuertos.setOpaque(true);
        btnAeropuertos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAeropuertosActionPerformed(evt);
            }
        });

        btnEquipajes.setBackground(new java.awt.Color(23, 42, 58));
        btnEquipajes.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnEquipajes.setForeground(new java.awt.Color(255, 255, 255));
        btnEquipajes.setText("EQUIPAJES");
        btnEquipajes.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnEquipajes.setFocusPainted(false);
        btnEquipajes.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnEquipajes.setOpaque(true);
        btnEquipajes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEquipajesActionPerformed(evt);
            }
        });

        btnPasajeros.setBackground(new java.awt.Color(23, 42, 58));
        btnPasajeros.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnPasajeros.setForeground(new java.awt.Color(255, 255, 255));
        btnPasajeros.setText("PASAJEROS");
        btnPasajeros.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnPasajeros.setFocusPainted(false);
        btnPasajeros.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnPasajeros.setOpaque(true);
        btnPasajeros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasajerosActionPerformed(evt);
            }
        });

        btnCerrarSesion.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnCerrarSesion.setText("CERRAR SESION");
        btnCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarSesionActionPerformed(evt);
            }
        });

        btnAsientos.setBackground(new java.awt.Color(23, 42, 58));
        btnAsientos.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnAsientos.setForeground(new java.awt.Color(255, 255, 255));
        btnAsientos.setText("ASIENTOS");
        btnAsientos.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnAsientos.setFocusPainted(false);
        btnAsientos.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAsientos.setOpaque(true);
        btnAsientos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAsientosActionPerformed(evt);
            }
        });

        btnConfiguracion.setBackground(new java.awt.Color(23, 42, 58));
        btnConfiguracion.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnConfiguracion.setForeground(new java.awt.Color(255, 255, 255));
        btnConfiguracion.setText("CONF AVIONES");
        btnConfiguracion.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnConfiguracion.setFocusPainted(false);
        btnConfiguracion.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnConfiguracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfiguracionActionPerformed(evt);
            }
        });

        btnReservas.setBackground(new java.awt.Color(23, 42, 58));
        btnReservas.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnReservas.setForeground(new java.awt.Color(255, 255, 255));
        btnReservas.setText("RESERVAS");
        btnReservas.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnReservas.setFocusPainted(false);
        btnReservas.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnReservas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReservasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlMenLayout = new javax.swing.GroupLayout(pnlMen);
        pnlMen.setLayout(pnlMenLayout);
        pnlMenLayout.setHorizontalGroup(
            pnlMenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMenLayout.createSequentialGroup()
                .addGroup(pnlMenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMenLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlMenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlMenLayout.createSequentialGroup()
                                .addGroup(pnlMenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(btnAviones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnPasajeros, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnEquipajes, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnAeropuertos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                                    .addComponent(btnRutas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnAerolineas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnVuelos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnTiquetes, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(btnAsientos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnConfiguracion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnReservas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(pnlMenLayout.createSequentialGroup()
                        .addGroup(pnlMenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlMenLayout.createSequentialGroup()
                                .addGap(53, 53, 53)
                                .addComponent(btnPanelAdministrativo))
                            .addGroup(pnlMenLayout.createSequentialGroup()
                                .addGap(62, 62, 62)
                                .addComponent(btnCerrarSesion)))
                        .addGap(0, 38, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlMenLayout.setVerticalGroup(
            pnlMenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMenLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(btnPanelAdministrativo)
                .addGap(31, 31, 31)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(btnAviones)
                .addGap(8, 8, 8)
                .addComponent(btnAerolineas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnVuelos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnTiquetes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRutas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAeropuertos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnEquipajes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnPasajeros)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAsientos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnConfiguracion)
                .addGap(18, 18, 18)
                .addComponent(btnReservas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(btnCerrarSesion)
                .addGap(25, 25, 25))
        );

        jLabel3.setText("SISTEMA DE GESTION DE VUELOS");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlMen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 71, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(pnlContenido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pnlContenido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12))
                    .addComponent(pnlMen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTiquetesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTiquetesActionPerformed
        seleccionarBoton(btnTiquetes);
        mostrarPanel(panelTiquete);

    }//GEN-LAST:event_btnTiquetesActionPerformed

    private void btnVuelosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVuelosActionPerformed
        seleccionarBoton(btnVuelos);
        mostrarPanel(panelVuelo);

    }//GEN-LAST:event_btnVuelosActionPerformed

    private void btnAerolineasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAerolineasActionPerformed
        seleccionarBoton(btnAerolineas);
        mostrarPanel(panelAerolineas);
    }//GEN-LAST:event_btnAerolineasActionPerformed

    private void btnPanelAdministrativoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPanelAdministrativoActionPerformed
        //LoginAdminn login = new LoginAdminn();
        //login.setLocationRelativeTo(this);
        //login.setVisible(true);
    }//GEN-LAST:event_btnPanelAdministrativoActionPerformed

    private void btnRutasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRutasActionPerformed
        seleccionarBoton(btnRutas);
        mostrarPanel(panelRuta);
    }//GEN-LAST:event_btnRutasActionPerformed

    private void btnAvionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAvionesActionPerformed
        seleccionarBoton(btnAviones);
        mostrarPanel(panelAvion);
    }//GEN-LAST:event_btnAvionesActionPerformed

    private void btnAeropuertosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAeropuertosActionPerformed
        seleccionarBoton(btnAeropuertos);
        mostrarPanel(panelAeropuerto);
    }//GEN-LAST:event_btnAeropuertosActionPerformed

    private void btnEquipajesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEquipajesActionPerformed
        seleccionarBoton(btnEquipajes);
        mostrarPanel(panelEquipaje);
    }//GEN-LAST:event_btnEquipajesActionPerformed

    private void btnPasajerosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPasajerosActionPerformed
        seleccionarBoton(btnPasajeros);
        mostrarPanel(panelGestionPasajeros);
    }//GEN-LAST:event_btnPasajerosActionPerformed

    private void btnCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarSesionActionPerformed
        int respuesta = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "¿Deseas cerrar sesión y volver al inicio?",
                "Cerrar sesión",
                javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (respuesta == javax.swing.JOptionPane.YES_OPTION) {
            if (alCerrarSesion != null) {
                // Reutiliza la pantalla de Inicio original (la que tiene los controladores conectados)
                alCerrarSesion.run();
            } else {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "No se pudo cerrar la sesión: falta conectar la pantalla de inicio.",
                        "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnCerrarSesionActionPerformed

    private void btnAsientosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAsientosActionPerformed
        seleccionarBoton(btnAsientos);
        mostrarPanel(panelAsientos);
    }//GEN-LAST:event_btnAsientosActionPerformed

    private void btnConfiguracionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfiguracionActionPerformed
        seleccionarBoton(btnConfiguracion);
        mostrarPanel(panelConfiguracion);
    }//GEN-LAST:event_btnConfiguracionActionPerformed

    private void btnReservasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReservasActionPerformed
        seleccionarBoton(btnReservas);
        mostrarPanel(panelReservas);
    }//GEN-LAST:event_btnReservasActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAerolineas;
    private javax.swing.JButton btnAeropuertos;
    private javax.swing.JButton btnAsientos;
    private javax.swing.JButton btnAviones;
    private javax.swing.JButton btnCerrarSesion;
    private javax.swing.JButton btnConfiguracion;
    private javax.swing.JButton btnEquipajes;
    private javax.swing.JButton btnPanelAdministrativo;
    private javax.swing.JButton btnPasajeros;
    private javax.swing.JButton btnReservas;
    private javax.swing.JButton btnRutas;
    private javax.swing.JButton btnTiquetes;
    private javax.swing.JButton btnVuelos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel2;
    public javax.swing.JPanel pnlContenido;
    private javax.swing.JPanel pnlMen;
    private javax.swing.JPanel pnlMenu;
    // End of variables declaration//GEN-END:variables
}
