/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vista;

/**
 * PANEL de selección de asiento (paso 3 de la reserva): muestra el mapa del avión.
 * 
 * @author prada
 */
public class JpnlSeleccionarAsiento extends javax.swing.JPanel {

    /**
     * Creates new form JpnlSeleccionarAsiento
     */
    public JpnlSeleccionarAsiento() {
    initComponents();

    setBackground(new java.awt.Color(244, 247, 250));

    estilizarEncabezado();
    estilizarBotonContinuar();
    estilizarInformacion();
    aplicarDiseno();
    }
    
    
    /** Aplica el estilo al encabezado con los datos del vuelo. */
    private void estilizarEncabezado() {

    jLabel3.setFont(
            new java.awt.Font(
                    "Segoe UI",
                    java.awt.Font.BOLD,
                    20
            )
    );

    jLabel2.setFont(
            new java.awt.Font(
                    "Segoe UI",
                    java.awt.Font.PLAIN,
                    13
            )
    );
}

private void estilizarInformacion() {

    lblVuelo.setFont(
            new java.awt.Font(
                    "Segoe UI",
                    java.awt.Font.BOLD,
                    14
            )
    );

    lblAvion.setFont(
            new java.awt.Font(
                    "Segoe UI",
                    java.awt.Font.BOLD,
                    14
            )
    );

    lblAsientoSeleccionado.setFont(
            new java.awt.Font(
                    "Segoe UI",
                    java.awt.Font.BOLD,
                    14
            )
    );

    lblLeyenda.setFont(
            new java.awt.Font(
                    "Segoe UI",
                    java.awt.Font.PLAIN,
                    13
            )
    );

    lblVuelo.setForeground(
            new java.awt.Color(38, 50, 56)
    );

    lblAvion.setForeground(
            new java.awt.Color(38, 50, 56)
    );

    lblAsientoSeleccionado.setForeground(
            new java.awt.Color(29, 140, 248)
    );

    lblLeyenda.setForeground(
            new java.awt.Color(96, 125, 139)
    );
}

private void estilizarBotonContinuar() {

    btnContinuar.setForeground(
            java.awt.Color.WHITE
    );

    btnContinuar.setFont(
            new java.awt.Font(
                    "Segoe UI",
                    java.awt.Font.BOLD,
                    13
            )
    );

    btnContinuar.setFocusPainted(false);
    btnContinuar.setBorderPainted(false);
    btnContinuar.setContentAreaFilled(false);
    btnContinuar.setOpaque(false);

    btnContinuar.setCursor(
            new java.awt.Cursor(
                    java.awt.Cursor.HAND_CURSOR
            )
    );

    btnContinuar.setUI(
            new javax.swing.plaf.basic.BasicButtonUI() {

        @Override
        public void paint(
                java.awt.Graphics g,
                javax.swing.JComponent c) {

            javax.swing.JButton boton =
                    (javax.swing.JButton) c;

            java.awt.Graphics2D g2 =
                    (java.awt.Graphics2D) g.create();

            g2.setRenderingHint(
                    java.awt.RenderingHints.KEY_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_ANTIALIAS_ON
            );

            java.awt.Color fondo =
                    new java.awt.Color(23, 42, 58);

            if (boton.getModel().isPressed()) {

                fondo = new java.awt.Color(
                        15, 100, 190
                );

            } else if (boton.getModel().isRollover()) {

                fondo = new java.awt.Color(
                        29, 140, 248
                );
            }

            g2.setColor(fondo);

            g2.fillRoundRect(
                    0,
                    0,
                    c.getWidth(),
                    c.getHeight(),
                    15,
                    15
            );

            g2.dispose();

            super.paint(g, c);
        }
    });
}

