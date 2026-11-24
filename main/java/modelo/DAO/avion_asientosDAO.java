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
import modelo.entidad.avion_asientos;

/** DAO de la configuración de asientos por avión (clases, filas y distribución): CRUD. */
public class avion_asientosDAO {

    Connection con = Conectar.conexi();

    /** Inserta un nuevo registro de configuración de asientos del avión en la base de datos. Devuelve true si se guardó. */
    public boolean guardar(avion_asientos a) {

        try {

            String sql = "INSERT INTO avion_asientos "
                    + "(nombre_clase, filas, distribucion, fk_avion) "
                    + "VALUES (?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, a.getNombre_clase());
            ps.setInt(2, a.getFilas());
            ps.setString(3, a.getDistribucion());
            ps.setString(4, a.getFk_avion());

            ps.executeUpdate();

            return true;

        } catch (Exception ex) {

            System.out.println(ex);
            return false;
        }
    }

    /** Consulta todos los registros de configuración de asientos del avión y los devuelve en una lista. */
    public ArrayList<avion_asientos> listar() {

        ArrayList<avion_asientos> lista = new ArrayList<>();

        try {

            String sql = "SELECT * FROM avion_asientos "
                    + "ORDER BY nombre_clase DESC";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                avion_asientos a = new avion_asientos();

                a.setId_clase(rs.getInt("id_clase"));
                a.setNombre_clase(rs.getString("nombre_clase"));
                a.setFilas(rs.getInt("filas"));
                a.setDistribucion(rs.getString("distribucion"));
                a.setFk_avion(rs.getString("fk_avion"));

                lista.add(a);
            }

        } catch (Exception ex) {

            System.out.println(ex);
        }

        return lista;
    }

    /** Actualiza en la base de datos los datos de configuración de asientos del avión. Devuelve true si se actualizó. */
    public boolean editar(avion_asientos a) {

        try {

            String sql = "UPDATE avion_asientos SET "
                    + "nombre_clase=?, filas=?, distribucion=?, fk_avion=? "
                    + "WHERE id_clase=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, a.getNombre_clase());
            ps.setInt(2, a.getFilas());
            ps.setString(3, a.getDistribucion());
            ps.setString(4, a.getFk_avion());
            ps.setInt(5, a.getId_clase());

            ps.executeUpdate();

            return true;

        } catch (Exception ex) {

            System.out.println(ex);
            return false;
        }
    }

    /** Elimina el registro de configuración de asientos del avión con ese identificador. Devuelve true si se eliminó. */
    public boolean eliminar(int id) {

        try {

            String sql = "DELETE FROM avion_asientos WHERE id_clase=?";

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