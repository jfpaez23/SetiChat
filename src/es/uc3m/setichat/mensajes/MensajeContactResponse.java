package es.uc3m.setichat.mensajes;

import java.util.ArrayList;
import java.util.List;

import es.uc3m.setichat.bd.Contacto;

public class MensajeContactResponse extends MensajeVacio{
	
	private List<Contacto> listaContactos;
	
	public MensajeContactResponse(){
		this.listaContactos = new ArrayList<Contacto>();
	}
	
	public void nuevoContacto(String nick, String mobile){
		this.listaContactos.add(new Contacto(nick, mobile));
	}

	public List<Contacto> getListaContactos() {
		return listaContactos;
	}

	public void setListaContactos(List<Contacto> listaContactos) {
		this.listaContactos = listaContactos;
	}
	
	
}
