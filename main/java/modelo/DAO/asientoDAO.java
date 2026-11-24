/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package modelo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import modelo.conexion.Conectar;
import modelo.entidad.asiento;

/**
 * DAO de Asientos: CRUD de asientos más operaciones especiales para el proceso de
 * reserva (buscar por vuelo y número, cambiar estado, ocupar de forma atómica y liberar).
 */
public class asientoDAO {

    Connection con = Conectar.conexi();

    /** Inserta un nuevo registro de asiento en la base de datos. Devuelve true si se guardó. */
    // =========================================================
    // GUARDAR
    // =========================================================
    public boolean guardar(asiento a) {
        try {

            String sql = "INSERT INTO asiento "
                    + "(num_asiento, clase, estado, fk_vuelo, precio) "
                    + "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, a.getNum_asiento());
            ps.setString(2, a.getClase());
            ps.setString(3, a.getEstado());
            ps.setInt(4, a.getFk_vuelo());
            ps.setDouble(5, a.getPrecio());

            ps.executeUpdate();

            return true;

        } catch (Exception ex) {

            System.out.println(ex);
            return false;
        }
    }

    /** Consulta todos los registros de asiento y los devuelve en una lista. */
    // =========================================================
    // LISTAR
    // =========================================================
    public ArrayList<asiento> listar() {

        ArrayList<asiento> lista = new ArrayList<>();

        try {

            String sql = "SELECT * FROM asiento";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                asiento a = new asiento();

                a.setId_asiento(
                        rs.getInt("id_asiento")
                );

                a.setNum_asiento(
                        rs.getString("num_asiento")
                );

                a.setClase(
                        rs.getString("clase")
                );

                a.setEstado(
                        rs.getString("estado")
                );

                a.setFk_vuelo(
                        rs.getInt("fk_vuelo")
                );

                a.setPrecio(
                        rs.getDouble("precio")
                );

                lista.add(a);
            }

        } catch (Exception ex) {

            System.out.println(ex);
        }

        return lista;
    }

    /** Actualiza en la base de datos los datos de asiento. Devuelve true si se actualizó. */
    // =========================================================
    // EDITAR
    // =========================================================
    public boolean editar(asiento a) {

        try {

            String sql = "UPDATE asiento SET "
                    + "num_asiento=?, "
                    + "clase=?, "
                    + "estado=?, "
                    + "fk_vuelo=?, "
                    + "precio=? "
                    + "WHERE id_asiento=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, a.getNum_asiento());
            ps.setString(2, a.getClase());
            ps.setString(3, a.getEstado());
            ps.setInt(4, a.getFk_vuelo());
            ps.setDouble(5, a.getPrecio());
            ps.setInt(6, a.getId_asiento());

            ps.executeUpdate();

            return true;

        } catch (Exception ex) {

            System.out.println(ex);
            return false;
        }
    }

    /** Elimina el registro de asiento con ese identificador. Devuelve true si se eliminó. */
    // =========================================================
    // ELIMINAR
    // =========================================================
    public boolean eliminar(int id) {

        try {

            String sql =
                    "DELETE FROM asiento "
                    + "WHERE id_asiento=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, id);

            ps.executeUpdate();

            return true;

        } catch (Exception ex) {

            System.out.println(ex);
            return false;
        }
    }

    /** Busca un asiento de un vuelo por su número (ej. 12A) y su clase. Devuelve null si no existe. */
    // =========================================================
    // BUSCAR ASIENTO POR VUELO Y NÚMERO
    // =========================================================
    public asiento buscarPorVueloYNumero(
            int fk_vuelo,
            String num_asiento,
            String clase) {

        try {

            String sql =
                    "SELECT * FROM asiento "
                    + "WHERE fk_vuelo = ? "
                    + "AND num_asiento = ? "
                    + "AND clase = ?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, fk_vuelo);
            ps.setString(2, num_asiento);
            ps.setString(3, clase);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                asiento a = new asiento();

                a.setId_asiento(
                        rs.getInt("id_asiento")
                );

                a.setNum_asiento(
                        rs.getString("num_asiento")
                );

                a.setClase(
                        rs.getString("clase")
                );

                a.setEstado(
                        rs.getString("estado")
                );

                a.setFk_vuelo(
                        rs.getInt("fk_vuelo")
                );

                a.setPrecio(
                        rs.getDouble("precio")
                );

                return a;
            }

        } catch (Exception ex) {

            System.out.println(
                    "Error buscando asiento: " + ex
            );
        }

        return null;
    }

    /** Cambia solo el estado (Disponible, Ocupado, Dañado...) de un asiento. */
    // =========================================================
    // ACTUALIZAR ESTADO
    // =========================================================
    public boolean actualizarEstado(
            int id_asiento,
            String estado) {

        try {

            String sql =
                    "UPDATE asiento "
                    + "SET estado = ? "
                    + "WHERE id_asiento = ?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, estado);
            ps.setInt(2, id_asiento);

            ps.executeUpdate();

            return true;

        } catch (Exception ex) {

            System.out.println(
                    "Error actualizando estado: "
                    + ex
            );

            return false;
        }
    }

    /** Devuelve todos los asientos de un vuelo (sirve para dibujar el mapa del avión). */
    // =========================================================
    // LISTAR ASIENTOS POR VUELO
    // =========================================================
    public ArrayList<asiento> listarPorVuelo(
            int fk_vuelo) {

        ArrayList<asiento> lista =
                new ArrayList<>();

        try {

            String sql =
                    "SELECT * FROM asiento "
                    + "WHERE fk_vuelo = ? "
                    + "ORDER BY id_asiento";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, fk_vuelo);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                asiento a = new asiento();

                a.setId_asiento(
                        rs.getInt("id_asiento")
                );

                a.setNum_asiento(
                        rs.getString("num_asiento")
                );

                a.setClase(
                        rs.getString("clase")
                );

                a.setEstado(
                        rs.getString("estado")
                );

                a.setFk_vuelo(
                        rs.getInt("fk_vuelo")
                );

                a.setPrecio(
                        rs.getDouble("precio")
                );

                lista.add(a);
            }

        } catch (Exception ex) {

            System.out.println(ex);
        }

        return lista;
    }

    /**
     * Marca el asiento como Ocupado solo si seguía Disponible (operación atómica).
     * Devuelve false si otra persona lo tomó antes, así nunca se vende dos veces.
     */
    // =========================================================
    // RESERVA ATOMICA: ocupa el asiento SOLO si sigue disponible.
    // Devuelve false si otra persona ya lo tomo (o si hay un error).
    // =========================================================
    public boolean reservarSiDisponible(int id_asiento) {
        String sql = "UPDATE asiento SET estado = 'Ocupado' WHERE id_asiento = ? AND estado = 'Disponible'";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id_asiento);
            return ps.executeUpdate() == 1;
        } catch (Exception ex) {
            System.out.println("Error reservando asiento: " + ex);
            return false;
        }
    }

    /** Deja el asiento otra vez Disponible (por cancelación o por un fallo al guardar). */
    // =========================================================
    // LIBERAR ASIENTO (vuelve a Disponible)
    // =========================================================
    public boolean liberar(int id_asiento) {
        return actualizarEstado(id_asiento, "Disponible");
    }
}
