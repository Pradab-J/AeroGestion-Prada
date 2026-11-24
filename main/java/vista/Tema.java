package vista;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 * Tema visual central de AEROGESTION PRADA.
 *
 * Uso rapido:
 *   1) Tema.instalar();            // una sola vez, al inicio del main
 *   2) Tema.aplicar(unPanel);      // estiliza todo el panel automaticamente
 *
 * No cambia nombres de componentes ni logica: solo colores, fuentes y bordes.
 * Funciona con o sin FlatLaf (si el .jar no esta, usa Nimbus/Metal).
 */
public final class Tema {

    /** Constructor: crea los componentes del panel y aplica el diseño. */
    private Tema() {
    }

    // ---------------------------------------------------------------
    // PALETA (la misma que ya usa el proyecto, ampliada con estados)
    // ---------------------------------------------------------------
    public static final Color NAVY       = new Color(23, 42, 58);     // #172A3A
    public static final Color NAVY_CLARO = new Color(36, 61, 82);     // hover del menu
    public static final Color AZUL       = new Color(29, 140, 248);   // #1D8CF8
    public static final Color AZUL_OSC   = new Color(15, 100, 190);   // presionado
    public static final Color AZUL_SUAVE = new Color(228, 241, 254);  // seleccion de tabla
    public static final Color FONDO      = new Color(244, 247, 250);  // #F4F7FA
    public static final Color BLANCO     = Color.WHITE;
    public static final Color TEXTO      = new Color(38, 50, 56);
    public static final Color TEXTO_SUAVE = new Color(96, 125, 139);
    public static final Color BORDE      = new Color(220, 230, 240);
    public static final Color BORDE_CAMPO = new Color(190, 203, 218);
    public static final Color VERDE      = new Color(46, 158, 107);
    public static final Color ROJO       = new Color(214, 69, 69);
    public static final Color NARANJA    = new Color(240, 160, 50);

    // ---------------------------------------------------------------
    // FUENTES
    // ---------------------------------------------------------------
    private static final String FAMILIA = elegirFamilia();

