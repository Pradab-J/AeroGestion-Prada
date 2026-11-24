package vista;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Pantalla final de la reserva: un tiquete de vuelo con talon desprendible.
 *
 * Se arma con un objeto Datos (lo llena confirmarReservaControlador) y trae dos
 * botones publicos: btnInicio (volver al inicio) y btnGuardar (guarda el tiquete
 * como imagen PNG; ya viene funcionando).
 */
public class JpnlTiqueteVuelo extends JPanel {

    /** Informacion que se imprime en el tiquete. Todo lo que quede vacio se muestra como "-". */
    public static class Datos {

        public String reserva = "", pasajero = "", documento = "", vuelo = "", avion = "";
        public String origenCiudad = "", origenCodigo = "", origenAeropuerto = "";
        public String destinoCiudad = "", destinoCodigo = "", destinoAeropuerto = "";
        public String fechaSalida = "", horaSalida = "", fechaLlegada = "", horaLlegada = "";
        public String asiento = "", clase = "", equipaje = "", peso = "";
        public String metodoPago = "", fechaEmision = "";
        public double precioAsiento, precioEquipaje, total;
    }

    public final JButton btnInicio = new JButton("Volver al inicio");
    public final JButton btnGuardar = new JButton("Guardar como imagen");
    private final Tiquete tiquete;
    private final String reserva;

    /** Constructor: crea los componentes del panel y aplica el diseño. */
    public JpnlTiqueteVuelo(Datos d) {
        this.reserva = d.reserva;
        setLayout(new BorderLayout(0, 16));
        setBackground(Tema.FONDO);
        setOpaque(true);

        add(armarBanner(), BorderLayout.NORTH);

        tiquete = new Tiquete(d);
        JPanel centro = new JPanel(new GridBagLayout());
        centro.setOpaque(false);
        centro.add(tiquete);
        add(centro, BorderLayout.CENTER);

        add(armarBotones(), BorderLayout.SOUTH);
    }

    /** Construye la franja superior del tiquete. */
    // ---------------------------------------------------------------
    // Banner superior: "Reserva confirmada"
    // ---------------------------------------------------------------
    private JComponent armarBanner() {
        JPanel b = new JPanel();
        b.setOpaque(false);
        b.setLayout(new BoxLayout(b, BoxLayout.Y_AXIS));

        JLabel icono = new JLabel(new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Tema.VERDE);
                g2.fillOval(x, y, 44, 44);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                Path2D.Float p = new Path2D.Float();
                p.moveTo(x + 12, y + 23);
                p.lineTo(x + 20, y + 31);
                p.lineTo(x + 33, y + 14);
                g2.draw(p);
                g2.dispose();
            }

            @Override
            public int getIconWidth() {
                return 44;
            }

