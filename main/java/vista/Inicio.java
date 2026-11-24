/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import java.awt.Color;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * VENTANA de la reserva del pasajero: muestra la portada y, al reservar, va
 * mostrando cada paso (vuelo, pasajero, asiento, equipaje, confirmación) con
 * botón Volver e indicador de pasos.
 *
 * @author Aprendiz
 */
public class Inicio extends javax.swing.JFrame {

    /**
     * Creates new form Inicio
     */
    public Inicio() {
        initComponents();
        cargarImagenAvion();

        estilizarBoton(btnReserva);
        estilizarBoton(btnIniciarSesion);

        disenarPantalla();
    }

    /**
     * Carga y muestra la imagen de portada.
     */
    private void cargarImagenAvion() {
        

        ImageIcon icono = new ImageIcon(
                getClass().getResource("/images/INICIO.jpeg")
        );
        Image imagen = icono.getImage();
        Image imagenRedimensionada = imagen.getScaledInstance(
                410,
                200,
                Image.SCALE_SMOOTH
        );
        lblAvion.setIcon(
                new ImageIcon(imagenRedimensionada)
        );
        lblAvion.setText("");
    }

    // =====================================================================
    // NAVEGACION DEL PROCESO DE RESERVA
    // =====================================================================
    private final java.util.ArrayDeque<javax.swing.JPanel> historial = new java.util.ArrayDeque<>();
    private javax.swing.JPanel panelActual;
    private Runnable alVolverInicio;

    /**
     * Accion que se ejecuta al volver a la portada (reiniciar formularios,
     * etc.).
     */
    public void setAlVolverInicio(Runnable accion) {
        this.alVolverInicio = accion;
    }

    /**
     * Muestra la portada (con sus botones funcionando) y reinicia el proceso.
     */
    public void mostrarInicio() {
        historial.clear();
        panelActual = null;
        java.awt.Container raiz = getContentPane();
        raiz.removeAll();
        raiz.setLayout(new java.awt.BorderLayout());
        raiz.add(jPanel2, java.awt.BorderLayout.CENTER);
        raiz.revalidate();
        raiz.repaint();
        if (alVolverInicio != null) {
            alVolverInicio.run();
        }
    }

    /**
     * Muestra una pantalla del proceso; recuerda la anterior para poder volver.
     */
    public void mostrarPanel(javax.swing.JPanel panel) {
        if (panelActual != null && panelActual != panel) {
            historial.push(panelActual);
        }
        panelActual = panel;
        construir(panel, true);
    }

    /**
     * Pantalla final (tiquete): sin boton de volver y sin historial.
     */
    public void mostrarFinal(javax.swing.JPanel panel) {
        historial.clear();
        panelActual = panel;
        construir(panel, false);
    }

    /**
     * Vuelve a la pantalla anterior; si no hay, vuelve a la portada.
     */
    public void volver() {
        if (historial.isEmpty()) {
            mostrarInicio();
            return;
        }
        javax.swing.JPanel anterior = historial.pop();
        panelActual = anterior;
        construir(anterior, true);
    }

    /**
     * Arma la pantalla: marco, barra superior (si corresponde) y el panel con
     * scroll.
     */
    private void construir(javax.swing.JPanel panel, boolean conBarra) {
        java.awt.Container raiz = getContentPane();
        raiz.removeAll();
        raiz.setLayout(new java.awt.BorderLayout());

        javax.swing.JPanel marco = new javax.swing.JPanel(new java.awt.BorderLayout(0, 14));
        marco.setBackground(Tema.FONDO);
        marco.setBorder(javax.swing.BorderFactory.createEmptyBorder(18, 32, 22, 32));
        if (conBarra) {
            marco.add(crearBarra(panel), java.awt.BorderLayout.NORTH);
        }
        marco.add(Tema.scrollAjustable(panel), java.awt.BorderLayout.CENTER);

        raiz.add(marco, java.awt.BorderLayout.CENTER);
        raiz.revalidate();
        raiz.repaint();
    }

