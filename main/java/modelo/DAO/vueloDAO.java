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
import modelo.entidad.vuelo;

/** DAO de Vuelos: CRUD de vuelos más búsquedas (por origen, destino y fecha, y por id). */
public class vueloDAO {

    Connection con = Conectar.conexi();

    /** Inserta un nuevo registro de vuelo en la base de datos. Devuelve true si se guardó. */
    public boolean guardar(vuelo v) {
        try {
            String sql = "INSERT INTO vuelo "
                    + "(fecha_salida, fecha_llegada, hora_salida, "
                    + "hora_llegada, estado, fk_avion, fk_ruta) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, v.getFecha_salida());
            ps.setString(2, v.getFecha_llegada());
            ps.setString(3, v.getHora_salida());
            ps.setString(4, v.getHora_llegada());
            ps.setString(5, v.getEstado());
            ps.setString(6, v.getFk_avion());
            ps.setInt(7, v.getFk_ruta());

            ps.executeUpdate();

            return true;

        } catch (Exception ex) {
            System.out.println(ex);
            return false;
        }
    }

    /** Consulta todos los registros de vuelo y los devuelve en una lista. */
    public ArrayList<vuelo> listar() {

        ArrayList<vuelo> lista = new ArrayList<>();

        try {

            String sql = "SELECT * FROM vuelo";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                vuelo v = new vuelo();

                v.setId_vuelo(
                        rs.getInt("id_vuelo")
                );

                v.setFecha_salida(
                        rs.getString("fecha_salida")
                );

                v.setFecha_llegada(
                        rs.getString("fecha_llegada")
                );

                v.setHora_salida(
                        rs.getString("hora_salida")
                );

                v.setHora_llegada(
                        rs.getString("hora_llegada")
                );

                v.setEstado(
                        rs.getString("estado")
                );

                v.setFk_avion(
                        rs.getString("fk_avion")
                );

                v.setFk_ruta(
                        rs.getInt("fk_ruta")
                );

                lista.add(v);
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }

        return lista;
    }

    /** Actualiza en la base de datos los datos de vuelo. Devuelve true si se actualizó. */
    public boolean editar(vuelo v) {

        try {

            String sql = "UPDATE vuelo SET "
                    + "fecha_salida=?, fecha_llegada=?, "
                    + "hora_salida=?, hora_llegada=?, "
                    + "estado=?, fk_avion=?, fk_ruta=? "
                    + "WHERE id_vuelo=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, v.getFecha_salida());
            ps.setString(2, v.getFecha_llegada());
            ps.setString(3, v.getHora_salida());
            ps.setString(4, v.getHora_llegada());
            ps.setString(5, v.getEstado());
            ps.setString(6, v.getFk_avion());
            ps.setInt(7, v.getFk_ruta());
            ps.setInt(8, v.getId_vuelo());

            ps.executeUpdate();

            return true;

        } catch (Exception ex) {
            System.out.println(ex);
            return false;
        }
    }

    /** Elimina el registro de vuelo con ese identificador. Devuelve true si se eliminó. */
    public boolean eliminar(int id) {

        try {

            String sql
                    = "DELETE FROM vuelo "
                    + "WHERE id_vuelo=?";

            PreparedStatement ps
                    = con.prepareStatement(sql);

            ps.setInt(1, id);

            ps.executeUpdate();

            return true;

        } catch (Exception ex) {
            System.out.println(ex);
            return false;
        }
    }

    /** Lista todos los vuelos disponibles que aún no han salido (hoy en adelante), del más próximo al más lejano. */
    public ArrayList<vuelo> listarDisponibles() {

        ArrayList<vuelo> lista = new ArrayList<>();

        String sql = "SELECT * FROM vuelo "
                + "WHERE estado = 'Disponible' AND fecha_salida >= CURDATE() "
                + "ORDER BY fecha_salida, hora_salida";

        try (PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                vuelo v = new vuelo();
                v.setId_vuelo(rs.getInt("id_vuelo"));
                v.setFecha_salida(rs.getString("fecha_salida"));
                v.setFecha_llegada(rs.getString("fecha_llegada"));
                v.setHora_salida(rs.getString("hora_salida"));
                v.setHora_llegada(rs.getString("hora_llegada"));
                v.setEstado(rs.getString("estado"));
                v.setFk_avion(rs.getString("fk_avion"));
                v.setFk_ruta(rs.getInt("fk_ruta"));
                lista.add(v);
            }

        } catch (Exception ex) {
            System.out.println("Error al listar vuelos disponibles: " + ex);
        }

        return lista;
    }

    /** Busca los vuelos que coinciden con el origen, destino y fecha indicados. */
    // BUSCAR VUELOS POR ORIGEN, DESTINO Y FECHA
    public ArrayList<vuelo> buscarVuelos(
            String origen,
            String destino,
            String fecha) {

        ArrayList<vuelo> lista
                = new ArrayList<>();

        try {

            String sql
                    = "SELECT v.* "
                    + "FROM vuelo v "
                    + "INNER JOIN ruta r "
                    + "ON v.fk_ruta = r.id_ruta "
                    + "WHERE r.origen = ? "
                    + "AND r.destino = ? "
                    + "AND v.fecha_salida = ? "
                    + "AND v.estado = 'Disponible'";

            PreparedStatement ps
                    = con.prepareStatement(sql);

            ps.setString(1, origen);
            ps.setString(2, destino);
            ps.setString(3, fecha);

            ResultSet rs
                    = ps.executeQuery();

            while (rs.next()) {

                vuelo v = new vuelo();

                v.setId_vuelo(
                        rs.getInt("id_vuelo")
                );

                v.setFecha_salida(
                        rs.getString("fecha_salida")
                );

                v.setFecha_llegada(
                        rs.getString("fecha_llegada")
                );

                v.setHora_salida(
                        rs.getString("hora_salida")
                );

                v.setHora_llegada(
                        rs.getString("hora_llegada")
                );

                v.setEstado(
                        rs.getString("estado")
                );

                v.setFk_avion(
                        rs.getString("fk_avion")
                );

                v.setFk_ruta(
                        rs.getInt("fk_ruta")
                );

                lista.add(v);
            }

        } catch (Exception ex) {

            System.out.println(ex);
        }

        return lista;
    }

    /** Devuelve el vuelo con ese id, o null si no existe. */
    public vuelo buscarPorId(int id_vuelo) {

        vuelo v = null;

        try {

            String sql = "SELECT * FROM vuelo "
                    + "WHERE id_vuelo = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id_vuelo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                v = new vuelo();

                v.setId_vuelo(rs.getInt("id_vuelo"));
                v.setFecha_salida(rs.getString("fecha_salida"));
                v.setFecha_llegada(rs.getString("fecha_llegada"));
                v.setHora_salida(rs.getString("hora_salida"));
                v.setHora_llegada(rs.getString("hora_llegada"));
                v.setEstado(rs.getString("estado"));
                v.setFk_avion(rs.getString("fk_avion"));
                v.setFk_ruta(rs.getInt("fk_ruta"));
            }

        } catch (Exception ex) {

            System.out.println(ex);
        }

        return v;
    }
}
