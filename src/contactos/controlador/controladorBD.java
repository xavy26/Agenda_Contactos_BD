/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contactos.controlador;

import contactos.modelo.Persona;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Paredes
 */
public class controladorBD {
    
    private final String url = "jdbc:derby://localhost:1527/Contactos";
    private final String usuario = "root";
    private final String clave = "12345";
    private static Connection conexion;
    private static PreparedStatement seleccionarPersona;
    private static PreparedStatement seleccionarPersonaPorApellido;
    private static PreparedStatement insertarNuevaPersona;

    public controladorBD() {
        try {
           Class.forName("org.apache.derby.jdbc.ClientDriver");
        } catch (ClassNotFoundException ex) {
            System.out.println("Error al cargar driver. " + ex);
        }
        try {
            conexion = DriverManager.getConnection(url, usuario, clave);
        } catch (SQLException ex) {
            System.out.println("Error al establecer conexion con la base de datos" + ex);
        }
        
    }

    public List<Persona> getPersonas(){
        List<Persona> l = new ArrayList<Persona>(); 
        try {
            seleccionarPersona = conexion.prepareStatement("slect * from persona");
            ResultSet r = seleccionarPersona.executeQuery();
            
            while(r.next()){
                int id = r.getInt("id");
                String nombre = r.getString("nombre");
                String apellido = r.getString("apellido");
                String email = r.getString("email");
                String telefono = r.getString("telefono");
                l.add(new Persona(id, nombre, apellido, email, telefono));
            }
            r.close();
        } catch (SQLException ex) {
            System.out.println("Error al realizar la consulta " + ex);
        }
        
        return l;
    }
    
    public List<Persona> getPersonasPorApellido(String apellido){
        List<Persona> l = new ArrayList<Persona>(); 
          try {
            seleccionarPersonaPorApellido = conexion.prepareStatement("select * from persona where apellidos" + apellido);
            ResultSet resultado = seleccionarPersonaPorApellido.executeQuery();

            while (resultado.next()) {
                int id = resultado.getInt("id");
                String nombres = resultado.getString("nombres");
                String apellidos = resultado.getString("apellidos");
                String email = resultado.getString("email");
                String telefono = resultado.getString("telefono");
                l.add(new Persona(id, nombres, apellidos, email, telefono));
            }
            resultado.close();
            conexion.close();
        } catch (SQLException ex) {
            System.out.println("Error al enviar consulta " + ex);
        }
        return l;
    }
    
    public int agregarPersona(String nombre, String apellidos, String email, String telefono){
       Persona pers = new Persona(this.getPersonas().size() + 1, nombre, apellidos, email, telefono);
        try {
            insertarNuevaPersona = conexion.prepareStatement("insert into persona values(" + (this.getPersonas().size() + 1) + ", '" + nombre 
                    + "', '" + apellidos + "', '" + email + "', '" + telefono + "')");
            return insertarNuevaPersona.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(controladorBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
}
