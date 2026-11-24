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
import modelo.entidad.ruta;

/** DAO de Rutas: consultas SQL de la tabla de rutas (CRUD). */
public class rutaDAO {

    Connection con = Conectar.conexi();

    /** Inserta un nuevo registro de ruta en la base de datos. Devuelve true si se guardó. */
    public boolean guardar(ruta r) {

        try {

            String sql = "INSERT INTO ruta "
                    + "(origen, destino, duracion, fk_aeropuerto) "
                    + "VALUES (?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, r.getOrigen());
            ps.setString(2, r.getDestino());
            ps.setInt(3, r.getDuracion());
            ps.setInt(4, r.getFk_aeropuerto());

            ps.executeUpdate();

            return true;

        } catch (Exception ex) {

            System.out.println(ex);
            return false;
        }
    }

    /** Consulta todos los registros de ruta y los devuelve en una lista. */
    public ArrayList<ruta> listar() {

        ArrayList<ruta> lista = new ArrayList<>();

        try {

            String sql = "SELECT * FROM ruta";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                ruta r = new ruta();

                r.setId_ruta(rs.getInt("id_ruta"));
                r.setOrigen(rs.getString("origen"));
                r.setDestino(rs.getString("destino"));
                r.setDuracion(rs.getInt("duracion"));
                r.setFk_aeropuerto(rs.getInt("fk_aeropuerto"));

                lista.add(r);
            }

        } catch (Exception ex) {

            System.out.println(ex);
        }

        return lista;
    }

    /** Actualiza en la base de datos los datos de ruta. Devuelve true si se actualizó. */
    public boolean editar(ruta r) {

        try {

            String sql = "UPDATE ruta SET "
                    + "origen=?, destino=?, duracion=?, fk_aeropuerto=? "
                    + "WHERE id_ruta=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, r.getOrigen());
            ps.setString(2, r.getDestino());
            ps.setInt(3, r.getDuracion());
            ps.setInt(4, r.getFk_aeropuerto());
            ps.setInt(5, r.getId_ruta());

            ps.executeUpdate();

            return true;

        } catch (Exception ex) {

            System.out.println(ex);
            return false;
        }
    }

    /** Elimina el registro de ruta con ese identificador. Devuelve true si se eliminó. */
    public boolean eliminar(int id) {

        try {

            String sql = "DELETE FROM ruta WHERE id_ruta=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);

            ps.executeUpdate();

            return true;

        } catch (Exception ex) {

            System.out.println(ex);
            return false;
        }
    }
}