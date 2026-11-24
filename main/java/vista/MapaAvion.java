package vista;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

/**
 * Mapa visual del avion, compartido por el ADMINISTRADOR y el PASAJERO.
 *
 * Dibuja: fuselaje con alas, secciones por clase, letras de columna, numero de
 * fila, pasillo real entre bloques y una leyenda con colores.
 *
 * No consulta la base de datos: el controlador le entrega las secciones y una
 * funcion que dice en que estado esta cada asiento.
 */
public final class MapaAvion {

    /** Constructor: crea los componentes del panel y aplica el diseño. */
    private MapaAvion() {
    }

    // ---------------------------------------------------------------
    // ESTADOS
    // ---------------------------------------------------------------
    public static final String DISPONIBLE = "Disponible";
    public static final String SELECCIONADO = "Seleccionado";
    public static final String OCUPADO = "Ocupado";
    public static final String DANADO = "Dañado";
    public static final String MANTENIMIENTO = "Mantenimiento";
    public static final String NO_EXISTE = "NoExiste";

    /** Convierte cualquier texto de estado de la BD a una de las constantes. */
    public static String estado(String texto, String porDefecto) {
        String n = normalizar(texto);
        if (n.isEmpty()) {
            return porDefecto;
        }
        if (n.startsWith("DISP") || n.equals("LIBRE")) {
            return DISPONIBLE;
        }
        if (n.startsWith("OCUP") || n.startsWith("RESERV") || n.startsWith("VEND")) {
            return OCUPADO;
        }
        if (n.startsWith("DANA")) {
            return DANADO;
        }
        if (n.startsWith("MANT")) {
            return MANTENIMIENTO;
        }
        if (n.startsWith("SELEC")) {
            return SELECCIONADO;
        }
        return porDefecto;
    }

