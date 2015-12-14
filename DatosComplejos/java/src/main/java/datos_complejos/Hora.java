package datos_complejos;

import java.io.Serializable;

public class Hora implements Serializable{
	private int minutos;
	private int segundos;

	public Hora(int minutos,int segundos){
		this.minutos=minutos;
		this.segundos=segundos;
	}

	public int getMinutos(){
		return this.minutos;
	}

	public int getSegundos(){
		return this.segundos;
	}

}