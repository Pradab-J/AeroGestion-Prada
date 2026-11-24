package controlador;

import java.awt.Component;
import java.text.Normalizer;
import java.time.LocalTime;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 * Validaciones comunes del sistema.
 *
 * Todos los metodos que validan devuelven NULL si el dato esta bien, o el
 * mensaje de error (en español) si esta mal. Se combinan con primero(...) y se
 * muestran con mostrar(...):
 *
 *   String error = Validar.primero(
 *           Validar.texto("Nombre", nombre, 2, 60),
 *           Validar.correo("Correo", correo));
 *   if (Validar.mostrar(vista, error)) { return; }
 */
public final class Validar {

    /** Constructor: guarda la vista y los DAO y conecta los botones con sus acciones. */
    private Validar() {
    }

    private static final Pattern CORREO = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$");
    private static final Pattern NOMBRE = Pattern.compile("^\\p{L}[\\p{L} .'-]*$");
    private static final Pattern DIGITOS = Pattern.compile("^[0-9]+$");
    private static final Pattern TELEFONO = Pattern.compile("^\\+?[0-9]{7,15}$");
    private static final Pattern ALFANUMERICO = Pattern.compile("^[A-Za-z0-9]+$");
    private static final Pattern CODIGO = Pattern.compile("^[A-Za-z0-9][A-Za-z0-9-]{3,29}$");
    private static final Pattern DISTRIBUCION = Pattern.compile("^[1-9](-[1-9]){0,3}$");

    // ---------------------------------------------------------------
    // LIMPIEZA Y CONVERSION
    // ---------------------------------------------------------------
    /** Quita espacios al inicio y al final y junta los espacios repetidos. */
    public static String limpiar(String s) {
        return s == null ? "" : s.trim().replaceAll("\\s+", " ");
    }

    public static boolean vacio(String s) {
        return s == null || s.trim().isEmpty();
    }

