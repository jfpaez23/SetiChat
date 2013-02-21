package es.uc3m.setichat.activity.comunicacion;

import java.io.Serializable;
import java.util.List;

import es.uc3m.setichat.bd.Contacto;

public class MainToContacts implements Serializable {

	private static final long serialVersionUID = -4363370913456662802L;

	String serverId;
	List<Contacto> contactos;

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public List<Contacto> getContactos() {
		return contactos;
	}

	public void setContactos(List<Contacto> contactos) {
		this.contactos = contactos;
	}
	
	
}