    /**
     * Crea la barra superior con el botón Volver y el indicador de pasos.
     */
    private javax.swing.JPanel crearBarra(javax.swing.JPanel panel) {
        javax.swing.JPanel barra = new javax.swing.JPanel(new java.awt.BorderLayout(20, 0));
        barra.setOpaque(false);

        boolean primera = historial.isEmpty();
        javax.swing.JButton volver = new javax.swing.JButton(primera ? "Volver al inicio" : "Volver");
        Tema.boton(volver, Tema.Tipo.SECUNDARIO);
        volver.setIcon(Tema.iconoVolver(12, Tema.AZUL));
        volver.setIconTextGap(8);
        volver.setPreferredSize(new java.awt.Dimension(primera ? 168 : 112, 38));
        volver.addActionListener(e -> volver());
        barra.add(volver, java.awt.BorderLayout.WEST);

        int paso = pasoDe(panel);
        if (paso >= 0) {
            barra.add(new BarraPasos(paso), java.awt.BorderLayout.EAST);
        }
        return barra;
    }

    /**
     * Dice en qué número de paso de la reserva está un panel (-1 si no es un
     * paso).
     */
    private static int pasoDe(javax.swing.JPanel p) {
        String n = p.getClass().getSimpleName();
        switch (n) {
            case "JpnlBuscarVuelo":
                return 0;
            case "JpnlPasajeros":
                return 1;
            case "JpnlSeleccionarAsiento":
                return 2;
            case "JpnlSeleccionarEquipaje":
                return 3;
            case "JpnlConfirmarReserva":
                return 4;
            default:
                return -1;
        }
    }

    /**
     * Indicador de pasos: Vuelo > Pasajero > Asiento > Equipaje > Confirmar.
     */
    private static final class BarraPasos extends javax.swing.JPanel {

        private static final String[] NOMBRES = {"Vuelo", "Pasajero", "Asiento", "Equipaje", "Confirmar"};
        private final int actual;

        BarraPasos(int actual) {
            this.actual = actual;
            setOpaque(false);
            setPreferredSize(new java.awt.Dimension(560, 38));
        }

        @Override
        protected void paintComponent(java.awt.Graphics g0) {
            java.awt.Graphics2D g = (java.awt.Graphics2D) g0.create();
            g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int n = NOMBRES.length;
            int w = getWidth(), h = getHeight();
            int paso = w / n;
            int r = 12;
            int cy = h / 2;
            for (int i = 0; i < n; i++) {
                int cx = i * paso + paso / 2 - 22;
                if (i < n - 1) {
                    g.setColor(i < actual ? Tema.VERDE : new Color(205, 214, 224));
                    g.fillRect(cx + r + 6 + g.getFontMetrics(Tema.fuente(java.awt.Font.BOLD, 12)).stringWidth(NOMBRES[i]) + 8,
                            cy - 1, Math.max(8, paso - r * 2 - 18 - g.getFontMetrics(Tema.fuente(java.awt.Font.BOLD, 12)).stringWidth(NOMBRES[i])), 2);
                }
                Color c = i < actual ? Tema.VERDE : (i == actual ? Tema.AZUL : new Color(205, 214, 224));
                g.setColor(c);
                g.fillOval(cx - r, cy - r, r * 2, r * 2);
                g.setColor(Color.WHITE);
                g.setFont(Tema.fuente(java.awt.Font.BOLD, 12));
                String num = i < actual ? "\u2713" : String.valueOf(i + 1);
                java.awt.FontMetrics fm = g.getFontMetrics();
                if (i < actual) {
                    g.setStroke(new java.awt.BasicStroke(2.2f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND));
                    g.drawLine(cx - 5, cy, cx - 1, cy + 4);
                    g.drawLine(cx - 1, cy + 4, cx + 5, cy - 4);
                } else {
                    g.drawString(num, cx - fm.stringWidth(num) / 2, cy + (fm.getAscent() - fm.getDescent()) / 2);
                }
                g.setColor(i == actual ? Tema.NAVY : Tema.TEXTO_SUAVE);
                g.setFont(Tema.fuente(i == actual ? java.awt.Font.BOLD : java.awt.Font.PLAIN, 12));
                g.drawString(NOMBRES[i], cx + r + 6, cy + 4);
            }
            g.dispose();
        }
    }