    private static String elegirFamilia() {
        String[] candidatas = {"Segoe UI", "Inter", "Roboto", "Helvetica Neue", "SansSerif"};
        java.util.Set<String> disponibles = new java.util.HashSet<>(java.util.Arrays.asList(
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
        for (String f : candidatas) {
            if (disponibles.contains(f)) {
                return f;
            }
        }
        return Font.SANS_SERIF;
    }

    public static Font fuente(int estilo, int tam) {
        return new Font(FAMILIA, estilo, tam);
    }

    public static final Font NORMAL = fuente(Font.PLAIN, 14);
    public static final Font NEGRITA = fuente(Font.BOLD, 14);
    public static final Font TITULO = fuente(Font.BOLD, 26);

    // Marca para no tocar componentes que ya tienen estilo propio
    private static final String MARCA = "tema.aplicado";
    private static final String OMITIR = "tema.omitir";

    // ---------------------------------------------------------------
    // INSTALACION GLOBAL
    // ---------------------------------------------------------------
    public static void instalar() {
        boolean ok = false;
        try {
            // FlatLaf: se agrega como libreria .jar en NetBeans
            Class<?> c = Class.forName("com.formdev.flatlaf.FlatLightLaf");
            // Se configura ANTES de instalarlo
            UIManager.put("Component.arc", 10);
            UIManager.put("Button.arc", 10);
            UIManager.put("TextComponent.arc", 10);
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ScrollBar.width", 10);
            UIManager.put("ScrollBar.thumb", new Color(190, 205, 220));
            UIManager.put("ScrollBar.track", FONDO);
            UIManager.put("Component.focusColor", AZUL);
            UIManager.put("Component.accentColor", AZUL);
            UIManager.put("TableHeader.separatorColor", NAVY);
            UIManager.put("Table.showHorizontalLines", Boolean.TRUE);
            UIManager.put("Table.showVerticalLines", Boolean.FALSE);
            UIManager.setLookAndFeel((LookAndFeel) c.getDeclaredConstructor().newInstance());
            ok = true;
        } catch (Throwable ex) {
            // sin FlatLaf: se intenta con Nimbus
        }
        if (!ok) {
            try {
                for (UIManager.LookAndFeelInfo i : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(i.getName())) {
                        UIManager.setLookAndFeel(i.getClassName());
                        break;
                    }
                }
            } catch (Exception ignore) {
            }
        }
        UIManager.put("defaultFont", NORMAL);
        UIManager.put("OptionPane.messageFont", NORMAL);
        UIManager.put("OptionPane.buttonFont", NEGRITA);
        UIManager.put("ToolTip.font", NORMAL);
    }

    // ---------------------------------------------------------------
    // APLICAR A UN PANEL COMPLETO (recorre todos los hijos)
    // ---------------------------------------------------------------
    public static void aplicar(Container raiz) {
        if (raiz instanceof JPanel) {
            Color bg = raiz.getBackground();
            if (bg == null || (!bg.equals(NAVY) && !bg.equals(BLANCO))) {
                raiz.setBackground(FONDO);
            }
        }
        recorrer(raiz);
    }

    private static void recorrer(Container raiz) {
        for (Component c : raiz.getComponents()) {
            if (c instanceof JScrollPane) {
                JScrollPane sp = (JScrollPane) c;
                Component vista = sp.getViewport().getView();
                if (vista instanceof JTable) {
                    tabla((JTable) vista, sp);
                } else {
                    if (sp.getBorder() instanceof javax.swing.plaf.UIResource) {
                        sp.setBorder(BorderFactory.createLineBorder(BORDE));
                    }
                    if (sp.getViewport().getBackground() instanceof javax.swing.plaf.UIResource) {
                        sp.getViewport().setBackground(BLANCO);
                    }
                }
                if (vista instanceof Container && !(vista instanceof JTable)) {
                    recorrer((Container) vista);
                }
                continue;
            }
            if (c instanceof JComponent
                    && Boolean.TRUE.equals(((JComponent) c).getClientProperty(OMITIR))) {
                continue;
            }
            if (c instanceof JTextField) {
                campo((JTextField) c);
            } else if (c instanceof JComboBox) {
                combo((JComboBox<?>) c);
                continue; // no entrar: contiene su propio boton de flecha
            } else if (c instanceof JButton) {
                botonAuto((JButton) c);
            } else if (c instanceof JLabel) {
                etiqueta((JLabel) c);
            } else if (c instanceof JPanel) {
                panelFondo((JPanel) c);
            }
            if (c instanceof Container) {
                recorrer((Container) c);
            }
        }
    }

    // ---------------------------------------------------------------
    // PANELES Y ETIQUETAS
    // ---------------------------------------------------------------
    private static void panelFondo(JPanel p) {
        // Solo normaliza los tonos "raros" de la paleta vieja; respeta navy/azul/blanco
        Color bg = p.getBackground();
        if (bg == null) {
            return;
        }
        int r = bg.getRed(), g = bg.getGreen(), b = bg.getBlue();
        boolean colorAjeno = (r == 204 && g == 255 && b == 255)
                || (r == 204 && g == 204 && b == 255)
                || (r == 204 && g == 204 && b == 204)
                || (r == 0 && g == 0 && b == 255);
        if (colorAjeno) {
            p.setBackground(FONDO);
        }
    }

    private static void etiqueta(JLabel l) {
        // No tocar etiquetas con imagen
        if (l.getIcon() != null) {
            return;
        }
        Font f = l.getFont();
        int tam = f == null ? 14 : f.getSize();
        int est = f == null ? Font.PLAIN : f.getStyle();
        Color fg = l.getForeground();
        boolean blanca = fg != null && fg.equals(BLANCO);
        boolean negroPorDefecto = fg == null || fg.equals(Color.BLACK)
                || fg.equals(UIManager.getColor("Label.foreground"));
        String txt = l.getText() == null ? "" : l.getText().trim();

        if (blanca) {
            // Texto blanco sobre fondo oscuro = encabezado de pagina
            boolean esTitulo = !txt.isEmpty() && txt.equals(txt.toUpperCase()) && tam < 20;
            if (esTitulo) {
                l.setFont(fuente(Font.BOLD, 22));
            } else if (tam < 20) {
                l.setFont(fuente(Font.PLAIN, 14));
                l.setForeground(new Color(200, 214, 229));
            }
            return;
        }
        if (negroPorDefecto) {
            l.setForeground(TEXTO);
        }
        if (tam < 14) {
            l.setFont(fuente(est, 14));
        } else if (f != null && ("Dialog".equals(f.getFamily()) || "Tahoma".equals(f.getFamily()))) {
            l.setFont(fuente(est, tam));
        }
    }

    // ---------------------------------------------------------------
    // CAMPOS DE TEXTO Y COMBOS
    // ---------------------------------------------------------------
    public static void campo(final JTextField t) {
        t.setFont(NORMAL);
        t.setForeground(TEXTO);
        t.setBackground(BLANCO);
        t.setCaretColor(AZUL);
        t.setSelectionColor(AZUL_SUAVE);
        t.setSelectedTextColor(TEXTO);
        t.setBorder(bordeCampo(BORDE_CAMPO));
        if (t.getClientProperty(MARCA) == null) {
            t.putClientProperty(MARCA, Boolean.TRUE);
            t.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    t.setBorder(bordeCampo(AZUL));
                }

                @Override
                public void focusLost(FocusEvent e) {
                    t.setBorder(bordeCampo(BORDE_CAMPO));
                }
            });
        }
    }

    private static Border bordeCampo(Color c) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(c, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8));
    }

    public static void combo(JComboBox<?> c) {
        c.setFont(NORMAL);
        c.setForeground(TEXTO);
        c.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /** Tipos de botón disponibles (principal, secundario, peligro...). */
    // ---------------------------------------------------------------
    // BOTONES
    // ---------------------------------------------------------------
    public enum Tipo {
        PRIMARIO, SECUNDARIO, PELIGRO, EXITO
    }

    /** Elige el estilo segun el texto del boton (GUARDAR, ELIMINAR, etc.). */
    private static void botonAuto(JButton b) {
        if (b instanceof MapaAvion.Silla || Boolean.TRUE.equals(b.getClientProperty("tema.menu"))) {
            return;
        }
        // Botones solo con icono (ej: el calendario de JDateChooser): no tocar
        if (b.getIcon() != null && (b.getText() == null || b.getText().trim().isEmpty())) {
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            return;
        }
        // Botones del menu lateral (alineados a la izquierda y con UI propia): no tocar
        if (b.getUI() != null && b.getUI().getClass().isAnonymousClass()
                && b.getHorizontalAlignment() == SwingConstants.LEFT) {
            return;
        }
        String t = b.getText() == null ? "" : b.getText().trim().toUpperCase();
        Tipo tipo;
        if (t.contains("ELIMINAR") || t.contains("BORRAR") || t.contains("CANCELAR")) {
            tipo = Tipo.PELIGRO;
        } else if (t.contains("LIMPIAR") || t.contains("VOLVER") || t.contains("EDITAR")
                || t.contains("ATRAS") || t.contains("ATRÁS") || t.contains("NUEVO")) {
            tipo = Tipo.SECUNDARIO;
        } else if (t.contains("CONFIRMAR") || t.contains("PAGAR")) {
            tipo = Tipo.EXITO;
        } else {
            tipo = Tipo.PRIMARIO;
        }
        boton(b, tipo);
    }

    public static void boton(JButton b, Tipo tipo) {
        b.putClientProperty("tema.tipo", tipo);
        b.setUI(new BotonUI());
        b.setFont(NEGRITA);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setRolloverEnabled(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(3, 16, 3, 16));
        b.setMargin(new Insets(3, 16, 3, 16));
        b.setMinimumSize(new Dimension(80, 30));
    }

    /** Pintado redondeado, igual en cualquier Look and Feel. */
    private static final class BotonUI extends BasicButtonUI {

        @Override
        public void paint(Graphics g, JComponent c) {
            JButton b = (JButton) c;
            Tipo tipo = (Tipo) b.getClientProperty("tema.tipo");
            if (tipo == null) {
                tipo = Tipo.PRIMARIO;
            }
            boolean sobre = b.getModel().isRollover();
            boolean presionado = b.getModel().isPressed();
            boolean activo = b.isEnabled();

            Color fondo, texto, borde = null;
            switch (tipo) {
                case SECUNDARIO:
                    fondo = presionado ? AZUL_SUAVE : (sobre ? AZUL_SUAVE : BLANCO);
                    texto = AZUL;
                    borde = AZUL;
                    break;
                case PELIGRO:
                    fondo = presionado ? new Color(170, 45, 45) : (sobre ? new Color(190, 55, 55) : ROJO);
                    texto = BLANCO;
                    break;
                case EXITO:
                    fondo = presionado ? new Color(30, 120, 80) : (sobre ? new Color(38, 135, 90) : VERDE);
                    texto = BLANCO;
                    break;
                default:
                    fondo = presionado ? AZUL_OSC : (sobre ? new Color(60, 155, 250) : AZUL);
                    texto = BLANCO;
            }
            if (!activo) {
                fondo = new Color(225, 230, 235);
                texto = new Color(150, 160, 170);
                borde = null;
            }

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setColor(fondo);
            g2.fill(new RoundRectangle2D.Float(0, 0, c.getWidth(), c.getHeight(), 12, 12));
            if (borde != null) {
                g2.setColor(borde);
                g2.setStroke(new BasicStroke(1.4f));
                g2.draw(new RoundRectangle2D.Float(0.7f, 0.7f, c.getWidth() - 1.4f, c.getHeight() - 1.4f, 12, 12));
            }
            g2.dispose();
            b.setForeground(texto);
            super.paint(g, c);
        }

        @Override
        public Dimension getPreferredSize(JComponent c) {
            Dimension d = super.getPreferredSize(c);
            return new Dimension(d.width + 4, Math.max(d.height, 36));
        }
    }

    // ---------------------------------------------------------------
    // BOTON DEL MENU LATERAL (administrador)
    // ---------------------------------------------------------------
    public static void botonMenu(JButton b) {
        b.putClientProperty("tema.menu", Boolean.TRUE);
        b.setUI(new MenuUI());
        b.setForeground(new Color(200, 214, 229));
        b.setFont(fuente(Font.PLAIN, 15));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setRolloverEnabled(true);
        b.setBorder(BorderFactory.createEmptyBorder(0, 22, 0, 10));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private static final class MenuUI extends BasicButtonUI {

        @Override
        public void paint(Graphics g, JComponent c) {
            JButton b = (JButton) c;
            boolean activo = Boolean.TRUE.equals(b.getClientProperty("activo"));
            boolean sobre = b.getModel().isRollover();
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (activo) {
                g2.setColor(new Color(29, 140, 248, 45));
                g2.fill(new RoundRectangle2D.Float(8, 2, c.getWidth() - 16, c.getHeight() - 4, 12, 12));
                g2.setColor(AZUL);
                g2.fill(new RoundRectangle2D.Float(8, 10, 4, c.getHeight() - 20, 4, 4));
                b.setForeground(BLANCO);
                b.setFont(fuente(Font.BOLD, 15));
            } else {
                if (sobre) {
                    g2.setColor(NAVY_CLARO);
                    g2.fill(new RoundRectangle2D.Float(8, 2, c.getWidth() - 16, c.getHeight() - 4, 12, 12));
                }
                b.setForeground(sobre ? BLANCO : new Color(200, 214, 229));
                b.setFont(fuente(Font.PLAIN, 15));
            }
            g2.dispose();
            super.paint(g, c);
        }
    }

    // ---------------------------------------------------------------
    // TABLAS
    // ---------------------------------------------------------------
    public static void tabla(final JTable t, JScrollPane sp) {
        t.setRowHeight(36);
        t.setFont(NORMAL);
        t.setForeground(TEXTO);
        t.setBackground(BLANCO);
        t.setGridColor(BORDE);
        t.setShowVerticalLines(false);
        t.setShowHorizontalLines(true);
        t.setIntercellSpacing(new Dimension(0, 1));
        t.setSelectionBackground(AZUL_SUAVE);
        t.setSelectionForeground(NAVY);
        t.setFillsViewportHeight(true);

        DefaultTableCellRenderer celda = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tb, Object v, boolean sel,
                    boolean foco, int fila, int col) {
                super.getTableCellRendererComponent(tb, v, sel, false, fila, col);
                setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                if (sel) {
                    setBackground(AZUL_SUAVE);
                    setForeground(NAVY);
                } else {
                    setBackground(fila % 2 == 0 ? BLANCO : new Color(249, 251, 253));
                    setForeground(TEXTO);
                }
                return this;
            }
        };
        t.setDefaultRenderer(Object.class, celda);
        t.setDefaultRenderer(String.class, celda);
        t.setDefaultRenderer(Number.class, celda);
        t.setDefaultRenderer(Integer.class, celda);
        t.setDefaultRenderer(Double.class, celda);

        JTableHeader h = t.getTableHeader();
        h.setReorderingAllowed(false);
        h.setPreferredSize(new Dimension(h.getPreferredSize().width, 40));
        h.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tb, Object v, boolean sel,
                    boolean foco, int fila, int col) {
                super.getTableCellRendererComponent(tb, v, false, false, fila, col);
                setBackground(NAVY);
                setForeground(BLANCO);
                setFont(NEGRITA);
                setHorizontalAlignment(SwingConstants.LEFT);
                setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                return this;
            }
        });

        if (sp != null) {
            sp.setBorder(BorderFactory.createLineBorder(BORDE));
            sp.getViewport().setBackground(BLANCO);
        }
    }

    // ---------------------------------------------------------------
    // UTILIDADES PARA DISENO MANUAL (pantallas nuevas)
    // ---------------------------------------------------------------
    /** Borde de "tarjeta": linea suave + relleno interno. */
    public static Border bordeTarjeta() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE, 1),
                BorderFactory.createEmptyBorder(16, 18, 16, 18));
    }

    /** Marca un componente para que Tema.aplicar() no lo modifique. */
    public static void omitir(JComponent c) {
        c.putClientProperty(OMITIR, Boolean.TRUE);
    }

    // ---------------------------------------------------------------
    // IMAGENES, ICONOS Y PANEL DE PORTADA
    // ---------------------------------------------------------------
    /** Carga una imagen del classpath (ej: "/images/INICIO.jpeg"). Devuelve null si no existe. */
    public static Image cargarImagen(String ruta) {
        try {
            java.net.URL url = Tema.class.getResource(ruta);
            if (url == null) {
                return null;
            }
            return new ImageIcon(url).getImage();
        } catch (Exception ex) {
            return null;
        }
    }

    /** Icono de avion de papel dibujado por codigo (sin archivos). */
    public static Icon iconoAvion(final int tam, final Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.translate(x, y);
                java.awt.geom.Path2D.Float a = new java.awt.geom.Path2D.Float();
                a.moveTo(tam * 0.06f, tam * 0.46f);
                a.lineTo(tam * 0.95f, tam * 0.06f);
                a.lineTo(tam * 0.63f, tam * 0.94f);
                a.lineTo(tam * 0.46f, tam * 0.58f);
                a.closePath();
                g2.setColor(color);
                g2.fill(a);
                java.awt.geom.Path2D.Float b = new java.awt.geom.Path2D.Float();
                b.moveTo(tam * 0.46f, tam * 0.58f);
                b.lineTo(tam * 0.95f, tam * 0.06f);
                b.lineTo(tam * 0.52f, tam * 0.50f);
                b.closePath();
                g2.setColor(new Color(0, 0, 0, 55));
                g2.fill(b);
                g2.dispose();
            }

            @Override
            public int getIconWidth() {
                return tam;
            }

            @Override
            public int getIconHeight() {
                return tam;
            }
        };
    }

    /** Panel que pinta una imagen a "cubrir" todo el espacio, con degradado azul oscuro encima. */
    public static class PanelImagen extends JPanel {

        private final Image img;

        public PanelImagen(Image img) {
            this.img = img;
            setBackground(NAVY);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            int w = getWidth(), h = getHeight();
            if (img != null && img.getWidth(null) > 0 && img.getHeight(null) > 0) {
                double iw = img.getWidth(null), ih = img.getHeight(null);
                double esc = Math.max(w / iw, h / ih);
                int dw = (int) Math.ceil(iw * esc), dh = (int) Math.ceil(ih * esc);
                g2.drawImage(img, (w - dw) / 2, (h - dh) / 2, dw, dh, null);
            }
            g2.setPaint(new GradientPaint(0, 0, new Color(23, 42, 58, 150),
                    0, h, new Color(23, 42, 58, 240)));
            g2.fillRect(0, 0, w, h);
            g2.dispose();
        }
    }

    /** Etiqueta de texto con ancho fijo que ajusta lineas (usa HTML simple). */
    public static JLabel parrafo(String texto, int ancho, Font f, Color c) {
        JLabel l = new JLabel("<html><div style='width:" + ancho + "px'>" + texto + "</div></html>");
        l.setFont(f);
        l.setForeground(c);
        return l;
    }

    // ===============================================================
    // PLANTILLA DE PAGINAS (cabecera + formulario en cuadricula + tabla)
    // ===============================================================

    /** Cabecera azul oscuro con titulo, subtitulo y (opcional) un componente a la derecha. */
    public static void cabecera(JPanel cab, JLabel titulo, String textoTitulo,
            JLabel subtitulo, String textoSubtitulo, JComponent derecha) {
        cab.removeAll();
        cab.setLayout(new BorderLayout());
        cab.setBackground(NAVY);
        cab.setOpaque(true);
        cab.setBorder(BorderFactory.createEmptyBorder(18, 26, 18, 26));
        cab.setPreferredSize(null);
        titulo.setText(textoTitulo);
        titulo.setFont(fuente(Font.BOLD, 24));
        titulo.setForeground(BLANCO);
        titulo.setPreferredSize(null);
        subtitulo.setText(textoSubtitulo);
        subtitulo.setFont(fuente(Font.PLAIN, 14));
        subtitulo.setForeground(new Color(200, 214, 229));
        subtitulo.setPreferredSize(null);
        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        textos.add(titulo);
        textos.add(Box.createVerticalStrut(4));
        textos.add(subtitulo);
        cab.add(textos, BorderLayout.WEST);
        if (derecha != null) {
            cab.add(derecha, BorderLayout.EAST);
        }
    }

    /** Panel blanco con borde suave (tarjeta). */
    public static JPanel tarjeta() {
        JPanel t = new JPanel(new BorderLayout());
        t.setBackground(BLANCO);
        t.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE, 1),
                BorderFactory.createEmptyBorder(18, 20, 18, 20)));
        return t;
    }

    /** Etiqueta encima del campo; el campo ocupa todo el ancho de su celda. */
    public static JPanel celda(JLabel etiqueta, String texto, JComponent campo) {
        etiqueta.setText(texto);
        etiqueta.setFont(fuente(Font.BOLD, 13));
        etiqueta.setForeground(TEXTO_SUAVE);
        etiqueta.setPreferredSize(null);
        Dimension d = campo.getPreferredSize();
        campo.setPreferredSize(new Dimension(Math.max(d.width, 140), 40));
        campo.setMinimumSize(new Dimension(60, 40));
        campo.setMaximumSize(null);
        JPanel c = new JPanel(new BorderLayout(0, 6));
        c.setOpaque(false);
        c.add(etiqueta, BorderLayout.NORTH);
        c.add(campo, BorderLayout.CENTER);
        return c;
    }

    /** Cuadricula de celdas. Una entrada null deja el hueco vacio. */
    public static JPanel cuadricula(int columnas, List<JComponent> celdas) {
        JPanel g = new JPanel(new GridBagLayout());
        g.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.NORTHWEST;
        gc.weightx = 1;
        for (int i = 0; i < celdas.size(); i++) {
            gc.gridx = i % columnas;
            gc.gridy = i / columnas;
            gc.insets = new Insets(0, gc.gridx == 0 ? 0 : 12, 14, gc.gridx == columnas - 1 ? 0 : 12);
            JComponent c = celdas.get(i);
            g.add(c == null ? Box.createGlue() : c, gc);
        }
        return g;
    }

    public static JPanel filaBotones(JButton... botones) {
        JPanel f = new JPanel();
        f.setOpaque(false);
        f.setLayout(new BoxLayout(f, BoxLayout.X_AXIS));
        boolean primero = true;
        for (JButton b : botones) {
            if (!primero) {
                f.add(Box.createHorizontalStrut(10));
            }
            primero = false;
            // ancho segun el texto (evita que "Guardar cambios" quede cortado)
            int ancho = b.getFontMetrics(NEGRITA).stringWidth(b.getText() == null ? "" : b.getText()) + 56;
            b.setPreferredSize(new Dimension(Math.max(ancho, 118), 40));
            f.add(b);
        }
        return f;
    }

    /**
     * Arma una pagina de gestion completa dentro de "raiz":
     * cabecera, tarjeta con formulario + botones y tarjeta con la tabla.
     *
     * @param campos filas {JLabel, String texto, JComponent} (o null = hueco)
     */
    public static void paginaCrud(JPanel raiz,
            JPanel cab, JLabel titulo, String textoTitulo, JLabel subtitulo, String textoSubtitulo,
            JLabel idEtiqueta, JTextField idCampo,
            int columnas, Object[][] campos, JButton[] botones,
            JLabel tituloTabla, String textoTabla, JScrollPane tabla) {

        // Id a la derecha de la cabecera
        JComponent derecha = null;
        if (idCampo != null && idEtiqueta != null) {
            JPanel id = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            id.setOpaque(false);
            idEtiqueta.setFont(fuente(Font.BOLD, 13));
            idEtiqueta.setForeground(new Color(200, 214, 229));
            idEtiqueta.setPreferredSize(null);
            idCampo.setPreferredSize(new Dimension(96, 36));
            id.add(idEtiqueta);
            id.add(idCampo);
            derecha = id;
        }
        cabecera(cab, titulo, textoTitulo, subtitulo, textoSubtitulo, derecha);

        // formulario
        List<JComponent> celdas = new ArrayList<>();
        for (Object[] f : campos) {
            celdas.add(f == null ? null : celda((JLabel) f[0], (String) f[1], (JComponent) f[2]));
        }
        JPanel tarjetaForm = tarjeta();
        tarjetaForm.add(cuadricula(columnas, celdas), BorderLayout.CENTER);
        if (botones != null && botones.length > 0) {
            JPanel pie = new JPanel(new BorderLayout());
            pie.setOpaque(false);
            pie.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
            pie.add(filaBotones(botones), BorderLayout.WEST);
            tarjetaForm.add(pie, BorderLayout.SOUTH);
        }

        JPanel centro = new JPanel(new BorderLayout(0, 18));
        centro.setOpaque(false);
        if (tabla != null) {
            centro.add(tarjetaForm, BorderLayout.NORTH);
            tituloTabla.setText(textoTabla);
            tituloTabla.setFont(fuente(Font.BOLD, 16));
            tituloTabla.setForeground(NAVY);
            tituloTabla.setPreferredSize(null);
            tituloTabla.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            tabla.setPreferredSize(new Dimension(400, 150));
            tabla.setMinimumSize(new Dimension(200, 110));
            JPanel tarjetaTabla = tarjeta();
            tarjetaTabla.add(tituloTabla, BorderLayout.NORTH);
            tarjetaTabla.add(tabla, BorderLayout.CENTER);
            centro.add(tarjetaTabla, BorderLayout.CENTER);
        } else {
            // sin tabla: tarjeta centrada y de ancho moderado
            JPanel marco = new JPanel(new GridBagLayout());
            marco.setOpaque(false);
            tarjetaForm.setPreferredSize(new Dimension(760, tarjetaForm.getPreferredSize().height));
            marco.add(tarjetaForm);
            centro.add(marco, BorderLayout.CENTER);
        }

        raiz.removeAll();
        raiz.setLayout(new BorderLayout(0, 18));
        raiz.setBackground(FONDO);
        raiz.setPreferredSize(null);
        raiz.add(cab, BorderLayout.NORTH);
        raiz.add(centro, BorderLayout.CENTER);
        aplicar(raiz);
        raiz.revalidate();
    }

    // ===============================================================
    // SCROLL QUE SE AJUSTA A LA VENTANA
    // ===============================================================
    /**
     * Envuelve un panel para que ocupe todo el espacio disponible y solo
     * muestre barras de desplazamiento cuando de verdad no cabe.
     */
    public static JScrollPane scrollAjustable(JComponent contenido) {
        VistaAjustable v = new VistaAjustable();
        v.setLayout(new BorderLayout());
        v.setOpaque(false);
        v.add(contenido, BorderLayout.CENTER);
        JScrollPane sp = new JScrollPane(v,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.setBorder(null);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.getVerticalScrollBar().setUnitIncrement(18);
        sp.getHorizontalScrollBar().setUnitIncrement(18);
        return sp;
    }

    private static final class VistaAjustable extends JPanel implements Scrollable {

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle r, int o, int d) {
            return 18;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle r, int o, int d) {
            return o == SwingConstants.VERTICAL ? r.height - 40 : r.width - 40;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return getParent() instanceof JViewport
                    && ((JViewport) getParent()).getWidth() >= getPreferredSize().width;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return getParent() instanceof JViewport
                    && ((JViewport) getParent()).getHeight() >= getPreferredSize().height;
        }
    }

    // ===============================================================
    // NAVEGACION: icono "volver" y reinicio de formularios
    // ===============================================================
    /** Flecha "<" dibujada por codigo (para botones Volver). */
    public static Icon iconoVolver(final int tam, final Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                java.awt.geom.Path2D.Float p = new java.awt.geom.Path2D.Float();
                p.moveTo(x + tam * 0.70f, y + tam * 0.12f);
                p.lineTo(x + tam * 0.25f, y + tam * 0.50f);
                p.lineTo(x + tam * 0.70f, y + tam * 0.88f);
                g2.draw(p);
                g2.dispose();
            }

            @Override
            public int getIconWidth() {
                return tam;
            }

            @Override
            public int getIconHeight() {
                return tam;
            }
        };
    }

    /** Deja un formulario como nuevo: textos vacios, combos en el primer item y tablas sin filas. */
    public static void limpiar(Container raiz) {
        for (Component c : raiz.getComponents()) {
            if (c instanceof JComboBox) {
                JComboBox<?> cb = (JComboBox<?>) c;
                if (cb.getItemCount() > 0) {
                    cb.setSelectedIndex(0);
                }
                continue;
            }
            if (c instanceof javax.swing.text.JTextComponent) {
                javax.swing.text.JTextComponent tc = (javax.swing.text.JTextComponent) c;
                if (tc.isEditable()) {
                    tc.setText("");
                }
                continue;
            }
            if (c instanceof JTable) {
                JTable tb = (JTable) c;
                if (tb.getModel() instanceof javax.swing.table.DefaultTableModel) {
                    ((javax.swing.table.DefaultTableModel) tb.getModel()).setRowCount(0);
                }
                tb.clearSelection();
                continue;
            }
            if (c instanceof Container) {
                limpiar((Container) c);
            }
        }
    }

    // ===============================================================
    // DATOS FRESCOS: recargar listas cuando la pantalla vuelve a mostrarse
    // ===============================================================
    /**
     * Ejecuta "accion" cada vez que el panel pasa a verse en pantalla.
     * Sirve para recargar los combos (aerolineas, aviones, rutas...) con lo ultimo
     * que se registro en otras pantallas, sin tener que cerrar y abrir el programa.
     */
    public static void alMostrar(final JComponent panel, final Runnable accion) {
        panel.addHierarchyListener(new java.awt.event.HierarchyListener() {
            @Override
            public void hierarchyChanged(java.awt.event.HierarchyEvent e) {
                if ((e.getChangeFlags() & java.awt.event.HierarchyEvent.SHOWING_CHANGED) != 0
                        && panel.isShowing()) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                accion.run();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * Recarga un combo conservando la opcion elegida (si todavia existe).
     * No dispara los ActionListener del combo. Devuelve true si la lista cambio.
     */
    public static boolean recargarCombo(JComboBox<String> combo, List<String> nuevos) {
        List<String> actuales = new ArrayList<>();
        for (int i = 0; i < combo.getItemCount(); i++) {
            actuales.add(combo.getItemAt(i));
        }
        if (actuales.equals(nuevos)) {
            return false;
        }
        Object previo = combo.getSelectedItem();
        java.awt.event.ActionListener[] oyentes = combo.getActionListeners();
        for (java.awt.event.ActionListener l : oyentes) {
            combo.removeActionListener(l);
        }
        try {
            combo.removeAllItems();
            for (String n : nuevos) {
                combo.addItem(n);
            }
            if (previo != null && nuevos.contains(previo.toString())) {
                combo.setSelectedItem(previo.toString());
            } else if (combo.getItemCount() > 0) {
                combo.setSelectedIndex(0);
            }
        } finally {
            for (java.awt.event.ActionListener l : oyentes) {
                combo.addActionListener(l);
            }
        }
        return true;
    }
}
