/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vista;

/**
 * PANEL de confirmación (último paso): resumen de la reserva y método de pago.
 * 
 * @author Aprendiz
 */
public class JpnlConfirmarReserva extends javax.swing.JPanel {

    /**
     * Creates new form JpnlConfirmarReserva
     */
    public JpnlConfirmarReserva() {
        initComponents();
        aplicarDiseño();
    }

   
    
    /** Aplica el estilo visual (colores, fuentes, tamaños y posiciones) a los componentes del panel. */
    private void aplicarDiseño() {

// =========================================================
// COLORES
// =========================================================
java.awt.Color fondo = new java.awt.Color(244, 247, 250);
java.awt.Color azulOscuro = new java.awt.Color(23, 42, 58);
java.awt.Color azul = new java.awt.Color(29, 140, 248);
java.awt.Color blanco = java.awt.Color.WHITE;
java.awt.Color gris = new java.awt.Color(90, 100, 110);
java.awt.Color borde = new java.awt.Color(220, 225, 230);

setBackground(fondo);

// =========================================================
// PANEL PRINCIPAL
// =========================================================
removeAll();
setLayout(new java.awt.BorderLayout());

// =========================================================
// ENCABEZADO
// =========================================================
jPanel2.removeAll();
jPanel2.setBackground(azulOscuro);
jPanel2.setBorder(
        javax.swing.BorderFactory.createEmptyBorder(
                18, 30, 18, 30
        )
);

jLabel3.setText("CONFIRMAR RESERVA");
jLabel3.setForeground(blanco);
jLabel3.setFont(
        new java.awt.Font(
                "Arial",
                java.awt.Font.BOLD,
                22
        )
);

jLabel2.setText(
        "Revisa los datos de tu reserva antes de confirmar"
);
jLabel2.setForeground(
        new java.awt.Color(220, 230, 240)
);
jLabel2.setFont(
        new java.awt.Font(
                "Arial",
                java.awt.Font.PLAIN,
                13
        )
);

jPanel2.setLayout(
        new javax.swing.BoxLayout(
                jPanel2,
                javax.swing.BoxLayout.Y_AXIS
        )
);

jLabel3.setAlignmentX(
        javax.swing.JComponent.LEFT_ALIGNMENT
);

jLabel2.setAlignmentX(
        javax.swing.JComponent.LEFT_ALIGNMENT
);

jPanel2.add(jLabel3);
jPanel2.add(
        javax.swing.Box.createVerticalStrut(5)
);
jPanel2.add(jLabel2);

add(jPanel2, java.awt.BorderLayout.NORTH);

// =========================================================
// CONTENEDOR CENTRAL
// =========================================================
javax.swing.JPanel contenido =
        new javax.swing.JPanel();

contenido.setBackground(fondo);
contenido.setLayout(
        new javax.swing.BoxLayout(
                contenido,
                javax.swing.BoxLayout.Y_AXIS
        )
);

contenido.setBorder(
        javax.swing.BorderFactory.createEmptyBorder(
                20, 30, 25, 30
        )
);

// =========================================================
// SECCIÓN 1 - INFORMACIÓN DEL VUELO
// =========================================================
javax.swing.JPanel panelVuelo =
        crearSeccion(
                "✈  INFORMACIÓN DEL VUELO",
                azulOscuro,
                blanco,
                borde
        );

javax.swing.JPanel datosVuelo =
        new javax.swing.JPanel(
                new java.awt.GridLayout(2, 1, 0, 8)
        );

datosVuelo.setBackground(blanco);

agregarDato(
        datosVuelo,
        "Vuelo",
        lblVuelo,
        gris
);

agregarDato(
        datosVuelo,
        "Avión",
        lblAvion,
        gris
);

panelVuelo.add(
        datosVuelo,
        java.awt.BorderLayout.CENTER
);

contenido.add(panelVuelo);

contenido.add(
        javax.swing.Box.createVerticalStrut(15)
);

// =========================================================
// SECCIÓN 2 - INFORMACIÓN DEL PASAJERO
// =========================================================
javax.swing.JPanel panelPasajero =
        crearSeccion(
                "👤  INFORMACIÓN DEL PASAJERO",
                azulOscuro,
                blanco,
                borde
        );

javax.swing.JPanel datosPasajero =
        new javax.swing.JPanel(
                new java.awt.GridLayout(4, 1, 0, 8)
        );

datosPasajero.setBackground(blanco);

agregarDato(
        datosPasajero,
        "Pasajero",
        lblPasajero,
        gris
);

agregarDato(
        datosPasajero,
        "Asiento",
        lblAsiento,
        gris
);

agregarDato(
        datosPasajero,
        "Equipaje",
        lblEquipaje,
        gris
);

agregarDato(
        datosPasajero,
        "Peso",
        lblPeso,
        gris
);

panelPasajero.add(
        datosPasajero,
        java.awt.BorderLayout.CENTER
);

contenido.add(panelPasajero);

contenido.add(
        javax.swing.Box.createVerticalStrut(15)
);

// =========================================================
// SECCIÓN 3 - RESUMEN DE PAGO
// =========================================================
javax.swing.JPanel panelPago =
        crearSeccion(
                "💰  RESUMEN DE PAGO",
                azulOscuro,
                blanco,
                borde
        );

javax.swing.JPanel datosPago =
        new javax.swing.JPanel(
                new java.awt.GridLayout(3, 1, 0, 8)
        );

datosPago.setBackground(blanco);

agregarDato(
        datosPago,
        "Precio del asiento",
        lblPrecioAsiento,
        gris
);

agregarDato(
        datosPago,
        "Precio del equipaje",
        lblPrecioEquipaje,
        gris
);

// TOTAL
javax.swing.JPanel filaTotal =
        new javax.swing.JPanel(
                new java.awt.BorderLayout()
        );

filaTotal.setBackground(blanco);
filaTotal.setBorder(
        javax.swing.BorderFactory.createMatteBorder(
                1, 0, 0, 0,
                borde
        )
);

javax.swing.JLabel tituloTotal =
        new javax.swing.JLabel("TOTAL");

tituloTotal.setFont(
        new java.awt.Font(
                "Arial",
                java.awt.Font.BOLD,
                16
        )
);

tituloTotal.setForeground(azulOscuro);

lblTotal.setFont(
        new java.awt.Font(
                "Arial",
                java.awt.Font.BOLD,
                18
        )
);

lblTotal.setForeground(azul);

filaTotal.add(
        tituloTotal,
        java.awt.BorderLayout.WEST
);

filaTotal.add(
        lblTotal,
        java.awt.BorderLayout.EAST
);

datosPago.add(filaTotal);

panelPago.add(
        datosPago,
        java.awt.BorderLayout.CENTER
);

contenido.add(panelPago);

contenido.add(
        javax.swing.Box.createVerticalStrut(15)
);

// =========================================================
// SECCIÓN 4 - MÉTODO DE PAGO
// =========================================================
javax.swing.JPanel panelMetodo =
        crearSeccion(
                "💳  MÉTODO DE PAGO",
                azulOscuro,
                blanco,
                borde
        );

javax.swing.JPanel contenidoPago =
        new javax.swing.JPanel(
                new java.awt.BorderLayout(
                        15, 0
                )
        );

contenidoPago.setBackground(blanco);

jLabel9.setText("Seleccione el método de pago:");
jLabel9.setForeground(gris);
jLabel9.setFont(
        new java.awt.Font(
                "Arial",
                java.awt.Font.PLAIN,
                14
        )
);

cmbMetodoPago.setFont(
        new java.awt.Font(
                "Arial",
                java.awt.Font.PLAIN,
                14
        )
);

cmbMetodoPago.setPreferredSize(
        new java.awt.Dimension(250, 40)
);

contenidoPago.add(
        jLabel9,
        java.awt.BorderLayout.WEST
);

contenidoPago.add(
        cmbMetodoPago,
        java.awt.BorderLayout.CENTER
);

panelMetodo.add(
        contenidoPago,
        java.awt.BorderLayout.CENTER
);

contenido.add(panelMetodo);

contenido.add(
        javax.swing.Box.createVerticalStrut(20)
);

// =========================================================
// BOTÓN CONFIRMAR
// =========================================================
btnConfirmar.setText("CONFIRMAR RESERVA");
btnConfirmar.setFont(
        new java.awt.Font(
                "Arial",
                java.awt.Font.BOLD,
                15
        )
);

btnConfirmar.setForeground(blanco);
btnConfirmar.setBackground(azul);
btnConfirmar.setFocusPainted(false);
btnConfirmar.setBorderPainted(false);
btnConfirmar.setCursor(
        new java.awt.Cursor(
                java.awt.Cursor.HAND_CURSOR
        )
);

btnConfirmar.setPreferredSize(
        new java.awt.Dimension(250, 48)
);

btnConfirmar.setMaximumSize(
        new java.awt.Dimension(
                Integer.MAX_VALUE,
                48
        )
);

javax.swing.JPanel panelBoton =
        new javax.swing.JPanel(
                new java.awt.FlowLayout(
                        java.awt.FlowLayout.RIGHT,
                        0,
                        0
                )
        );

panelBoton.setBackground(fondo);
panelBoton.setAlignmentX(
        javax.swing.JComponent.LEFT_ALIGNMENT
);

panelBoton.add(btnConfirmar);

contenido.add(panelBoton);

// =========================================================
// SCROLL PARA PANTALLAS PEQUEÑAS
// =========================================================
javax.swing.JScrollPane scroll =
        new javax.swing.JScrollPane(
                contenido
        );

scroll.setBorder(null);
scroll.setHorizontalScrollBarPolicy(
        javax.swing.ScrollPaneConstants
                .HORIZONTAL_SCROLLBAR_NEVER
);

scroll.getVerticalScrollBar().setUnitIncrement(16);

add(
        scroll,
        java.awt.BorderLayout.CENTER
);

revalidate();
repaint();


}

// =============================================================
// CREA UNA SECCIÓN COMPLETA
// =============================================================
private javax.swing.JPanel crearSeccion(
String titulo,
java.awt.Color azulOscuro,
java.awt.Color blanco,
java.awt.Color borde) {


javax.swing.JPanel panel =
        new javax.swing.JPanel(
                new java.awt.BorderLayout()
        );

panel.setBackground(blanco);

panel.setBorder(
        javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(
                        borde,
                        1
                ),
                javax.swing.BorderFactory.createEmptyBorder(
                        0, 20, 18, 20
                )
        )
);

javax.swing.JPanel encabezado =
        new javax.swing.JPanel(
                new java.awt.BorderLayout()
        );

encabezado.setBackground(blanco);

encabezado.setBorder(
        javax.swing.BorderFactory.createEmptyBorder(
                15, 0, 12, 0
        )
);

javax.swing.JLabel labelTitulo =
        new javax.swing.JLabel(titulo);

labelTitulo.setFont(
        new java.awt.Font(
                "Arial",
                java.awt.Font.BOLD,
                15
        )
);

labelTitulo.setForeground(azulOscuro);

encabezado.add(
        labelTitulo,
        java.awt.BorderLayout.WEST
);

panel.add(
        encabezado,
        java.awt.BorderLayout.NORTH
);

return panel;


}

