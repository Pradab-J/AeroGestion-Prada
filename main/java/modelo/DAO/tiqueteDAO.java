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
import modelo.entidad.tiquete;

/**
 * DAO de Tiquetes: guarda, lista, edita y elimina tiquetes, y busca el tiquete
 * que ocupa un asiento en un vuelo. Al guardar, el tiquete queda en estado "Activa".
 */
public class tiqueteDAO {

    Connection con = Conectar.conexi();

    /** Inserta un nuevo registro de tiquete en la base de datos. Devuelve true si se guardó. */
    // GUARDAR
    public boolean guardar(tiquete t) {
        try {

            String sql = "INSERT INTO tiquete "
                    + "(fecha_emision, reserva, metodo_pago, fk_equipaje, "
                    + "fk_pasajero, fk_asiento, fk_vuelo, "
                    + "precio_asiento, precio_equipaje, total, estado_reserva) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'Activa')";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, t.getFecha_emision());
            ps.setString(2, t.getReserva());
            ps.setString(3, t.getMetodo_pago());
            ps.setInt(4, t.getEquipaje());
            ps.setString(5, t.getPasajero());
            ps.setInt(6, t.getAsiento());
            ps.setInt(7, t.getVuelo());

            // PRECIOS
            ps.setDouble(8, t.getPrecio_asiento());
            ps.setDouble(9, t.getPrecio_equipaje());
            ps.setDouble(10, t.getTotal());

            ps.executeUpdate();

            return true;

        } catch (Exception ex) {
            System.out.println("Error al guardar tiquete: " + ex);
            return false;
        }
    }

    /** Consulta todos los registros de tiquete y los devuelve en una lista. */
    // LISTAR
    public ArrayList<tiquete> listar() {

        ArrayList<tiquete> lista = new ArrayList<>();

        try {

            String sql = "SELECT * FROM tiquete";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                tiquete t = new tiquete();

                t.setId_tiquete(rs.getInt("id_tiquete"));
                t.setFecha_emision(rs.getString("fecha_emision"));
                t.setReserva(rs.getString("reserva"));
                t.setMetodo_pago(rs.getString("metodo_pago"));
                t.setEquipaje(rs.getInt("fk_equipaje"));
                t.setPasajero(rs.getString("fk_pasajero"));
                t.setAsiento(rs.getInt("fk_asiento"));
                t.setVuelo(rs.getInt("fk_vuelo"));

                // PRECIOS
                t.setPrecio_asiento(
                        rs.getDouble("precio_asiento")
                );

                t.setPrecio_equipaje(
                        rs.getDouble("precio_equipaje")
                );

                t.setTotal(
                        rs.getDouble("total")
                );

                lista.add(t);
            }

        } catch (Exception ex) {
            System.out.println("Error al listar tiquetes: " + ex);
        }

        return lista;
    }

    /** Actualiza en la base de datos los datos de tiquete. Devuelve true si se actualizó. */
    // EDITAR
    public boolean editar(tiquete t) {

        try {

            String sql = "UPDATE tiquete SET "
                    + "fecha_emision=?, "
                    + "reserva=?, "
                    + "metodo_pago=?, "
                    + "fk_equipaje=?, "
                    + "fk_pasajero=?, "
                    + "fk_asiento=?, "
                    + "fk_vuelo=?, "
                    + "precio_asiento=?, "
                    + "precio_equipaje=?, "
                    + "total=? "
                    + "WHERE id_tiquete=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, t.getFecha_emision());
            ps.setString(2, t.getReserva());
            ps.setString(3, t.getMetodo_pago());
            ps.setInt(4, t.getEquipaje());
            ps.setString(5, t.getPasajero());
            ps.setInt(6, t.getAsiento());
            ps.setInt(7, t.getVuelo());

            // PRECIOS
            ps.setDouble(8, t.getPrecio_asiento());
            ps.setDouble(9, t.getPrecio_equipaje());
            ps.setDouble(10, t.getTotal());

            ps.setInt(11, t.getId_tiquete());

            ps.executeUpdate();

            return true;

        } catch (Exception ex) {
            System.out.println("Error al editar tiquete: " + ex);
            return false;
        }
    }

    /** Elimina el registro de tiquete con ese identificador. Devuelve true si se eliminó. */
    // ELIMINAR
    public boolean eliminar(int id) {

        try {

            String sql = "DELETE FROM tiquete WHERE id_tiquete=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);

            ps.executeUpdate();

            return true;

        } catch (Exception ex) {
            System.out.println("Error al eliminar tiquete: " + ex);
            return false;
        }
    }
    
    
    /** Busca el tiquete que tiene un asiento en un vuelo (sirve para saber quién lo ocupa). */
    // BUSCAR TIQUETE POR ASIENTO Y VUELO
    public tiquete buscarPorAsientoYVuelo(int idAsiento, int idVuelo) {
        tiquete t = null;

        try {
            String sql = "SELECT * FROM tiquete "
                    + "WHERE fk_asiento = ? AND fk_vuelo = ? "
                    + "LIMIT 1";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idAsiento);
            ps.setInt(2, idVuelo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                t = new tiquete();

                t.setId_tiquete(rs.getInt("id_tiquete"));
                t.setFecha_emision(rs.getString("fecha_emision"));
                t.setReserva(rs.getString("reserva"));
                t.setMetodo_pago(rs.getString("metodo_pago"));
                t.setEquipaje(rs.getInt("fk_equipaje"));
                t.setPasajero(rs.getString("fk_pasajero"));
                t.setAsiento(rs.getInt("fk_asiento"));
                t.setVuelo(rs.getInt("fk_vuelo"));
                t.setPrecio_asiento(rs.getDouble("precio_asiento"));
                t.setPrecio_equipaje(rs.getDouble("precio_equipaje"));
                t.setTotal(rs.getDouble("total"));
            }

        } catch (Exception e) {
            System.out.println("Error al buscar tiquete por asiento: " + e);
        }

        return t;
    }

}