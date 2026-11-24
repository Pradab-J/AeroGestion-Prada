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
import modelo.entidad.equipaje;

/** DAO de Equipajes: CRUD de equipajes y un guardado que devuelve el id generado. */
public class equipajeDAO {

    Connection con = Conectar.conexi();

    /** Inserta un nuevo registro de equipaje en la base de datos. Devuelve true si se guardó. */
    // GUARDAR
    public boolean guardar(equipaje e) {

        try {

            String sql = "INSERT INTO equipaje "
                    + "(peso, estado, tipo, precio) "
                    + "VALUES (?, ?, ?, ?)";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setDouble(1, e.getPeso());
            ps.setString(2, e.getEstado());
            ps.setString(3, e.getTipo());
            ps.setDouble(4, e.getPrecio());

            ps.executeUpdate();

            return true;

        } catch (Exception ex) {

            System.out.println(ex);
            return false;
        }
    }

    /** Inserta el equipaje y devuelve el id que generó la base de datos (para ligarlo al tiquete). Devuelve -1 si falla. */
    // GUARDAR Y OBTENER ID
    public int guardarYObtenerId(equipaje e) {

        try {

            String sql = "INSERT INTO equipaje "
                    + "(peso, estado, tipo, precio) "
                    + "VALUES (?, ?, ?, ?)";

            PreparedStatement ps =
                    con.prepareStatement(
                            sql,
                            java.sql.Statement.RETURN_GENERATED_KEYS
                    );

            ps.setDouble(1, e.getPeso());
            ps.setString(2, e.getEstado());
            ps.setString(3, e.getTipo());
            ps.setDouble(4, e.getPrecio());

            ps.executeUpdate();

            ResultSet rs =
                    ps.getGeneratedKeys();

            if (rs.next()) {

                return rs.getInt(1);
            }

        } catch (Exception ex) {

            System.out.println(
                    "Error al guardar equipaje: "
                    + ex
            );
        }

        return -1;
    }

    /** Consulta todos los registros de equipaje y los devuelve en una lista. */
    // LISTAR
    public ArrayList<equipaje> listar() {

        ArrayList<equipaje> lista =
                new ArrayList<>();

        try {

            String sql =
                    "SELECT * FROM equipaje";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                equipaje e =
                        new equipaje();

                e.setId_equipaje(
                        rs.getInt("id_equipaje")
                );

                e.setPeso(
                        rs.getDouble("peso")
                );

                e.setEstado(
                        rs.getString("estado")
                );

                e.setTipo(
                        rs.getString("tipo")
                );

                e.setPrecio(
                        rs.getDouble("precio")
                );

                lista.add(e);
            }

        } catch (Exception ex) {

            System.out.println(ex);
        }

        return lista;
    }

    /** Actualiza en la base de datos los datos de equipaje. Devuelve true si se actualizó. */
    // EDITAR
    public boolean editar(equipaje e) {

        try {

            String sql =
                    "UPDATE equipaje SET "
                    + "peso=?, estado=?, tipo=?, precio=? "
                    + "WHERE id_equipaje=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setDouble(1, e.getPeso());
            ps.setString(2, e.getEstado());
            ps.setString(3, e.getTipo());
            ps.setDouble(4, e.getPrecio());
            ps.setInt(5, e.getId_equipaje());

            ps.executeUpdate();

            return true;

        } catch (Exception ex) {

            System.out.println(ex);
            return false;
        }
    }

    /** Elimina el registro de equipaje con ese identificador. Devuelve true si se eliminó. */
    // ELIMINAR
    public boolean eliminar(int id) {

        try {

            String sql =
                    "DELETE FROM equipaje "
                    + "WHERE id_equipaje=?";

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
}