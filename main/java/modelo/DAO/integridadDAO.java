package modelo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelo.conexion.Conectar;

/**
 * Comprobaciones de integridad: duplicados, dependencias entre tablas y
 * operaciones que tocan varias tablas a la vez (con transaccion).
 *
 * Si no hay conexion con la base de datos, las consultas devuelven valores
 * "seguros" (se asume que SI hay dependencias) para no borrar nada por error.
 */
public final class integridadDAO {

    /** Clase de utilidades estáticas: no se crean objetos de ella. */
    private integridadDAO() {
    }

    private static Connection con;

    /** Devuelve una conexión reutilizable; la vuelve a abrir si se cerró. */
    private static synchronized Connection conexion() throws SQLException {
        if (con == null || con.isClosed() || !con.isValid(2)) {
            con = Conectar.conexi();
        }
        if (con == null) {
            throw new SQLException("Sin conexión con la base de datos");
        }
        return con;
    }

    /** Evita inyección SQL: solo permite letras y guion bajo en nombres de tabla o columna. */
    private static void nombreSeguro(String s) {
        if (s == null || !s.matches("[A-Za-z_]+")) {
            throw new IllegalArgumentException("Nombre de tabla o columna no permitido: " + s);
        }
    }

    // ---------------------------------------------------------------
    // CONTAR / EXISTE
    // ---------------------------------------------------------------
    /** Cuenta filas de "tabla" donde "columna" = valor. Si hay error devuelve 1 (modo seguro). */
    public static int contar(String tabla, String columna, Object valor) {
        nombreSeguro(tabla);
        nombreSeguro(columna);
        return contarSql("SELECT COUNT(*) FROM " + tabla + " WHERE " + columna + " = ?", valor);
    }

    /** Indica si existe al menos una fila con ese valor. */
    public static boolean existe(String tabla, String columna, Object valor) {
        return contar(tabla, columna, valor) > 0;
    }

    /**
     * Hay OTRA fila (distinta de la que se edita) con el mismo valor?
     * idColumna/idValor identifican la fila actual; con idValor = null se asume alta nueva.
     */
    public static boolean existeOtro(String tabla, String columna, Object valor, String idColumna, Object idValor) {
        nombreSeguro(tabla);
        nombreSeguro(columna);
        nombreSeguro(idColumna);
        if (idValor == null) {
            return existe(tabla, columna, valor);
        }
        return contarSql("SELECT COUNT(*) FROM " + tabla + " WHERE " + columna + " = ? AND " + idColumna + " <> ?",
                valor, idValor) > 0;
    }

