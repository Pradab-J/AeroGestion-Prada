/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */




package modelo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import modelo.conexion.Conectar;

/**
 * DAO de Reservas (lado administrador): lista todas las reservas con el nombre
 * del pasajero y el asiento, y permite cancelarlas por código.
 */
public class reservaDAO {

    /**
     * Lista todas las reservas (tiquetes con código) con el nombre del pasajero y el
     * asiento, de la más reciente a la más antigua.
     */
    public List<Object[]> listarReservas() {
        List<Object[]> lista = new ArrayList<>();

        String sql = "SELECT "
                + "t.reserva AS codigo, "
                + "t.fecha_emision AS fecha, "
                + "t.fk_pasajero AS documento, "
                + "COALESCE(CONCAT(p.nombre, ' ', p.apellido), "
                + "t.fk_pasajero) AS pasajero, "
                + "t.fk_vuelo AS vuelo, "
                + "COALESCE(a.num_asiento, "
                + "CAST(t.fk_asiento AS CHAR)) AS asiento, "
                + "COALESCE(t.estado_reserva, 'Activa') AS estado "
                + "FROM tiquete t "
                + "LEFT JOIN pasajeros p "
                + "ON t.fk_pasajero = p.documento "
                + "LEFT JOIN asiento a "
                + "ON t.fk_asiento = a.id_asiento "
                + "WHERE t.reserva IS NOT NULL "
                + "AND t.reserva <> '' "
                + "ORDER BY t.id_tiquete DESC";

        try (Connection con = Conectar.conexi();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] fila = {
                    rs.getString("codigo"),
                    rs.getDate("fecha"),
                    rs.getString("documento"),
                    rs.getString("pasajero"),
                    rs.getInt("vuelo"),
                    rs.getString("asiento"),
                    rs.getString("estado")
                };

                lista.add(fila);
            }

            System.out.println(
                    "Reservas encontradas: " + lista.size()
            );

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al consultar reservas: "
                    + e.getMessage(), e
            );
        }

        return lista;
    }

    /** Cancela la reserva (si esta vigente) y libera el asiento. Devuelve false si no se pudo. */
    public boolean cancelarPorCodigo(String codigo) {
        String cancelar = "UPDATE tiquete SET estado_reserva = 'Cancelada' "
                + "WHERE reserva = ? AND (estado_reserva IS NULL OR estado_reserva = 'Activa')";
        String liberar = "UPDATE asiento a INNER JOIN tiquete t ON t.fk_asiento = a.id_asiento "
                + "SET a.estado = 'Disponible' WHERE t.reserva = ?";

        try (Connection con = Conectar.conexi()) {
            con.setAutoCommit(false);
            try (PreparedStatement pc = con.prepareStatement(cancelar);
                 PreparedStatement pl = con.prepareStatement(liberar)) {
                pc.setString(1, codigo);
                if (pc.executeUpdate() < 1) {
                    con.rollback();
                    return false;
                }
                pl.setString(1, codigo);
                pl.executeUpdate();
                con.commit();
                return true;
            } catch (Exception ex) {
                con.rollback();
                throw ex;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al cancelar la reserva: " + e.getMessage(), e);
        }
    }
}