// =============================================================
// CREA UNA FILA DE DATO ALINEADA
// =============================================================
private void agregarDato(
javax.swing.JPanel panel,
String titulo,
javax.swing.JLabel valor,
java.awt.Color gris) {


javax.swing.JPanel fila =
        new javax.swing.JPanel(
                new java.awt.BorderLayout()
        );

fila.setBackground(
        java.awt.Color.WHITE
);

javax.swing.JLabel label =
        new javax.swing.JLabel(
                titulo
        );

label.setFont(
        new java.awt.Font(
                "Arial",
                java.awt.Font.BOLD,
                14
        )
);

label.setForeground(gris);

valor.setFont(
        new java.awt.Font(
                "Arial",
                java.awt.Font.PLAIN,
                14
        )
);

valor.setHorizontalAlignment(
        javax.swing.SwingConstants.RIGHT
);

fila.add(
        label,
        java.awt.BorderLayout.WEST
);

fila.add(
        valor,
        java.awt.BorderLayout.EAST
);

panel.add(fila);


}

    
    
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cmbMetodoPago = new javax.swing.JComboBox<>();
        btnConfirmar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblVuelo = new javax.swing.JLabel();
        lblAvion = new javax.swing.JLabel();
        lblPasajero = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        lblAsiento = new javax.swing.JLabel();
        lblEquipaje = new javax.swing.JLabel();
        lblPeso = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        lblPrecioAsiento = new javax.swing.JLabel();
        lblPrecioEquipaje = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        jPanel2.setBackground(new java.awt.Color(23, 42, 58));

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("CONFIRMAR RESERVA");

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText(" Revisa los datos de tu vuelo antes de confirmar");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        jLabel9.setText("Metodo de pago");

        cmbMetodoPago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tarjeta de credito", "Tarjeta debito", "PSE", "Nequi", "Daviplata", " " }));

        btnConfirmar.setText("CONFIRMAR RESERVA");

        jLabel1.setText("Informacion del vuelo");

        lblVuelo.setText("Vuelo");

        lblAvion.setText("Avion");

        lblPasajero.setText("Pasajero");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblVuelo)
                    .addComponent(lblAvion)
                    .addComponent(lblPasajero))
                .addContainerGap(146, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblVuelo)
                .addGap(18, 18, 18)
                .addComponent(lblAvion)
                .addGap(18, 18, 18)
                .addComponent(lblPasajero)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        lblAsiento.setText("Asiento");

        lblEquipaje.setText("Equipaje");

        lblPeso.setText("Peso");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAsiento)
                    .addComponent(lblEquipaje)
                    .addComponent(lblPeso))
                .addContainerGap(125, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAsiento)
                .addGap(18, 18, 18)
                .addComponent(lblEquipaje)
                .addGap(18, 18, 18)
                .addComponent(lblPeso)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jLabel4.setText("Seleccion");

        lblPrecioAsiento.setText("Precio asiento");

        lblPrecioEquipaje.setText("Precio equipaje");

        lblTotal.setText("Total");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPrecioEquipaje)
                    .addComponent(lblPrecioAsiento)
                    .addComponent(lblTotal))
                .addContainerGap(121, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblPrecioAsiento)
                .addGap(18, 18, 18)
                .addComponent(lblPrecioEquipaje)
                .addGap(18, 18, 18)
                .addComponent(lblTotal)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jLabel5.setText("Resumen de pago");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(180, 180, 180)
                        .addComponent(btnConfirmar))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel9)
                        .addGap(39, 39, 39)
                        .addComponent(cmbMetodoPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(196, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(cmbMetodoPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addComponent(btnConfirmar)
                .addGap(15, 15, 15))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnConfirmar;
    public javax.swing.JComboBox<String> cmbMetodoPago;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    public volatile javax.swing.JLabel lblAsiento;
    public volatile javax.swing.JLabel lblAvion;
    public volatile javax.swing.JLabel lblEquipaje;
    public volatile javax.swing.JLabel lblPasajero;
    public volatile javax.swing.JLabel lblPeso;
    public javax.swing.JLabel lblPrecioAsiento;
    public javax.swing.JLabel lblPrecioEquipaje;
    public javax.swing.JLabel lblTotal;
    public volatile javax.swing.JLabel lblVuelo;
    // End of variables declaration//GEN-END:variables
}