    /**
     * Aplica el estilo visual a un botón de la portada.
     */
    private void estilizarBoton(javax.swing.JButton boton) {
        boton.setForeground(Color.WHITE);
        boton.setFont(
                new java.awt.Font(
                        "Segoe UI",
                        java.awt.Font.BOLD,
                        14
                )
        );
        boton.setHorizontalAlignment(
                javax.swing.SwingConstants.CENTER
        );
        boton.setBorder(
                javax.swing.BorderFactory.createEmptyBorder(
                        5, 15, 5, 15
                )
        );
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setCursor(
                new java.awt.Cursor(
                        java.awt.Cursor.HAND_CURSOR
                )
        );
        boton.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(
                    java.awt.Graphics g,
                    javax.swing.JComponent c) {
                javax.swing.JButton b
                        = (javax.swing.JButton) c;
                java.awt.Graphics2D g2
                        = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(
                        java.awt.RenderingHints.KEY_ANTIALIASING,
                        java.awt.RenderingHints.VALUE_ANTIALIAS_ON
                );
                Color fondo;
                if (b.getModel().isPressed()) {
                    fondo = new Color(15, 100, 190);
                } else if (b.getModel().isRollover()) {
                    fondo = new Color(29, 140, 248);
                } else {
                    fondo = new Color(23, 42, 58);
                }
                g2.setColor(fondo);
                g2.fillRoundRect(
                        0,
                        0,
                        c.getWidth(),
                        c.getHeight(),
                        18,
                        18
                );
                g2.dispose();
                super.paint(g, c);
            }
        });
    }

    /**
     * Aplica el diseño (colores, fuentes, posiciones) a la portada.
     */
    // =====================================================================
    // DISENO MODERNO (se aplica despues de initComponents, sin tocarlo)
    // =====================================================================
    private void disenarPantalla() {
        setTitle("AeroGestión Prada");
        java.awt.Container raiz = getContentPane();
        raiz.removeAll();
        raiz.setLayout(new java.awt.BorderLayout());

        // ---------- Lado izquierdo: portada con la imagen ----------
        Tema.PanelImagen portada = new Tema.PanelImagen(Tema.cargarImagen("/images/INICIO.jpeg"));
        portada.setLayout(new java.awt.GridBagLayout());
        portada.setPreferredSize(new java.awt.Dimension(500, 700));

        javax.swing.JPanel marca = new javax.swing.JPanel();
        marca.setOpaque(false);
        marca.setLayout(new javax.swing.BoxLayout(marca, javax.swing.BoxLayout.Y_AXIS));
        javax.swing.JLabel icono = new javax.swing.JLabel(Tema.iconoAvion(56, Color.WHITE));
        javax.swing.JLabel nombre = new javax.swing.JLabel("AeroGestión Prada");
        nombre.setFont(Tema.fuente(java.awt.Font.BOLD, 38));
        nombre.setForeground(Color.WHITE);
        javax.swing.JLabel lema = Tema.parrafo(
                "Reserva tu vuelo de forma simple, rápida y segura.", 330,
                Tema.fuente(java.awt.Font.PLAIN, 17), new Color(205, 220, 235));
        icono.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        nombre.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        lema.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        marca.add(icono);
        marca.add(javax.swing.Box.createVerticalStrut(18));
        marca.add(nombre);
        marca.add(javax.swing.Box.createVerticalStrut(10));
        marca.add(lema);
        java.awt.GridBagConstraints gc = new java.awt.GridBagConstraints();
        gc.anchor = java.awt.GridBagConstraints.WEST;
        gc.insets = new java.awt.Insets(0, 56, 0, 30);
        portada.add(marca, gc);

        // ---------- Lado derecho: acciones ----------
        javax.swing.JPanel derecha = new javax.swing.JPanel(new java.awt.GridBagLayout());
        derecha.setBackground(Color.WHITE);

        javax.swing.JPanel caja = new javax.swing.JPanel();
        caja.setOpaque(false);
        caja.setLayout(new javax.swing.BoxLayout(caja, javax.swing.BoxLayout.Y_AXIS));

        javax.swing.JLabel saludo = new javax.swing.JLabel("BIENVENIDO");
        saludo.setFont(Tema.fuente(java.awt.Font.BOLD, 12));
        saludo.setForeground(Tema.AZUL);
        javax.swing.JLabel titulo = new javax.swing.JLabel("¿Qué desea hacer hoy?");
        titulo.setFont(Tema.fuente(java.awt.Font.BOLD, 30));
        titulo.setForeground(Tema.NAVY);
        javax.swing.JLabel sub = Tema.parrafo(
                "Reserve su vuelo en pocos pasos o ingrese al panel administrativo.", 340,
                Tema.fuente(java.awt.Font.PLAIN, 15), Tema.TEXTO_SUAVE);

        btnReserva.setText("Reservar un vuelo");
        Tema.boton(btnReserva, Tema.Tipo.PRIMARIO);

        btnConsultar.setText("Consultar reserva");
        Tema.boton(btnConsultar, Tema.Tipo.SECUNDARIO);

        btnIniciarSesion.setText("Acceso administrador");
        Tema.boton(btnIniciarSesion, Tema.Tipo.SECUNDARIO);

        for (javax.swing.JButton b : new javax.swing.JButton[]{
            btnReserva, btnConsultar, btnIniciarSesion}) {
            b.setFont(Tema.fuente(java.awt.Font.BOLD, 16));
            b.setPreferredSize(new java.awt.Dimension(340, 52));
            b.setMaximumSize(new java.awt.Dimension(340, 52));
            b.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        }

        javax.swing.JLabel pie = new javax.swing.JLabel("Sistema de gestión de vuelos");
        pie.setFont(Tema.fuente(java.awt.Font.PLAIN, 12));
        pie.setForeground(new Color(150, 165, 178));

        saludo.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        titulo.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        sub.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        pie.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        caja.add(saludo);
        caja.add(javax.swing.Box.createVerticalStrut(8));
        caja.add(titulo);
        caja.add(javax.swing.Box.createVerticalStrut(10));
        caja.add(sub);
        caja.add(javax.swing.Box.createVerticalStrut(34));
        caja.add(btnReserva);
        caja.add(javax.swing.Box.createVerticalStrut(14));
        caja.add(btnConsultar);
        caja.add(javax.swing.Box.createVerticalStrut(14));
        caja.add(btnIniciarSesion);
        caja.add(javax.swing.Box.createVerticalStrut(40));
        caja.add(pie);
        derecha.add(caja);

        jPanel2.removeAll();
        jPanel2.setLayout(new java.awt.BorderLayout());
        jPanel2.setBackground(Color.WHITE);
        jPanel2.add(portada, java.awt.BorderLayout.WEST);
        jPanel2.add(derecha, java.awt.BorderLayout.CENTER);
        raiz.add(jPanel2, java.awt.BorderLayout.CENTER);

        setMinimumSize(new java.awt.Dimension(960, 640));
        setSize(1040, 700);
        setLocationRelativeTo(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        btnReserva = new javax.swing.JButton();
        btnIniciarSesion = new javax.swing.JButton();
        btnConsultar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lblAvion = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(244, 247, 250));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        btnReserva.setText("RESERVA DE VUELO");
        btnReserva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReservaActionPerformed(evt);
            }
        });

        btnIniciarSesion.setText("INICIAR SESION");
        btnIniciarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarSesionActionPerformed(evt);
            }
        });

        btnConsultar.setText("CONSULTAR RESERVA");
        btnConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsultarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(92, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnConsultar, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnIniciarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReserva, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(88, 88, 88))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(btnReserva, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(btnConsultar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addComponent(btnIniciarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51))
        );

        jLabel1.setFont(new java.awt.Font("Bernard MT Condensed", 0, 36)); // NOI18N
        jLabel1.setText("AEROGESTION  PRADA");

        lblAvion.setText("jLabel2");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(86, 86, 86))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAvion, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(jLabel1)
                .addGap(39, 39, 39)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblAvion, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel2.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnReservaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReservaActionPerformed

    }//GEN-LAST:event_btnReservaActionPerformed

    private void btnIniciarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciarSesionActionPerformed


    }//GEN-LAST:event_btnIniciarSesionActionPerformed

    private void btnConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultarActionPerformed
        // La acción la atiende inicioControlador.
    }//GEN-LAST:event_btnConsultarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnConsultar;
    public javax.swing.JButton btnIniciarSesion;
    public javax.swing.JButton btnReserva;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblAvion;
    // End of variables declaration//GEN-END:variables
}
