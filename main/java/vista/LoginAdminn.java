/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import java.awt.Color;

/**
 * VENTANA de inicio de sesión del administrador.
 * 
 * @author Aprendiz
 */
public class LoginAdminn extends javax.swing.JFrame {

    /**
     * Creates new form LoginAdmin
     */
    public LoginAdminn() {
        initComponents();
        estilizarBoton(btnIngresar, new Color(29, 140, 248));
        estilizarBoton(btnVolver, new Color(23, 42, 58));

        disenarPantalla();
        setLocationRelativeTo(null);
    }

    /** Aplica el estilo visual a un botón del login. */
    private void estilizarBoton(
            javax.swing.JButton boton,
            Color color) {
        boton.setForeground(Color.WHITE);
        boton.setFont(
                new java.awt.Font(
                        "Segoe UI",
                        java.awt.Font.BOLD,
                        13
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
                Color fondo = color;
                if (b.getModel().isPressed()) {
                    fondo = new Color(15, 100, 190);
                } else if (b.getModel().isRollover()) {
                    fondo = new Color(29, 140, 248);
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

    /** Aplica el diseño (colores, fuentes, posiciones) a la pantalla de login. */
    // =====================================================================
    // DISENO MODERNO (se aplica despues de initComponents, sin tocarlo)
    // =====================================================================
    private void disenarPantalla() {
        setTitle("AeroGestión Prada - Acceso administrador");
        java.awt.Container raiz = getContentPane();
        raiz.removeAll();
        raiz.setLayout(new java.awt.BorderLayout());

        // ---------- Lado izquierdo: portada ----------
        Tema.PanelImagen portada = new Tema.PanelImagen(Tema.cargarImagen("/images/INICIO.jpeg"));
        portada.setLayout(new java.awt.GridBagLayout());
        portada.setPreferredSize(new java.awt.Dimension(380, 540));
        javax.swing.JPanel marca = new javax.swing.JPanel();
        marca.setOpaque(false);
        marca.setLayout(new javax.swing.BoxLayout(marca, javax.swing.BoxLayout.Y_AXIS));
        javax.swing.JLabel icono = new javax.swing.JLabel(Tema.iconoAvion(48, Color.WHITE));
        javax.swing.JLabel nombre = new javax.swing.JLabel("AeroGestión Prada");
        nombre.setFont(Tema.fuente(java.awt.Font.BOLD, 30));
        nombre.setForeground(Color.WHITE);
        javax.swing.JLabel lema = Tema.parrafo("Panel de control para la gestión de vuelos.", 270,
                Tema.fuente(java.awt.Font.PLAIN, 16), new Color(205, 220, 235));
        icono.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        nombre.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        lema.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        marca.add(icono);
        marca.add(javax.swing.Box.createVerticalStrut(16));
        marca.add(nombre);
        marca.add(javax.swing.Box.createVerticalStrut(8));
        marca.add(lema);
        java.awt.GridBagConstraints gc = new java.awt.GridBagConstraints();
        gc.anchor = java.awt.GridBagConstraints.WEST;
        gc.insets = new java.awt.Insets(0, 44, 0, 24);
        portada.add(marca, gc);

        // ---------- Lado derecho: formulario ----------
        javax.swing.JPanel derecha = new javax.swing.JPanel(new java.awt.GridBagLayout());
        derecha.setBackground(Color.WHITE);
        javax.swing.JPanel caja = new javax.swing.JPanel();
        caja.setOpaque(false);
        caja.setLayout(new javax.swing.BoxLayout(caja, javax.swing.BoxLayout.Y_AXIS));

        jLabel1.setText("Acceso administrador");
        jLabel1.setFont(Tema.fuente(java.awt.Font.BOLD, 28));
        jLabel1.setForeground(Tema.NAVY);
        jLabel1.setPreferredSize(null);
        javax.swing.JLabel sub = Tema.parrafo("Ingrese sus credenciales para continuar.", 340,
                Tema.fuente(java.awt.Font.PLAIN, 15), Tema.TEXTO_SUAVE);

        jLabel2.setText("Usuario");
        jLabel3.setText("Contraseña");
        for (javax.swing.JLabel l : new javax.swing.JLabel[]{jLabel2, jLabel3}) {
            l.setFont(Tema.fuente(java.awt.Font.BOLD, 13));
            l.setForeground(Tema.TEXTO);
            l.setPreferredSize(null);
        }
        Tema.campo(txtUsuario);
        Tema.campo(txtContrasena);
        for (javax.swing.JTextField t : new javax.swing.JTextField[]{txtUsuario, txtContrasena}) {
            t.setFont(Tema.fuente(java.awt.Font.PLAIN, 15));
            t.setPreferredSize(new java.awt.Dimension(340, 44));
            t.setMaximumSize(new java.awt.Dimension(340, 44));
        }

        btnIngresar.setText("Ingresar");
        Tema.boton(btnIngresar, Tema.Tipo.PRIMARIO);
        btnVolver.setText("Volver al inicio");
        Tema.boton(btnVolver, Tema.Tipo.SECUNDARIO);
        for (javax.swing.JButton b : new javax.swing.JButton[]{btnIngresar, btnVolver}) {
            b.setFont(Tema.fuente(java.awt.Font.BOLD, 15));
            b.setPreferredSize(new java.awt.Dimension(340, 48));
            b.setMaximumSize(new java.awt.Dimension(340, 48));
        }

        java.awt.Component[] orden = {jLabel1, sub, null, jLabel2, txtUsuario, null, jLabel3,
            txtContrasena, null, btnIngresar, null, btnVolver};
        int[] espacio = {0, 8, 28, 0, 6, 16, 0, 6, 28, 0, 12, 0};
        for (int i = 0; i < orden.length; i++) {
            if (orden[i] == null) {
                caja.add(javax.swing.Box.createVerticalStrut(espacio[i]));
            } else {
                ((javax.swing.JComponent) orden[i]).setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
                caja.add(orden[i]);
            }
        }
        derecha.add(caja);

        jPanel2.removeAll();
        jPanel2.setLayout(new java.awt.BorderLayout());
        jPanel2.setBackground(Color.WHITE);
        jPanel2.add(portada, java.awt.BorderLayout.WEST);
        jPanel2.add(derecha, java.awt.BorderLayout.CENTER);
        raiz.add(jPanel2, java.awt.BorderLayout.CENTER);

        setSize(860, 540);
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtContrasena = new javax.swing.JPasswordField();
        btnIngresar = new javax.swing.JButton();
        btnVolver = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(244, 247, 250));

        jLabel1.setFont(new java.awt.Font("Bernard MT Condensed", 0, 36)); // NOI18N
        jLabel1.setText("INICIAR SESION");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        jLabel2.setText("Usuario");

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        jLabel3.setText("Contraseña");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtContrasena, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(85, 85, 85)
                            .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(175, 175, 175)
                            .addComponent(jLabel2))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(166, 166, 166)
                            .addComponent(jLabel3))))
                .addContainerGap(99, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(jLabel3)
                .addGap(27, 27, 27)
                .addComponent(txtContrasena, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        btnIngresar.setText("INGRESAR");

        btnVolver.setText("VOLVER");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(28, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(122, 122, 122)
                        .addComponent(btnIngresar)
                        .addGap(54, 54, 54)
                        .addComponent(btnVolver))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(136, 136, 136)
                        .addComponent(jLabel1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jLabel1)
                .addGap(28, 28, 28)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnIngresar)
                    .addComponent(btnVolver))
                .addContainerGap(79, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LoginAdminn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginAdminn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginAdminn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginAdminn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginAdminn().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnIngresar;
    public javax.swing.JButton btnVolver;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    public javax.swing.JPasswordField txtContrasena;
    public javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