    /** Aplica el estilo visual (colores, fuentes, tamaños y posiciones) a los componentes del panel. */
    // =====================================================================
    // DISENO MODERNO (se aplica despues de initComponents, sin tocarlo)
    // =====================================================================
    private void aplicarDiseno() {
        Tema.cabecera(jPanel2, jLabel3, "SELECCIONAR ASIENTO", jLabel2,
                "Elija el asiento que prefiera para su vuelo", null);
        lblLeyenda.setVisible(false);

        // Mapa del avion (el controlador lo dibuja dentro de pnlCabina)
        jScrollPane1.setBorder(null);
        jScrollPane1.getViewport().setBackground(Tema.FONDO);
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(18);
        jScrollPane1.getHorizontalScrollBar().setUnitIncrement(18);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(400, 330));
        pnlCabina.setBackground(Tema.FONDO);
        MapaAvion.mensaje(pnlCabina, "Cargando distribución del avión...");
        javax.swing.JPanel tarjetaMapa = new javax.swing.JPanel(new java.awt.BorderLayout());
        tarjetaMapa.setBorder(javax.swing.BorderFactory.createLineBorder(Tema.BORDE));
        tarjetaMapa.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        // Barra inferior: datos del vuelo, asiento elegido y boton continuar
        lblVuelo.setFont(Tema.fuente(java.awt.Font.BOLD, 15));
        lblVuelo.setForeground(Tema.NAVY);
        lblAvion.setFont(Tema.fuente(java.awt.Font.PLAIN, 13));
        lblAvion.setForeground(Tema.TEXTO_SUAVE);
        lblAsientoSeleccionado.setFont(Tema.fuente(java.awt.Font.BOLD, 15));
        lblAsientoSeleccionado.setForeground(Tema.AZUL);
        btnContinuar.setText("Continuar");
        btnContinuar.setPreferredSize(new java.awt.Dimension(170, 44));

        javax.swing.JPanel datos = new javax.swing.JPanel();
        datos.setOpaque(false);
        datos.setLayout(new javax.swing.BoxLayout(datos, javax.swing.BoxLayout.Y_AXIS));
        lblVuelo.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        lblAvion.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        datos.add(lblVuelo);
        datos.add(lblAvion);

        javax.swing.JPanel barra = Tema.tarjeta();
        barra.setLayout(new java.awt.BorderLayout(24, 0));
        barra.add(datos, java.awt.BorderLayout.WEST);
        barra.add(lblAsientoSeleccionado, java.awt.BorderLayout.CENTER);
        barra.add(btnContinuar, java.awt.BorderLayout.EAST);

        removeAll();
        setLayout(new java.awt.BorderLayout(0, 18));
        setBackground(Tema.FONDO);
        setPreferredSize(null);
        add(jPanel2, java.awt.BorderLayout.NORTH);
        add(tarjetaMapa, java.awt.BorderLayout.CENTER);
        add(barra, java.awt.BorderLayout.SOUTH);
        Tema.aplicar(this);
        lblAsientoSeleccionado.setForeground(Tema.AZUL);
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
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblVuelo = new javax.swing.JLabel();
        lblAvion = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        pnlCabina = new javax.swing.JPanel();
        lblAsientoSeleccionado = new javax.swing.JLabel();
        btnContinuar = new javax.swing.JButton();
        lblLeyenda = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(23, 42, 58));

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("SELECCIONAR ASIENTO");

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Selecciona los asientos disponibles");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2))
                .addContainerGap(359, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        lblVuelo.setText("Vuelo ");

        lblAvion.setText("Avion");

        pnlCabina.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pnlCabinaLayout = new javax.swing.GroupLayout(pnlCabina);
        pnlCabina.setLayout(pnlCabinaLayout);
        pnlCabinaLayout.setHorizontalGroup(
            pnlCabinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 538, Short.MAX_VALUE)
        );
        pnlCabinaLayout.setVerticalGroup(
            pnlCabinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 297, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(pnlCabina);

        lblAsientoSeleccionado.setText("Asiento seleccionado: Ninguno ");

        btnContinuar.setText("CONTINUAR");

        lblLeyenda.setText("Disponible     Ocupado     Seleccionado");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 540, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblVuelo)
                                    .addComponent(lblAvion)
                                    .addComponent(lblAsientoSeleccionado))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(232, 232, 232)
                                .addComponent(btnContinuar))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblLeyenda)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblVuelo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblAvion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(lblLeyenda)
                .addGap(18, 18, 18)
                .addComponent(lblAsientoSeleccionado)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnContinuar)
                .addGap(21, 21, 21))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnContinuar;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JLabel lblAsientoSeleccionado;
    public javax.swing.JLabel lblAvion;
    private javax.swing.JLabel lblLeyenda;
    public javax.swing.JLabel lblVuelo;
    public javax.swing.JPanel pnlCabina;
    // End of variables declaration//GEN-END:variables
}