    /** Quita tildes y pasa a minúsculas para comparar textos. */
    // ---------------------------------------------------------------
    // CLASES (tolera "Bussines", "Business", "Economica", "Económica"...)
    // ---------------------------------------------------------------
    public static String normalizar(String s) {
        if (s == null) {
            return "";
        }
        return Normalizer.normalize(s.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").toUpperCase();
    }

    /** Clave unica de clase: BUSINESS, ECONOMICA, PRIMERA u otra en mayusculas. */
    public static String claveClase(String clase) {
        String n = normalizar(clase);
        if (n.startsWith("BUS")) {
            return "BUSINESS";
        }
        if (n.startsWith("ECO")) {
            return "ECONOMICA";
        }
        if (n.startsWith("PRIM") || n.startsWith("FIRST")) {
            return "PRIMERA";
        }
        return n;
    }

    /** Texto que se guarda en la BD (igual al que ya usaba el sistema). */
    public static String clasePorBD(String clave) {
        if ("BUSINESS".equals(clave)) {
            return "BUSINESS";
        }
        if ("ECONOMICA".equals(clave)) {
            return "ECONÓMICA";
        }
        return clave;
    }

    /** Devuelve el texto bonito de una clase para mostrar en pantalla. */
    public static String etiquetaClase(String clave) {
        if ("BUSINESS".equals(clave)) {
            return "Business";
        }
        if ("ECONOMICA".equals(clave)) {
            return "Económica";
        }
        if ("PRIMERA".equals(clave)) {
            return "Primera";
        }
        return clave.isEmpty() ? clave : clave.charAt(0) + clave.substring(1).toLowerCase();
    }

    /** Devuelve el orden en que se dibujan las clases (primera clase al frente). */
    private static int ordenClase(String clave) {
        if ("PRIMERA".equals(clave)) {
            return 0;
        }
        if ("BUSINESS".equals(clave)) {
            return 1;
        }
        if ("ECONOMICA".equals(clave)) {
            return 2;
        }
        return 3;
    }

    /** Clave para buscar un asiento: numero + clase normalizada. */
    public static String clave(String numero, String clase) {
        return (numero == null ? "" : numero.trim().toUpperCase()) + "_" + claveClase(clase);
    }

    /** Una sección del avión (una clase de cabina con sus filas y distribución). */
    // ---------------------------------------------------------------
    // SECCION (una clase del avion)
    // ---------------------------------------------------------------
    public static final class Seccion {

        public final String clave;
        public final int filas;
        public final String distribucion;
        public final int[] bloques;

        public Seccion(String clase, int filas, String distribucion) {
            this.clave = claveClase(clase);
            this.filas = Math.max(0, filas);
            this.distribucion = distribucion == null ? "" : distribucion.trim();
            this.bloques = parsear(this.distribucion);
        }

        public int asientosPorFila() {
            int t = 0;
            for (int b : bloques) {
                t += b;
            }
            return t;
        }

        public boolean valida() {
            return filas > 0 && bloques.length > 0;
        }
    }

    /** Todos los numeros de asiento de una seccion: 1A, 1B, 1C, ... en orden. */
    public static List<String> numeros(Seccion s) {
        List<String> r = new ArrayList<>();
        if (!s.valida()) {
            return r;
        }
        for (int fila = 1; fila <= s.filas; fila++) {
            char letra = 'A';
            for (int b : s.bloques) {
                for (int i = 0; i < b; i++) {
                    r.add(fila + String.valueOf(letra++));
                }
            }
        }
        return r;
    }

    /** "3-3", "2 - 4 - 2", "3,3" -> {3,3}. Ignora lo que no sea numero. */
    public static int[] parsear(String d) {
        List<Integer> r = new ArrayList<>();
        if (d != null) {
            for (String p : d.split("[^0-9]+")) {
                if (p.isEmpty()) {
                    continue;
                }
                try {
                    int n = Integer.parseInt(p);
                    if (n > 0 && n <= 12) {
                        r.add(n);
                    }
                } catch (NumberFormatException ex) {
                    // se ignora
                }
            }
        }
        int[] a = new int[r.size()];
        for (int i = 0; i < a.length; i++) {
            a[i] = r.get(i);
        }
        return a;
    }

    /**
     * Ordena las secciones (Primera, Business, Economica...) y descarta
     * repetidas: si una clase esta dos veces, queda la ultima.
     */
    public static List<Seccion> ordenar(List<Seccion> origen) {
        Map<String, Seccion> unicas = new LinkedHashMap<>();
        for (Seccion s : origen) {
            unicas.put(s.clave, s);
        }
        List<Seccion> lista = new ArrayList<>(unicas.values());
        lista.sort(Comparator.comparingInt(s -> ordenClase(s.clave)));
        return lista;
    }

    // ---------------------------------------------------------------
    // INTERFACES PARA EL CONTROLADOR
    // ---------------------------------------------------------------
    /** Dice en que estado esta un asiento. Devolver null = el asiento no existe. */
    public interface Origen {

        String estado(String claveClase, String numero);
    }

    /** Acción que se ejecuta cuando se hace clic en una silla. */
    public interface Clic {

        void alHacerClic(Silla silla);
    }

    /** Botón que representa una silla; cambia de color según su estado. */
    // ---------------------------------------------------------------
    // SILLA (cada asiento)
    // ---------------------------------------------------------------
    public static final class Silla extends JButton {

        private final String clave;
        private final String numero;
        private String estado;

        public Silla(String clave, String numero, String estado) {
            this.clave = clave;
            this.numero = numero;
            this.estado = estado;
            setFocusable(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setRolloverEnabled(true);
            setPreferredSize(new Dimension(44, 42));
            setMinimumSize(new Dimension(44, 42));
            actualizarTooltip();
        }

        public String getClaveClase() {
            return clave;
        }

        public String getNumero() {
            return numero;
        }

        public String getEstado() {
            return estado;
        }

        public void setEstado(String nuevo) {
            this.estado = nuevo;
            actualizarTooltip();
            repaint();
        }

        private void actualizarTooltip() {
            String e = NO_EXISTE.equals(estado) ? "Sin crear para este vuelo" : estado;
            setToolTipText(numero + " · " + etiquetaClase(clave) + " · " + e);
            setCursor(isEnabled() ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
                    : Cursor.getDefaultCursor());
        }

        @Override
        public void setEnabled(boolean b) {
            super.setEnabled(b);
            setCursor(b ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) : Cursor.getDefaultCursor());
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();

            Color relleno, borde, texto;
            boolean punteado = false;
            switch (estado == null ? DISPONIBLE : estado) {
                case SELECCIONADO:
                    relleno = Tema.AZUL;
                    borde = Tema.AZUL_OSC;
                    texto = Color.WHITE;
                    break;
                case OCUPADO:
                    relleno = new Color(251, 229, 229);
                    borde = Tema.ROJO;
                    texto = new Color(170, 45, 45);
                    break;
                case DANADO:
                    relleno = new Color(254, 241, 217);
                    borde = Tema.NARANJA;
                    texto = new Color(160, 100, 10);
                    break;
                case MANTENIMIENTO:
                    relleno = new Color(228, 232, 237);
                    borde = new Color(140, 152, 165);
                    texto = new Color(95, 108, 120);
                    break;
                case NO_EXISTE:
                    relleno = new Color(246, 248, 250);
                    borde = new Color(205, 212, 220);
                    texto = new Color(170, 178, 188);
                    punteado = true;
                    break;
                default:
                    relleno = new Color(230, 246, 238);
                    borde = Tema.VERDE;
                    texto = new Color(25, 110, 72);
            }
            if (isEnabled() && getModel().isRollover() && !SELECCIONADO.equals(estado)) {
                relleno = relleno.brighter();
                borde = borde.darker();
            }

            // cuerpo del asiento
            RoundRectangle2D cuerpo = new RoundRectangle2D.Float(1.5f, 6f, w - 3f, h - 7.5f, 12, 12);
            g2.setColor(relleno);
            g2.fill(cuerpo);
            g2.setColor(borde);
            g2.setStroke(punteado
                    ? new BasicStroke(1.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, new float[]{3f, 3f}, 0f)
                    : new BasicStroke(1.5f));
            g2.draw(cuerpo);
            // respaldo (cabecera del asiento)
            g2.setStroke(new BasicStroke(1f));
            g2.setColor(borde);
            g2.fill(new RoundRectangle2D.Float(w * 0.22f, 1f, w * 0.56f, 8f, 8, 8));

            // texto
            g2.setFont(Tema.fuente(Font.BOLD, 11));
            FontMetrics fm = g2.getFontMetrics();
            String t = numero;
            g2.setColor(texto);
            g2.drawString(t, (w - fm.stringWidth(t)) / 2f, 6 + (h - 6 + fm.getAscent() - fm.getDescent()) / 2f);
            g2.dispose();
        }
    }

    // ---------------------------------------------------------------
    // CONSTRUCCION DEL MAPA
    // ---------------------------------------------------------------
    /**
     * Construye el fuselaje completo con todas las secciones.
     *
     * @param soloDisponiblesClicables true (pasajero): solo se puede hacer clic en
     * asientos Disponibles. false (admin): se puede hacer clic en todos.
     * @param registro opcional: aqui se guardan las sillas creadas
     * (clave "1A_ECONOMICA") para poder cambiarlas despues.
     */
    public static JPanel construir(List<Seccion> secciones, Origen origen, boolean soloDisponiblesClicables,
            Clic clic, Map<String, Silla> registro) {
        Fuselaje fus = new Fuselaje();
        fus.setLayout(new BoxLayout(fus, BoxLayout.Y_AXIS));
        fus.setBorder(BorderFactory.createEmptyBorder(30, Fuselaje.ALA + 26, 40, Fuselaje.ALA + 26));

        JLabel frente = etiquetaSuave("FRENTE");
        frente.setAlignmentX(Component.CENTER_ALIGNMENT);
        fus.add(frente);
        fus.add(Box.createVerticalStrut(26));

        boolean hayAlguna = false;
        for (Seccion s : ordenar(secciones)) {
            hayAlguna = true;
            JPanel p = seccion(s, origen, soloDisponiblesClicables, clic, registro);
            p.setAlignmentX(Component.CENTER_ALIGNMENT);
            fus.add(p);
            fus.add(Box.createVerticalStrut(22));
        }
        if (!hayAlguna) {
            JLabel vacio = new JLabel("Este avión aún no tiene clases configuradas.");
            vacio.setFont(Tema.fuente(Font.PLAIN, 14));
            vacio.setForeground(Tema.TEXTO_SUAVE);
            vacio.setAlignmentX(Component.CENTER_ALIGNMENT);
            fus.add(vacio);
        }
        JLabel cola = etiquetaSuave("COLA");
        cola.setAlignmentX(Component.CENTER_ALIGNMENT);
        fus.add(cola);
        return fus;
    }

    /** Dibuja una sección (una clase) con sus filas y sillas. */
    private static JPanel seccion(Seccion s, Origen origen, boolean soloDisp, Clic clic, Map<String, Silla> registro) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);