            @Override
            public int getIconHeight() {
                return 44;
            }
        });
        JLabel titulo = new JLabel("¡Reserva confirmada!");
        titulo.setFont(Tema.fuente(Font.BOLD, 26));
        titulo.setForeground(Tema.NAVY);
        JLabel sub = new JLabel("Su tiquete fue generado correctamente. Guárdelo o preséntelo al momento de abordar.");
        sub.setFont(Tema.fuente(Font.PLAIN, 14));
        sub.setForeground(Tema.TEXTO_SUAVE);

        icono.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.add(icono);
        b.add(Box.createVerticalStrut(8));
        b.add(titulo);
        b.add(Box.createVerticalStrut(4));
        b.add(sub);
        return b;
    }

    /** Construye los botones (guardar imagen e ir al inicio). */
    private JComponent armarBotones() {
        Tema.boton(btnGuardar, Tema.Tipo.SECUNDARIO);
        Tema.boton(btnInicio, Tema.Tipo.PRIMARIO);
        btnGuardar.setPreferredSize(new Dimension(210, 44));
        btnInicio.setPreferredSize(new Dimension(190, 44));
        btnGuardar.addActionListener(e -> guardarImagen());
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        p.setOpaque(false);
        p.add(btnGuardar);
        p.add(btnInicio);
        return p;
    }

    /** Guarda el tiquete como imagen PNG en el lugar que elija el usuario. */
    // ---------------------------------------------------------------
    // Guardar como imagen
    // ---------------------------------------------------------------
    private void guardarImagen() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Guardar tiquete");
        String base = reserva == null || reserva.isEmpty() ? "tiquete" : "tiquete-" + reserva;
        fc.setSelectedFile(new File(base + ".png"));
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imagen PNG (*.png)", "png"));
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File f = fc.getSelectedFile();
        if (!f.getName().toLowerCase().endsWith(".png")) {
            f = new File(f.getParentFile(), f.getName() + ".png");
        }
        try {
            ImageIO.write(tiquete.imagen(2.0), "png", f);
            JOptionPane.showMessageDialog(this, "Tiquete guardado en:\n" + f.getAbsolutePath(),
                    "Tiquete guardado", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar el tiquete:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ===============================================================
    // TIQUETE (dibujo)
    // ===============================================================
    private static final class Tiquete extends JPanel {

        static final int W = 880, H = 404, M = 14, STUB = 250, CAB = 80;
        private final Datos d;

        Tiquete(Datos d) {
            this.d = d;
            setOpaque(false);
            setPreferredSize(new Dimension(W, H));
        }

        BufferedImage imagen(double escala) {
            setSize(W, H);
            BufferedImage img = new BufferedImage((int) (W * escala), (int) (H * escala), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = img.createGraphics();
            g.setColor(Tema.FONDO);
            g.fillRect(0, 0, img.getWidth(), img.getHeight());
            g.scale(escala, escala);
            paint(g);
            g.dispose();
            return img;
        }

        @Override
        protected void paintComponent(Graphics g0) {
            Graphics2D g = (Graphics2D) g0.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

            int cx = (getWidth() - W) / 2 + M, cy = (getHeight() - H) / 2 + M;
            int cw = W - 2 * M, ch = H - 2 * M;
            int perf = cx + cw - STUB;          // x de la linea de corte

            // ---- sombra suave ----
            for (int i = 7; i >= 1; i--) {
                g.setColor(new Color(23, 42, 58, 7));
                g.fill(new RoundRectangle2D.Float(cx - i + 1, cy - i + 5, cw + 2 * i - 2, ch + 2 * i - 2, 30 + i, 30 + i));
            }

            // ---- forma del tiquete: tarjeta con muescas en el corte ----
            Area forma = new Area(new RoundRectangle2D.Float(cx, cy, cw, ch, 26, 26));
            forma.subtract(new Area(new Ellipse2D.Float(perf - 13, cy - 13, 26, 26)));
            forma.subtract(new Area(new Ellipse2D.Float(perf - 13, cy + ch - 13, 26, 26)));
            g.setColor(Color.WHITE);
            g.fill(forma);

            Shape clipOriginal = g.getClip();
            g.clip(forma);

            // ---- franjas de cabecera ----
            g.setColor(Tema.NAVY);
            g.fillRect(cx, cy, perf - cx, CAB);
            g.setColor(Tema.AZUL);
            g.fillRect(perf, cy, cx + cw - perf, CAB);
            // brillo sutil en la franja
            g.setPaint(new GradientPaint(cx, cy, new Color(255, 255, 255, 22), cx, cy + CAB, new Color(255, 255, 255, 0)));
            g.fillRect(cx, cy, cw, CAB);

            // ---- marca ----
            Tema.iconoAvion(34, Color.WHITE).paintIcon(this, g, cx + 28, cy + 21);
            texto(g, "AeroGestión Prada", cx + 74, cy + 40, Tema.fuente(Font.BOLD, 21), Color.WHITE);
            texto(g, "TIQUETE DE VUELO", cx + 75, cy + 60, espaciada(Tema.fuente(Font.BOLD, 11), 0.18f), new Color(176, 198, 220));

            // clase (pildora a la derecha de la franja)
            String clase = vacio(d.clase).toUpperCase();
            Font fc = espaciada(Tema.fuente(Font.BOLD, 12), 0.1f);
            int wc = g.getFontMetrics(fc).stringWidth(clase) + 30;
            int xc = perf - 28 - wc;
            g.setColor(new Color(255, 255, 255, 38));
            g.fill(new RoundRectangle2D.Float(xc, cy + 25, wc, 30, 30, 30));
            textoCentro(g, clase, xc + wc / 2, cy + 45, fc, Color.WHITE);

            // ---- ruta ----
            int x0 = cx + 30, xr = perf - 30, yR = cy + CAB + 24;
            texto(g, vacio(d.origenCodigo), x0, yR + 44, Tema.fuente(Font.BOLD, 46), Tema.NAVY);
            textoDerecha(g, vacio(d.destinoCodigo), xr, yR + 44, Tema.fuente(Font.BOLD, 46), Tema.NAVY);
            texto(g, recortar(g, vacio(d.origenCiudad), Tema.fuente(Font.BOLD, 15), 190), x0, yR + 68, Tema.fuente(Font.BOLD, 15), Tema.NAVY);
            textoDerecha(g, recortar(g, vacio(d.destinoCiudad), Tema.fuente(Font.BOLD, 15), 190), xr, yR + 68, Tema.fuente(Font.BOLD, 15), Tema.NAVY);
            texto(g, recortar(g, d.origenAeropuerto, Tema.fuente(Font.PLAIN, 12), 200), x0, yR + 86, Tema.fuente(Font.PLAIN, 12), Tema.TEXTO_SUAVE);
            textoDerecha(g, recortar(g, d.destinoAeropuerto, Tema.fuente(Font.PLAIN, 12), 200), xr, yR + 86, Tema.fuente(Font.PLAIN, 12), Tema.TEXTO_SUAVE);

            // linea punteada con avion en el medio
            int ly = yR + 30, lx1 = x0 + 150, lx2 = xr - 150;
            if (lx2 > lx1 + 60) {
                g.setColor(new Color(176, 192, 210));
                g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10f, new float[]{2f, 7f}, 0f));
                int mid = (lx1 + lx2) / 2;
                g.drawLine(lx1, ly, mid - 22, ly);
                g.drawLine(mid + 22, ly, lx2, ly);
                g.setStroke(new BasicStroke(1f));
                Graphics2D gi = (Graphics2D) g.create();
                gi.rotate(Math.toRadians(45), mid, ly);
                Tema.iconoAvion(26, Tema.AZUL).paintIcon(this, gi, mid - 13, ly - 13);
                gi.dispose();
            }

            // separador
            int ySep = yR + 98;
            g.setColor(new Color(228, 235, 242));
            g.fillRect(cx + 30, ySep, perf - cx - 60, 1);

            // ---- cuadricula de datos ----
            int gx = cx + 30, gy = ySep + 20, colW = (perf - cx - 60) / 4, filaH = 44;
            dato(g, "PASAJERO", vacio(d.pasajero), gx, gy, colW * 2 - 10);
            dato(g, "DOCUMENTO", vacio(d.documento), gx + colW * 2, gy, colW - 10);
            dato(g, "VUELO", d.vuelo.isEmpty() ? "-" : d.vuelo, gx + colW * 3, gy, colW);

            gy += filaH;
            dato(g, "FECHA DE SALIDA", fechaBonita(d.fechaSalida), gx, gy, colW - 10);
            dato(g, "HORA DE SALIDA", horaCorta(d.horaSalida), gx + colW, gy, colW - 10);
            String llegada = horaCorta(d.horaLlegada);
            if (!d.fechaLlegada.isEmpty() && !d.fechaLlegada.equals(d.fechaSalida)) {
                String f = fechaBonita(d.fechaLlegada);
                llegada += " · " + (f.length() > 6 ? f.substring(0, 6) : f); // llega otro dia: "06:10 · 16 oct"
            }
            dato(g, "LLEGADA", llegada, gx + colW * 2, gy, colW - 6);
            dato(g, "AVIÓN", vacio(d.avion), gx + colW * 3 + 14, gy, colW - 14);

            gy += filaH;
            String eq = vacio(d.equipaje);
            if (!d.peso.isEmpty() && !eq.equals("-")) {
                eq = eq + " · " + d.peso;
            }
            dato(g, "EQUIPAJE", eq, gx, gy, colW * 2 - 10);
            dato(g, "MÉTODO DE PAGO", vacio(d.metodoPago), gx + colW * 2, gy, colW * 2);

            // ---- talon (derecha) ----
            int sx = perf + 26, sw = cx + cw - perf - 52;
            texto(g, "RESERVA", sx, cy + 33, espaciada(Tema.fuente(Font.BOLD, 10), 0.18f), new Color(214, 232, 252));
            texto(g, recortar(g, vacio(d.reserva), Tema.fuente(Font.BOLD, 15), sw), sx, cy + 56, Tema.fuente(Font.BOLD, 15), Color.WHITE);

            int ty = cy + CAB + 24;
            textoCentro(g, "ASIENTO", sx + sw / 2, ty + 8, espaciada(Tema.fuente(Font.BOLD, 11), 0.18f), Tema.TEXTO_SUAVE);
            textoCentro(g, vacio(d.asiento), sx + sw / 2, ty + 64, Tema.fuente(Font.BOLD, 58), Tema.AZUL);

            // codigo de barras (decorativo)
            barras(g, vacio(d.reserva), sx + 4, ty + 92, sw - 8, 50);
            textoCentro(g, vacio(d.reserva), sx + sw / 2, ty + 158, Tema.fuente(Font.PLAIN, 10), Tema.TEXTO_SUAVE);

            // total
            g.setColor(new Color(228, 235, 242));
            g.fillRect(sx, cy + ch - 62, sw, 1);
            texto(g, "TOTAL PAGADO", sx, cy + ch - 40, espaciada(Tema.fuente(Font.BOLD, 10), 0.18f), Tema.TEXTO_SUAVE);
            texto(g, "$" + String.format("%,.0f", d.total), sx, cy + ch - 14, Tema.fuente(Font.BOLD, 26), Tema.NAVY);

            // ---- pie del cuerpo: emitido + estado ----
            texto(g, "Emitido el " + fechaBonita(d.fechaEmision), cx + 30, cy + ch - 15, Tema.fuente(Font.PLAIN, 12), Tema.TEXTO_SUAVE);
            String estado = "CONFIRMADO";
            Font fe = espaciada(Tema.fuente(Font.BOLD, 11), 0.12f);
            int we = g.getFontMetrics(fe).stringWidth(estado) + 26;
            int xe = perf - 30 - we;
            g.setColor(new Color(228, 246, 238));
            g.fill(new RoundRectangle2D.Float(xe, cy + ch - 36, we, 26, 26, 26));
            textoCentro(g, estado, xe + we / 2, cy + ch - 19, fe, new Color(25, 120, 78));

            g.setClip(clipOriginal);

            // ---- contorno y linea de corte ----
            g.setColor(new Color(214, 224, 235));
            g.setStroke(new BasicStroke(1.2f));
            g.draw(forma);
            g.setColor(new Color(190, 202, 216));
            g.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, new float[]{6f, 6f}, 0f));
            g.drawLine(perf, cy + 20, perf, cy + ch - 20);
            g.dispose();
        }

        // ---------- ayudas de dibujo ----------
        private void dato(Graphics2D g, String etiqueta, String valor, int x, int y, int ancho) {
            texto(g, etiqueta, x, y, espaciada(Tema.fuente(Font.BOLD, 10), 0.14f), Tema.TEXTO_SUAVE);
            Font f = Tema.fuente(Font.BOLD, 15);
            texto(g, recortar(g, valor, f, ancho), x, y + 20, f, Tema.NAVY);
        }

        private static void texto(Graphics2D g, String s, int x, int y, Font f, Color c) {
            g.setFont(f);
            g.setColor(c);
            g.drawString(s, x, y);
        }

        private static void textoDerecha(Graphics2D g, String s, int xDer, int y, Font f, Color c) {
            g.setFont(f);
            g.setColor(c);
            g.drawString(s, xDer - g.getFontMetrics().stringWidth(s), y);
        }

        private static void textoCentro(Graphics2D g, String s, int xc, int y, Font f, Color c) {
            g.setFont(f);
            g.setColor(c);
            g.drawString(s, xc - g.getFontMetrics().stringWidth(s) / 2, y);
        }

        private static Font espaciada(Font f, float tracking) {
            Map<TextAttribute, Object> m = new HashMap<>();
            m.put(TextAttribute.TRACKING, tracking);
            return f.deriveFont(m);
        }

        private static String recortar(Graphics2D g, String s, Font f, int max) {
            if (s == null) {
                return "";
            }
            FontMetrics fm = g.getFontMetrics(f);
            if (fm.stringWidth(s) <= max) {
                return s;
            }
            String pts = "…";
            while (s.length() > 1 && fm.stringWidth(s + pts) > max) {
                s = s.substring(0, s.length() - 1);
            }
            return s.trim() + pts;
        }

        /** Barras decorativas generadas a partir del codigo de reserva (no es un codigo escaneable). */
        private void barras(Graphics2D g, String codigo, int x, int y, int ancho, int alto) {
            g.setColor(Tema.NAVY);
            g.fillRect(x, y, 2, alto);
            int pos = x + 5;
            boolean barra = true;
            int i = 0;
            while (pos < x + ancho - 8) {
                int c = codigo.charAt(i % codigo.length()) * 7 + i * 13;
                int w = 1 + (c % 3); // 1 a 3 px
                if (barra) {
                    g.fillRect(pos, y, w, alto);
                }
                pos += w;
                barra = !barra;
                i++;
            }
            g.fillRect(x + ancho - 2, y, 2, alto);
        }

        private static String vacio(String s) {
            return s == null || s.trim().isEmpty() ? "-" : s.trim();
        }

        private static String fechaBonita(String s) {
            if (s == null || s.trim().isEmpty()) {
                return "-";
            }
            try {
                java.util.Date f = new SimpleDateFormat("yyyy-MM-dd").parse(s.trim().substring(0, Math.min(10, s.trim().length())));
                String t = new SimpleDateFormat("dd MMM yyyy", new Locale("es", "CO")).format(f);
                return t.replace(".", "");
            } catch (Exception e) {
                return s.trim();
            }
        }

        private static String horaCorta(String s) {
            if (s == null || s.trim().isEmpty()) {
                return "-";
            }
            s = s.trim();
            return s.length() >= 5 ? s.substring(0, 5) : s;
        }
    }

    /** Utilidad para quienes arman Datos: genera un codigo de 3 letras a partir de una ciudad. */
    public static String codigoDesdeCiudad(String ciudad) {
        if (ciudad == null) {
            return "";
        }
        String n = Normalizer.normalize(ciudad.trim(), Normalizer.Form.NFD)
                .replaceAll("[^A-Za-z]", "").toUpperCase();
        return n.length() >= 3 ? n.substring(0, 3) : n;
    }
}
