/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Conexión con la base de datos MySQL (esquema "gestionaerolinea").
 * Aquí se definen la ruta, el usuario y la contraseña. Todos los DAO llaman a
 * {@link #conexi()} cada vez que necesitan hablar con la base de datos.
 * 
 * @author Aprendiz
 */
public class Conectar {
    
    private static String ruta="jdbc:mysql://localhost:3306/gestionaerolinea";
    private static String user="root";
    private static String pass="2727";
    
    public static Connection conexion;
    public static Statement consulta;
    
    /**
     * Abre una conexión nueva con la base de datos MySQL. Devuelve null si falla
     * (por ejemplo, MySQL apagado o contraseña incorrecta).
     */
    public static Connection conexi(){
        try{
            conexion= DriverManager.getConnection(ruta,user,pass);
            consulta= conexion.createStatement();
            
        }catch(SQLException e){
            // Si falla, no se debe reutilizar una conexion anterior (podria estar cerrada)
            conexion = null;
            consulta = null;
            e.printStackTrace();
        }
        return conexion;
    }
    /** Devuelve el Statement de la última conexión abierta. */
    public static Statement consultas(){
        return consulta;
    }
    
}
