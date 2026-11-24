package modelo.DAO;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import modelo.conexion.Conectar;
import modelo.entidad.pasajero;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

/**
 * Clase DAO (Data Access Object) encargada de realizar operaciones
 * de acceso a datos para los pasajeros, como consultarlos y registrarlos.
 *
 * Esta clase conecta la aplicación Java con la tabla "pasajeros" de MySQL.
 *
 * @author Aprendiz
 */
public class pasajeroDAO {

    // Guarda la conexión a la base de datos que devuelve la clase Conectar.
    // Se utiliza para ejecutar las consultas SQL de esta clase.
    Connection con = Conectar.conexi();

    /**
     * Consulta todos los pasajeros registrados en la tabla "pasajeros".
     *
     * @return una lista de objetos pasajero con los datos consultados.
     *         Si ocurre un error, devuelve la lista que se haya podido llenar
     *         (normalmente vacía).
     */
    public ArrayList<pasajero> listarPasajeros() {

        // Crea una lista vacía donde se almacenará cada pasajero consultado.
        ArrayList<pasajero> lista = new ArrayList<>();

        try {
            // Consulta todas las columnas y filas de la tabla pasajeros.
            String sql = "SELECT * FROM pasajeros";

            // Crea un objeto Statement para ejecutar la consulta SQL.
            Statement st = con.createStatement();

            // Ejecuta la consulta y guarda los resultados en un ResultSet.
            ResultSet rs = st.executeQuery(sql);

            // Recorre los resultados, una fila de la tabla a la vez.
            while (rs.next()) {

                // Crea un objeto pasajero para almacenar los datos de la fila actual.
                pasajero p = new pasajero();

                // Copia cada columna de la base de datos en su atributo correspondiente.
                p.setDocumento(rs.getString("documento"));
                p.setTipoDocumento(rs.getString("tipo_documento"));
                p.setNombre(rs.getString("nombre"));
                p.setApellido(rs.getString("apellido"));
                p.setGenero(rs.getString("genero"));
                p.setFechaNacimiento(rs.getString("fecha_nacimiento"));
                p.setNacionalidad(rs.getString("nacionalidad"));
                p.setTelefono(rs.getString("telefono"));
                p.setCorreo(rs.getString("correo"));

                // Agrega el pasajero completo a la lista.
                lista.add(p);
            }

        } catch (Exception e) {
            // Muestra en la consola el error ocurrido durante la consulta.
            System.out.println(e);
        }

        // Devuelve la lista con los pasajeros encontrados.
        return lista;
    }

    /**
     * Inserta un pasajero nuevo en la tabla "pasajeros".
     *
     * @param p objeto pasajero que contiene los datos que se van a guardar.
     * @return true si la inserción se ejecuta correctamente; false si ocurre un error.
     */
    public boolean guardarPasajeros(pasajero p) {

        try {
            // Sentencia SQL con nueve campos que se llenarán mediante parámetros.
            String sql = "INSERT INTO pasajeros "
                    + "(documento, tipo_documento, nombre, apellido, genero, fecha_nacimiento, nacionalidad, telefono, correo) "
                    + "VALUES (?,?,?,?,?,?,?,?,?)";

            // Prepara la consulta para asignar los valores de forma segura mediante parámetros.
            PreparedStatement ps = con.prepareStatement(sql);

            // Asigna a cada signo ? el valor del atributo correspondiente del pasajero.
            ps.setString(1, p.getDocumento());
            ps.setString(2, p.getTipoDocumento());
            ps.setString(3, p.getNombre());
            ps.setString(4, p.getApellido());
            ps.setString(5, p.getGenero());
            ps.setString(6, p.getFechaNacimiento());
            ps.setString(7, p.getNacionalidad());
            ps.setString(8, p.getTelefono());
            ps.setString(9, p.getCorreo());

            // Ejecuta el INSERT para registrar el pasajero en la base de datos.
            ps.executeUpdate();

            // Indica que el registro se guardó sin lanzar una excepción.
            return true;

        } catch (Exception e) {
            // Muestra el error en la consola y comunica que no se pudo guardar.
            System.out.println(e);
            return false;
        }
    }

}
