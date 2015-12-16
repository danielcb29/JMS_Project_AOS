package datos_complejos;

import java.io.Serializable;
/**
 * Clase ejemplo para envio de objetos
 * @author Daniel Correa, Cristina Extremera 
 *
 */

public class Estudiante implements Serializable{
	private String nombre;
	private String apellido;
	private String dni;
	private int edad;
	
	
	public Estudiante(String nombre, String apellido, String dni, int edad) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.dni = dni;
		this.edad = edad;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public int getEdad() {
		return edad;
	}
	public void setEdad(int edad) {
		this.edad = edad;
	}
	
	@Override
	public String toString() {
		return "Estudiante [nombre=" + nombre + ", apellido=" + apellido + ", dni=" + dni + ", edad=" + edad + "]";
	}


}