        if (!s.valida()) {
            JLabel l = new JLabel("<html><div style='text-align:center;width:260px'><b>" + etiquetaClase(s.clave)
                    + "</b>: la distribución \"" + s.distribucion + "\" no es válida. Use números separados por guion, por ejemplo 3-3.</div></html>");
            l.setFont(Tema.fuente(Font.PLAIN, 13));
            l.setForeground(Tema.ROJO);
            p.add(l);
            return p;
        }

        int nBloques = s.bloques.length;
        int columnasTotales = 2 + s.asientosPorFila() + (nBloques - 1); // etiquetas + asientos + pasillos

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(3, 3, 3, 3);

        // titulo de la clase
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = columnasTotales;
        gc.insets = new Insets(0, 0, 8, 0);
        p.add(titulo(s), gc);
        gc.gridwidth = 1;
        gc.insets = new Insets(3, 3, 3, 3);

        // letras de columna
        int col = 1;
        char letra = 'A';
        for (int b = 0; b < nBloques; b++) {
            for (int i = 0; i < s.bloques[b]; i++) {
                gc.gridx = col++;
                gc.gridy = 1;
                JLabel l = etiquetaSuave(String.valueOf(letra++));
                l.setHorizontalAlignment(SwingConstants.CENTER);
                p.add(l, gc);
            }
            if (b < nBloques - 1) {
                gc.gridx = col++;
                gc.gridy = 1;
                p.add(Box.createHorizontalStrut(30), gc);
            }
        }

