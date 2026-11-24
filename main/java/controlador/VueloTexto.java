package controlador;

import java.util.HashMap;
import java.util.Map;
import modelo.DAO.rutaDAO;
import modelo.DAO.vueloDAO;
import modelo.entidad.ruta;
import modelo.entidad.vuelo;

/**
 * Arma el texto con el que se muestra un vuelo en los combos (listas desplegables).
 * En vez de mostrar solo el número (id), muestra algo fácil de reconocer:
 * "id - Origen a Destino - fecha hora", por ejemplo "12 - Bogota a Medellin - 2026-09-01 05:00".
 * El id siempre va primero, así los controladores lo obtienen con split(" - ")[0].
 */
public final class VueloTexto {

    private VueloTexto() {
    }

    /** Devuelve un mapa id de ruta -> "Origen a Destino". Se consulta una sola vez por carga del combo. */
    public static Map<Integer, String> rutas() {
        Map<Integer, String> mapa = new HashMap<>();
        for (ruta r : new rutaDAO().listar()) {
            mapa.put(r.getId_ruta(), r.getOrigen() + " a " + r.getDestino());
        }
        return mapa;
    }

    /** Describe un vuelo sin el id: "Origen a Destino - fecha hora de salida". */
    public static String detalle(vuelo v, Map<Integer, String> rutas) {
        String hora = v.getHora_salida() == null ? "" : v.getHora_salida();
        if (hora.length() >= 5) {
            hora = hora.substring(0, 5); // 05:00:00 -> 05:00
        }
        return rutas.getOrDefault(v.getFk_ruta(), "Ruta " + v.getFk_ruta())
                + " - " + v.getFecha_salida() + " " + hora;
    }

    /** Texto de un vuelo para el combo: id, ruta, fecha y hora de salida. */
    public static String texto(vuelo v, Map<Integer, String> rutas) {
        return v.getId_vuelo() + " - " + detalle(v, rutas);
    }

    /** Devuelve un mapa id de vuelo -> detalle ("Origen a Destino - fecha hora"), para mostrarlo en otros combos. */
    public static Map<Integer, String> detalles() {
        Map<Integer, String> rutas = rutas();
        Map<Integer, String> mapa = new HashMap<>();
        for (vuelo v : new vueloDAO().listar()) {
            mapa.put(v.getId_vuelo(), detalle(v, rutas));
        }
        return mapa;
    }

    /** Obtiene el id del vuelo a partir del texto del combo (lo que hay antes del primer " - "). */
    public static int id(Object itemCombo) {
        return Integer.parseInt(itemCombo.toString().split(" - ")[0].trim());
    }
}
