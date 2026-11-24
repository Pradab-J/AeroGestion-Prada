/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import modelo.conexion.Conectar;

/**
 * DAO de "Mis reservas" (lado del pasajero): consulta una reserva usando el
 * documento y el código, y permite cancelarla liberando el asiento.
 */
public class consultaReservaDAO {

    /**
     * Busca una reserva por documento del pasajero y código de reserva. Devuelve un
     * arreglo con los datos (id, código, fecha, pasajero, vuelo, asiento, equipaje,
     * precios, total y estado) o null si no existe.
     */
    public Object[] consultarReserva(
            String documento, String codigo) throws Exception {

        String sql = "SELECT "
                + "t.id_tiquete, "
                + "t.reserva, "
                + "t.fecha_emision, "
                + "t.fk_pasajero, "
                + "p.nombre AS pasajero, "
                + "t.fk_vuelo, "
                + "COALESCE(a.num_asiento, "
                + "CAST(t.fk_asiento AS CHAR)) AS asiento, "
                + "t.fk_equipaje, "
                + "t.precio_asiento, "
                + "t.precio_equipaje, "
                + "t.total, "
                + "COALESCE(t.estado_reserva, 'Activa') AS estado_reserva "
                + "FROM tiquete t "
                + "LEFT JOIN pasajeros p "
                + "ON t.fk_pasajero = p.documento "
                + "LEFT JOIN asiento a "
                + "ON t.fk_asiento = a.id_asiento "
                + "WHERE t.fk_pasajero = ? "
                + "AND t.reserva = ? "
                + "LIMIT 1";

        try (Connection con = Conectar.conexi(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, documento.trim());
            ps.setString(2, codigo.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Object[]{
                        rs.getInt("id_tiquete"),
                        rs.getString("reserva"),
                        rs.getDate("fecha_emision"),
                        rs.getString("fk_pasajero"),
                        rs.getString("pasajero"),
                        rs.getInt("fk_vuelo"),
                        rs.getString("asiento"),
                        rs.getObject("fk_equipaje"),
                        rs.getBigDecimal("precio_asiento"),
                        rs.getBigDecimal("precio_equipaje"),
                        rs.getBigDecimal("total"),
                        rs.getString("estado_reserva")
                    };
                }
            }
        }

        return null;
    }

    /**
     * Marca la reserva como Cancelada (solo si sigue vigente) y libera su asiento,
     * todo en una sola transacción. Devuelve false si no se pudo cancelar.
     */
    public boolean cancelarReserva(
            int idTiquete,
            String documento,
            String codigo) throws Exception {

        String sql = "UPDATE tiquete "
                + "SET estado_reserva = 'Cancelada' "
                + "WHERE id_tiquete = ? "
                + "AND fk_pasajero = ? "
                + "AND reserva = ? "
                + "AND (estado_reserva IS NULL OR estado_reserva = 'Activa')";
        // Al cancelar, el asiento vuelve a quedar disponible (antes quedaba ocupado para siempre)
        String liberar = "UPDATE asiento a INNER JOIN tiquete t ON t.fk_asiento = a.id_asiento "
                + "SET a.estado = 'Disponible' WHERE t.id_tiquete = ?";

        try (Connection con = Conectar.conexi()) {
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(sql);
                    PreparedStatement pl = con.prepareStatement(liberar)) {

                ps.setInt(1, idTiquete);
                ps.setString(2, documento.trim());
                ps.setString(3, codigo.trim());

                if (ps.executeUpdate() != 1) {
                    con.rollback();
                    return false;
                }
                pl.setInt(1, idTiquete);
                pl.executeUpdate();
                con.commit();
                return true;
            } catch (Exception ex) {
                con.rollback();
                throw ex;
            }
        }
    }
}