    /** Clave para comparar textos sin importar mayusculas, tildes ni espacios sobrantes. */
    public static String clave(String s) {
        if (s == null) {
            return "";
        }
        return Normalizer.normalize(limpiar(s), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").toLowerCase();
    }

    /** Entero positivo de hasta 9 digitos, o null si no es valido. */
    public static Integer aEntero(String s) {
        if (s == null) {
            return null;
        }
        String t = s.trim();
        if (!t.matches("^-?[0-9]{1,9}$")) {
            return null;
        }
        try {
            return Integer.parseInt(t);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    /** Decimal normal (acepta coma o punto). Rechaza NaN, Infinity y notacion cientifica. */
    public static Double aDecimal(String s) {
        if (s == null) {
            return null;
        }
        String t = s.trim().replace(',', '.');
        if (!t.matches("^-?[0-9]{1,9}(\\.[0-9]{1,4})?$")) {
            return null;
        }
        try {
            double d = Double.parseDouble(t);
            return Double.isNaN(d) || Double.isInfinite(d) ? null : d;
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    /** Hora HH:mm o HH:mm:ss. Devuelve null si no es una hora real. */
    public static LocalTime aHora(String s) {
        if (s == null) {
            return null;
        }
        String t = s.trim();
        if (!t.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9](:[0-5][0-9])?$")) {
            return null;
        }
        try {
            String[] p = t.split(":");
            return LocalTime.of(Integer.parseInt(p[0]), Integer.parseInt(p[1]),
                    p.length > 2 ? Integer.parseInt(p[2]) : 0);
        } catch (Exception ex) {
            return null;
        }
    }

    // ---------------------------------------------------------------
    // VALIDACIONES (devuelven null si esta bien)
    // ---------------------------------------------------------------
    public static String texto(String campo, String valor, int min, int max) {
        String v = limpiar(valor);
        if (v.isEmpty()) {
            return "El campo «" + campo + "» es obligatorio.";
        }
        if (v.length() < min) {
            return "«" + campo + "» debe tener al menos " + min + " caracteres.";
        }
        if (v.length() > max) {
            return "«" + campo + "» no puede superar los " + max + " caracteres.";
        }
        for (int i = 0; i < v.length(); i++) {
            if (Character.isISOControl(v.charAt(i))) {
                return "«" + campo + "» contiene caracteres no permitidos.";
            }
        }
        return null;
    }

    /** Solo letras (con tildes y ñ), espacios, punto, apostrofo y guion. */
    public static String nombre(String campo, String valor, int min, int max) {
        String e = texto(campo, valor, min, max);
        if (e != null) {
            return e;
        }
        if (!NOMBRE.matcher(limpiar(valor)).matches()) {
            return "«" + campo + "» solo puede contener letras, espacios, punto, guion y apóstrofo.";
        }
        return null;
    }

    public static String correo(String campo, String valor) {
        String e = texto(campo, valor, 5, 100);
        if (e != null) {
            return e;
        }
        if (!CORREO.matcher(limpiar(valor)).matches()) {
            return "«" + campo + "» no es un correo válido (ejemplo: nombre@dominio.com).";
        }
        return null;
    }

    public static String telefono(String campo, String valor) {
        String e = texto(campo, valor, 7, 16);
        if (e != null) {
            return e;
        }
        String t = limpiar(valor).replace(" ", "").replace("-", "");
        if (!TELEFONO.matcher(t).matches()) {
            return "«" + campo + "» debe tener entre 7 y 15 dígitos (solo números).";
        }
        return null;
    }

    /** CC y TI: solo numeros. Extranjera: letras y numeros. */
    public static String documento(String tipo, String valor) {
        String v = limpiar(valor);
        if (v.isEmpty()) {
            return "El campo «Documento» es obligatorio.";
        }
        boolean extranjera = tipo != null && clave(tipo).startsWith("extranj");
        if (extranjera) {
            if (!ALFANUMERICO.matcher(v).matches() || v.length() < 5 || v.length() > 15) {
                return "El documento extranjero debe tener entre 5 y 15 letras o números, sin espacios.";
            }
        } else {
            if (!DIGITOS.matcher(v).matches() || v.length() < 5 || v.length() > 12) {
                return "El documento debe tener entre 5 y 12 dígitos (solo números).";
            }
        }
        return null;
    }

    public static String entero(String campo, String valor, int min, int max) {
        if (vacio(valor)) {
            return "El campo «" + campo + "» es obligatorio.";
        }
        Integer n = aEntero(valor);
        if (n == null) {
            return "«" + campo + "» debe ser un número entero (sin letras ni símbolos).";
        }
        if (n < min || n > max) {
            return "«" + campo + "» debe estar entre " + min + " y " + max + ".";
        }
        return null;
    }

    /** Decimal mayor que "minimoExclusivo" y menor o igual que "max". */
    public static String decimalPositivo(String campo, String valor, double max) {
        if (vacio(valor)) {
            return "El campo «" + campo + "» es obligatorio.";
        }
        Double d = aDecimal(valor);
        if (d == null) {
            return "«" + campo + "» debe ser un número válido (por ejemplo 15 o 15.5).";
        }
        if (d <= 0) {
            return "«" + campo + "» debe ser mayor que 0.";
        }
        if (d > max) {
            return "«" + campo + "» no puede ser mayor que " + (max == Math.floor(max) ? String.valueOf((long) max) : String.valueOf(max)) + ".";
        }
        return null;
    }

    /** Codigo IATA: exactamente 3 letras. */
    public static String iata(String valor) {
        String v = limpiar(valor);
        if (!v.matches("^[A-Za-z]{3}$")) {
            return "El código IATA debe tener exactamente 3 letras (ejemplo: BOG).";
        }
        return null;
    }

    /** Distribucion de asientos por fila, ejemplo 3-3 o 2-4-2 (maximo 4 bloques, 12 asientos por fila). */
    public static String distribucion(String valor) {
        String v = limpiar(valor).replace(" ", "");
        if (v.isEmpty()) {
            return "El campo «Distribución» es obligatorio.";
        }
        if (!DISTRIBUCION.matcher(v).matches()) {
            return "La distribución debe ser números del 1 al 9 separados por guion, por ejemplo 3-3 o 2-4-2.";
        }
        int total = 0;
        for (String p : v.split("-")) {
            total += Integer.parseInt(p);
        }
        if (total > 12) {
            return "La distribución no puede tener más de 12 asientos por fila (la suya tiene " + total + ").";
        }
        return null;
    }

    public static String codigoReserva(String valor) {
        String v = limpiar(valor);
        if (!CODIGO.matcher(v).matches()) {
            return "El código de reserva debe tener entre 4 y 30 caracteres (letras, números o guion).";
        }
        return null;
    }

    public static String seleccion(String campo, Object valor) {
        return valor == null ? "Seleccione una opción en «" + campo + "»." : null;
    }

    /** Valida todos los datos de un pasajero. Devuelve null si estan bien. */
    public static String pasajero(String tipo, String doc, String nombre, String apellido, Object genero,
            String nacionalidad, String telefono, String correo, java.util.Date nacimiento) {
        String error = primero(
                seleccion("Tipo de documento", tipo == null || tipo.isEmpty() ? null : tipo),
                documento(tipo, doc),
                nombre("Nombre", nombre, 2, 50),
                nombre("Apellido", apellido, 2, 50),
                seleccion("Género", genero),
                nombre("Nacionalidad", nacionalidad, 3, 40),
                telefono("Teléfono", telefono),
                correo("Correo", correo),
                nacimiento == null ? "Seleccione la fecha de nacimiento." : null);
        if (error != null) {
            return error;
        }
        java.time.LocalDate n = nacimiento.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        if (n.isAfter(java.time.LocalDate.now())) {
            return "La fecha de nacimiento no puede ser futura.";
        }
        if (n.isBefore(java.time.LocalDate.now().minusYears(120))) {
            return "La fecha de nacimiento no es válida (más de 120 años).";
        }
        return null;
    }

    // ---------------------------------------------------------------
    // COMBINAR Y MOSTRAR
    // ---------------------------------------------------------------
    /** Devuelve el primer mensaje de error que no sea null (o null si todo esta bien). */
    public static String primero(String... errores) {
        for (String e : errores) {
            if (e != null) {
                return e;
            }
        }
        return null;
    }

    /** Si hay error lo muestra y devuelve true (para hacer "return"). */
    public static boolean mostrar(Component padre, String error) {
        if (error == null) {
            return false;
        }
        aviso(padre, error);
        return true;
    }

    public static void aviso(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Revise los datos", JOptionPane.WARNING_MESSAGE);
    }
}