        // filas
        for (int fila = 1; fila <= s.filas; fila++) {
            col = 0;
            gc.gridy = fila + 1;
            gc.gridx = col++;
            p.add(numeroFila(fila), gc);
            letra = 'A';
            for (int b = 0; b < nBloques; b++) {
                for (int i = 0; i < s.bloques[b]; i++) {
                    String numero = fila + String.valueOf(letra++);
                    String est = origen.estado(s.clave, numero);
                    boolean existe = est != null;
                    final Silla silla = new Silla(s.clave, numero, existe ? est : NO_EXISTE);
                    boolean clicable = existe && (!soloDisp || DISPONIBLE.equals(est));
                    silla.setEnabled(clicable);
                    if (clicable && clic != null) {
                        silla.addActionListener(e -> clic.alHacerClic(silla));
                    }
                    if (registro != null) {
                        registro.put(clave(numero, s.clave), silla);
                    }
                    gc.gridx = col++;
                    p.add(silla, gc);
                }
                if (b < nBloques - 1) {
                    gc.gridx = col++;
                    p.add(Box.createHorizontalStrut(30), gc);
                }
            }
            gc.gridx = col;
            p.add(numeroFila(fila), gc);
        }
        return p;
    }

    /** Crea el título de una sección. */
    private static JComponent titulo(Seccion s) {
        JPanel t = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        t.setOpaque(false);
        JLabel nombre = new JLabel(etiquetaClase(s.clave).toUpperCase());
        nombre.setFont(Tema.fuente(Font.BOLD, 13));
        nombre.setForeground(Tema.NAVY);
        JLabel det = new JLabel(s.filas + (s.filas == 1 ? " fila" : " filas") + " · " + s.distribucion);
        det.setFont(Tema.fuente(Font.PLAIN, 12));
        det.setForeground(Tema.TEXTO_SUAVE);
        t.add(nombre);
        t.add(det);
        return t;
    }

    /** Crea la etiqueta con el número de fila. */
    private static JLabel numeroFila(int n) {
        JLabel l = new JLabel(String.valueOf(n), SwingConstants.CENTER);
        l.setFont(Tema.fuente(Font.BOLD, 11));
        l.setForeground(new Color(150, 163, 176));
        l.setPreferredSize(new Dimension(22, 20));
        return l;
    }

    /** Crea una etiqueta de texto de color suave. */
    private static JLabel etiquetaSuave(String t) {
        JLabel l = new JLabel(t);
        l.setFont(Tema.fuente(Font.BOLD, 11));
        l.setForeground(new Color(150, 163, 176));
        return l;
    }

    // ---------------------------------------------------------------
    // FUSELAJE (dibujo del avion)
    // ---------------------------------------------------------------
    private static final class Fuselaje extends JPanel {

        static final int ALA = 70;

        Fuselaje() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int W = getWidth(), H = getHeight();
            float x0 = ALA, w0 = W - 2f * ALA;
            if (w0 <= 20) {
                g2.dispose();
                return;
            }

            // alas y cola
            Color ala = new Color(208, 217, 228);
            g2.setColor(ala);
            float y1 = Math.max(110, H * 0.30f);
            g2.fill(poligono(x0 + 2, y1, x0 - ALA + 2, y1 + 100, x0 - ALA + 2, y1 + 132, x0 + 2, y1 + 112));
            g2.fill(poligono(x0 + w0 - 2, y1, x0 + w0 + ALA - 2, y1 + 100, x0 + w0 + ALA - 2, y1 + 132, x0 + w0 - 2, y1 + 112));
            float yc = H - 90;
            g2.fill(poligono(x0 + 2, yc, x0 - 34, yc + 44, x0 - 34, yc + 62, x0 + 2, yc + 38));
            g2.fill(poligono(x0 + w0 - 2, yc, x0 + w0 + 34, yc + 44, x0 + w0 + 34, yc + 62, x0 + w0 - 2, yc + 38));

            // cuerpo con nariz redondeada
            float nariz = Math.min(w0 * 1.1f, H * 0.5f);
            Area cuerpo = new Area(new RoundRectangle2D.Float(x0, 0, w0, nariz * 1.6f, w0, nariz * 1.3f));
            cuerpo.add(new Area(new RoundRectangle2D.Float(x0, nariz * 0.6f, w0, H - nariz * 0.6f - 2, 56, 56)));
            g2.setColor(Color.WHITE);
            g2.fill(cuerpo);
            g2.setColor(new Color(190, 203, 218));
            g2.setStroke(new BasicStroke(3f));
            g2.draw(cuerpo);
            g2.dispose();
        }

        private static Path2D poligono(float... p) {
            Path2D.Float r = new Path2D.Float();
            r.moveTo(p[0], p[1]);
            for (int i = 2; i < p.length; i += 2) {
                r.lineTo(p[i], p[i + 1]);
            }
            r.closePath();
            return r;
        }
    }

    /** Crea la leyenda de colores (disponible, ocupado, etc.). */
    // ---------------------------------------------------------------
    // LEYENDA
    // ---------------------------------------------------------------
    public static JPanel leyenda(boolean admin) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 0));
        p.setOpaque(false);
        p.add(item(DISPONIBLE, "Disponible"));
        if (admin) {
            p.add(item(OCUPADO, "Ocupado"));
            p.add(item(DANADO, "Dañado"));
            p.add(item(MANTENIMIENTO, "Mantenimiento"));
        } else {
            p.add(item(SELECCIONADO, "Seleccionado"));
            p.add(item(OCUPADO, "Ocupado"));
            p.add(item(MANTENIMIENTO, "No disponible"));
        }
        return p;
    }

    /** Crea un elemento de la leyenda (color + texto). */
    private static JComponent item(String estado, String texto) {
        JPanel i = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        i.setOpaque(false);
        Silla muestra = new Silla("", "", estado);
        muestra.setEnabled(false);
        muestra.setPreferredSize(new Dimension(22, 22));
        JLabel l = new JLabel(texto);
        l.setFont(Tema.fuente(Font.PLAIN, 13));
        l.setForeground(Tema.TEXTO);
        i.add(muestra);
        i.add(l);
        return i;
    }

    // ---------------------------------------------------------------
    // INSTALAR EN EL CONTENEDOR DE LA VISTA (pnlCabina)
    // ---------------------------------------------------------------
    /**
     * Pone la leyenda y el avion dentro de pnlCabina, centrados.
     * Cambia el layout del contenedor (arregla el GroupLayout vacio del
     * diseñador, que dejaba los paneles agregados con tamaño 0x0).
     */
    public static void instalar(JPanel pnlCabina, JPanel avion, boolean admin) {
        pnlCabina.removeAll();
        pnlCabina.setLayout(new GridBagLayout());
        pnlCabina.setBackground(Tema.FONDO);
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        gc.insets = new Insets(14, 10, 10, 10);
        pnlCabina.add(leyenda(admin), gc);
        gc.gridy = 1;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.NORTH;
        gc.insets = new Insets(0, 10, 20, 10);
        pnlCabina.add(avion, gc);
        pnlCabina.revalidate();
        pnlCabina.repaint();
    }

    /** Limpia el contenedor y muestra un mensaje (ej: "Seleccione un vuelo"). */
    public static void mensaje(JPanel pnlCabina, String texto) {
        pnlCabina.removeAll();
        pnlCabina.setLayout(new GridBagLayout());
        pnlCabina.setBackground(Tema.FONDO);
        JLabel l = new JLabel(texto);
        l.setFont(Tema.fuente(Font.PLAIN, 15));
        l.setForeground(Tema.TEXTO_SUAVE);
        pnlCabina.add(l);
        pnlCabina.revalidate();
        pnlCabina.repaint();
    }
}