    /** Ejecuta un SELECT COUNT(*) con parametros. En caso de error devuelve 1 (modo seguro). */
    private static int contarSql(String sql, Object... params) {
        try {
            Connection c = conexion();
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? rs.getInt(1) : 0;
                }
            }
        } catch (Exception ex) {
            System.out.println("integridadDAO: " + ex);
            return 1;
        }
    }

    // ---------------------------------------------------------------
    // CONSULTAS ESPECIFICAS
    // ---------------------------------------------------------------
    /** Rutas que usan esta ciudad como origen o destino. */
    public static int rutasPorCiudad(String ciudad) {
        return contarSql("SELECT COUNT(*) FROM ruta WHERE origen = ? OR destino = ?", ciudad, ciudad);
    }

    /** Tiquetes vendidos en vuelos que usan este avion. */
    public static int tiquetesDeAvion(String modeloAvion) {
        return contarSql("SELECT COUNT(*) FROM tiquete t INNER JOIN vuelo v ON t.fk_vuelo = v.id_vuelo "
                + "WHERE v.fk_avion = ?", modeloAvion);
    }

    /** Tiquetes en vuelos que usan esta ruta. */
    public static int tiquetesDeRuta(int idRuta) {
        return contarSql("SELECT COUNT(*) FROM tiquete t INNER JOIN vuelo v ON t.fk_vuelo = v.id_vuelo "
                + "WHERE v.fk_ruta = ?", idRuta);
    }

    /**
     * Vuelos del mismo avion que se cruzan en el tiempo con [inicio, fin]
     * (formato yyyy-MM-dd HH:mm:ss). Ignora cancelados y finalizados.
     */
    public static int vuelosSolapados(String modeloAvion, String inicio, String fin, int idExcluir) {
        return contarSql("SELECT COUNT(*) FROM vuelo WHERE fk_avion = ? AND id_vuelo <> ? "
                + "AND estado NOT IN ('Cancelado', 'Finalizado') "
                + "AND TIMESTAMP(fecha_salida, hora_salida) < ? "
                + "AND TIMESTAMP(fecha_llegada, hora_llegada) > ?",
                modeloAvion, idExcluir, fin, inicio);
    }

    /** Tiquetes activos (no cancelados) que tienen este asiento, sin contar uno (idTiqueteExcluir). */
    public static int tiquetesActivosDeAsiento(int idAsiento, int idTiqueteExcluir) {
        return contarSql("SELECT COUNT(*) FROM tiquete WHERE fk_asiento = ? AND id_tiquete <> ? "
                + "AND (estado_reserva IS NULL OR estado_reserva <> 'Cancelada')", idAsiento, idTiqueteExcluir);
    }

    /** El pasajero ya tiene una reserva vigente (no cancelada) en este vuelo? Si hay error devuelve 1 (modo seguro). */
    public static boolean pasajeroYaReservoVuelo(String documento, int idVuelo) {
        return contarSql("SELECT COUNT(*) FROM tiquete WHERE fk_pasajero = ? AND fk_vuelo = ? "
                + "AND (estado_reserva IS NULL OR estado_reserva <> 'Cancelada')", documento, idVuelo) > 0;
    }

    // ---------------------------------------------------------------
    // OPERACIONES CON TRANSACCION
    // ---------------------------------------------------------------
    /** Borra el vuelo y sus asientos juntos: o se borra todo o no se borra nada. */
    public static boolean eliminarVueloConAsientos(int idVuelo) {
        Connection c = null;
        try {
            c = conexion();
            c.setAutoCommit(false);
            try (PreparedStatement a = c.prepareStatement("DELETE FROM asiento WHERE fk_vuelo = ?");
                    PreparedStatement v = c.prepareStatement("DELETE FROM vuelo WHERE id_vuelo = ?")) {
                a.setInt(1, idVuelo);
                a.executeUpdate();
                v.setInt(1, idVuelo);
                boolean borrado = v.executeUpdate() == 1;
                if (!borrado) {
                    c.rollback();
                    return false;
                }
            }
            c.commit();
            return true;
        } catch (Exception ex) {
            System.out.println("integridadDAO.eliminarVueloConAsientos: " + ex);
            try {
                if (c != null) {
                    c.rollback();
                }
            } catch (SQLException ignore) {
            }
            return false;
        } finally {
            try {
                if (c != null) {
                    c.setAutoCommit(true);
                }
            } catch (SQLException ignore) {
            }
        }
    }

    /**
     * Borra un tiquete y deja libre su asiento (si el tiquete estaba activo),
     * todo en una sola transaccion.
     */
    public static boolean eliminarTiqueteLiberandoAsiento(int idTiquete) {
        Connection c = null;
        try {
            c = conexion();
            c.setAutoCommit(false);
            try (PreparedStatement l = c.prepareStatement(
                    "UPDATE asiento a INNER JOIN tiquete t ON t.fk_asiento = a.id_asiento "
                    + "SET a.estado = 'Disponible' "
                    + "WHERE t.id_tiquete = ? AND (t.estado_reserva IS NULL OR t.estado_reserva <> 'Cancelada')");
                    PreparedStatement d = c.prepareStatement("DELETE FROM tiquete WHERE id_tiquete = ?")) {
                l.setInt(1, idTiquete);
                l.executeUpdate();
                d.setInt(1, idTiquete);
                if (d.executeUpdate() != 1) {
                    c.rollback();
                    return false;
                }
            }
            c.commit();
            return true;
        } catch (Exception ex) {
            System.out.println("integridadDAO.eliminarTiqueteLiberandoAsiento: " + ex);
            try {
                if (c != null) {
                    c.rollback();
                }
            } catch (SQLException ignore) {
            }
            return false;
        } finally {
            try {
                if (c != null) {
                    c.setAutoCommit(true);
                }
            } catch (SQLException ignore) {
            }
        }
    }

    /**
     * Total de asientos configurados para un avion (filas x asientos por fila de cada clase),
     * sin contar la clase idClaseExcluir (0 para contarlas todas). Si hay error devuelve 0.
     */
    public static int asientosConfigurados(String modeloAvion, int idClaseExcluir) {
        int total = 0;
        try {
            Connection c = conexion();
            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT filas, distribucion FROM avion_asientos WHERE fk_avion = ? AND id_clase <> ?")) {
                ps.setString(1, modeloAvion);
                ps.setInt(2, idClaseExcluir);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        total += rs.getInt(1) * asientosPorFila(rs.getString(2));
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("integridadDAO.asientosConfigurados: " + ex);
        }
        return total;
    }

    /** "2-4-2" -> 8 */
    public static int asientosPorFila(String distribucion) {
        int t = 0;
        if (distribucion != null) {
            for (String p : distribucion.split("[^0-9]+")) {
                if (!p.isEmpty()) {
                    try {
                        t += Integer.parseInt(p);
                    } catch (NumberFormatException ignore) {
                    }
                }
            }
        }
        return t;
    }
}
